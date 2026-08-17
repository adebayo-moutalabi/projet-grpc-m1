package fr.istic.grpc.delivery.server;

import fr.istic.grpc.delivery.DeliveryServiceGrpc;
import fr.istic.grpc.delivery.Delivery.*;
import io.grpc.Status;
import io.grpc.stub.ServerCallStreamObserver;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class DeliveryServiceImpl extends DeliveryServiceGrpc.DeliveryServiceImplBase {

    // map des colis selon order_id
    private final Map<String, OrderState> orders = new ConcurrentHashMap<>();

    // UNARY : CreateOrder 
    @Override
    public void createOrder(OrderRequest request, StreamObserver<OrderResponse> responseObserver) {
        String orderId = UUID.randomUUID().toString().substring(0, 4);      //génération aléatoire de l'id du colis
        orders.put(orderId, new OrderState(orderId));           //création du nouveau colis et ajout à la hashmap

        //afficher la nouvelle commande dans la console serveur
        System.out.println("Commande créée : " + orderId + " (" + request.getSenderAddress()
                + " -> " + request.getRecipientAddress() + ")" + " | Objet : " + request.getItemDescription());

        //build de la réponse
        OrderResponse response = OrderResponse.newBuilder()
                .setOrderId(orderId)
                .setConfirmed(true)     //permet de confirmer au sender que la commande a bien été générée
                .build();

        responseObserver.onNext(response);      //envoi de la rep
        responseObserver.onCompleted();         //et fin de la réponse serveur
    }

    // SERVER STREAMING : TrackDelivery suivi de colis
    @Override
    public void trackDelivery(TrackRequest request, StreamObserver<Position> responseObserver) {
        String orderId = request.getOrderId();              //recup l'id

        if (!orders.containsKey(orderId)) {                 //si l'id est pas dans la liste alors erreur envoyée au sender
            responseObserver.onError(Status.NOT_FOUND         //Status.NOT_FOUND = 404 en HTTP
                    .withDescription("Commande inconnue : " + orderId)
                    .asRuntimeException());     //indispensable parce que gRPC veut un Throwable 
            return;
        }

        ServerCallStreamObserver<Position> serverObserver =
                (ServerCallStreamObserver<Position>) responseObserver;      //cast le StreamObs en ServerCallStreamObs

        System.out.println("Suivi démarré pour la commande " + orderId);

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();      //et créer le scheduler qui envoie une pos toutes les 10s
        ScheduledFuture<?> task = scheduler.scheduleAtFixedRate(() -> {
            try {
                double lat = (Math.random() * 360000) - 180000;
                double lon = (Math.random() * 360000) - 180000;         //coords générées aléatoirement pour le moment
                Position position = Position.newBuilder()
                        .setLatitude(lat)
                        .setLongitude(lon)
                        .build();
                serverObserver.onNext(position);            //et envoi de la position
                System.out.println("Position envoyée pour " + orderId + " : [" + lat +  ";" + lon + "]");
            } catch (Exception e) {         //si erreur
                System.out.println("Erreur lors de l'envoi de la position : " + e.getMessage());
            }
        }, 0, 10, TimeUnit.SECONDS);

        serverObserver.setOnCancelHandler(() -> {           //quand le client Ctrl+C on stoppe le flux
            System.out.println("Client déconnecté du suivi de " + orderId);
            task.cancel(true);
            scheduler.shutdown();
        });
    }

    // CLIENT STREAMING : UploadProof gestion des png envoyés par le livreur
    @Override
    public StreamObserver<Photo> uploadProof(StreamObserver<UploadAck> responseObserver) {
        return new StreamObserver<Photo>() {
            private final List<String> filenames = new ArrayList<>();
            private String orderId = null;

            @Override
            public void onNext(Photo photo) {           //à chaque envoi de photo par le livreur
                orderId = photo.getOrderId();           //récup l'id
                filenames.add(photo.getFilename());     //ajouter le nom de la photo à la liste temp
                System.out.println("Photo reçue pour " + orderId + " : " + photo.getFilename());           
            }

            @Override
            public void onError(Throwable t) {          
                System.out.println("Erreur pendant l'upload : " + t.getMessage());
            }

            @Override
            public void onCompleted() {                 //quand le livreur a fini d'envoyer des photos
                if (orderId != null) {
                    OrderState state = orders.get(orderId);     // récupérer l'état stocké de la commande
                    if (state != null) {                //important sinon y'a quelques crash 
                        state.getPhotos().addAll(filenames);        //et ajouter toutes les photos du livreur à liste de photos de la commande
                    }
                }
                System.out.println(filenames.size() + " photo(s) reçue(s) au total pour " + orderId);

                UploadAck ack = UploadAck.newBuilder()
                        .setPhotosReceived(filenames.size())
                        .build();               //construction d'un accusé de réception des photos
                responseObserver.onNext(ack);       //envoi au livreur et fin
                responseObserver.onCompleted(); 
            }
        };
    }

    // BIDI STREAMING : SupportChat 
    @Override
    public StreamObserver<ChatMessage> supportChat(StreamObserver<ChatMessage> responseObserver) {
        return new StreamObserver<ChatMessage>() {
            private String orderId = null;

            @Override
            public void onNext(ChatMessage message) {
                orderId = message.getOrderId();         //récupération de l'order_id dans le premier message reçu
                OrderState state = orders.get(orderId);
                if (state == null) {                    //verif que l'order existe
                    System.out.println("Message reçu pour une commande inconnue : " + orderId);
                    return;
                }

                // Enregistrement de ce participant au premier message reçu
                if (!state.getChatParticipants().contains(responseObserver)) {
                    state.getChatParticipants().add(responseObserver);
                    System.out.println(message.getSender() + " a rejoint le chat de la commande " + orderId);
                }
                /**
                 * IMPORTANT : comme on enregistre le participant au premier msg reçu et pas avant (on connait pas encore l'order_id), 
                 * on va systématiquement perdre le premier message envoyé. C'est pour ça que je demande aux deux participants d'envoyer un message dummy qui sera sacrifié pour l'init.
                 */

                //affichage du msg
                System.out.println("[" + orderId + "] " + message.getSender() + ": " + message.getContent());

                //redif à tous ceux qui écoutent sur cet order_id
                for (StreamObserver<ChatMessage> participant : state.getChatParticipants()) {
                    if (participant != responseObserver) {          // =! important pour pas renvoyer le message à celui qui l'a écrit
                        synchronized (participant) {
                            participant.onNext(message);            // le onNext est sync pour éviter 2 appels en même temps de 2 participants
                        }
                    }
                }
            }


            @Override
            // quand le stream client crashe anormalement
            public void onError(Throwable t) {
                System.out.println("Erreur dans le chat : " + t.getMessage());
                removeParticipant();
            }

            
            @Override
            // quand le stream client ferme normalement (ligne vide)
            public void onCompleted() {
                removeParticipant();
                responseObserver.onCompleted();
            }

            //supprime le streamObserver du participants qui a fermé son chat grâce à son orderId
            private void removeParticipant() {
                if (orderId != null) {
                    OrderState state = orders.get(orderId);
                    if (state != null) {
                        state.getChatParticipants().remove(responseObserver);
                    }
                }
            }
        };
    }
}