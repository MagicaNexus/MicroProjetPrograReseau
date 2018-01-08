import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import Classe_Client.*;

//Serveur de base pris sur OpenClassRoom

public class SyncServeur {
	
	static private int compteur = 0; //Compeut pour compter le nombre de clients connectés
	private Socket clientSocket;
	
	
	//méthode pour avoir le compteur
	synchronized public int getCompteur()
	{
		return compteur;
	}
	
	//methode pour incrémenter le compteur lorsqu'un client se connecte
	synchronized public void incrementation()
	{
		compteur++;
	}
	
	//methode pour décrémenter le compteur lorsqu'un client se déconnecte
	synchronized public void décrémentation()
	{
		compteur--;
	}
	
	
	//Constructeur calculant le nombre de clients connectés
	public SyncServeur(Socket clientSocket)
	{
		this.clientSocket = clientSocket;
		incrementation();
		System.out.println(getCompteur() + " clients connectés");
	}
	
	
	//Connexion du serveur
	public static void main(String[] args) {
			
			int srvport = Integer.parseInt(args[2]);
			String repRacine = args[3]; // récuperation des parametres
			ServerSocket socket;
			try 
			{
				socket = new ServerSocket(srvport);
				Thread t = new Thread(new Accepterclients(socket));
				t.start();
				System.out.println("Le serveur est prêt pour la connexion");
				
				
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
	}

