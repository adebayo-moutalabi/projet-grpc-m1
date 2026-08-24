Pour lancer l'exec, il vous faudra 3 terminaux : 


TERMINAL SERVEUR : 
Se placer dans /deliveryProject/java-server/
Exec : mvn clean compile exec:java              (note : update votre Maven si besoin, essayez pas de lancer depuis vscode ça marchera pas)

Y'a rien à faire dans le serveur, c'est utile pour bien suivre ce qui se passe par contre.



///////////////////////////////////////////////////////////

TERMINAUX CLIENTS : 
(sender/livreur)


Sender : 
Se placer dans /deliveryProject/python-client/
Exec : 
source venv/bin/activate        -> ça crée un environnement virtuel python requis pour grpc, sinon y'aura une erreur. 
python3 sender_client.py

//

Livreur : 
Se placer dans /deliveryProject/python-client/
Exec : 
source venv/bin/activate        -> pareil
python3 livreur_client.py

/////////////////////////////////////////////////////////

Exécution normale : 
Le sender crée un colis (Unary)
Il peut demander à avoir la pos du livreur chaque 10s (Sstreaming)
Le livreur peut envoyer des photos au serveur (ici c'est des string), autant qu'il veut (Cstreaming)
Et les deux peuvent rejoindre un chat pour parler (BidiStreaming)