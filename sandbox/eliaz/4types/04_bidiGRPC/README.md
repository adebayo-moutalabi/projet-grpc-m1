FONCTIONNEMENT BIDI STREAMING : 

SERVEUR : 
En Java, il reçoit des entiers successifs du client, les additionne et renvoie à chaque entier reçu la somme actuelle au client.
Lancement depuis /java-server
mvn clean compile exec:java

CLIENT : 
En python, il permet à l'user de choisir les entiers à envoyer au serveur. L'user peut arrêter le streaming avec une ligne vide, auquel cas la connexion sera interrompue. Il reçoit à chaque entier la valeur intermédiaire de la somme locale au serveur.
Lancement depuis /python-client
source venv/bin/activate
python3 client.py


TL;DR : 
Le client choisit et envoie des entiers à la suite au serveur qui les additionne et les renvoie au client à chaque nouvelle réception, jusqu'à ce que le client termine l'échange. C'est un format 1 req/1 rep mais en bidirectional streaming.