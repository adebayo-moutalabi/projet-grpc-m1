from concurrent import futures
import grpc

import addition_pb2
import addition_pb2_grpc 
# LANCEMENT depuis /python-server
# python3 -m venv venv
# source venv/bin/activate
# python3 server.py 


class AdditionServiceServicer(addition_pb2_grpc.AdditionServiceServicer):
    # le service d'addition
    def Add(self, request, context):
        print(f"Requête reçue : {request.a} + {request.b}") # print de la réception de la req du client
        result = request.a + request.b              # calcul
        print(f"Le résultat est : {result}. Envoi de la réponse au client.")
        return addition_pb2.AddResponse(result=result)      # envoi de la réponse



# fonction du serveur
# on utilise futures qui permet de gérer les threads en python
def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))        # crée le serveur gRPC avec 10 threads max ici
    addition_pb2_grpc.add_AdditionServiceServicer_to_server(
        AdditionServiceServicer(), server
    )                       # init du service Addition qui gère les requêtes entrantes
    port = "8080"
    server.add_insecure_port(f"[::]:{port}")            # binding du serveur au port 8080
    server.start()                  # init du serveur
    print(f"Serveur gRPC démarré sur le port {port}")
    server.wait_for_termination()           # je le fais tourner à l'infini pour l'exemple


if __name__ == "__main__":
    serve()