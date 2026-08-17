import sys, threading
import grpc
import suivi_colis_pb2 as pb
import suivi_colis_pb2_grpc as pbg

cid  = sys.argv[1]
role = sys.argv[2] if len(sys.argv) > 2 else "client"
ROLE = pb.LIVREUR if role == "livreur" else pb.CLIENT

def saisie():
    """Generateur : chaque ligne tapee devient un ChatMessage."""
    yield pb.ChatMessage(colis_id=cid, expediteur=ROLE, auteur=role,
                         texte=f"{role} a rejoint la discussion")
    for ligne in sys.stdin:
        ligne = ligne.strip()
        if ligne in ("/quit", "/q"):
            break
        if ligne:
            yield pb.ChatMessage(colis_id=cid, expediteur=ROLE,
                                 auteur=role, texte=ligne)

with grpc.insecure_channel("localhost:50051") as ch:
    stub = pbg.SuiviColisStub(ch)
    try:
        for msg in stub.Discuter(saisie()):
            print(f"  <{msg.auteur}> {msg.texte}")
    except grpc.RpcError as e:
        print(f"Erreur {e.code().name} : {e.details()}")