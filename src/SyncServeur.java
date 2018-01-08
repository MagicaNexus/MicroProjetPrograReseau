import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import Classe_Client.*;

//Serveur de base pris sur OpenClassRoom

public class SyncServeur {
	
	static private int compteur = 0; //Compeut pour compter le nombre de clients connect�s
	private Socket clientSocket;
	
	
	//m�thode pour avoir le compteur
	synchronized public int getCompteur()
	{
		return compteur;
	}
	
	//methode pour incr�menter le compteur lorsqu'un client se connecte
	synchronized public void incrementation()
	{
		compteur++;
	}
	
	//methode pour d�cr�menter le compteur lorsqu'un client se d�connecte
	synchronized public void d�cr�mentation()
	{
		compteur--;
	}
	
	
	//Constructeur calculant le nombre de clients connect�s
	public SyncServeur(Socket clientSocket)
	{
		this.clientSocket = clientSocket;
		incrementation();
		System.out.println(getCompteur() + " clients connect�s");
	}
	
	//Connexion du serveur
	public static void main(String[] args) {
			
			ServerSocket socket;
			try 
			{
				socket = new ServerSocket(2009);
				Thread t = new Thread(new Accepterclients(socket));
				t.start();
				System.out.println("Le serveur est pr�t pour la connexion");
				
				
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
	}

