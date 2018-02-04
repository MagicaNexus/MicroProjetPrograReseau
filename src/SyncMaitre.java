import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class SyncMaitre extends SyncEsclave{
	
	public static void main(String[] args) {
		int svrNomPort = 0;
		String repSrc = null, repRacine = null;
		String options ="";
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
	    boolean z = true;
	    String choix = null;
		if (args.length <3)
		{
			System.out.println("Il faut au moins mettre : java SyncMaitre serveurPort repertoireSource repertoireRacine");
		}else {
			if(args.length >5){
				System.out.println("Seule 2 options simultanees sont possibles -w -s, -e -s");
				System.out.println("Reselectionner le choix : w, s, e en collant les 2 lettres au max ");
				while (z == true) {
			        choix = sc.nextLine();
			        if (choix.length()<=2) {
			          z = false;
			          args[3] = choix.substring(0, 1); 
			          options += args[3];
			          if(choix.length()==2)
			          {
			        	  args[4] = choix.substring(1);
			          	  options += args[4];
			          }  
			        } else
			          System.out.println("Reselectionner le choix : w, s, e en collant les 2 lettres au max ");
			      }
				System.out.println("Les options sont :" + options);
			}
			svrNomPort = Integer.parseInt(args[0]);
			repSrc = args[1];
			repRacine = args[2];
			if(choix == "") {
				options += args[3];
				if(args.length==4)
					options += args[4];
			}
			System.out.println("Les options sont :" + options);	
		}
		
		Socket socket; //Creation socket 
		File Source;  //Creation d'un dossier maitre
		File Racine; //Creation d'un dossier serveur
		choix = "";
		z=true;
		
		try {  
			
			//Connexion du maitre
			System.out.println("Je suis le Maitre et je viens de me connecter");
			//Attribution de l'adresse et du port de la socket
		    socket = new Socket(InetAddress.getLocalHost(),svrNomPort);	
		    
		    //Choix du mode de transfert
		    System.out.println("Choix du mode de transfert de fichier :\n"
		    		+ "push vers serveur --> watchdog \n"
		    		+ "pull vers client --> watchdog + suppression\n");
		    
		    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
		    String repS = (String) in.readObject();
		    System.out.println("Repertoire racine envoye du serveur : \n"+ repS);
		    in.close();
		    if(repS.equals(repRacine)) {
		    	System.out.println("Repertoire racine ok \n");
		    }else {
		    	System.out.println("Repertoire racine"+repRacine+"\nremplace par"+ repS +"\n");
		    	repRacine = repS;
		    	System.out.println("Repertoire racine modifie: \n"+ repRacine);
		    }

	        Source = new File (repSrc);
		    Racine = new File (repRacine);
		    
		    System.out.println("Voulez vous pull ou push ? \n");
		    while (z == true) {
		        choix = sc.nextLine();
		        if ("pull".equals(choix) || "push".equals(choix)) {
		          z = false;
		        } else
		          System.out.println("Reselectionner le choix : 'pull' ou 'push' ");
		      }
		      System.out.println("Okay, vous avez choisi de faire un " + choix);
		    
		      if("pull".equals(choix))
		      {
		    	  if(options.length()==0) {
						System.out.println("On execute le mode watchdog et suppression par défaut \n");
						viderDossier(Source);
						pullW(Racine,Source);
						
					}else {
						switch(options.indexOf('s')) {
						case -1 : 
							System.out.println("Le mode suppression n'est pas selectionné : \n");
							break;
						default :
							System.out.println("Le mode suppression est selectionné : \n");
							pullS(Racine,Source);
							break;
						}
					
						switch(options.indexOf('e')) {
							case -1 : 
								System.out.println("Le mode écrasement n'est pas selectionné : \n");
								break;
							default :
								System.out.println("Le mode écrasement est selectionné : \n");
								pullE(Racine,Source);
								break;
						}
					
						switch(options.indexOf('w')) {
							case -1 : 
								System.out.println("Le mode watchdog n'est pas selectionné : \n");
								break;
							default :
								System.out.println("Le mode watchdog est selectionné : \n");
								pullW(Racine,Source);
								break;
						}
					}
		      }else {
		    	  if(options.length()==0) {
					System.out.println("On execute le mode watchdog par défaut \n");
					pullW(Source, Racine);
		    	  }else {
					switch(options.indexOf('s')) {
						case -1 : 
							System.out.println("Le mode suppression n'est pas selectionné : \n");
							break;
						default :
							System.out.println("Le mode suppression est selectionné : \n");
							pullS(Source, Racine);
							break;
					}
				
					switch(options.indexOf('e')) {
						case -1 : 
							System.out.println("Le mode écrasement n'est pas selectionné : \n");
							break;
						default :
							System.out.println("Le mode écrasement est selectionné : \n");
							pullE(Source, Racine);
							break;
					}
				
					switch(options.indexOf('w')) {
						case -1 : 
							System.out.println("Le mode watchdog n'est pas selectionné : \n");
							break;
						default :
							System.out.println("Le mode watchdog est selectionné : \n");
							pullW(Source, Racine);
							break;
					}
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
}

	