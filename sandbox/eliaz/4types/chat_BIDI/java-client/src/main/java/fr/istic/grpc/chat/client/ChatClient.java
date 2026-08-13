package fr.istic.grpc.chat.client;

import fr.istic.grpc.chat.ChatServiceGrpc;
import fr.istic.grpc.chat.Chat.ChatMessage;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * LANCEMENT : 
 * depuis un terminal /java-client/
 * mvn clean compile exec:java -Dexec.args="[NOM]"
 */

public class ChatClient {
    public static void main(String[] args) throws InterruptedException {
        String username = (args.length > 0) ? args[0] : "Anonyme";

        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 8080)
                .usePlaintext()
                .build();

        try {
            ChatServiceGrpc.ChatServiceStub asyncStub = ChatServiceGrpc.newStub(channel);

            CountDownLatch finishLatch = new CountDownLatch(1);

            StreamObserver<ChatMessage> responseObserver = new StreamObserver<ChatMessage>() {
                @Override
                public void onNext(ChatMessage message) {
                    System.out.println("[" + message.getUser() + "] " + message.getMessage());
                }

                @Override
                public void onError(Throwable t) {
                    System.err.println("Erreur RPC : " + t.getMessage());
                    finishLatch.countDown();
                }

                @Override
                public void onCompleted() {
                    System.out.println("Le serveur a fermé la connexion.");
                    finishLatch.countDown();
                }
            };

            StreamObserver<ChatMessage> requestObserver = asyncStub.joinChat(responseObserver);

            System.out.println("Connecté en tant que " + username
                    + ". Tapez vos messages (ligne vide + Entrée pour quitter) :");

            Thread stdinThread = new Thread(() -> {
                Scanner scanner = new Scanner(System.in);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.isEmpty()) {
                        break;
                    }
                    ChatMessage message = ChatMessage.newBuilder()
                            .setUser(username)
                            .setMessage(line)
                            .build();
                    requestObserver.onNext(message);
                }
                requestObserver.onCompleted();
            });
            stdinThread.setDaemon(true);
            stdinThread.start();

            finishLatch.await(1, TimeUnit.HOURS);
        } finally {
            channel.shutdown();
        }
    }
}