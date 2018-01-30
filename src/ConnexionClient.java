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
			String requete = in.readLine();
			String[] separation = new String[5];
			separation = requete.split(" ");
			//separation[4] = Integer.toString(port); essayer de recup le port pour le maitre ou esclaves
			oos.writeObject(separation);
			oos.flush();

			socket.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
}