// Client Go du prototype "suivi de colis".
//
// Ce binaire parle au serveur Python (server.py) qui ecoute sur
// localhost:50051. Les deux programmes n'ont rien en commun a part le
// fichier proto/suivi_colis.proto : c'est la demonstration concrete de
// l'interoperabilite multi-langage de gRPC.
//
// Un seul binaire, quatre sous-commandes, une par pattern gRPC :
//
//	go run . creer
//	go run . suivre  COL-XXXX
//	go run . envoyer COL-XXXX
//	go run . chat    COL-XXXX livreur
package main

import (
	"bufio"
	"context"
	"fmt"
	"io"
	"math/rand"
	"os"
	"path/filepath"
	"strings"

	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials/insecure"
	"google.golang.org/grpc/status"

	"colisgo/colispb"
)

const adresseServeur = "localhost:50051"

var noms = []string{"Alice", "Bob", "Chloe", "David", "Emma", "Farid", "Gaelle", "Hugo"}

var adresses = []string{
	"12 rue de Rennes", "3 avenue Jean Jaures", "17 place Sainte-Anne",
	"8 boulevard de la Liberte", "45 rue Saint-Michel",
	"22 quai Emile Zola", "5 rue Vasselot",
}

func main() {
	if len(os.Args) < 2 {
		usage()
		os.Exit(2)
	}

	// grpc.NewClient ne se connecte pas tout de suite : la connexion est
	// etablie paresseusement au premier appel. Le serveur etant en clair
	// (pas de TLS), on lui passe des credentials "insecure".
	conn, err := grpc.NewClient(adresseServeur,
		grpc.WithTransportCredentials(insecure.NewCredentials()))
	if err != nil {
		fmt.Println("Connexion impossible :", err)
		os.Exit(1)
	}
	defer conn.Close()

	// Le stub est l'equivalent Go de SuiviColisStub cote Python.
	stub := colispb.NewSuiviColisClient(conn)

	switch os.Args[1] {
	case "creer":
		err = creer(stub)
	case "suivre":
		err = suivre(stub, argument(2, ""))
	case "envoyer":
		err = envoyer(stub, argument(2, ""))
	case "chat":
		err = chat(stub, argument(2, ""), argument(3, "client"))
	default:
		usage()
		os.Exit(2)
	}

	if err != nil {
		afficherErreur(err)
	}
}

// ----------------------------------------------------------------- UNARY ----
//
// Pattern unaire : une requete, une reponse. En Go cela se traduit par un
// simple appel de methode qui retourne (*Reponse, error) -- aucun stream,
// aucune goroutine. C'est le seul des quatre patterns qui ressemble a un
// appel de fonction ordinaire.
func creer(stub colispb.SuiviColisClient) error {
	destinataire := noms[rand.Intn(len(noms))]
	adresse := adresses[rand.Intn(len(adresses))]

	rep, err := stub.CreerColis(context.Background(), &colispb.CreerColisRequest{
		Destinataire: destinataire,
		Adresse:      adresse,
		Depot:        positionRennes(),
		Destination:  positionRennes(),
	})
	if err != nil {
		return err
	}

	fmt.Printf("%s  %s  ->  %s, %s\n", rep.GetColisId(), rep.GetStatut(), destinataire, adresse)
	return nil
}

// ------------------------------------------------------- SERVER STREAMING ---
//
// Pattern server streaming : une requete, un flux de reponses. L'appel ne
// retourne plus un objet mais un stream, sur lequel on boucle avec Recv().
// Recv() bloque jusqu'au message suivant et retourne io.EOF quand le serveur
// a fini d'emettre -- c'est l'equivalent Go du "for maj in stub.SuivreColis()"
// de Python.
func suivre(stub colispb.SuiviColisClient, colisID string) error {
	if colisID == "" {
		return fmt.Errorf("usage : go run . suivre COL-XXXX")
	}

	flux, err := stub.SuivreColis(context.Background(),
		&colispb.SuivreColisRequest{ColisId: colisID})
	if err != nil {
		return err
	}

	for {
		maj, err := flux.Recv()
		if err == io.EOF { // fin normale du flux
			break
		}
		if err != nil { // erreur gRPC : par exemple NOT_FOUND si le colis n'existe pas
			return err
		}
		fmt.Printf("%3d%%  %.4f, %.4f  %s\n",
			maj.GetProgressionPct(),
			maj.GetPosition().GetLatitude(),
			maj.GetPosition().GetLongitude(),
			maj.GetStatut())
	}

	fmt.Println("Flux termine.")
	return nil
}

// ------------------------------------------------------- CLIENT STREAMING ---
//
// Pattern client streaming : un flux de requetes, une seule reponse. Ici
// c'est le client qui appelle Send() en boucle, puis CloseAndRecv() qui
// ferme le flux montant et attend la reponse unique du serveur. Cote Python
// le meme role etait tenu par un generateur passe au stub.
func envoyer(stub colispb.SuiviColisClient, colisID string) error {
	if colisID == "" {
		return fmt.Errorf("usage : go run . envoyer COL-XXXX")
	}

	flux, err := stub.EnvoyerPreuve(context.Background())
	if err != nil {
		return err
	}

	dossier := dossierPhotos()
	for _, nom := range []string{"p1.png", "p2.png", "p3.png"} {
		// Si Send echoue, c'est que le serveur a deja clos le flux (colis
		// inconnu par exemple) : on arrete d'emettre et on va chercher le
		// vrai motif de l'erreur avec CloseAndRecv juste en dessous.
		if err := envoyerPhoto(flux, filepath.Join(dossier, nom), colisID); err != nil {
			break
		}
	}

	rep, err := flux.CloseAndRecv()
	if err != nil {
		return err
	}

	fmt.Printf("%d photos, %d octets, %s\n",
		rep.GetPhotosRecues(), rep.GetOctetsRecus(), rep.GetStatut())
	return nil
}

// Envoie une photo sur le flux montant, decoupee en blocs de 64 Ko.
// Seul le PREMIER bloc porte colis_id et nom_fichier ; un bloc final vide
// avec fin_de_photo=true signale au serveur la fin de cette photo.
func envoyerPhoto(flux colispb.SuiviColis_EnvoyerPreuveClient, chemin, colisID string) error {
	f, err := os.Open(chemin)
	if err != nil {
		return err
	}
	defer f.Close()

	premier := true

	for {
		// Un tampon neuf a chaque tour : le bloc part dans un message
		// protobuf, on ne veut pas ecraser ses octets au tour suivant.
		tampon := make([]byte, 64*1024)
		n, errLecture := f.Read(tampon)
		if n > 0 {
			bloc := &colispb.PhotoChunk{Contenu: tampon[:n]}
			if premier {
				bloc.ColisId = colisID
				bloc.NomFichier = chemin
				premier = false
			}
			if err := flux.Send(bloc); err != nil {
				return err
			}
		}
		if errLecture == io.EOF {
			break
		}
		if errLecture != nil {
			return errLecture
		}
	}

	return flux.Send(&colispb.PhotoChunk{FinDePhoto: true})
}

// ---------------------------------------------------------- BIDIRECTIONAL ---
//
// Pattern bidirectionnel : deux flux independants sur la meme connexion.
// C'est le seul cas ou Go impose deux goroutines, parce que Send() et Recv()
// bloquent chacun de leur cote et doivent tourner en parallele :
//   - une goroutine lit le clavier et fait Send() ;
//   - une goroutine fait Recv() en boucle et affiche.
//
// La terminaison passe par le canal "fini", ecrit uniquement par la
// goroutine de reception : quand le serveur clot le flux, main est
// debloque et le programme s'arrete.
func chat(stub colispb.SuiviColisClient, colisID, role string) error {
	if colisID == "" {
		return fmt.Errorf("usage : go run . chat COL-XXXX [livreur|client]")
	}

	expediteur := colispb.Role_CLIENT
	if role == "livreur" {
		expediteur = colispb.Role_LIVREUR
	}

	// Annuler le contexte coupe le flux : indispensable pour que la
	// goroutine de saisie ne survive pas au retour de la fonction.
	ctx, annuler := context.WithCancel(context.Background())
	defer annuler()

	flux, err := stub.Discuter(ctx)
	if err != nil {
		return err
	}

	// Goroutine 1 : reception. C'est elle qui decide de la fin de la session.
	fini := make(chan error, 1)
	go func() {
		for {
			msg, err := flux.Recv()
			if err == io.EOF {
				fini <- nil
				return
			}
			if err != nil {
				fini <- err
				return
			}
			fmt.Printf("  <%s> %s\n", msg.GetAuteur(), msg.GetTexte())
		}
	}()

	// Goroutine 2 : saisie clavier. Elle ne signale rien sur "fini" : apres
	// CloseSend() le serveur cloture le flux descendant, ce que la goroutine
	// de reception verra sous forme d'un io.EOF. Les messages encore en
	// transit sont donc affiches avant de quitter.
	go func() {
		defer flux.CloseSend()

		// Message d'accueil, comme dans le client Python.
		if err := flux.Send(&colispb.ChatMessage{
			ColisId: colisID, Expediteur: expediteur, Auteur: role,
			Texte: role + " a rejoint la discussion",
		}); err != nil {
			return
		}

		lecteur := bufio.NewScanner(os.Stdin)
		for lecteur.Scan() {
			ligne := strings.TrimSpace(lecteur.Text())
			if ligne == "/quit" || ligne == "/q" {
				return
			}
			if ligne == "" {
				continue
			}
			if err := flux.Send(&colispb.ChatMessage{
				ColisId: colisID, Expediteur: expediteur, Auteur: role, Texte: ligne,
			}); err != nil {
				return
			}
		}
	}()

	return <-fini
}

// ------------------------------------------------------------- utilitaires --

// Un point au hasard dans l'agglomeration rennaise.
func positionRennes() *colispb.Position {
	return &colispb.Position{
		Latitude:  48.10 + rand.Float64()*0.06,
		Longitude: -1.70 + rand.Float64()*0.08,
	}
}

// Les photos vivent dans sandbox/rodrigue/photos/ ; le binaire Go, lui, est
// lance depuis go/. On accepte les deux emplacements.
func dossierPhotos() string {
	if _, err := os.Stat("photos"); err == nil {
		return "photos"
	}
	return filepath.Join("..", "photos")
}

// Affiche une erreur gRPC sur une seule ligne, code puis message.
// status.FromError sait extraire le code standard (NotFound,
// InvalidArgument, Unavailable...) porte par l'erreur : on ne panique
// jamais sur une erreur reseau.
func afficherErreur(err error) {
	if st, ok := status.FromError(err); ok {
		fmt.Printf("Erreur %s : %s\n", st.Code(), st.Message())
		return
	}
	fmt.Println("Erreur :", err)
}

func argument(i int, defaut string) string {
	if len(os.Args) > i {
		return os.Args[i]
	}
	return defaut
}

func usage() {
	fmt.Println("Usage :")
	fmt.Println("  go run . creer")
	fmt.Println("  go run . suivre  COL-XXXX")
	fmt.Println("  go run . envoyer COL-XXXX")
	fmt.Println("  go run . chat    COL-XXXX [livreur|client]")
}
