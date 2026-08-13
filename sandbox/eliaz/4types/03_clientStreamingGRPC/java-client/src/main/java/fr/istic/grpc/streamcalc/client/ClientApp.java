package fr.istic.grpc.streamcalc.client;

import fr.istic.grpc.streamcalc.StreamCalcServiceGrpc;
import fr.istic.grpc.streamcalc.Streamcalc.NumberRequest;
import fr.istic.grpc.streamcalc.Streamcalc.SumReply;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * ~/Documents/M1CR/PROJET_gRPC/4types/03_clientStreamingGRPC/java-client$ mvn clean compile exec:java
 */

public class ClientApp {
    public static void main(String[] args) throws InterruptedException {
        List<Integer> numbers = List.of(3, 7, 12, 5, 20);       // entiers envoyés par le client (1 entier par requête)


        // création du channel gRPC 
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080).usePlaintext().build(); //usePlainText() -> pas d'encrypt TLS car pas besoin 

        try {
            StreamCalcServiceGrpc.StreamCalcServiceStub asyncStub =
                    StreamCalcServiceGrpc.newStub(channel);     // création du stub async client avec lequel il appelle la méthode distante du serveur
            // pas comme BlockingStub, ici le client est pas bloqué en attendant la réponse du serveur

                    //indispensable sinon le programme se ferme avant que le serveur n'envoie sa réponse
            CountDownLatch latch = new CountDownLatch(1);

            StreamObserver<SumReply> responseObserver = new StreamObserver<SumReply>() {        // def des comportements du client quand le serveur répond
                //si nombre reçu du serveur (la réponse)
                @Override
                public void onNext(SumReply reply) {
                    System.out.println("Somme reçue du serveur : " + reply.getSum());
                }

                //si erreur
                @Override
                public void onError(Throwable t) {
                    System.err.println("Erreur RPC : " + t.getMessage());
                    latch.countDown();          // on decr le latch pour pas bloquer le client en cas d'erreur (tant pis)
                }

                //quand le serveur a terminé
                @Override
                public void onCompleted() {
                    System.out.println("Le serveur a terminé le calcul");
                    latch.countDown();          //on termine le client
                }
            };

            StreamObserver<NumberRequest> requestObserver = asyncStub.sum(responseObserver);        // création du stream d'envoi des entiers

            try {
                for (int n : numbers) {
                    System.out.println("Envoi de : " + n);
                    requestObserver.onNext(NumberRequest.newBuilder().setNumber(n).build());        // envoi d'un entier
                }
            } catch (RuntimeException e) {
                requestObserver.onError(e);
                throw e;
            }

            requestObserver.onCompleted();      // indiquer au serv que le client a terminé la transmission des entiers

            // On attend la réponse du serveur (10 secondes là)         (on attend un onCompleted() ou un onError())
            if (!latch.await(10, TimeUnit.SECONDS)) {
                System.err.println("Erreur timeout : pas de réponse du serveur.");
            }
        } finally {
            channel.shutdown();     // fermer le channel
        }
    }
}