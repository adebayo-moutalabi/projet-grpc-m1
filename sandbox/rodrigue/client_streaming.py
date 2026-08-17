import sys
import grpc
import suivi_colis_pb2 as pb
import suivi_colis_pb2_grpc as pbg

cid = sys.argv[1]

def chunks(colis_id, chemins, taille=64*1024):
    try:
        for p in chemins:
            with open(p, "rb") as f:
                while True:
                    bloc = f.read(taille)
                    if not bloc:
                        break
                    yield pb.PhotoChunk(colis_id=colis_id, nom_fichier=p,
                                        contenu=bloc, fin_de_photo=False)
            yield pb.PhotoChunk(colis_id=colis_id, nom_fichier=p,
                                contenu=b"", fin_de_photo=True)
    except Exception as e:
        print(f"Erreur dans le générateur {p} : {e}")
        raise

with grpc.insecure_channel("localhost:50051") as ch:
    stub = pbg.SuiviColisStub(ch)
    try:
        r = stub.EnvoyerPreuve(chunks(cid, ["photos/p1.png", "photos/p2.png",
                                            "photos/p3.png"]))
        print(r.photos_recues, "photos,", r.octets_recus, "octets,",
              pb.Statut.Name(r.statut))
    except grpc.RpcError as e:
        print(f"Erreur {e.code().name} : {e.details()}")