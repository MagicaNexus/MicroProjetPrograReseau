import java.io.*;
import java.net.*;

public class Accepterclients implements Runnable{
	private ServerSocket socketserver;
	private Socket socket;
	String serv;

	public Accepterclients(ServerSocket s, String repRacine) {
		socketserver = s;
		serv = repRacine;
	}
	public void run() {
		try {
			while (true) {
				socket = socketserver.accept(); // Un client se connecte on l'accepte => Pas d'identification encore
				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
				out.writeObject(serv);
				out.flush();
				out.close();
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
