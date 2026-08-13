import queue
import threading
from concurrent import futures

import grpc

import chat_pb2
import chat_pb2_grpc

# LANCEMENT :
# python3 -m venv venv
# source bin/venv/activate
# pip install grpcio==1.64.0
# pip install grpcio-tools==1.64.0
# python -m grpc_tools.protoc   -I ../proto   --python_out=.   --grpc_python_out=.   ../proto/chat.proto
# depuis /python-server/
# python3 server.py


# Verrou protégeant l'accès à la liste des clients connectés
clients_lock = threading.Lock()
clients = []  # liste de queue.Queue, une par client connecté


def broadcast(message):
    """Dépose le message dans la file de sortie de chaque client connecté."""
    with clients_lock:
        for client_queue in clients:
            client_queue.put(message)


class ChatServiceServicer(chat_pb2_grpc.ChatServiceServicer):
    def joinChat(self, request_iterator, context):
        # File d'attente des messages à renvoyer à CE client
        outgoing = queue.Queue()
        with clients_lock:
            clients.append(outgoing)
        print(f"Nouveau client connecté (total : {len(clients)})")

        def read_incoming():
            """Tourne dans un thread séparé : lit en boucle les messages
            envoyés par ce client et les rediffuse à tout le monde."""
            try:
                for message in request_iterator:
                    print(f"{message.user}: {message.message}")
                    broadcast(message)
            finally:
                with clients_lock:
                    if outgoing in clients:
                        clients.remove(outgoing)
                # Débloque la boucle d'émission ci-dessous quand le client part
                outgoing.put(None)
                print(f"Client déconnecté (total : {len(clients)})")

        reader_thread = threading.Thread(target=read_incoming, daemon=True)
        reader_thread.start()

        # Boucle d'émission : bloque tant qu'aucun message n'est disponible
        while True:
            message = outgoing.get()
            if message is None:  # signal de fin envoyé par read_incoming()
                break
            yield message


def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    chat_pb2_grpc.add_ChatServiceServicer_to_server(ChatServiceServicer(), server)
    port = "8080"
    server.add_insecure_port(f"[::]:{port}")
    server.start()
    print(f"Serveur démarré sur le port {port}")
    server.wait_for_termination()


if __name__ == "__main__":
    serve()