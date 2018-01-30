import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.net.UnknownHostException;

//Client de base pris sur OpenClassRoom

public class SyncEsclave extends File {

	public SyncEsclave(URI arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {

		int port = Integer.parseInt(args[4]);
		String pseudo = args[0];
		String repCible = args[2], repRacine = args[3];
		Socket socket;
		boolean opReussi; // Booleen pour un opération de l'esclave reussie

		try {
			System.out.println("Je suis l'esclave et je viens de me connecter");
			socket = new Socket(InetAddress.getLocalHost(), port);
			// Transfer.transfert(/*le contenu du serveur*/, new FileOutputStream(/*lieu de
			// stockage System.getProperty("user.dir") + "/" + "monFichier.txt"*/), true);
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Choix du mode d'utilisation : ");
			String mode = in.readLine();

			oos.writeObject(mode);
			oos.flush();

			/* Push de suppression */
			if (mode.equals("push -s")) {
				System.out.println(pseudo + ", vous avez choisi le mode push de suppression !");
				// TODO la manipulation quand l'esclave fait un push supp

			}

			/* Push d'écrasement */
			if (mode.equals("push -e")) {
				System.out.println(pseudo +", vous avez choisi le mode push d'écrasement !");
				// TODO la manipulation quand l'esclave fait un push ecrasement
			}

			/* Push watchdog */
			if (mode.equals("push -w")) {
				System.out.println(pseudo +", vous avez choisi le mode push watchdog !");
				// TODO la manipulation quand l'esclave fait un push watchdog
			}

			/* Pull */
			if (mode.equals("pull")) {
				System.out.println("Esclave, vous avez choisi le mode pull !");
				// TODO la manipultaion quand l'esclave fait un pull
			}

			else {
				System.out.println("Mode non reconnu, veuillez vous reconnecter");
			}

			socket.close();

		} catch (UnknownHostException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public void pull(String depart, String arrivee) {
		File origine = new File(depart);

	}
}
