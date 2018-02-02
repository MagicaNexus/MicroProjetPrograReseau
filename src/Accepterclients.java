
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.Synthesizer;

public class Accepterclients implements Runnable

{
	private ServerSocket socketserver;
	private Socket socket;
	List<Metadonnee> metadAll = new ArrayList<Metadonnee>();
	public SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	String[] doc = {"ATransferer.txt"}; 
	String serv;

	public Accepterclients(ServerSocket s, String repRacine) {
		socketserver = s;
		serv = repRacine;
	}
 
	@SuppressWarnings("unchecked")
	public void run() {
 
		try {
			while (true) {
				socket = socketserver.accept(); // Un client se connecte on l'accepte => Pas d'identification encore
				
				//ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
				out.writeObject(serv);
				out.flush();
				/*File recv = (File) in.readObject();*/
				//File contenu = new File(serv); // Création d'un dossier maitre
				//boolean oui = contenu.mkdirs();
				//System.out.println("A ete cree " + oui  + " recu : "+ recv.listFiles().toString());
				
				//System.out.println("De nom " + contenu.getName() +" De type : " +
						//contenu.isDirectory()+ " chemin : " + contenu.getAbsolutePath() );
				
				//copieDoc(recv.listFiles(), contenu);
				/*String parent = recv.getName()+ "\\";
				
				Object meta = in.readObject();
				metadAll = (List<Metadonnee>) meta;*/
				//recupereDocument(metadAll,contenu,in);
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void copieDoc(File[] m,File contenu) throws ClassNotFoundException, IOException
	{
		 
		for(File f: m) 
		{
			System.out.println("\n"+ f.getName()  + "De parent : " +f.getParent()  );
			File nF = new File (contenu.getAbsolutePath(),f.getName());
			System.out.println(f.getPath()+ "     " + f.getAbsolutePath() + "\n");
			System.out.println(nF.getPath().replace(System.getProperty("user.dir"), "")+ "     " + nF.getAbsolutePath() + "\n");
			if (f.isDirectory()) 
			{
				boolean oui = nF.mkdir();
				copieDoc(f.listFiles(),nF);
				//f.setLastModified();
				
			} else {
				nF.createNewFile();
				//f.setLastModified(m.get(i).dateM);
				 FileOutputStream out = new FileOutputStream(f);
				/* for (int j = 0;j<m.get(i).co.length;j++)
					{
						out.write(m.get(i).co[j]);
					}*/
		}
		
	}			
			
					
			
		
	}	
	public static void recupereDocument(List<Metadonnee> m,File contenu,ObjectInputStream in) throws ClassNotFoundException, IOException
	{
		for(int i=0;i<m.size();i++)
		{
			
			System.out.println("\n"+m.get(i).nom);
			m.get(i).setChemin(m.get(i).chemin.replace("Maitre", contenu.getAbsolutePath()));
			System.out.println(m.get(i).chemin + "\n");
			File f = new File(m.get(i).chemin); // Création d'un dossier maitre
			System.out.println(f.getAbsolutePath() + "\n");
			if (f.isDirectory()) 
			{
				boolean oui = f.mkdir();
				f.setLastModified(m.get(i).dateM);
				
			} else {
				f.createNewFile();
				f.setLastModified(m.get(i).dateM);
				 FileOutputStream out = new FileOutputStream(f);
				 for (int j = 0;j<m.get(i).co.length;j++)
					{
						out.write(m.get(i).co[j]);
					}
					
				
			}
		}
		
	}	
}
