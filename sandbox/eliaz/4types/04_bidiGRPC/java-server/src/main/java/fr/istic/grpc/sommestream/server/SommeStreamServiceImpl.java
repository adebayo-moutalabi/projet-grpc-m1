package fr.istic.grpc.sommestream.server;

import fr.istic.grpc.sommestream.SommeStreamServiceGrpc;
import fr.istic.grpc.sommestream.Sommestream.IntMessage;
import fr.istic.grpc.sommestream.Sommestream.SommeMessage;
import io.grpc.stub.StreamObserver;

public class SommeStreamServiceImpl extends SommeStreamServiceGrpc.SommeStreamServiceImplBase {

    @Override
    public StreamObserver<IntMessage> sommeStream(StreamObserver<SommeMessage> responseObserver) {
        System.out.println("Nouveau client connecté");

        return new StreamObserver<IntMessage>() {
            private int sum = 0;

            @Override
            public void onNext(IntMessage request) {        // à chaque nouvel entier reçu
                sum += request.getValue();              // extraire l'entier et l'ajouter à la somme courante
                System.out.println("Reçu : " + request.getValue() + " -> somme actuelle : " + sum);     
                responseObserver.onNext(SommeMessage.newBuilder().setSum(sum).build());     // envoyer au client la nouvelle somme
            }

            @Override
            public void onError(Throwable t) {          // cas d'erreur du client
                System.out.println("Erreur X");
            }

            @Override
            public void onCompleted() {                 // quand le client a terminé d'envoyer tous ses entiers (ie la somme finale)
                System.out.println("Stream client terminé, somme finale : " + sum);
                responseObserver.onCompleted();
            }
        };
    }
}