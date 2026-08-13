import grpc

import sommestream_pb2
import sommestream_pb2_grpc
# python3 -m venv venv
# source venv/bin/activate

#python3 client.py depuis /python-client

def generate_numbers():
    print("Entrez des entiers successivement ou une ligne vide pour terminer :")
    while True:
        line = input().strip()              # l'user entre un entier
        if line == "":                  # ou la ligne vide pour terminer
            break
        value = int(line)
        yield sommestream_pb2.IntMessage(value=value)       


def run():
    with grpc.insecure_channel("localhost:8080") as channel:                # création de la connexion gRPC
        stub = sommestream_pb2_grpc.SommeStreamServiceStub(channel)         # et du stub client

        responses = stub.SommeStream(generate_numbers())            # setup du stream bidirectionnel

        for response in responses:                  # on itère sur les réponses (en théorie on a quand même 1 req/1 rep même si c'est du streaming)
            print(f"Somme reçue du serveur : {response.sum}")


if __name__ == "__main__":
    run()