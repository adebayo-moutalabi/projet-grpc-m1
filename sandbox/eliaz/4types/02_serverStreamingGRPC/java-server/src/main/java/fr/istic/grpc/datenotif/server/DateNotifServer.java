package fr.istic.grpc.datenotif.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

/**
 * LANCER AVEC 
 *  /java-server$ mvn clean compile exec:java
 */

public class DateNotifServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        int port = 8080;
        // crée le serv gRPC sur le port 8080 avec le service DateNotif
        Server server = ServerBuilder.forPort(port).addService(new DateNotifServiceImpl()).build();
        // start le serveur
        server.start();
        System.out.println("Serveur DateNotif démarré sur le port " + port);
        server.awaitTermination();      // le serv tourne jusqu'à ce que je le termine manuellement
    }
}