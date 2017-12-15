import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//Serveur de base pris sur OpenClassRoom

public class Serveur {

	public static void main(String[] zero) {
		
		ServerSocket socketserver  ;
		Socket socketduserveur ;

		try {
		
			socketserver = new ServerSocket(2008);
			socketduserveur = socketserver.accept(); 
			System.out.println("Nouvelle connexion");
		        socketserver.close();
		        socketduserveur.close();

		}catch (IOException e) {
			e.printStackTrace();
		}
	}

}

