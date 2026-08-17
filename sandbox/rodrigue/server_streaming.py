import sys
import grpc
import suivi_colis_pb2 as pb
import suivi_colis_pb2_grpc as pbg

cid = sys.argv[1]

with grpc.insecure_channel("localhost:50051") as ch:
    stub = pbg.SuiviColisStub(ch)
    try:
        for maj in stub.SuivreColis(pb.SuivreColisRequest(colis_id=cid)):
            print(f"{maj.progression_pct:3d}%  "
                  f"{maj.position.latitude:.4f}, {maj.position.longitude:.4f}  "
                  f"{pb.Statut.Name(maj.statut)}")
        print("Flux termine.")
    except grpc.RpcError as e:
        print(f"Erreur {e.code().name} : {e.details()}")