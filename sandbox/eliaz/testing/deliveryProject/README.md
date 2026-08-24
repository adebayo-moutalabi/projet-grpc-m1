# DeliveryTracking — Démonstration des 4 types de RPC gRPC

Ceci est le manuel utilisateur du prototype d'application de démonstration illustrant les **4 types de communication gRPC** (Unary, Server Streaming, Client Streaming, Bidirectional Streaming) à travers divers outils de suivi de livraison de colis.

- **1 Serveur** : Java (Maven)
- **2 Clients** : Python (client Sender + client Livreur)

---

## Table des matières

1. [Vue d'ensemble](#1-vue-densemble)
2. [Fil rouge](#2-fil-rouge)
3. [Le fichier .proto](#3-le-fichier-proto)
4. [Prérequis](#4-prérequis)
5. [Installation](#5-installation)
6. [Lancer l'application](#6-lancer-lapplication)
7. [Scénario de démonstration pas à pas](#7-scénario-de-démonstration-pas-à-pas)
8. [Comprendre ce qui se passe à l'exécution](#8-comprendre-ce-qui-se-passe-à-lexécution)
9. [Structure du code](#9-structure-du-code)
10. [Limitations connues](#10-limitations-connues)
11. [Dépannage](#11-dépannage)

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

## 2. Fil rouge


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

### 5.2. Installer le serveur Java

Aucune étape manuelle de génération de code : le plugin Maven `protobuf-maven-plugin` génère automatiquement les classes Java à partir de `proto/delivery.proto` au moment du build.

```bash
cd java-server
mvn clean compile
```

> La première exécution peut être plus longue : Maven télécharge le binaire `protoc` correspondant à l'os de votre machine.

### 5.3. Installer les clients Python

```bash
cd python-clients
python3 -m venv venv
source venv/bin/activate      # Windows : venv\Scripts\activate
pip install -r requirements.txt
```

**`requirements.txt`**
```
grpcio==1.64.0
grpcio-tools==1.64.0
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

Trois terminaux sont nécessaires pour une démonstration complète.

### Terminal 1 — Serveur Java

```bash
cd java-server
mvn clean compile exec:java
```

Sortie attendue :
```
Serveur DeliveryService démarré sur le port 50051
```

Le serveur reste bloquant : c'est normal, il attend les connexions entrantes.

### Terminal 2 — Client Expéditeur/Destinataire

```bash
cd python-clients
source venv/bin/activate
python sender_client.py
```

### Terminal 3 — Client Livreur

```bash
cd python-clients
source venv/bin/activate
python courier_client.py
```

---

## 7. Scénario de démonstration pas à pas

### Étape 1 — Créer une commande (Unary)

Dans le **Terminal 2** :
```
Adresse d'expédition : 1 rue de Rennes
Adresse de destination : 2 rue de Nantes
Description de l'objet : Colis fragile
Commande créée : a1b2c3d4 (confirmée : True)
```

📌 **Notez l'`order_id` affiché** (ici `a1b2c3d4`) : il devra être communiqué manuellement au livreur pour la suite (dans une vraie application, il serait transmis par SMS/e-mail — ici on le tape simplement dans le Terminal 3).

### Étape 2 — Suivre le livreur (Server Streaming)

Toujours dans le **Terminal 2**, choisissez l'option **1** du menu :
```
1. Suivre la livraison
2. Ouvrir le chat avec le livreur
3. Quitter
Choix : 1
Suivi de la commande a1b2c3d4 (Ctrl+C pour arrêter)...
Position du livreur : [95234.1 ; -152341.7]
Position du livreur : [-42318.9 ; 88213.4]
...
```
Une nouvelle position aléatoire arrive **toutes les 10 secondes**. Faites `Ctrl+C` pour revenir au menu.

### Étape 3 — Envoyer les photos de livraison (Client Streaming)

Dans le **Terminal 3** :
```
Numéro de la commande à livrer : a1b2c3d4
1. Envoyer les photos de preuve de livraison
2. Ouvrir le chat avec le client
3. Quitter
Choix : 1
Nom du fichier : photo1.png
Nom du fichier : photo2.png
Nom du fichier :
Accusé de réception : 2 photo(s) reçue(s)
```
Le serveur n'accuse réception (`UploadAck`) qu'une fois **toutes** les photos envoyées (ligne vide pour terminer le flux).

### Étape 4 — Discuter en cas de problème (Bidirectional Streaming)

Dans le **Terminal 2**, choisissez l'option **2**, puis dans le **Terminal 3**, choisissez également l'option **2**, avec le **même `order_id`** :

Terminal 2 :
```
> Bonjour, où êtes-vous ?
[livreur] J'arrive dans 5 minutes
```

Terminal 3 :
```
> J'arrive dans 5 minutes
[client] Bonjour, où êtes-vous ?
```

Les messages tapés dans un terminal apparaissent instantanément dans l'autre.

---

## 8. Comprendre ce qui se passe à l'exécution

### 8.1. `CreateOrder` — Unary

1. Le client envoie **une seule** requête `OrderRequest` et attend **une seule** réponse.
2. Le serveur génère un `order_id` (UUID tronqué), crée une entrée `OrderState` dans sa `Map<orderId, OrderState>`, et répond immédiatement.
3. L'appel est bloquant côté client (le stub Python attend la réponse avant de continuer).

### 8.2. `TrackDelivery` — Server Streaming

1. Le client envoie **une seule** requête (`TrackRequest` contenant l'`order_id`).
2. Le serveur répond par un **flux ouvert indéfiniment** : toutes les 10 secondes, il génère une position aléatoire et l'envoie via `onNext()`.
3. Côté serveur, un `ScheduledExecutorService` dédié gère cette temporisation. Un `ServerCallStreamObserver` avec `setOnCancelHandler` permet de détecter la déconnexion du client (`Ctrl+C`) et d'arrêter proprement ce thread planifié — sans quoi il continuerait à tourner indéfiniment en mémoire, même après le départ du client.
4. Côté client Python, un simple `for position in stub.TrackDelivery(request):` bloque à chaque itération jusqu'à la prochaine position reçue.

### 8.3. `UploadProof` — Client Streaming

1. Le client envoie **plusieurs** messages `Photo` successifs (un par photo), sans attendre de réponse intermédiaire.
2. Le serveur accumule les noms de fichiers dans une liste locale à l'appel, au fil des `onNext()`.
3. Quand le client termine son flux (ligne vide → générateur épuisé → `onCompleted()` déclenché côté serveur), le serveur enregistre les photos dans l'`OrderState` correspondant et répond **une seule fois** avec `UploadAck`.
4. Si l'`order_id` fourni n'existe pas, le serveur répond par une erreur `NOT_FOUND` dès la première photo reçue, sans attendre la fin du flux.

### 8.4. `SupportChat` — Bidirectional Streaming

C'est le RPC le plus complexe, car client et serveur échangent des messages de façon totalement asynchrone et indépendante.

1. **Routage par commande** : chaque `OrderState` maintient sa propre liste `chatParticipants` (les `StreamObserver` des deux parties connectées à *cette* commande). Un message envoyé par le client n'est donc rediffusé qu'au livreur de la **même** commande, jamais aux autres conversations en cours.
2. **Message de connexion silencieux** : dès l'ouverture du flux, chaque client envoie automatiquement un premier `ChatMessage` avec `content=""`. Ce message sert uniquement à enregistrer le participant dans `chatParticipants` côté serveur, sans être affiché ni rediffusé — cela évite qu'un message envoyé juste après l'ouverture du chat par l'autre partie ne soit perdu faute d'enregistrement.
3. **Diffusion synchronisée** : lorsqu'un message « réel » (contenu non vide) arrive, le serveur le rediffuse à tous les *autres* participants de la commande. L'appel à `onNext()` est protégé par un bloc `synchronized` sur chaque `StreamObserver` cible, car plusieurs threads (un par participant connecté) peuvent potentiellement vouloir y écrire en parallèle.
4. **Journalisation des connexions/déconnexions** : le serveur affiche en console quand un acteur (`client` ou `livreur`) rejoint ou quitte le chat d'une commande, ce qui est utile pour suivre en direct ce qu'il se passe pendant une démonstration.

### 8.5. Ce que vous devriez observer côté serveur

```
Serveur DeliveryService démarré sur le port 50051
Commande créée : a1b2c3d4 (1 rue de Rennes -> 2 rue de Nantes)
Position envoyée pour a1b2c3d4 : [95234.1 ; -152341.7]
Photo reçue pour a1b2c3d4 : photo1.png
Photo reçue pour a1b2c3d4 : photo2.png
2 photo(s) reçue(s) au total pour a1b2c3d4
client a rejoint le chat de la commande a1b2c3d4
livreur a rejoint le chat de la commande a1b2c3d4
[a1b2c3d4] client: Bonjour, où êtes-vous ?
[a1b2c3d4] livreur: J'arrive dans 5 minutes
client a quitté le chat de la commande a1b2c3d4
livreur a quitté le chat de la commande a1b2c3d4
```

---

## 9. Structure du code

### 9.1. Serveur Java

| Fichier | Rôle |
|---|---|
| `DeliveryServer.java` | Point d'entrée : démarre le serveur gRPC sur le port 50051. |
| `DeliveryServiceImpl.java` | Implémentation des 4 RPC. Contient la `Map<orderId, OrderState>`, seule source de vérité partagée entre les appels. |
| `OrderState.java` | État d'**une** commande : liste des photos reçues et liste des participants au chat de cette commande. |

### 9.2. Clients Python

| Fichier | Rôle |
|---|---|
| `sender_client.py` | Client Expéditeur/Destinataire : `CreateOrder`, `TrackDelivery`, `SupportChat`. |
| `courier_client.py` | Client Livreur : `UploadProof`, `SupportChat`. |

### 9.3. Pourquoi `OrderState` utilise des `CopyOnWriteArrayList`

Le serveur gRPC traite chaque appel dans son propre thread. Pour une même commande, plusieurs threads peuvent donc accéder en parallèle à son `OrderState` (par exemple : le client et le livreur qui écrivent simultanément dans le chat). `CopyOnWriteArrayList` garantit que ces accès concurrents restent sûrs, sans nécessiter de verrouillage manuel lors des lectures (notamment lors de la boucle de rediffusion des messages).

---

## 10. Limitations connues

Ces simplifications sont assumées pour garder l'exemple pédagogique et concis :

- **Pas de persistance** : toutes les données (`orders`) vivent en mémoire et sont perdues à l'arrêt du serveur.
- **Pas de nettoyage automatique** : une commande créée reste indéfiniment dans la `Map`, même après livraison complète — aucun mécanisme de clôture/TTL n'est implémenté.
- **Position GPS simulée** : les coordonnées sont générées aléatoirement côté serveur, sans lien avec une vraie géolocalisation.
- **Pas de sécurité/TLS** : les échanges utilisent `usePlaintext()` / `insecure_channel`, adapté à une démonstration locale mais à proscrire en production.
- **`order_id` transmis "à la main"** : dans cette version, le livreur doit connaître l'`order_id` par un autre canal (il n'y a pas de notification automatique des nouvelles commandes vers le livreur).
- **Perte de message théoriquement possible dans le chat** : le message de connexion silencieux réduit fortement le risque de perte du tout premier message échangé, mais ne l'élimine pas à 100 % dans l'absolu (pas d'accusé de réception explicite de l'enregistrement).

---

## 11. Dépannage

| Symptôme | Cause probable | Solution |
|---|---|---|
| `invalid target release: XX` à la compilation Java | Version de `maven-compiler-plugin` trop ancienne pour votre JDK | Déclarer explicitement `maven-compiler-plugin` (≥3.13.0) et utiliser `maven.compiler.release` dans le `pom.xml` |
| `The import fr.istic.grpc.delivery cannot be resolved` dans l'IDE, mais `mvn compile exec:java` fonctionne | L'IDE n'a pas encore indexé `target/generated-sources/protobuf` | Forcer un rafraîchissement Maven dans l'IDE (`Update Project` / `Reload All Maven Projects`) après un `mvn compile` |
| `StatusRuntimeException: UNAVAILABLE` côté client Python | Le serveur Java n'est pas démarré, ou tourne sur un autre port | Vérifier que `mvn clean compile exec:java` est bien lancé et affiche `démarré sur le port 50051` |
| Le chat ne montre rien lors du tout premier message | Le message de connexion silencieux n'a pas été implémenté ou a été supprimé par erreur | Vérifier la présence du `yield ChatMessage(..., content="")` en tête du générateur côté client |
| `Commande inconnue : xxxxxxxx` | L'`order_id` saisi côté livreur ne correspond à aucune commande créée | Vérifier l'`order_id` affiché par `sender_client.py` lors de la création, et le recopier exactement (sensible à la casse) |
| Modification du `.proto` sans effet côté Python | Le code Python n'est pas régénéré automatiquement (contrairement à Java) | Relancer manuellement la commande `python -m grpc_tools.protoc ...` après toute modification du `.proto` |

---

## Aller plus loin

Idées d'extensions pour approfondir la démonstration :
- Notifier automatiquement le livreur des nouvelles commandes (nécessite un 5ᵉ RPC de server streaming côté livreur, cf. discussion de conception).
- Ajouter une clôture explicite de commande (RPC `CompleteDelivery`) pour nettoyer la `Map` côté serveur.
- Sécuriser les échanges avec TLS (`usePlaintext()` → certificats).
- Persister l'état des commandes dans une base de données plutôt qu'en mémoire.
