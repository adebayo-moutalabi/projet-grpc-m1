FONCTIONNEMENT CLIENT-STREAMING : 

SERVEUR : 
En python, il reçoit chacun des entiers de la liste du client et les additionne progressivement. Quand il reçoit le onCompleted du client, il lui renvoie la somme qu'il a obtenue.
Lancement depuis /python-server
source venv/bin/activate
python3 server.py

CLIENT :
Le client java envoie entier par entier le contenu de sa liste. Quand il a terminé, il attend la réponse du serveur ou le timeout.
Lancement depuis /java-client
mvn clean compile exec:java


TL;DR
Le client envoie des entiers au serveur qui les additionne progressivement et renvoie la somme finale.
La différence avec le Unary est que le client envoie plusieurs requêtes et que la liste d'entiers est statique.