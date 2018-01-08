


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.*;

public class Accepterclients implements Runnable

{

	   private ServerSocket socketserver;
	   private Socket socket;
	   private int nbrclient = 1;
		public Accepterclients(ServerSocket s){
			socketserver = s;
		}
		
		public void run() {

	        try {
	        	while(true){
	        		  socket = socketserver.accept(); // Un client se connecte on l'accepte => Pas d'identification encore
	                  System.out.println("Le client numéro "+nbrclient+ " est connecté !");
	                  nbrclient++;
	                  Transfer.transfert(socket.getInputStream(),new FileOutputStream("AStocker.txt"), false);
	                  socket.close();
	                  
	        	}
	        
	        } catch (IOException e) {
				e.printStackTrace();
			}
		}

}
