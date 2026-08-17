# Prototype « suivi de colis » — les 4 patterns gRPC

Bac à sable de démonstration des **quatre patterns gRPC** sur un cas unique :
le suivi d'un colis, du dépôt jusqu'à la preuve de livraison.

Un seul contrat, `proto/suivi_colis.proto` (package `colis`, service
`SuiviColis`), un serveur Python, et des clients dans **deux langages**.

| Pattern | RPC | Client Python | Client Go |
| :-- | :-- | :-- | :-- |
| Unary | `CreerColis` | `unary.py` | `go run . creer` |
| Server streaming | `SuivreColis` | `server_streaming.py` | `go run . suivre` |
| Client streaming | `EnvoyerPreuve` | `client_streaming.py` | `go run . envoyer` |
| Bidirectional | `Discuter` | `bidi.py` | `go run . chat` |

## Arborescence

```text
sandbox/rodrigue/
├── proto/suivi_colis.proto   le contrat, partagé par les deux langages
├── server.py                 le serveur (les 4 handlers)
├── etat.py                   logique métier (dict + threading.Lock)
├── unary.py                  client unaire
├── server_streaming.py       client server streaming
├── client_streaming.py       client client streaming
├── bidi.py                   client bidirectionnel
├── photos/                   3 fichiers de 200 Ko pour le test d'upload
├── gen.sh                    génération des stubs Python ET Go
└── go/
    ├── go.mod
    ├── main.go               les 4 clients Go, en sous-commandes
    └── colispb/              code généré (non versionné)
```

## Côté Python

```bash
python3 -m venv .venv
.venv/bin/pip install grpcio grpcio-tools
./gen.sh                       # génère suivi_colis_pb2*.py
```

Lancement — un terminal pour le serveur, un par client :

```bash
.venv/bin/python server.py                  # terminal 0

.venv/bin/python unary.py                   # -> affiche COL-XXXX
.venv/bin/python server_streaming.py COL-XXXX
.venv/bin/python client_streaming.py COL-XXXX
.venv/bin/python bidi.py COL-XXXX livreur
```

---

## Multi-langage

Le dossier `go/` contient les **mêmes quatre clients, écrits en Go**. Ils
parlent au **serveur Python déjà lancé**, sur `localhost:50051`, sans qu'une
seule ligne de ce serveur n'ait été modifiée.

### 1. Installer Go

```bash
# Debian / Ubuntu
sudo apt install golang-go

# ou bien l'archive officielle, sans droits root :
curl -fsSL https://go.dev/dl/go1.26.6.linux-amd64.tar.gz | tar -C ~/.local/lib -xz
export PATH="$HOME/.local/lib/go/bin:$PATH"

go version
```

### 2. Installer les plugins de génération

`protoc` ne sait pas produire du Go tout seul : il délègue à deux plugins,
qu'on installe avec Go lui-même.

```bash
go install google.golang.org/protobuf/cmd/protoc-gen-go@latest
go install google.golang.org/grpc/cmd/protoc-gen-go-grpc@latest

# les binaires atterrissent dans $(go env GOPATH)/bin, qui doit être dans le PATH
export PATH="$PATH:$(go env GOPATH)/bin"
```

> Pas besoin d'installer `protoc` séparément : `gen.sh` réutilise le
> compilateur embarqué dans `grpcio-tools` (`python -m grpc_tools.protoc`),
> c'est le même binaire et il sait charger les plugins Go.

### 3. Générer

```bash
./gen.sh
```

Le script génère désormais les deux côtés :

```text
Stubs Python generes.
Stubs Go generes.
```

Le code Go sort dans `go/colispb/`, grâce à la seule ligne ajoutée au
contrat pour l'occasion — sans aucun effet sur la génération Python :

```proto
option go_package = "./colispb";
```

Si les plugins sont absents, `gen.sh` génère le Python et signale simplement
que la partie Go est ignorée : le workflow Python reste intact.

### 4. Lancer les clients Go

Le serveur Python doit tourner. Les commandes se lancent depuis `go/` :

```bash
cd go

go run . creer                     # unary        -> CreerColis
go run . suivre  COL-XXXX          # server strm  -> SuivreColis
go run . envoyer COL-XXXX          # client strm  -> EnvoyerPreuve
go run . chat    COL-XXXX livreur  # bidi         -> Discuter
```

Un seul binaire avec quatre sous-commandes, plutôt que quatre exécutables :
cela évite quatre `go.mod` et quatre dossiers pour un si petit prototype.
Le dernier argument de `chat` est le rôle, `livreur` ou `client`
(défaut : `client`) ; `/quit` termine la discussion.

### Ce que cela démontre

**Un seul fichier `.proto`, deux langages, un seul serveur.** Le client Go et
le client Python sont des programmes sans rien en commun — pas de
bibliothèque partagée, pas de format d'échange écrit à la main — et ils
travaillent pourtant sur les mêmes colis : un colis créé par `go run . creer`
se suit avec `server_streaming.py`, et un client `chat` Go discute en temps
réel avec un `bidi.py` Python dans le même salon. C'est exactement l'intérêt
d'un contrat IDL : le `.proto` est la seule chose que les deux équipes ont à
se mettre d'accord, le reste est généré.

### Correspondance Python / Go

Les quatre patterns se traduisent différemment dans les deux langages — c'est
la partie intéressante à lire dans `go/main.go` :

| | Python | Go |
| :-- | :-- | :-- |
| Unary | appel qui retourne l'objet | appel qui retourne `(*Réponse, error)` |
| Server streaming | `for maj in stub.SuivreColis(...)` | boucle sur `flux.Recv()` jusqu'à `io.EOF` |
| Client streaming | un **générateur** passé au stub | `flux.Send()` en boucle puis `CloseAndRecv()` |
| Bidirectionnel | générateur + boucle `for` | **deux goroutines** + un channel de fin |

Le bidirectionnel est le seul cas où Go impose du parallélisme explicite :
`Send()` et `Recv()` bloquent chacun de leur côté, il faut donc une goroutine
qui lit le clavier et une goroutine qui reçoit. La fin de session passe par
le channel `fini`, écrit par la goroutine de réception.

Dernier détail de vocabulaire : sur une erreur, Python affiche
`Erreur NOT_FOUND` et Go affiche `Erreur NotFound`. C'est le **même code
gRPC** (5) — seule la convention de nommage du langage change.
