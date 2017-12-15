package Classe_Client;

import java.io.IOException;
import java.net.*;

class Accepterclients implements Runnable {

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
	                  System.out.println("Le client num�ro "+nbrclient+ " est connect� !");
	                  nbrclient++;
	                  socket.close();
	                  
	        	}
	        
	        } catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
