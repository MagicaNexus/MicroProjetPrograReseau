
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.Synthesizer;

public class Accepterclients implements Runnable

{

	File contenu = new File("Serveur"); // Création d'un dossier maitre
	boolean oui = contenu.mkdirs();
	private ServerSocket socketserver;
	private Socket socket;
	List<Metadonnee> metadAll = new ArrayList<Metadonnee>();
	public SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	String[] doc = {"ATransferer.txt"}; 

	public Accepterclients(ServerSocket s) {
		socketserver = s;
	}

	@SuppressWarnings("unchecked")
	public void run() {
 
		try {
			while (true) {
				socket = socketserver.accept(); // Un client se connecte on l'accepte => Pas d'identification encore
				
				ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
				String parent = contenu.getName()+ "\\";
				
				Object meta = in.readObject();
				metadAll = (List<Metadonnee>) meta;
				recupereDocument(metadAll,contenu,in);
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
