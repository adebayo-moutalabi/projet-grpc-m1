import grpc

import delivery_pb2
import delivery_pb2_grpc
import time

#python -m grpc_tools.protoc \
#  -I ../proto \
#  --python_out=. \
#  --grpc_python_out=. \
#  ../proto/delivery.proto

#envoyer les "photos" de preuve de livraison au serveur
def upload_proof(stub, order_id):
    def generate_photos():
        print("Entrez les noms des photos à envoyer (ligne vide pour terminer) :")
        while True:
            filename = input("Nom de la photo : ")
            if filename == "":
                break
            yield delivery_pb2.Photo(order_id=order_id, filename=filename)      # création du message Photo { order_id: "a123" ; filename: "photo1.png"}
            time.sleep(1)       # ça permet de laisser le temps au onError de s'exec si jamais l'order_id était invalide et donc de pas print la ligne 16 pour rien
    try:
        ack = stub.UploadProof(generate_photos())           #récupération de l'ack du serveur
        print(f"ACK Serveur : {ack.photos_received} photo(s) reçue(s)")
    except grpc.RpcError as e:              # si erreur (probablement un order_id invalide)
        print(f"ERREUR : {e.details()}")


def chat(stub, order_id):
    def generate_messages():
        
        yield delivery_pb2.ChatMessage(order_id=order_id, sender="Livreur", content="")
        
        print("Chat avec le client (ligne vide pour quitter) :")
        #print("ATTENTION : POUR BIEN INITIALISER LE CHAT, VEUILLEZ ENTRER \"CONFIRMER\" EN PREMIER MESSAGE.\n")
        while True:
            line = input("> ")
            if line == "":
                return
            yield delivery_pb2.ChatMessage(order_id=order_id, sender="Livreur", content=line) # création du message ChatMessage { order_id: "a123" ; sender: "Livreur" ; content: "[line]"}

    try:
        responses = stub.SupportChat(generate_messages())           #récupération des messages du chat
        for message in responses:   
            print(f"[{message.sender}] {message.content}\n> ")          #et affichage
    except grpc.RpcError as e:          # pareil, probablement si order_id invalide
        print(f"ERREUR : {e.details()}")


def main():
    with grpc.insecure_channel("localhost:8080") as channel:
        stub = delivery_pb2_grpc.DeliveryServiceStub(channel)

        #liaison du livreur et de la commande
        order_id = input("Numéro de la commande à livrer : ")           #TODO pas encore de vérif que l'order_id est le bon pour le moment 

        #boucle du livreur
        while True:
            print("\n")
            print("1. Envoyer les photos de preuve de livraison")
            print("2. Ouvrir le chat avec le client")
            print("3. Quitter")
            choice = input("Choix : ")

            #envoi photos
            if choice == "1":
                upload_proof(stub, order_id)
            #ouvrir chat
            elif choice == "2":
                chat(stub, order_id)
            #fin
            elif choice == "3":
                break
            #erreur ou pas reconnu
            else:
                print("Choix invalide.")


if __name__ == "__main__":
    main()