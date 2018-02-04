import java.io.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class SyncEsclave {

	public static void main(String[] args) throws IOException {
		
		int svrNomPort = 0;
		String repCible = null, repRacine = null;
		String options = ""; 
		if (args.length <3)
		{
			System.out.println("Il faut au moins mettre : java SyncMaitre serveurPort repertoireSource repertoireRacine");
		}else {
			if(args.length >5){
				System.out.println("Seule 2 options simultanees sont possibles -w -s, -e -s");
			}
			svrNomPort = Integer.parseInt(args[0]);
			repCible = args[1];
			repRacine = args[2];
			for(int i = 3;i<args.length;i++)
			{
				options += args[i];
			}
			System.out.println("Les options sont :" + options + options.length());
		}
		
		Socket socket;
		File source;
		File dest;
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		boolean z = true;
		String choix = null;

		try {
			socket = new Socket(InetAddress.getLocalHost(), svrNomPort);
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			
	        String repS = (String) in.readObject();
	        System.out.println("Repertoire racine envoye du serveur : \n"+ repS);
	        if(repS.equals(repRacine)) {
	          System.out.println("Repertoire racine ok \n");
	        }else {
	          System.out.println("Repertoire racine"+repRacine+"\nremplace par"+ repS +"\n");
	          repRacine = repS;
	          System.out.println("Repertoire racine modifie : \n"+ repRacine);
	        }
	        source = new File(repRacine);
			dest = new File(repCible);
			
			if(options.length()==0) {
				System.out.println("On execute le mode écrasement par défaut \n");
				copyDirectory(source, dest);
			}else {
				switch(options.indexOf('s')) {
				case -1 : 
					System.out.println("Le mode suppression n'est pas selectionné : \n");
				default :
					System.out.println("Le mode suppression est selectionné : \n");
					pullS(source, dest);
				}
			
				switch(options.indexOf('e')) {
					case -1 : 
						System.out.println("Le mode écrasement n'est pas selectionné : \n");
					default :
						System.out.println("Le mode écrasement est selectionné : \n");
						pullE(source, dest);
				}
			
				switch(options.indexOf('w')) {
					case -1 : 
						System.out.println("Le mode watchdog n'est pas selectionné : \n");
					default :
						System.out.println("Le mode watchdog est selectionné : \n");
						pullW(source, dest);
				}
			}
			socket.close();

		} catch (UnknownHostException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void pullE(File source, File dest) throws IOException {
		copyDirectory(source, dest);
	}

	// PULL S EST OK
	public static void pullS(File source, File dest) throws IOException {
		viderDossier(dest);
		copyDirectory(source, dest);
	}

	public static void pullW(File source, File dest) throws IOException {
		copyDirectoryWatchdog(source, dest);
	}

	public static void viderDossier(File D){
		for (File f : D.listFiles()){
			if (f.isDirectory())
				viderDossier(f);
			f.delete();
		}
	}

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
	public static void copyDirectoryWatchdog(final File from, final File to) throws IOException {
		if (!to.exists()) {
			to.mkdir();
		}
		final File[] inDir = from.listFiles();
		for (int i = 0; i < inDir.length; i++) {
			final File file = inDir[i];
			if(from.lastModified() > to.lastModified())
				copy(file, new File(to, file.getName()));
		}
	}
}
