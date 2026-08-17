import threading, uuid
from dataclasses import dataclass, field

_verrou = threading.Lock()          # Objectif : proteger l'acces a _colis
_colis  = {}                        # colis_id -> Colis

@dataclass
class Colis:
    colis_id: str
    destinataire: str
    adresse: str
    depot: tuple          # (lat, lon)
    destination: tuple
    statut: int           # suivi_colis_pb2.CREE / EN_TRANSIT / LIVRE
    progression: int = 0
    photos: list = field(default_factory=list)
    messages: list = field(default_factory=list)   # historique du chat

def creer(destinataire, adresse, depot, destination):
    # genere "COL-" + uuid.uuid4().hex[:4].upper()
    # stocke sous _verrou, retourne le Colis
    colis_id = "COL-" + uuid.uuid4().hex[:4].upper()
    c = Colis(colis_id=colis_id, destinataire=destinataire, adresse=adresse, depot=depot, destination=destination, statut=1)
    with _verrou:
        _colis[colis_id] = c
    return c 

def lire(colis_id):
    # retourne le Colis ou None -- sous _verrou
    with _verrou:
        return _colis.get(colis_id)

def avancer(colis_id, pct):
    # met a jour progression + statut, retourne le Colis
    # CREE -> EN_TRANSIT des que pct > 0 ; -> LIVRE a 100
    with _verrou:
        c = _colis.get(colis_id)
        if c is None:
            return None
        c.progression = pct
        if pct > 0 and c.statut == 1:
            c.statut = 2
        if pct >= 100:
            c.statut = 3
        return c
    
# Bloc de test

if __name__ == "__main__":
    c = creer("Alice", "1 rue de Paris", (48.8566, 2.3522), (48.8584, 2.2945))
    print(f"{c.colis_id} cree       --> statut={c.statut}")
    
    avancer(c.colis_id, 50)
    print(f"{c.colis_id} à 50%       --> statut={lire(c.colis_id).statut}")
    
    avancer(c.colis_id, 100)
    print(f"{c.colis_id} à 100% --> statut={lire(c.colis_id).statut}")
    
    print(f"Inconnu       --> {lire('COL-XXXX')}")