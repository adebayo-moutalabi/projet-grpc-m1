import grpc

import delivery_pb2
import delivery_pb2_grpc

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
            filename = input("Nom du fichier : ")
            if filename == "":
                break
            yield delivery_pb2.Photo(order_id=order_id, filename=filename)      # création du message Photo { order_id: "a123" ; filename: "photo1.png"}

    ack = stub.UploadProof(generate_photos())           #récupération de l'ack du serveur
    print(f"ACK Serveur : {ack.photos_received} photo(s) reçue(s)")


def chat(stub, order_id):
    def generate_messages():
        print("Chat avec le client (ligne vide pour quitter) :")
        print("ATTENTION : POUR BIEN INITIALISER LE CHAT, VEUILLEZ ENTRER \"CONFIRMER\" EN PREMIER MESSAGE.\n")
        while True:
            line = input("> ")
            if line == "":
                return
            yield delivery_pb2.ChatMessage(order_id=order_id, sender="Livreur", content=line) # création du message ChatMessage { order_id: "a123" ; sender: "Livreur" ; content: "[line]"}

    responses = stub.SupportChat(generate_messages())           #récupération des messages du chat
    for message in responses:   
        print(f"[{message.sender}] {message.content}")          #et affichage


def main():
    with grpc.insecure_channel("localhost:8080") as channel:
        stub = delivery_pb2_grpc.DeliveryServiceStub(channel)

        #liaison du livreur et de la commande
        order_id = input("Numéro de la commande à livrer : ")

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