import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

//Client de base pris sur OpenClassRoom

public class SyncMaitre {
	
	public static void main(String[] zero) {
		
		Socket socket;
		File file = new File("H:\\test.txt");
		try {
			System.out.println("Je suis le Maitre et je viens de me connecter");
		    socket = new Socket(InetAddress.getLocalHost(),2009);	
	        Transfer.transfert(new FileInputStream(file), socket.getOutputStream(), true);
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
