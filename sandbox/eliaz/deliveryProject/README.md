# DeliveryTracking — Démonstration des 4 types de RPC gRPC

**(clic droit sur le fichier -> Open Preview)**

Ceci est le manuel utilisateur du prototype d'application de démonstration illustrant les **4 types de communication gRPC** (Unary, Server Streaming, Client Streaming, Bidirectional Streaming) à travers divers outils de suivi de livraison de colis.

- **1 Serveur** : Java (Maven)
- **2 Clients** : Python (client Sender + client Livreur)

---

## Table des matières

1. [Vue d'ensemble](#1-vue-densemble)
2. [Order_id](#2-order_id)
3. [Le fichier .proto](#3-le-fichier-proto)
4. [Prérequis](#4-prérequis)
5. [Installation](#5-installation)
6. [Lancer l'application](#6-lancer-lapplication)
7. [Scénario de démonstration](#7-scénario-de-démonstration)
8. [Précisions sur les 4 types de service](#8-précisions-sur-les-4-types-de-service)
9. [Structure du code](#9-structure-du-code)
10. [Limitations actuelles](#10-limitations-actuelles)
---

## 1. Vue d'ensemble

L'application simule le parcours d'un colis, du dépôt de la commande jusqu'à sa livraison. Chaque étape de ce parcours correspond à l'un des 4 types de service fournis par gRPC :

| Étape métier | Type de RPC | Méthode |
|---|---|---|
| Le Sender crée une commande | **Unary** | `CreateOrder` |
| Le Sender suit la position du  livreur en temps réel | **Server Streaming** | `TrackDelivery` |
| Le Livreur envoie au serveur les photos de preuve de livraison | **Client Streaming** | `UploadProof` |
| Le Sender et le Livreur discutent en cas de problème | **Bidirectional Streaming** | `SupportChat` |

Toutes ces méthodes sont regroupées dans un unique service gRPC : `DeliveryService`.

---

## 2. Order_id


Le fil rouge de l'application est l'**`order_id`.** Il est généré côté serveur lors de `CreateOrder`, puis réutilisé par le client dans tous les appels suivants (`TrackDelivery`, `SupportChat`) et communiqué manuellement au livreur, qui l'utilise à son tour (`UploadProof`, `SupportChat`). C'est ce qui permet au serveur de retrouver, pour chaque appel, à quelle commande il se rapporte, via une classe `OrderState` associée à chaque `order_id`.

---

## 3. Le fichier `.proto`

Le fichier **`proto/delivery.proto`**
est le **contrat partagé** entre le serveur Java et les clients Python : chacun génère son propre code à partir de ce même fichier, garantissant que les deux côtés parlent le même langage quand bien même celui de leur code respectif est différent.

---

## 4. Prérequis

| Outil | Version | Pour vérifier |
|---|---|---|
| JDK | 17 ou supérieur | `java -version` |
| Maven | 3.8+ | `mvn -version` |
| Python | 3.9+ | `python3 --version` |
| pip | à jour | `pip --version` |

Pour ce prototype, aucune base de données ni service externe n'est nécessaire : l'état de l'application est conservé **en mémoire** côté serveur, le temps de son exécution.

---

## 5. Installation

### 5.1. Arborescence du projet

```
deliveryProject/
├── proto/
│   └── delivery.proto
├── java-server/
│   ├── pom.xml
│   └── src/main/java/fr/istic/grpc/delivery/server/
│       ├── DeliveryServer.java
│       ├── DeliveryServiceImpl.java
│       └── OrderState.java
└── python-clients/
    ├── requirements.txt
    ├── sender_client.py
    └── courier_client.py
```
Note : une fois que vous aurez lancé le serveur java une première fois, un nouveau fichier `target` contenant les classes java générées sera visible dans `java-server/target/`
### 5.2. Setup du serveur Java

Il n'y a aucune étape manuelle de génération de code : le plugin Maven `protobuf-maven-plugin` génère automatiquement les classes Java à partir de `proto/delivery.proto` au moment du build.

```bash
cd java-server
mvn clean compile
```

> La première exécution peut être plus longue : Maven télécharge le binaire `protoc` correspondant à l'os de votre machine.

### 5.3. Setup des clients Python

```bash
cd python-clients
python3 -m venv venv
source venv/bin/activate 
pip install grpcio==1.64.0
pip install grpcio-tools==1.64.0
```

Contrairement à Java, la génération du code Python est **manuelle** et doit être relancée à chaque modification du `.proto` :

```bash
python -m grpc_tools.protoc \
  -I ../proto \
  --python_out=. \
  --grpc_python_out=. \
  ../proto/delivery.proto
```

Cela produit deux fichiers dans `python-clients/` :
- `delivery_pb2.py` — les classes de messages (`OrderRequest`, `Position`, etc.)
- `delivery_pb2_grpc.py` — le stub client et la classe de service

---

## 6. Lancer l'application

Trois terminaux sont nécessaires pour une bonne démonstration.

### Terminal 1 — Serveur Java

```bash
cd java-server
mvn clean compile exec:java
```

Sortie attendue :
```
Serveur DeliveryService démarré sur le port 8080
```

Il n'y a aucune autre action requise du côté serveur, il reste cependant un bon point d'observation de l'exécution.

### Terminal 2 — Client Sender

```bash
cd python-clients
source venv/bin/activate
python sender_client.py
```

### Terminal 3 — Client Livreur

```bash
cd python-clients
source venv/bin/activate
python livreur_client.py
```

---

## 7. Scénario de démonstration

### Étape 1 — Créer une commande (Unary)

Dans le Terminal 2 **(SENDER)** :
```
Adresse d'expédition : 1 rue de Paris
Adresse de destination : 2 rue de Rennes
Description de l'objet : Table
Commande créée : a1b2 (confirmée : True)
```

**Notez l'`order_id` affiché** (ici `a1b2`) : il devra être communiqué manuellement au livreur pour la suite (dans une vraie application, il serait transmis par SMS/e-mail. Ici on le tape simplement dans le Terminal 3 du Livreur).

### Étape 2 — Suivre le livreur (Server Streaming)

Toujours dans le Terminal 2 **(SENDER)**, choisissez l'option **1** du menu :
```
1. Suivre la livraison
2. Ouvrir le chat avec le livreur
3. Quitter
Choix : 1
Suivi de la commande a1b2 (Ctrl+C pour quitter le stream)...
Position du livreur : [95234.1 ; -152341.7]
Position du livreur : [-42318.9 ; 88213.4]
...
```
Une nouvelle position arrive **toutes les 10 secondes**. Les coordonnées sont pour l'instant générées aléatoirement et n'ont donc aucun cohérence entre elles.  
Faites `Ctrl+C` pour revenir au menu.

### Étape 3 — Envoyer les photos de livraison (Client Streaming)
Dans cette application qui n'est encore qu'un prototype, aucune photo réelle n'est envoyé : ici le Livreur envoie des chaînes de caractères qu'on imagine être des photos.

Dans le Terminal 3 **(LIVREUR)** :
```
Numéro de la commande à livrer : a1b2 
1. Envoyer les photos de preuve de livraison
2. Ouvrir le chat avec le client
3. Quitter
Choix : 1
Entrez les noms des photos à envoyer (ligne vide pour terminer) :
Nom du fichier : photo1.png
Nom du fichier : photo2.png
Nom du fichier :    //ligne vide
ACK Serveur : 2 photo(s) reçue(s)
```
Le serveur n'accuse réception (via `UploadAck`) qu'une fois **toutes** les photos envoyées par le Livreur (ligne vide pour terminer le flux).

### Étape 4 — Discuter en cas de problème (Bidirectional Streaming)

Dans le **Terminal 2**, choisissez l'option **2**, puis dans le **Terminal 3**, choisissez également l'option **2**, avec le **même `order_id`** :

Terminal 2 **(SENDER)**:
```
> Bonjour, où êtes-vous ?
[Livreur] J'arrive dans 5 minutes
```

Terminal 3 **(LIVREUR)**:
```
[Client] Bonjour, où êtes-vous ?
> J'arrive dans 5 minutes
```

Les messages tapés dans un terminal apparaissent instantanément dans l'autre. Il est possible que des sauts de ligne intempestifs apparaissent mais ils ne perturbent en rien le fonctionnement du chat. 
Pour quitter le chat, le participant doit entrer une ligne vide. 

De plus, tous les messages envoyés ainsi que les connexions et déconnexions au chat sont visibles dans le terminal Serveur.

---

## 8. Précisions sur les 4 types de service 

### 8.1. `CreateOrder` — Unary

1. Le client envoie **une seule** requête `OrderRequest` et attend **une seule** réponse. C'est le principe de l'Unary.
2. Le serveur génère un `order_id` , crée une entrée `OrderState` dans sa `Map<orderId, OrderState>`, et répond immédiatement.
3. L'appel est bloquant côté client (le stub Python attend la réponse avant de continuer son exécution).

### 8.2. `TrackDelivery` — Server Streaming

1. Le client envoie **une seule** requête (`TrackRequest` contenant l'`order_id` du colis qu'il veut suivre).
2. Le serveur répond par un **flux ouvert indéfiniment** : toutes les 10 secondes, il génère une position aléatoire et l'envoie via `onNext()`.
3. On utilise un `ScheduledExecutorService` pour timer l'envoi de la position. Un `ServerCallStreamObserver` avec `setOnCancelHandler` permet de détecter la déconnexion du client (`Ctrl+C`) et d'arrêter proprement le thread sans quoi il continuerait à tourner indéfiniment en mémoire, même après le départ du client.
4. Côté client Python, un simple `for position in stub.TrackDelivery(request):` bloque à chaque itération jusqu'à la prochaine position reçue, ou bien un (`Ctrl+C`).

### 8.3. `UploadProof` — Client Streaming

1. Le client envoie **plusieurs** messages `Photo` successifs (un par photo), sans attendre de réponse intermédiaire.
2. Le serveur accumule les noms de fichiers dans une liste locale à l'appel, au fil des `onNext()`.
3. Quand le client termine son flux (ligne vide → `onCompleted()` déclenché côté serveur), le serveur enregistre les photos dans l'`OrderState` correspondant et répond **une seule fois** avec `UploadAck`.
4. Si l'`order_id` fourni par le livreur au préalable n'existe pas, le serveur répond par une erreur `NOT_FOUND` dès la première photo reçue, sans attendre la fin du flux.

### 8.4. `SupportChat` — Bidirectional Streaming

C'est le RPC le plus complexe, car client et serveur échangent des messages de façon totalement asynchrone et indépendante, sans ordre défini.

1. **Routage par commande** : chaque `OrderState` maintient sa propre liste `chatParticipants` (les `StreamObserver` des deux parties connectées à *cet* `order_id`). Un message envoyé par le client n'est donc rediffusé qu'au livreur de la **même** commande, jamais aux autres conversations en cours.
2. **Message de connexion silencieux** : dès l'ouverture du flux, chaque client envoie automatiquement un premier `ChatMessage` avec `content=""`. Ce message vide sert uniquement à enregistrer le participant dans `chatParticipants` côté serveur, sans être affiché ni rediffusé. Cela évite qu'un message envoyé juste après l'ouverture du chat par l'autre partie ne soit perdu faute d'enregistrement.
3. **Diffusion synchronisée** : lorsqu'un message « réel » (dont le contenu est non vide) arrive, le serveur le rediffuse à tous les autres participants de la commande. L'appel à `onNext()` est protégé par un bloc `synchronized` sur chaque `StreamObserver` cible, car plusieurs threads (un par participant connecté) peuvent potentiellement vouloir y écrire en parallèle.
4. **Journalisation des connexions/déconnexions** : le serveur affiche en console quand un acteur (`client` ou `livreur`) rejoint ou quitte le chat d'une commande, ce qui est utile pour suivre en direct ce qu'il se passe pendant une exécution.



## 9. Structure du code

### 9.1. Serveur Java

| Fichier | Rôle |
|---|---|
| `DeliveryServer.java` | Point d'entrée : démarre le serveur gRPC sur le port 8080. |
| `DeliveryServiceImpl.java` | Implémentation des 4 RPC. Contient la `Map<orderId, OrderState>`, partagée entre les appels. |
| `OrderState.java` | État d'**une** commande : liste des photos reçues et liste des participants au chat de cette commande. |

### 9.2. Clients Python

| Fichier | Rôle |
|---|---|
| `sender_client.py` | Client Expéditeur : `CreateOrder`, `TrackDelivery`, `SupportChat`. |
| `livreur_client.py` | Client Livreur : `UploadProof`, `SupportChat`. |


## 10. Limitations actuelles

Ces simplifications sont assumées pour garder l'exemple concis :

- **Pas de persistance** : toutes les données (`orders`) vivent en mémoire et sont perdues à l'arrêt du serveur.
- **Pas de nettoyage automatique** : une commande créée reste indéfiniment dans la `Map`, même après livraison complète — aucun mécanisme de nettoyage n'est implémenté.
- **Position GPS simulée** : les coordonnées sont générées aléatoirement côté serveur, sans lien avec une vraie géolocalisation.
- **Pas de sécurité/TLS** : aucun échange n'est chiffré, ce qu'on veut en général éviter en production.
- **`order_id` transmis "magique"** : dans cette version, le livreur doit connaître l'`order_id` par un autre canal (il n'y a pas de notification automatique des nouvelles commandes vers le livreur, car cela demanderait un novueau service RPC).
- **Perte de message théoriquement possible dans le chat** : le message de connexion silencieux réduit fortement le risque de perte du tout premier message échangé, mais ne l'élimine pas à 100 % dans l'absolu .

---

