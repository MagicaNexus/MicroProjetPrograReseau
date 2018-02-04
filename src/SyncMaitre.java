import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class SyncMaitre extends SyncEsclave{
	
	public static void main(String[] args) {
		int svrNomPort = 0;
		String repSrc = null, repRacine = null;
		String options ="";
		if (args.length <3)
		{
			System.out.println("Il faut au moins mettre : java SyncMaitre serveurPort repertoireSource repertoireRacine");
		}else {
			if(args.length >5){
				System.out.println("Seule 2 options simultanees sont possibles -w -s, -e -s");
			}
			svrNomPort = Integer.parseInt(args[0]);
			repSrc = args[1];
			repRacine = args[2];
			for(int i = 3;i<args.length;i++)
			{
				options += args[i];
			}
			System.out.println("Les options sont :" + options);
		}
		
		
		Socket socket; //Creation socket 
		File Source;  //Creation d'un dossier maitre
		File Racine; //Creation d'un dossier serveur
		
		try {  
			
			//Connexion du maitre
			System.out.println("Je suis le Maitre et je viens de me connecter");
			//Attribution de l'adresse et du port de la socket
		    socket = new Socket(InetAddress.getLocalHost(),svrNomPort);	
		    
		    //Choix du mode de transfert
		    System.out.println("Choix du mode de transfert de fichier :\n"
		    		+ "push vers serveur --> watchdog \n"
		    		+ "pull vers client --> watchdog + ecrasement\n");
		    
		    System.out.println("Le mode par default est watchdog\n");
		    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
		    String repS = (String) in.readObject();
		    System.out.println("Repertoire racine envoye du serveur : \n"+ repS);
		    in.close();
		    if(repS.equals(repRacine)) {
		    	System.out.println("Repertoire racine ok \n");
		    }else {
		    	System.out.println("Repertoire racine"+repRacine+"\nremplace par"+ repS +"\n");
		    	repRacine = repS;
		    	System.out.println("Repertoire racine modifie: \n"+ repRacine);
		    }

	        Source = new File (repSrc);
		    Racine = new File (repRacine);
		    copyDirectory(Source , Racine);
			socket.close();

		} catch (UnknownHostException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

	