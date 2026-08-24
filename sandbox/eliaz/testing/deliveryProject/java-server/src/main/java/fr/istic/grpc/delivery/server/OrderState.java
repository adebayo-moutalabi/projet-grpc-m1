package fr.istic.grpc.delivery.server;

import fr.istic.grpc.delivery.Delivery.ChatMessage;
import io.grpc.stub.StreamObserver;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Classe nécessaire pour stocker l'état d'une commande entre plusieurs appels RPC qui sont (indépendants).
 * Il n'y a pas de stockage de la localisation parce que je génère aléatoirement les coordonnées
 */
public class OrderState {
    private final String orderId;       //id de la commande, clé primaire
    private final List<String> photos = new CopyOnWriteArrayList<>();       //liste des photos envoyées par le livreur
    private final List<StreamObserver<ChatMessage>> chatParticipants = new CopyOnWriteArrayList<>();    //stocker les deux StreamObserver (client/livreur) quand ils se connectent
    // j'utilise un CopyOnWriteArrayList comme vu en IPD pour être thread safe même si c'est lourd en performance. Ca reste OK parce qu'en théorie il y a au max 2 personnes à la fois sur le chat.

    public OrderState(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public List<StreamObserver<ChatMessage>> getChatParticipants() {
        return chatParticipants;
    }
}