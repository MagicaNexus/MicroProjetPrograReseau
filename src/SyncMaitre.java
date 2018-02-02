import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

//Client de base pris sur OpenClassRoom

public class SyncMaitre extends SyncEsclave{
	
	public static void main(String[] args) {
		int svrNomPort = 0;
		String repSrc = null, repRacine = null;
		String options ="";
		if (args.length <5)
		{
			System.out.println("Il faut au moins mettre : java SyncMaitre serveurPort repertoireSource repertoireRacine");
		}else {
			if(args.length >7){
				System.out.println("Seule 2 options simultanées sont possibles -w -s, -e -s");
			}
			svrNomPort = Integer.parseInt(args[2]);
			repSrc = args[3];
			repRacine = args[4];
			for(int i = 5;i<args.length;i++)
			{
				options += args[i];
			}
			System.out.println("Les options sont :" + options);
		}
		
		
		Socket socket; //Création socket 
		File Source;  //Création d'un dossier maitre
		File Racine; //Création d'un dossier maitre
		
		/*List<Metadonnee> metadAll = new ArrayList<Metadonnee>();*/ 
		try {  
			
			//Connexion du maitre
			System.out.println("Je suis le Maitre et je viens de me connecter");
			//Attribution de l'adresse et du port de la socket
		    socket = new Socket(InetAddress.getLocalHost(),svrNomPort);	
		    
		    //Choix du mode de transfert
		    System.out.println("Choix du mode de transfert de fichier :\n"
		    		+ "push vers serveur --> watchdog \n"
		    		+ "pull vers client --> watchdog + ecrasement\n");
		    
		    System.out.println("Le mode par défault est watchdog\n");
		    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
		    String repS = (String) in.readObject();
		    System.out.println("Repertoire racine envoyé du serveur : \n"+ repS);
		    if(repS.equals(repRacine)) {
		    	System.out.println("Repertoire racine ok \n");
		    }else {
		    	System.out.println("Repertoire racine"+repRacine+"\nremplace par"+ repS +"\n");
		    	repRacine = repS;
		    	System.out.println("Repertoire racine modifié : \n"+ repRacine);
		    }

		    //boolean En cas de repertoire
			//System.out.println("Est un repertoire : " + contenu.isDirectory());
			
			//Affichage du contenu du repertoire
			/*System.out.println("Qui contient :");
	        afficheDocument(contenu.list(),contenu.getName());
	        System.out.println("Confirmation fin affichage...................\n\n\n");*/
	        Source = new File (repSrc);
		    Racine = new File (repRacine);
		    copyDirectory(Source , Racine);
	        
	        
	        /*ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()); //Création d'un objet de sortie
	        
	        out.writeObject(contenu);
	        out.flush();
	        
	        transfertChemin(contenu.listFiles() ,contenu.getName(), metadAll);
	        System.out.println(metadAll.size());
	        out.writeObject("Nombre de fichiers : "+metadAll);
	        out.flush();*/
	       
		    socket.close();

		}catch (UnknownHostException e) {
			
			e.printStackTrace();
		}catch (IOException e) {
			
			e.printStackTrace();
		} /*catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/ catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void copy(final InputStream inStream, final OutputStream outStream, final int bufferSize)
			throws IOException {
		final byte[] buffer = new byte[bufferSize];
		int nbRead;
		while ((nbRead = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, nbRead);
		}
	}

	public static void copyDirectory(final File from, final File to) throws IOException {
		if (!to.exists()) {
			to.mkdir();
		}
		final File[] inDir = from.listFiles();
		for (int i = 0; i < inDir.length; i++) {
			final File file = inDir[i];
			copy(file, new File(to, file.getName()));
		}
	}

	public static void copyFile(final File from, final File to) throws IOException {
		final InputStream inStream = new FileInputStream(from);
		final OutputStream outStream = new FileOutputStream(to);
		copy(inStream, outStream, (int) Math.min(from.length(), 4 * 1024));
		inStream.close();
		outStream.close();
	}

	public static void copy(final File from, final File to) throws IOException {
		if (from.isFile()) {
			copyFile(from, to);
		} else if (from.isDirectory()) {
			copyDirectory(from, to);
		} else {
			throw new FileNotFoundException(from.toString() + " does not exist");
		}
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void transfertChemin(File[] paths ,String parent, List<Metadonnee> metaAll) throws IOException, InterruptedException
	{
		System.out.println("\n---- Début du transfert du chemin......................");
		System.out.println("Affichage de la taille : " + paths.length);
		
		for(File f:paths) 
		{
			
				System.out.println("Création du fichier dans" + f.getParent() + f.getName() +"à l'emplacement suivant : \n" +f.getAbsolutePath());
				if (f.isDirectory())
				{
					//parent += "\\" + f.getName();
					metaAll.add(new Metadonnee (f.getName(),f.getAbsolutePath().replace(System.getProperty("user.dir"), ""),f.length(),f.lastModified(),null, null));
					transfertChemin(f.listFiles(),f.getAbsolutePath() /*parent*/, metaAll);
					//debug
					System.out.println("Changement de repertoire réussi : " + parent);
				} else {
					System.out.println("Taille du fichier : " + f.length());
				    
					if (f.length() > 0)
					{
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
					}else {
						System.out.println("Objet vide");
						//metaAll.add(new Metadonnee (f.getName(),f.getAbsolutePath().replace(System.getProperty("user.dir"), ""),f.length(),f.lastModified(),null, null));
					}
					
				}
				
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
