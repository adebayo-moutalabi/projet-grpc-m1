FONCTIONNEMENT DU UNARY :   (une req/une rep)

SERVEUR
Le serveur est codé en Python. Il définit le service d'addition de deux entiers qui sera appelé par le client.
Pour lancer le serveur, il est nécessaire d'avoir installé au préalable les outils grpcio et grpcio-tools :
pip install grpcio==1.64.0
pip install grpcio-tools==1.64.0
Puis :
python3 -m venv venv        (c'est l'environnement virtuel de python, requis pour du grpc)
source venv/bin/activate
python3 server.py

Un bon indicateur visuel du setup : la ligne de cmd de votre terminal doit commencer par (venv) avant le hostname

CLIENT
Le client est en Java, il démarre une connexion vers le serveur et appelle sa méthode après avoir demandé deux entiers au client.
Lancer le client depuis /java-client/ se fait via Maven :
mvn clean compile exec:java



Le client envoie la requête contenant les deux entiers au serveur, qui les additionne et renvoie la somme au client.