package fr.istic.grpc.sommestream.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class SommeStreamServer {
    // c'est du classique serveur gRPC java sur le port 8080 qui implémente le service SommeStream
    public static void main(String[] args) throws IOException, InterruptedException {
        int port = 8080;
        Server server = ServerBuilder.forPort(port)
                .addService(new SommeStreamServiceImpl())
                .build();

        server.start();
        System.out.println("Serveur SommeStream démarré sur le port " + port);
        server.awaitTermination();
    }
}