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
				System.out.println("Seule 2 options simultan�es sont possibles -w -s, -e -s");
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
		
		
		Socket socket; //Cr�ation socket 
		File Source;  //Cr�ation d'un dossier maitre
		File Racine; //Cr�ation d'un dossier maitre
		
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
		    
		    System.out.println("Le mode par d�fault est watchdog\n");
		    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
		    String repS = (String) in.readObject();
		    System.out.println("Repertoire racine envoy� du serveur : \n"+ repS);
		    if(repS.equals(repRacine)) {
		    	System.out.println("Repertoire racine ok \n");
		    }else {
		    	System.out.println("Repertoire racine"+repRacine+"\nremplace par"+ repS +"\n");
		    	repRacine = repS;
		    	System.out.println("Repertoire racine modifi� : \n"+ repRacine);
		    }

	        Source = new File (repSrc);
		    Racine = new File (repRacine);
		    copyDirectory(Source , Racine);
	        
	       
		    socket.close();

			/* transfertDocument(contenu.list() ,contenu.getName(), socket); */
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()); // Cr�ation d'un objet de sortie

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

		//R�cup�ration des donn�es
		int port = Integer.parseInt(args[4]);
		String pseudo = args[0];
		String repCible = args[2];
		String repRacine = args[3];
		
		//Cr�ation socket
		Socket socket;

		try {
			System.out.println("Je suis l'esclave et je viens de me connecter"); //Confirmation
			socket = new Socket(InetAddress.getLocalHost(), port); //Connexion � la socket
			
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

			/* Push d'�crasement 
			if (mode.equals("push -e")) {
				System.out.println(pseudo +", vous avez choisi le mode push d'�crasement !");
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