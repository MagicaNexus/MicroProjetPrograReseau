import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

//Client de base pris sur OpenClassRoom

public class SyncMaitre {
	
	public static void main(String[] args) {
		
		/*int svrNomPort = Integer.parseInt(args[2]);
		String repSrc = args[3], repRacine = args[4];*/
		Socket socket;
		File file = new File("Maitre\\ATransferer.txt");
		try {
			System.out.println("Je suis le Maitre et je viens de me connecter");
		    socket = new Socket(InetAddress.getLocalHost(),8082/*svrNomPort*/);	
	        Transfer.transfert(new FileInputStream(file), socket.getOutputStream(), false);
		    socket.close();

		}catch (UnknownHostException e) {
			
			e.printStackTrace();
		}catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	public void push()
	{
		
	}
}
