package fr.istic.grpc.delivery.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class DeliveryServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        int port = 8080;
        Server server = ServerBuilder.forPort(port)
                .addService(new DeliveryServiceImpl())
                .build();

        server.start();
        System.out.println("Serveur DeliveryService démarré sur le port " + port);
        server.awaitTermination();
    }
}