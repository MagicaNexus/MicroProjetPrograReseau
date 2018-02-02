import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.net.UnknownHostException;

//Client de base pris sur OpenClassRoom

public class SyncEsclave extends File {
	
	public SyncEsclave(URI arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		
		int svrNomPort = Integer.parseInt(args[2]); 
		String repCible = args[3], repRacine = args[4];
		Socket socket;
		

		try {
			System.out.println("Je suis l'esclave et je viens de me connecter");
		    socket = new Socket(InetAddress.getLocalHost(), svrNomPort);	
		    //Transfer.transfert(/*le contenu du serveur*/, new FileOutputStream(/*lieu de stockage System.getProperty("user.dir") + "/" + "monFichier.txt"*/), true);
	        socket.close();

		}catch (UnknownHostException e) {
			
			e.printStackTrace();
		}catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	public void pull(String depart, String arrivee)
	{
		File origine = new File(depart);
		
		
	}
}

