import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//Serveur de base pris sur OpenClassRoom

public class SyncServeur {

	//Connexion du serveur
	public static void main(String[] args) {
			
			/*int srvport = Integer.parseInt(args[2]);
			String repRacine = args[3]; // récuperation des parametres*/
			ServerSocket socket;
			System.out.println("En attente de reception.............");
			try  
			{
				socket = new ServerSocket(8082/*srvport*/);
				Thread t = new Thread(new Accepterclients(socket));
				t.start(); 
				System.out.println("Le serveur est prêt pour la connexion");
				
				
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
	}

