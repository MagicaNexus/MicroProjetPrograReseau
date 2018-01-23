import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;

//Client de base pris sur OpenClassRoom

public class SyncMaitre {
	
	public static void main(String[] args) {
		
		/*int svrNomPort = Integer.parseInt(args[2]);
		String repSrc = args[3], repRacine = args[4];*/
		
		
		
		Socket socket; //Création socket 
		File contenu = new File ("Maitre"); //Création d'un dossier maitre
		File file = new File("Maitre\\ATransferer.txt"); //Création d'un nouveau fichier tkt atranferer
		String[] doc = {"ATransferer.txt"}; //Tableau de string contenant le nom du fichier créé
		
		try {
			
			//Connexion du maitre
			System.out.println("Je suis le Maitre et je viens de me connecter");
			//Attribution de l'adresse et du oprt de la socket
		    socket = new Socket(InetAddress.getLocalHost(),8082/*svrNomPort*/);	
		    
		    //Choix du mode de transfert
		    System.out.println("Choix du mode de transfert de fichier :\n"
		    		+ "1 : mode ecrasement fichier existant deja dans "
		    		+ "le repertoire destination est ecrase par le fichier du repertoire source.\n"
		    		+ "2 : mode suppression ou un fichier existant dans le repertoire destination "
		    		+ "mais pas dans le repertoire source est supprime dans le repertoire destination.\n"
		    		+ "3 : mode watchdog ou un fichier existant deja dans le repertoire destination"
		    		+ " est ecrase uniquement par une version plus recente du fichier du repertoire source.\n");
		    
		    //boolean En cas de repertoire
			System.out.println("Est un repertoire : " + contenu.isDirectory());
			
			//Affichage du contenu du repertoire
			System.out.println("Qui contient :");
	        afficheDocument(contenu.list(),contenu.getName());
	        System.out.println("Confirmation fin affichage...................\n\n\n");
	        
	        
	        /*transfertDocument(contenu.list() ,contenu.getName(), socket);*/
	        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()); //Création d'un objet de sortie
	        
	        
	        transfertDocument(doc,contenu.getName(),out); //Transfert du document --> Changement file - contenu
	        
	        
	        out.writeUTF(doc[0]);
	        Transfer.transfert(new FileInputStream(file), out, false);
		    socket.close();

		}catch (UnknownHostException e) {
			
			e.printStackTrace();
		}catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	public static void transfertDocument(String[] paths ,String parent, ObjectOutputStream out) throws IOException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		System.out.println("---- Début du transfert......................");
		System.out.println("Affichage du chemin : " + paths);
		System.out.println("Affichage du parent : " + parent);
		for(String path:paths) 
		{
			File f = new File (parent+"\\"+path);
			//Debug
			System.out.println("Création du fichier à l'emplacement suivant : " +parent+"\\"+path);
			if (f.isDirectory())
			{
				parent += "\\" + f.getName();
				transfertDocument(f.list(),f.getName(), out);
				//debug
				System.out.println("Changement de repertoire réussi : " + parent);
			}
			out.writeUTF(path); 
			Metadonnee m = new Metadonnee (f.getName(),f.getCanonicalPath(),f.length(),f.lastModified());
			System.out.println("Date de derniere modification vu par le fichier: " + sdf.format(f.lastModified()));
			System.out.println("Date de derniere modification de la metadonnée : " + sdf.format(m.dateM));
			out.writeObject(m);
			//debug
			System.out.println("\n\nCréation métadonnées ok\n\n");
			Transfer.transfert(new FileInputStream(f), out, true);
			
			System.out.println("\n\nFin du transfert .........................................");
         }
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
