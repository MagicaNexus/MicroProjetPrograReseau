import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.sun.org.apache.bcel.internal.generic.PUSH;

//Client de base pris sur OpenClassRoom

public class SyncEsclave extends File {

	public SyncEsclave(URI arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public static void main(ArrayList<String> requete) throws InterruptedException {

		//Récupération des données
		int port = Integer.parseInt(requete.get(4));
		String pseudo = requete.get(0);
		String repSource = requete.get(2);
		String repDest = requete.get(3);
		String[] doc = { "ATransferer.txt" };
		ArrayList<String> nomDocs = new ArrayList();
		
		//Création socket
		Socket socket;

		try {
			System.out.println("Je suis l'esclave et je viens de me connecter"); //Confirmation
			socket = new Socket(InetAddress.getLocalHost(), port); //Connexion à la socket
			
			// Transfer.transfert(/*le contenu du serveur*/, new FileOutputStream(/*lieu de
			// stockage System.getProperty("user.dir") + "/" + "monFichier.txt"*/), true);
			
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Choix du mode d'utilisation : ");
			String mode = in.readLine();

			oos.writeObject(mode);
			oos.flush();

			/* Pull */
			if (mode.equals("pull")) {
				System.out.println("Esclave, vous avez choisi le mode pull !");
				pull(repSource, repDest);
				pull2(doc, repDest, oos, nomDocs);
			}

			else {
				System.out.println("Mode non reconnu, veuillez vous reconnecter");
			}

			socket.close();

		} catch (UnknownHostException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	
	/************************************************************* M E T H O D E S *************************************************************************/
	//********************************************************* Méthodes principales ***********************************************************************/
	
	public static void pull(String depart, String arrivee) {
		File origine = new File(depart);
		// TODO la manipultaion quand l'esclave fait un pull
	}
	
	public static void pull2(String[] paths ,String parent, ObjectOutputStream out, ArrayList<String> nomDocs) throws IOException, InterruptedException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		System.out.println("\n---- Début du transfert......................");
		System.out.println("Affichage du chemin : " + paths);
		System.out.println("Affichage du parent : " + parent);
		for(String path:paths) 
		{
			File f = new File (parent+"\\"+path);
			//Debug
			//nomDocs.add(f.);
			System.out.println("Création du fichier à l'emplacement suivant : " +parent+"\\"+path);
			if (f.isDirectory())
			{
				parent += "\\" + f.getName();
				pull2(f.list(),parent, out, nomDocs);
				//debug
				System.out.println("Changement de repertoire réussi : " + parent);
			}
			Thread.sleep(1000);
			out.writeUTF(path); 
			Metadonnee m = new Metadonnee (f.getName(),f.getCanonicalPath(),f.length(),f.lastModified());
			System.out.println("Date de derniere modification vu par le fichier: " + sdf.format(f.lastModified()));
			System.out.println("Date de derniere modification de la metadonnée : " + sdf.format(m.dateM));
			Thread.sleep(1000);
			out.writeObject(m);
			//debug
			System.out.println("\n\nCréation métadonnées ok\n\n");
			Thread.sleep(1000);
			Transfer.transfert(new FileInputStream(f), out, false);
			
			System.out.println("\n\nFin du transfert .........................................");
         }
		out.close();
	}
	
	
	//********************************************************* Méthodes secondaires  ***********************************************************************/
	
	public static void suppElementDossier(File DossierSrc)
	{
		//On sélectionne tous les fichiers dans le dossier
		for(File f : DossierSrc.listFiles())
		{
			if(f.isDirectory()) //Si le fichier est un dossier
				suppElementDossier(DossierSrc);
			DossierSrc.delete(); //Suppression des éléments
			System.out.println("Elements supprimés"); //Confirmation
		}
	}
	
}
