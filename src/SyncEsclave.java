import java.io.BufferedReader;
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

	
	/************************************************************* M E T H O D E S *************************************************************************/
	//********************************************************* Méthodes principales ***********************************************************************/
	
	public static void pull(String depart, String arrivee) {
		File origine = new File(depart);
		// TODO la manipultaion quand l'esclave fait un pull
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
