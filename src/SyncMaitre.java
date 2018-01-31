import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

//Client de base pris sur OpenClassRoom

public class SyncMaitre {
	
	public static void main(ArrayList<String> requete) throws InterruptedException {
		int port = Integer.parseInt(requete.get(4));
		String cheminSrc = requete.get(2), cheminDest = requete.get(2);

		Socket socket; // Création socket
		File contenu = new File("Maitre"); // Création d'un dossier maitre
		
		File source = new File(System.getProperty("user.dir"),"\\source");
		source.mkdirs();
		File file = new File("Maitre\\ATransferer.txt"); // Création d'un nouveau fichier tkt atranferer
		String[] doc = { "ATransferer.txt" }; // Tableau de string contenant le nom du fichier créé
		ArrayList<String> nomDocs = new ArrayList<String>();
		/*String test = "H:\\Mes documents\\4A\\programation reseaux, concurrente et distribuée\\MicroProjet\\MicroProjetPrograReseau\\Maitre\\test\\test2\\test2ception.txt";
		test.replaceAll(
				"H:\\Mes documents\\4A\\programation reseaux, concurrente et distribuée\\MicroProjet\\MicroProjetPrograReseau\\",
				"");
		System.out.println("test regex java" + test);*/
		try {

			// Connexion du maitre
			System.out.println("Je suis le Maitre et je viens de me connecter");
			// Attribution de l'adresse et du oprt de la socket
			socket = new Socket(InetAddress.getLocalHost(), port/* svrNomPort */);

			// Choix du mode de transfert
			System.out.println(
					"Choix du mode de transfert de fichier :\n" + "1 : mode ecrasement fichier existant deja dans "
							+ "le repertoire destination est ecrase par le fichier du repertoire source.\n"
							+ "2 : mode suppression ou un fichier existant dans le repertoire destination "
							+ "mais pas dans le repertoire source est supprime dans le repertoire destination.\n"
							+ "3 : mode watchdog ou un fichier existant deja dans le repertoire destination"
							+ " est ecrase uniquement par une version plus recente du fichier du repertoire source.\n");

			
			// boolean En cas de repertoire
			System.out.println("Est un repertoire : " + contenu.isDirectory());
			System.out.println(contenu.getAbsolutePath().replace(System.getProperty("user.dir"), ""));
			System.out.println(file.getAbsolutePath().replace(System.getProperty("user.dir"), ""));
			
			// Affichage du contenu du repertoire
			System.out.println("Qui contient :");
			afficheDocument(contenu.list(), contenu.getName());
			System.out.println("Confirmation fin affichage...................\n\n\n");

			/* transfertDocument(contenu.list() ,contenu.getName(), socket); */
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()); // Création d'un objet de sortie

			transfertDocument(contenu.list(), contenu.getName(), out, nomDocs); // Transfert du document --> Changement
																				// file - contenu

			out.writeUTF(doc[0]);

			Transfer.transfert(new FileInputStream(file), out, false);
			socket.close();

		} catch (UnknownHostException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	
	 /*import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.net.UnknownHostException;

import com.sun.org.apache.bcel.internal.generic.PUSH;

//Client de base pris sur OpenClassRoom

public class SyncEsclave extends File {

	public SyncEsclave(URI arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {

		//Récupération des données
		int port = Integer.parseInt(args[4]);
		String pseudo = args[0];
		String repCible = args[2];
		String repRacine = args[3];
		
		//Création socket
		Socket socket;

		try {
			System.out.println("Je suis l'esclave et je viens de me connecter"); //Confirmation
			socket = new Socket(InetAddress.getLocalHost(), port); //Connexion à la socket
			
			// Transfer.transfert(, new FileOutputStream(/*lieu de
			// stockage System.getProperty("user.dir") + "/" + "monFichier.txt"), true);
			
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Choix du mode d'utilisation : ");
			String mode = in.readLine();

			oos.writeObject(mode);
			oos.flush();

			/* Push de suppression 
			if (mode.equals("push -s")) {
				System.out.println(pseudo + ", vous avez choisi le mode push de suppression !");
				pushSupp();

			}

			/* Push d'écrasement 
			if (mode.equals("push -e")) {
				System.out.println(pseudo +", vous avez choisi le mode push d'écrasement !");
				pushEcra();
			}

			/* Push watchdog 
			else if (mode.equals("push -w")) {
				System.out.println(pseudo +", vous avez choisi le mode push watchdog !");
				pushWatchdog();
			}

			/* Pull 
			if (mode.equals("pull")) {
				System.out.println("Esclave, vous avez choisi le mode pull !");
				pull(repCible, repRacine);
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

	
	/************************************************************* M E T H O D E S ************************************************************************
	//********************************************************* Méthodes principales **********************************************************************
	
	public static void pull(String depart, String arrivee) {
		File origine = new File(depart);
		// TODO la manipultaion quand l'esclave fait un pull
	}
	
	public static void pushSupp()
	{
		// TODO la manipulation quand l'esclave fait un push supp
		String source = "H:\\Mes documents\\4A\\S7\\Progra réseau\\Micro-projet\\MicroProjetPrograReseau\\DossierSource\\";
		File DossierSrc = new File("H:\\Mes documents\\4A\\S7\\Progra réseau\\Micro-projet\\MicroProjetPrograReseau\\DossierSource\\");
		suppElementDossier(DossierSrc);
		DossierSrc.mkdirs(); //Récupere tous les fichiers du dossier
		
	}
	
	public static void pushEcra()
	{
		// TODO la manipulation quand l'esclave fait un push ecrasement
	}
	
	public static void pushWatchdog()
	{
		// TODO la manipulation quand l'esclave fait un push watchdog
	}
	
	
	
	
	//********************************************************* Méthodes secondaires  **********************************************************************
	
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
	
}*/
	public static void transfertDocument(String[] paths ,String parent, ObjectOutputStream out, ArrayList<String> nomDocs) throws IOException, InterruptedException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		System.out.println("\n---- Début du transfert......................");
		System.out.println("Affichage du chemin : " + paths);
		System.out.println("Affichage du parent : " + parent);
		for(String path:paths) 
		{
			File f = new File (parent+"\\"+path);
			//Debug
			//nomDocs.add(f.getName());
			System.out.println("Création du fichier à l'emplacement suivant : " +parent+"\\"+path);
			if (f.isDirectory())
			{
				parent += "\\" + f.getName();
				transfertDocument(f.list(),parent, out, nomDocs);
				//debug
				System.out.println("Changement de repertoire réussi : " + parent);
			}
			Thread.sleep(1000);
			out.writeUTF(path); 
			Metadonnee m = new Metadonnee (f.getName(), f.getCanonicalPath().replace(System.getProperty("user.dir"), ""),f.length(),f.lastModified());
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
	public static void afficheDocument(String[] paths ,String parent) throws IOException
	{
		System.out.println("Parent : " + parent);
		for(String path:paths) {
			File f = new File (parent+"\\"+path);
			if (f.isDirectory())
			{
				parent += "\\" + f.getName();
				afficheDocument(f.list(),parent);
			}
            afficheMetaDonnee(f);
         }
		System.out.println("\n\nFin de l'affichage .........................................");
	}
	
	public static void afficheMetaDonnee(File f) throws IOException
	{
		 Metadonnee m = new Metadonnee (f.getName(),f.getCanonicalPath(),f.length(),f.lastModified());
		 System.out.println(m.toString());
	}
	public static Metadonnee transfertMetaDonnee(File f) throws IOException
	{
		 Metadonnee m = new Metadonnee (f.getName(),f.getCanonicalPath(),f.length(),f.lastModified());
		 System.out.println(m.toString());
		 return m;
		 
	}
}
