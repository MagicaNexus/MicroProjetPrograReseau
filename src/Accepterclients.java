
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;

import javax.sound.midi.Synthesizer;

public class Accepterclients implements Runnable

{

	static File contenu = new File("Serveur"); // Création d'un dossier maitre
	private ServerSocket socketserver;
	private Socket socket;
	private int nbrclient = 1;
	public SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	String[] doc = {"ATransferer.txt"};

	public Accepterclients(ServerSocket s) {
		socketserver = s;
	}

	public void run() {

		try {
			while (true) {
				socket = socketserver.accept(); // Un client se connecte on l'accepte => Pas d'identification encore
				System.out.println("Le client numéro " + nbrclient + " est connecté !");
				nbrclient++;
				ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
				String fileName = in.readUTF();
				transfertDocument(doc,contenu.getName(),in);
				
				/*Je me suis perdu ici et c'est la fin du cours, a voir si ma fonction est bonne ou pas !*/

				socket.close();

			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void transfertDocument(String[] paths, String parent, ObjectInputStream in)
			throws IOException, ClassNotFoundException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Metadonnee m = (Metadonnee) in.readObject();
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
			Transfer.transfert(in, new FileOutputStream(f), true);
			f.setLastModified(m.dateM);
			System.out.println("Date modif du fichier : " + sdf.format(f.lastModified()));

			System.out.println("\n\nFin du transfert .........................................");
		}
	}
}
