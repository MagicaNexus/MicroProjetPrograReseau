import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Serveur {

	static private int compteur = 0; // Compeut pour compter le nombre de clients connectés
	private Socket clientSocket;

	// méthode pour avoir le compteur
	synchronized public int getCompteur() {
		return compteur;
	}

	// methode pour incrémenter le compteur lorsqu'un client se connecte
	synchronized public void incrementation() {
		compteur++;
	}

	// methode pour décrémenter le compteur lorsqu'un client se déconnecte
	synchronized public void décrémentation() {
		compteur--;
	}

	// Constructeur calculant le nombre de clients connectés
	public Serveur(Socket clientSocket) {
		this.clientSocket = clientSocket;
		incrementation();
		System.out.println(getCompteur() + " clients connectés");
	}

	public static void main(String[] args) {

		ServerSocket socket; // Création du ServerSocket
		// Affichage pour faire joli
		System.out.println(
				"Vous êtes sur le serveur.......\nConnexion en cours de préparation........\nVeuillez entrer le port du client :");

		// Scanner pour récuprer le numéro de port
		Scanner sc = new Scanner(System.in);
		int port = sc.nextInt(); // Récupération

		System.out.println("Connexion sur le port " + port + " en cours ......"); // Confirmation

		try {
			// Mise en place d'un serveur
			socket = new ServerSocket(8082);
			Thread t = new Thread(new ConnexionClients(socket));
			t.start(); // Démarrage du Thread
			System.out.println("\n\nConnexion réussie. En attente des clients ... ..."); // Confirmation

			// Exceptions
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
}

class ConnexionClients implements Runnable {

	static File contenu = new File("Serveur"); // Création d'un dossier maitre
	private ServerSocket socketserver; // Création du ServerSocket
	private Socket socket; // Création de la socket
	private int nbrclient = 0; // Initialisation du nombre de clients connectés
	boolean boucle = true; // Boolean pour le while de la fonction run()
	public SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); // Formatage de la date - metadonnées
	String[] doc = { "ATransferer.txt" }; // Création chaine de caractère pour fichier txt

	/* Constructeur */
	public ConnexionClients(ServerSocket s) {
		socketserver = s;
	}

	// Runnable
	public void run() {

		try {

			while (boucle) {
				socket = socketserver.accept(); // Un client se connecte on l'accepte
				nbrclient++; // Incrémentation du nombre de clients connectés

				// Confirmation de connexion du client
				System.out.println("Nouveau client en cours de connexion .... Client " + nbrclient);
				InputStream strIn = socket.getInputStream();
				OutputStream strOut = socket.getOutputStream();
				ObjectInputStream objIn = new ObjectInputStream(strIn);
				ObjectOutputStream ObjOut = new ObjectOutputStream(strOut);

				// Tableau de String pour récuperer les données envoyées par le client
				String[] requete = (String[]) objIn.readObject();

				// Debug pour savoir si c'est bon ce qu'envoie le client
				for (int i = 0; i <= 4; i++)
					System.out.println(requete[i]);

				// Identification du client
				/* RAPPEL : requete[0] = pseudo et requete[1] = mdp */
				if (requete[0].equals("admin") && requete[1].equals("admin1")) {
					System.out.println("Un maitre est sur le serveur ............");
					SyncMaitre.main(requete);
				}

				if (requete[0].equals("client") && requete[1].equals("client1")) {
					System.out.println("Un client est sur le serveur");
					SyncEsclave.main(requete);
				} else {
					System.out.println("Erreur : Pas le bon couple pseudo/password\n");
				}

				//TODO and ameliorer
				//String fileName = objIn.readUTF();
				//transfertDocument(doc, contenu.getName(), objIn);

			}

			// Expections
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void transfertDocument(String[] paths, String parent, ObjectInputStream in)
			throws IOException, ClassNotFoundException {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Metadonnee m = (Metadonnee) in.readObject();

		/* Affichage */

		System.out.println("Affichage métadonnées --> " + m.toString());
		System.out.println("---- Début du transfert......................");
		System.out.println("Affichage du chemin : " + paths);
		System.out.println("Affichage du parent : " + parent);

		System.out.println("Date de derniere modification de la metadonnée : " + sdf.format(m.dateM));

		for (String path : paths) {
			File f = new File(contenu.getName(), m.nom);
			// Debug
			System.out.println("Création du fichier à l'emplacement suivant : " + parent + "\\" + path);
			if (f.isDirectory()) {
				parent += "\\" + f.getName();
				transfertDocument(f.list(), f.getName(), in);
				// debug
				System.out.println("Changement de repertoire réussi : " + parent);
			}

			// debug
			System.out.println("\n\nCréation métadonnées ok\n\n");
			Transfer.transfert(in, new FileOutputStream(f), false);
			f.setLastModified(m.dateM);
			System.out.println("Date modif du fichier : " + sdf.format(f.lastModified()));

			System.out.println("\n\nFin du transfert .........................................");
		}
	}
}
