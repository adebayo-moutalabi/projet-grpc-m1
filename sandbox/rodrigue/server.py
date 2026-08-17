from concurrent import futures
import queue
import threading
import time
from google.protobuf.timestamp_pb2 import Timestamp
import grpc

import etat
import suivi_colis_pb2 as pb
import suivi_colis_pb2_grpc as pbg


# Rooms de discussion : colis_id -> queue.Queue
# Un room par colis_id
_rooms = {}
_rooms_verrou = threading.Lock()          # protege l'acces a _rooms


def maintenant():
    ts = Timestamp()
    ts.GetCurrentTime()
    return ts


class Service(pbg.SuiviColisServicer):
    
    ## Pattern UNARY
    # Un objet Colis est cree, stocke dans etat._colis, et retourne au client
    def CreerColis(self, request, context):
        c = etat.creer(request.destinataire, request.adresse, (request.depot.latitude, request.depot.longitude), (request.destination.latitude, request.destination.longitude))
        print(f"CreerColis {c.colis_id} pour {c.destinataire} a {c.adresse}")
        return pb.CreerColisResponse(colis_id=c.colis_id, statut=pb.CREE, cree_le=maintenant())
    
    
    ## Pattern SERVER STREAMING
    # Le client envoie un colis_id, le serveur retourne un flux de messages Colis
    def SuivreColis(self, request, context):
        c = etat.lire(request.colis_id)
        if c is None:
            context.abort(grpc.StatusCode.NOT_FOUND, f"Colis {request.colis_id} inconnu")
        print(f"[SuivreColis] debut du suivi de {c.colis_id}")
        lat0, lon0 = c.depot
        lat1, lon1 = c.destination
        
        for pas in range(0, 11): # 11 positions de 0% à 100%
            if not context.is_active():
                print(f"[SuivreColis] suivi de {c.colis_id} interrompu par le client")
                return
            
            pct = pas * 10
            lat = lat0 + (lat1 - lat0) * pct / 10
            lon = lon0 + (lon1 - lon0) * pct / 10
            maj = etat.avancer(c.colis_id, pct)
            
            yield pb.PositionUpdate(
                colis_id=c.colis_id,
                position=pb.Position(latitude=lat, longitude=lon),
                statut=maj.statut,
                progression_pct=pct,
                horodatage=maintenant()
            )
            if pct < 100:
                time.sleep(1.5)  # simule le temps de transport
            
            print(f"[SuivreColis] suivi de {c.colis_id} : {pct}%")
        return
        
    
    ### Pattern CLIENT STREAMING
    # Le client envoie un flux de messages PhotoChunk, le serveur retourne un message
    def EnvoyerPreuve(self, request_iterator, context):
        colis_id, photos, octets = None, 0, 0
        
        for chunk in request_iterator:
            if colis_id is None:
                colis_id = chunk.colis_id
                if not colis_id:
                    context.abort(grpc.StatusCode.INVALID_ARGUMENT, "1er chunk sans colis_id")
                if etat.lire(colis_id) is None:
                    context.abort(grpc.StatusCode.NOT_FOUND, f"Colis {colis_id} inconnu")
            octets += len(chunk.contenu)
            if chunk.fin_de_photo:
                photos += 1
                print(f"[EnvoyerPreuve] {colis_id} : photo {photos} de {octets} octets")
        
        c = etat.avancer(colis_id, 100)
        print(f"[EnvoyerPreuve] {colis_id} : {photos} photos recues, colis livre")
        return pb.PreuveResponse(colis_id=colis_id, photos_recues=photos, octets_recus=octets, statut=c.statut)
    
    
    ## Pattern BIDIRECTIONAL STREAMING
    # Le client envoie un flux de messages SuivreColisRequest, le serveur retourne un flux de messages PositionUpdate
    def Discuter(self, request_iterator, context):
        ma_queue = queue.Queue()
        colis_id = None
        
        def lecteur():
            """Thread qui consomme le flux montant et diffuse au salon."""
            nonlocal colis_id
            try:
                for msg in request_iterator:
                    if colis_id is None:
                        colis_id = msg.colis_id
                        c = etat.lire(colis_id)
                        if c is None:
                            ma_queue.put(None)
                            return
                        # inscription dans le salon
                        with _rooms_verrou:
                            _rooms.setdefault(colis_id, []).append(ma_queue)
                        # rejeu de l'historique pour celui qui arrive
                        for ancien in list(c.messages):
                            ma_queue.put(ancien)
                        print(f"[Discuter] {msg.auteur} rejoint {colis_id}")
 
                    msg.horodatage.CopyFrom(maintenant())   # horloge serveur
                    c = etat.lire(colis_id)
                    if c is not None:
                        c.messages.append(msg)
                    with _rooms_verrou:
                        for q in _rooms.get(colis_id, []):
                            q.put(msg)
            finally:
                ma_queue.put(None)                            # sentinelle
 
        t = threading.Thread(target=lecteur, daemon=True)
        t.start()
 
        try:
            while True:
                msg = ma_queue.get()
                if msg is None:                               # fin de session
                    break
                yield msg
        finally:
            with _rooms_verrou:
                if colis_id in _rooms and ma_queue in _rooms[colis_id]:
                    _rooms[colis_id].remove(ma_queue)
            print(f"[Discuter] depart du salon {colis_id}")
        
        
def serve():
    s = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    pbg.add_SuiviColisServicer_to_server(Service(), s)
    s.add_insecure_port("[::]:50051")
    s.start()
    print("Serveur démarré sur le port50051")
    s.wait_for_termination()

if __name__ == "__main__":
    serve()