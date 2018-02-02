import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

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

	        Source = new File (repSrc);
		    Racine = new File (repRacine);
		    copyDirectory(Source , Racine);
	        
	       
		    socket.close();

		}catch (UnknownHostException e) {
			
			e.printStackTrace();
		}catch (IOException e) {
			
			e.printStackTrace();
		}catch (ClassNotFoundException e) {
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
}