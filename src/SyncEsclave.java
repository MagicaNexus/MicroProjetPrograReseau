import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

//Client de base pris sur OpenClassRoom

public class SyncEsclave {
	
	public static void main(String[] zero) {
		
		Socket socket;

		try {
			System.out.println("Je suis l'esclave et je viens de me connecter");
		    socket = new Socket(InetAddress.getLocalHost(),2009);	
	        socket.close();

		}catch (UnknownHostException e) {
			
			e.printStackTrace();
		}catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	public void pull()
	{
		
	}
}

