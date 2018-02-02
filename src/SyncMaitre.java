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
		
		
		Socket socket; //Création socket 
		File contenu = new File ("Maitre"); //Création d'un dossier maitre
		List<Metadonnee> metadAll = new ArrayList<Metadonnee>(); 
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
			//System.out.println("Est un repertoire : " + contenu.isDirectory());
			
			//Affichage du contenu du repertoire
			/*System.out.println("Qui contient :");
	        afficheDocument(contenu.list(),contenu.getName());
	        System.out.println("Confirmation fin affichage...................\n\n\n");*/
	        
	        
	        
	        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()); //Création d'un objet de sortie
	        
	        transfertChemin(contenu.listFiles() ,contenu.getName(), metadAll);
	        System.out.println(metadAll.size());
	        out.writeObject(metadAll);
	        out.flush();
	       
		    socket.close();

		}catch (UnknownHostException e) {
			
			e.printStackTrace();
		}catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	public static void transfertChemin(File[] paths ,String parent, List<Metadonnee> metaAll) throws IOException, InterruptedException
	{
		System.out.println("\n---- Début du transfert du chemin......................");
		System.out.println("Affichage du parent : " + paths.length);
		
		for(File f:paths) 
		{
			
				System.out.println("Création du fichier à l'emplacement suivant : " +f.getAbsolutePath());
				if (f.isDirectory())
				{
					parent += "\\" + f.getName();
					transfertChemin(f.listFiles(),parent, metaAll);
					//debug
					System.out.println("Changement de repertoire réussi : " + parent);
				}
				byte buf[] = new byte[1024];
			    int taille;
			    String message = "";
			    
			    FileInputStream in = new FileInputStream(f);
				taille=in.read(buf);
				byte mes[] = new byte[taille];
				for (int i = 0;i<taille;i++)
				{
					message += (char)buf[i];
					mes[i] = buf[i];
				}
				metaAll.add(new Metadonnee (f.getName(),f.getAbsolutePath().replace(System.getProperty("user.dir"), ""),f.length(),f.lastModified(),message, mes));
				//debug
				System.out.println("\n\nCréation métadonnées ok\n\n");		
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
            //afficheMetaDonnee(f);
         }
		System.out.println("\n\nFin de l'affichage .........................................");
	}
	
	/*public static void afficheMetaDonnee(File f) throws IOException
	{
		 Metadonnee m = new Metadonnee (f.getName(),f.getCanonicalPath(),f.length(),f.lastModified());
		 System.out.println(m.toString());
	}
	public static Metadonnee transfertMetaDonnee(File f) throws IOException
	{
		 Metadonnee m = new Metadonnee (f.getName(),f.getCanonicalPath(),f.length(),f.lastModified());
		 System.out.println(m.toString());
		 return m;
		 
	}*/
}
