FONCTIONNEMENT DU SERV STREAMING :  (une req/un flux de réponses)

SERVEUR :
Le serveur codé en java dans DateNotifServer implémente le service SubscribeDate dans DateNotifServiceImpl.
Chaque client qui souscrit au service DateNotif reçoit la date toutes les 10 secondes du serveur.
Lancement depuis /java-server : 
mvn clean compile exec:java


CLIENT : 
Codé en python, il souscrit au service du serveur gRPC et reçoit la date chaque 10s jusqu'à sa déconnexion (ou la fermeture du serveur)
Lancement depuis /python-client :
source bin/venv/activate
python3 client.py


Le client souscrit au service de notification de Date du serveur et les reçoit toutes les 10s indéfiniment jusqu'à ce qu'un des deux acteurs soit terminé.