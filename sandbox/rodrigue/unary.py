import random
import grpc
import suivi_colis_pb2 as pb
import suivi_colis_pb2_grpc as pbg

NOMS = ["Alice", "Bob", "Chloe", "David", "Emma", "Farid", "Gaelle", "Hugo"]
ADRESSES = ["12 rue de Rennes", "3 avenue Jean Jaures", "17 place Sainte-Anne",
            "8 boulevard de la Liberte", "45 rue Saint-Michel",
            "22 quai Emile Zola", "5 rue Vasselot"]


def pos_rennes():
    """Un point au hasard dans l'agglomeration rennaise."""
    return pb.Position(latitude=48.10 + random.uniform(0, 0.06),
                       longitude=-1.70 + random.uniform(0, 0.08))


dest    = random.choice(NOMS)
adresse = random.choice(ADRESSES)

with grpc.insecure_channel("localhost:50051") as ch:
    stub = pbg.SuiviColisStub(ch)
    try:
        r = stub.CreerColis(pb.CreerColisRequest(
            destinataire=dest, adresse=adresse,
            depot=pos_rennes(), destination=pos_rennes()))
        print(f"{r.colis_id}  {pb.Statut.Name(r.statut)}  ->  {dest}, {adresse}")
    except grpc.RpcError as e:
        print(f"Erreur {e.code().name} : {e.details()}")