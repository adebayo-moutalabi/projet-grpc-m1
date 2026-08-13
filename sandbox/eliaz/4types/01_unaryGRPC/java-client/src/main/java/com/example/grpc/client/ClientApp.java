package com.example.grpc.client;

import com.example.grpc.addition.AddRequest;
import com.example.grpc.addition.AddResponse;
import com.example.grpc.addition.AdditionServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.Scanner;

/** 
 * /java-client$ mvn compile exec:java 
 */

public class ClientApp {
    public static void main(String[] args) throws InterruptedException {
        // j'utilise un scanner pour permettre à l'user d'entrer les deux entiers lui-même
        Scanner sc = new Scanner(System.in);
        System.out.print("Entrez l'entier a : ");
        int a = sc.nextInt();
        System.out.print("Entrez l'entier b : ");
        int b = sc.nextInt();
        sc.close();
        System.out.println("Envoi au serveur de la requête d'addition de " + a + " et " + b);
        // création de la connexion gRPC sur la machine locale au port 8080 pour match le server python
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080).usePlaintext().build(); 
        // pas de TLS parce que c'est un exemple basique


        try {
            //création du stub client (ce qui lui sert à appeler le AdditionService du serveur)
            AdditionServiceGrpc.AdditionServiceBlockingStub stub =
                    AdditionServiceGrpc.newBlockingStub(channel);
            // ATTENTION LE STUB EST BLOQUANT ICI DONC LE CLIENT SERA BLOQUE JUSQUA LA RECEPTION DE LA REPONSE DU SERVEUR

            // construction de la req
            AddRequest request = AddRequest.newBuilder().setA(a).setB(b).build();

            // appel au serveur
            AddResponse response = stub.add(request);

            System.out.println("Réponse du serveur obtenue, le résultat est " + response.getResult());
        } finally {
            channel.shutdown();     // on ferme la co
        }
    }
}