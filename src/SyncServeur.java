import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import Classe_Client.*;

//Serveur de base pris sur OpenClassRoom

public class SyncServeur {

	public static void main(String[] args) {
			
			ServerSocket socket;
			try {
			socket = new ServerSocket(2009);
			Thread t = new Thread(new Accepterclients(socket));
			t.start();
			System.out.println("Le serveur est prêt pour la connexion");
			
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
	}

