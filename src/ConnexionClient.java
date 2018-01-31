import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ConnexionClient {

	private int port;
	public ConnexionClient(int port)
	{
		this.port = port;
	}
	public static void main(String[] args) {
		System.out.println(
				"Vous êtes sur le client .........\nVeuillez entrer le numéro du port du serveur sur lequel vous souhaitez vous connecter :");
		Scanner sc = new Scanner(System.in);
		int port = sc.nextInt();
		System.out.println("Connexion sur le serveur de port " + port + " en cours......");

		try {
			Socket socket = new Socket(InetAddress.getLocalHost(), port);
			System.out.println("Connexion réussie ...");
			
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			// ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

			System.out.println("Entrer 'pseudo motdepasse cheminsource chemindest' separé par un espace : ");
			String requete = in.readLine() + " " + Integer.toString(port);
			System.out.println(requete);
			String[] separation;
			ArrayList<String> separationf = new ArrayList<String>();
			separation = requete.split(" ");
			/*for(int i = 0; i<=separation.length;i++)
			{
				separationf.add(separation[i]); 
				System.out.println(separationf.get(i));
			}*/
			oos.writeObject(separation);
			oos.flush();
			System.out.println("a ecrit");
			socket.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void goToEsclave()
	{
		
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
}