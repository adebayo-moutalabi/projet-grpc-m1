import grpc

import delivery_pb2
import delivery_pb2_grpc

# CREATION DU COLIS
def create_order(stub):
    #le client entre son adresse, celle où il veut envoyer le colis et une description de l'objet à livrer
    sender = input("Adresse d'expédition : ")
    recipient = input("Adresse de destination : ")
    description = input("Description de l'objet : ")

    #construction de la requête
    request = delivery_pb2.OrderRequest(
        sender_address=sender,
        recipient_address=recipient,
        item_description=description,
    )
    response = stub.CreateOrder(request)        #envoi de la requête au serveur gRPC
    print(f"Commande créée : {response.order_id} (confirmée : {response.confirmed})")
    return response.order_id            #et récupérer l'id généré par le serveur

# sUIVI DES COLIS
def track_delivery(stub, order_id):
    request = delivery_pb2.TrackRequest(order_id=order_id)          #construction de la requête protobuf
    print(f"Suivi de la commande {order_id} (Ctrl+C pour quitter le stream)")
    try:
        for position in stub.TrackDelivery(request):            #obtention du stream RPC
            print(f"Position du livreur : [{position.latitude:.1f} ; {position.longitude:.1f}]")
    except grpc.RpcError as e:      #erruer
        print(f"Erreur sur le stream : {e.details()}")
    except KeyboardInterrupt:       #fin du stream
        print("Stream arrêté.")

# CHAT AVEC LE LIVREUR
def chat(stub, order_id):
    def generate_messages():        #stream Client -> Serveur
        
        yield delivery_pb2.ChatMessage(order_id=order_id, sender="client", content="")      #msg vide pour l'init
        
        print("Chat avec le livreur (ligne vide pour quitter) :")
        #print("ATTENTION : POUR BIEN INITIALISER LE CHAT, VEUILLEZ ENTRER \"CONFIRMER\" EN PREMIER MESSAGE.\n")
        while True:
            line = input("> ")
            if line == "":      # le client veut quitter le chat
                return
            yield delivery_pb2.ChatMessage(order_id=order_id, sender="Client", content=line)    # -> construit ChatMessage { order_id: "a123" ; sender: "client" ; content: "[line]"}

    responses = stub.SupportChat(generate_messages())       #génération du stream de message
    for message in responses:
        print(f"[{message.sender}] {message.content}\n> ")      #affichage des msg


def main():
    with grpc.insecure_channel("localhost:8080") as channel:        #ouvrir connexion
        stub = delivery_pb2_grpc.DeliveryServiceStub(channel)   #creation du stub client qui appelle les méthodes gRPC 

        order_id = create_order(stub)   # creation de la commande

        while True:             #menu de l'application
            print("\n")
            print("1. Suivre la livraison")
            print("2. Ouvrir le chat avec le livreur")
            print("3. Quitter")
            choice = input("Choix : ")

            if choice == "1":
                track_delivery(stub, order_id)      #appel au suivi du livreur
            elif choice == "2":
                chat(stub, order_id)                #appel au chat
            elif choice == "3":
                break                               #sortie
            else:
                print("Choix invalide.")


if __name__ == "__main__":
    main()