from concurrent import futures
import grpc

import streamcalc_pb2
import streamcalc_pb2_grpc

# python3 server.py depuis python-server avec venv/bin/activate
class StreamCalcServiceServicer(streamcalc_pb2_grpc.StreamCalcServiceServicer):
    def Sum(self, request_iterator, context):           # méthode appelée par le asyncStub.sum(responseObserver); du client java. Chaque onNext() du client ajoute une req à l'iterator
        total = 0
        count = 0
        for request in request_iterator:            # en client streaming le request_iterator contient les différentes req du client -> il faut les parcourir
            total += request.number
            count += 1
            print(f"Nombre reçu : {request.number} (total partiel : {total})")

        print(f"Stream terminé ({count} nombres reçus), la somme finale est = {total}")
        return streamcalc_pb2.SumReply(sum=total)           # le serv crée le SumReply contenant le field sum et la valeur obtenue


# gestion du serveur gRPC, exactement comme le unary
def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    streamcalc_pb2_grpc.add_StreamCalcServiceServicer_to_server(
        StreamCalcServiceServicer(), server
    )
    port = "8080"
    server.add_insecure_port(f"[::]:{port}")
    server.start()
    print(f"Serveur gRPC démarré sur le port {port}")
    server.wait_for_termination()


if __name__ == "__main__":
    serve()