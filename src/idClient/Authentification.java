package idClient;

import java.net.*;
import java.io.*;

public class Authentification implements Runnable {

	private Socket socket;
	private PrintWriter out = null;
	private BufferedReader in = null;
	private String ID = null, password =  null;
	public boolean autentificationE = false;
	public boolean autentificationM = false;
	public Thread t2;
	
	
	//Constructeur
	public Authentification(Socket s){
		 socket = s;
	}
	
	//Void Run
	public void run() {
	
		try {
			
			
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream());
			
		//Tant que l'authentification n'a pas eu lieu que ce soit maitre ou esclave
		while(!autentificationE || !autentificationM)
		{
			
			//On demande le login et le password
			out.println("Entrez votre identifiant :");
			out.flush();
			ID = in.readLine(); //Enregistrement de l'identifiant dans ID
			
			
			out.println("Entrez votre mot de passe :");
			out.flush();
			password = in.readLine(); //Enregistrement du mot de passe dans password

			
			//On regarde si l'utilisateur est un utilisateur connu
			if(isValidM(ID, password)){
																		//L'utilisateur est connecté
				out.println("Connection");
				System.out.println(ID +" vient de se connecter ");
				out.flush();
				autentificationM = true;	
			}
			
			if(isValidE(ID, password)){
				//L'utilisateur est connecté
				out.println("Connection");
				System.out.println(ID +" vient de se connecter ");
				out.flush();
				autentificationE = true;	
			}
			
			else 
			{
				out.println("erreur, pas le bon utlisateur ou le bon mot de passe"); 
				out.flush();
			}
		 }//Fin du while
		
		if(autentificationM = true)
		{
			//Un maitre s'est connecté, on le dirige vers son interface de communication avec le serveur
			
			
			//................................//
		}
		
		
		else if (autentificationE = true)
		{
			//Un esclave s'est connecté, on le dirige vers son interface de communication avec le serveur
			
			
			//...........................//
		}
			
		} //Fin du try
		
		//Gestion de l'exception IO
		catch (IOException e) {
			
			System.err.println(ID+" ne répond pas !");
		}
	}
	
	
	//Validation de l'utilisateur
	private static boolean isValidM(String ID, String password) {
		
		boolean connexion = false;
		
		//Génération manuelle des logins et mdp
		String idMaitre = "master";
		String passMaitre = "admin";
		
		
		if(ID.equals(idMaitre) && password.equals(passMaitre))
		{
			  	 
			connexion=true;
		}

	return connexion;
		
	}
	
		//Validation de l'utilisateur
		private static boolean isValidE(String ID, String password) {
			
			boolean connexion = false;
			
			//Génération manuelle des logins et mdp
			String idEsclave= "esclave";
			String passEsclave = "pass";
			
			
			if(ID.equals(idEsclave) && password.equals(passEsclave))
			{
				  	 
				connexion=true;
			}

		return connexion;
			
		}
	
		


}
