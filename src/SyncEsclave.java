import java.io.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class SyncEsclave {

	public static void main(String[] args) {

		int svrNomPort = 0;
		String repCible = null, repRacine = null;
		String options ="";
		if (args.length <5)
		{
			System.out.println("Il faut au moins mettre : java SyncMaitre serveurPort repertoireSource repertoireRacine");
		}else {
			if(args.length >7){
				System.out.println("Seule 2 options simultan�es sont possibles -w -s, -e -s");
			}
			svrNomPort = Integer.parseInt(args[2]);
			repCible = args[3];
			repRacine = args[4];
			for(int i = 5;i<args.length;i++)
			{
				options += args[i];
			}
			System.out.println("Les options sont :" + options);
		}
		
		Socket socket;
		File source = new File(repRacine);
		File dest = new File(repCible);
		Scanner sc = new Scanner(System.in);
		boolean z = true;
		String choix = null;

		try {
			System.out.println("Bonjour Esclave, que voulez vous faire ? (pull +' '+ e,s,w) "); // On demande ce que
																								// veut faire l'esclave

			while (z == true) { // V�rification de la frappe de l'esclave
				choix = sc.nextLine(); // Il tape son choix
				if ("pull e".equals(choix) || "pull s".equals(choix) || "pull w".equals(choix)) {
					z = false; // On sort de la boucle si c'est bon
				} else
					System.out.println("Res�lectionner le choix :  "); // Sinon on res�lectionne le choix
			}

			// Validation
			System.out.println("Okay, vous avez choisi de faire un " + choix);
			socket = new Socket(InetAddress.getLocalHost(), svrNomPort);

			// En fonction du choix, on lance la m�thode ad�quate
			if ("pull e".equals(choix)) {
				pullE(source, dest);
			}

			if ("pull s".equals(choix)) {
				pullS(source, dest);
			}

			if ("pull e".equals(choix)) {
				pullW(source, dest);
			}

			socket.close();

		} catch (UnknownHostException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	// ******************************PULL E**********************************//
	public static void pullE(File source, File dest) throws IOException {
		copyDirectory(source, dest);
	}

	// ******************************PULL S**********************************//
	public static void pullS(File source, File dest) throws IOException {

		viderDossier(dest);
		copyDirectory(source, dest);

	}

	// ******************************PULL W**********************************//
	public static void pullW(File source, File dest) throws IOException {

		copyDirectoryWatchdog(source, dest);

	}

	// M�thode pour vider et supprimer les �l�ments d'un dossier
	public static void viderDossier(File D)

	{

		for (File f : D.listFiles())

		{

			if (f.isDirectory())
				viderDossier(f);

			f.delete();

		}

	}

	// M�thode pour copier et coller un ou des fichiers d'un emplacement A � un
	// emplacement B

	public static void copy(final InputStream inStream, final OutputStream outStream, final int bufferSize)
			throws IOException {
		final byte[] buffer = new byte[bufferSize];
		int nbRead;
		while ((nbRead = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, nbRead);
		}
	}

	public static void copyDirectory(final File from, final File to) throws IOException {
		if (!to.exists()) {
			to.mkdir();
		}
		final File[] inDir = from.listFiles();
		for (int i = 0; i < inDir.length; i++) {
			final File file = inDir[i];
			//On s�l�ctionne tous les fichiers d'un repertoire et on les copie
			copy(file, new File(to, file.getName()));
		}
	}

	public static void copyFile(final File from, final File to) throws IOException {
		final InputStream inStream = new FileInputStream(from);
		final OutputStream outStream = new FileOutputStream(to);
		copy(inStream, outStream, (int) Math.min(from.length(), 4 * 1024));
		inStream.close();
		outStream.close();
	}

	public static void copy(final File from, final File to) throws IOException {
		if (from.isFile()) {
			copyFile(from, to);
		} else if (from.isDirectory()) {
			copyDirectory(from, to);
		} else {
			throw new FileNotFoundException(from.toString() + " does not exist");
		}
	}

	
	//M�thode sp�ciale pour le watchdog
	public static void copyDirectoryWatchdog(final File from, final File to) throws IOException {
		if (!to.exists()) {
			to.mkdir();
		}
		final File[] inDir = from.listFiles();
		for (int i = 0; i < inDir.length; i++) {
			final File file = inDir[i];
			if (from.lastModified() > to.lastModified()) //On v�rifie si le fichier est plus r�cent
				copy(file, new File(to, file.getName())); //et on le copie
		}
	}
}
