package fr.istic.grpc.datenotif.server;

import fr.istic.grpc.datenotif.DateNotifGrpc;
import fr.istic.grpc.datenotif.Datenotif.DateRequest;
import fr.istic.grpc.datenotif.Datenotif.DateMessage;
import io.grpc.stub.ServerCallStreamObserver;
import io.grpc.stub.StreamObserver;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class DateNotifServiceImpl extends DateNotifGrpc.DateNotifImplBase {
    //la méthode subscribeDate est appelée quand un nouveau client subscribe (bv)
    @Override
    public void subscribeDate(DateRequest request, StreamObserver<DateMessage> responseObserver) {  // on utilise bien un StreamObserver en réponse car le serveur va envoyer plusieurs réponses à la même requête (server streaming)
        ServerCallStreamObserver<DateMessage> serverObserver =
                (ServerCallStreamObserver<DateMessage>) responseObserver;       //serverObserver pour envoyer les msg au client (.onNext())

        System.out.println("Un nouveau client s'est abonné au stream de ddate");

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();      // création du scheduler qui va envoyer les msg au client
        
        ScheduledFuture<?> task = scheduler.scheduleAtFixedRate(() -> { //scheduleAtFixedRate pour envoyer la date toutes les 10 secondes
            try {
                String now = LocalDateTime.now().toString();
                DateMessage message = DateMessage.newBuilder().setDate(now).build();        // création du DateMessage avec la date en field "date"
                serverObserver.onNext(message);     //envoi du msg au client via le stream gRPC
                System.out.println("Date envoyée : " + now);
            } catch (Exception e) {
                //le client s'est déconnecté pendant l'envoi
                System.out.println("Erreur lors de l'envoi, arrêt du stream : " + e.getMessage());
            }
        }, 0, 10, TimeUnit.SECONDS);

        //fin du stream quand le client ferme la connexion
        serverObserver.setOnCancelHandler(() -> {
            System.out.println("Client s'est déconnecté, arrêt du stream");
            task.cancel(true);
            scheduler.shutdown();
        });
    }
}