import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

//Client de base pris sur OpenClassRoom

public class SyncMaitre {
	
	public static void main(String[] args) throws InterruptedException {
		
		/*int svrNomPort = Integer.parseInt(args[2]);
		String repSrc = args[3], repRacine = args[4];*/
		
		
		Socket socket; //Cr�ation socket 
		File contenu = new File ("Maitre"); //Cr�ation d'un dossier maitre
		File file = new File("Maitre\\ATransferer.txt"); //Cr�ation d'un nouveau fichier tkt atranferer
		String[] doc = {"ATransferer.txt"}; //Tableau de string contenant le nom du fichier cr��
		List<Metadonnee> metadAll = new ArrayList<Metadonnee>();
		try {  
			
			//Connexion du maitre
			System.out.println("Je suis le Maitre et je viens de me connecter");
			//Attribution de l'adresse et du oprt de la socket
		    socket = new Socket(InetAddress.getLocalHost(),8082/*svrNomPort*/);	
		    
		    //Choix du mode de transfert
		    /*System.out.println("Choix du mode de transfert de fichier :\n"
		    		+ "1 : mode ecrasement fichier existant deja dans "
		    		+ "le repertoire destination est ecrase par le fichier du repertoire source.\n"
		    		+ "2 : mode suppression ou un fichier existant dans le repertoire destination "
		    		+ "mais pas dans le repertoire source est supprime dans le repertoire destination.\n"
		    		+ "3 : mode watchdog ou un fichier existant deja dans le repertoire destination"
		    		+ " est ecrase uniquement par une version plus recente du fichier du repertoire source.\n");*/
		    
		    //boolean En cas de repertoire
			//System.out.println("Est un repertoire : " + contenu.isDirectory());
			
			//Affichage du contenu du repertoire
			/*System.out.println("Qui contient :");
	        afficheDocument(contenu.list(),contenu.getName());
	        System.out.println("Confirmation fin affichage...................\n\n\n");*/
	        
	        
	        
	        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()); //Cr�ation d'un objet de sortie
	        
	        transfertChemin(contenu.listFiles() ,contenu.getName(), metadAll);
	        System.out.println(metadAll.size());
	        out.writeObject(metadAll);
	        out.flush();
	        transfertDocument(contenu.listFiles(),out); //Transfert du document --> Changement file - contenu
	        
	        /*out.writeUTF(file.getName());
	        out.flush();
	       
			
	        Transfer.transfert(new FileInputStream(file), out, false);*/
		    socket.close();

		}catch (UnknownHostException e) {
			
			e.printStackTrace();
		}catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	public static void transfertChemin(File[] paths ,String parent, List<Metadonnee> metaAll) throws IOException, InterruptedException
	{
		System.out.println("\n---- D�but du transfert du chemin......................");
		System.out.println("Affichage du parent : " + paths.length);
		
		for(File f:paths) 
		{
			
				System.out.println("Cr�ation du fichier � l'emplacement suivant : " +f.getAbsolutePath());
				if (f.isDirectory())
				{
					parent += "\\" + f.getName();
					transfertChemin(f.listFiles(),parent, metaAll);
					//debug
					System.out.println("Changement de repertoire r�ussi : " + parent);
				}
				metaAll.add(new Metadonnee (f.getName(),f.getAbsolutePath().replace(System.getProperty("user.dir"), ""),f.length(),f.lastModified()));
				//debug
				System.out.println("\n\nCr�ation m�tadonn�es ok\n\n");		
				System.out.println("\n\nFin du transfert .........................................");
			
			
         }
	}

	public static void transfertDocument(File[] paths , ObjectOutputStream out) throws IOException, InterruptedException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		System.out.println("\n---- D�but du transfert......................");
		
		for(File f:paths) 
		{
			System.out.println("Cr�ation du fichier � l'emplacement suivant : " +f.getAbsolutePath());
			if (f.isDirectory())
			{
				
				transfertDocument(f.listFiles(), out );
				//debug
				System.out.println("Changement de repertoire r�ussi : " + f.getName());
			}
			
			/*Metadonnee m = new Metadonnee (f.getName(),f.getAbsolutePath().replace(System.getProperty("user.dir"), ""),f.length(),f.lastModified());
			System.out.println(m.toString());
			
			out.writeObject(m);
			out.flush();
			//debug
			System.out.println("\n\nCr�ation m�tadonn�es ok\n\n");*/
		
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
