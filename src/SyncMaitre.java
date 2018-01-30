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
	
	public static void main(String[] args) throws InterruptedException {
		int port = Integer.parseInt(args[4]);
		String cheminSrc = args[2], cheminDest = args[3];
		
		
		
		Socket socket; //Cr�ation socket 
		File contenu = new File ("Maitre"); //Cr�ation d'un dossier maitre
		File file = new File("Maitre\\ATransferer.txt"); //Cr�ation d'un nouveau fichier tkt atranferer
		String[] doc = {"ATransferer.txt"}; //Tableau de string contenant le nom du fichier cr��
		ArrayList<String> nomDocs = new ArrayList();
		String test = "H:\\Mes documents\\4A\\programation reseaux, concurrente et distribu�e\\MicroProjet\\MicroProjetPrograReseau\\Maitre\\test\\test2\\test2ception.txt";
		test.replaceAll("H:\\Mes documents\\4A\\programation reseaux, concurrente et distribu�e\\MicroProjet\\MicroProjetPrograReseau\\", "");
		System.out.println("test regex java" + test);
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
	        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()); //Cr�ation d'un objet de sortie
	        
	        
	        transfertDocument(contenu.list(),contenu.getName(),out,nomDocs); //Transfert du document --> Changement file - contenu
	        
	        
	        out.writeUTF(doc[0]);
	        
	        Transfer.transfert(new FileInputStream(file), out, false);
		    socket.close();

		}catch (UnknownHostException e) {
			
			e.printStackTrace();
		}catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	public static void transfertDocument(String[] paths ,String parent, ObjectOutputStream out, ArrayList<String> nomDocs) throws IOException, InterruptedException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		System.out.println("\n---- D�but du transfert......................");
		System.out.println("Affichage du chemin : " + paths);
		System.out.println("Affichage du parent : " + parent);
		for(String path:paths) 
		{
			File f = new File (parent+"\\"+path);
			//Debug
			//nomDocs.add(f.);
			System.out.println("Cr�ation du fichier � l'emplacement suivant : " +parent+"\\"+path);
			if (f.isDirectory())
			{
				parent += "\\" + f.getName();
				transfertDocument(f.list(),parent, out, nomDocs);
				//debug
				System.out.println("Changement de repertoire r�ussi : " + parent);
			}
			Thread.sleep(1000);
			out.writeUTF(path); 
			Metadonnee m = new Metadonnee (f.getName(),f.getCanonicalPath(),f.length(),f.lastModified());
			System.out.println("Date de derniere modification vu par le fichier: " + sdf.format(f.lastModified()));
			System.out.println("Date de derniere modification de la metadonn�e : " + sdf.format(m.dateM));
			Thread.sleep(1000);
			out.writeObject(m);
			//debug
			System.out.println("\n\nCr�ation m�tadonn�es ok\n\n");
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
