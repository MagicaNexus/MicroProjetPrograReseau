import java.io.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;

//Client de base pris sur OpenClassRoom

public class SyncEsclave extends File {
	
	public SyncEsclave(URI arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		
		int svrNomPort = Integer.parseInt(args[2]); 
		String repCible = args[3], repRacine = args[4];
		Socket socket;
		File source = new File("Serveur");
		File dest = new File("Esclave");

		try {
			System.out.println("Je suis l'esclave et je viens de me connecter");
		    socket = new Socket(InetAddress.getLocalHost(), svrNomPort);
		    copyDirectory(source, dest);
	        socket.close();

		}catch (UnknownHostException e) {
			
			e.printStackTrace();
		}catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	public static void copy(final InputStream inStream, final OutputStream outStream, final int bufferSize) throws IOException {
		 final byte[] buffer = new byte[bufferSize];
		 int nbRead;
		 while ((nbRead = inStream.read(buffer)) != -1) {
		 	outStream.write(buffer, 0, nbRead);
		 }
		}
		   
		public static void copyDirectory(final File from, final File to) throws IOException {
		 if (! to.exists()) {
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
		 copy(inStream, outStream, (int) Math.min(from.length(), 4*1024));
		 inStream.close();
		 outStream.close();
		}
		public static void copy(final File from, final File to) throws IOException {
		 if (from.isFile()) {
		 	copyFile(from, to);
		 } else if (from.isDirectory()){
		 	copyDirectory(from, to);
		 } else {
		 	throw new FileNotFoundException(from.toString() + " does not exist" );
		 }
		} 

	
}
