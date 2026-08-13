import grpc

import datenotif_pb2
import datenotif_pb2_grpc

# LANCER AVEC :
# python3 -m venv venv
# source venv/bin/activate
# /python-client$ python3 client.py 
#

def run():
    with grpc.insecure_channel("localhost:8080") as channel:    # creation du channel gRPC lié à localhost:8080 , sans encrypt TLS (insecure)
        stub = datenotif_pb2_grpc.DateNotifStub(channel)        # creation du stub client pour appeler le service du .proto

        request = datenotif_pb2.DateRequest()       # creation d'un DateRequest vide

        print("Reception des stream de Date du serveur (Ctrl+C pour stop)")
        try:
            for message in stub.SubscribeDate(request):     # subscription au serveur
                print(f"Date reçue : {message.date}")       # et extraction du champ date du DateMessage
        except KeyboardInterrupt:
            print("Arrêt du stream demandé par l'utilisateur.")


if __name__ == "__main__":
    run()