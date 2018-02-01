
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
				
				/*Object meta = in.readObject();
				Metadonnee met = (Metadonnee) meta;
				System.out.println(met.nom);
				File file = new File( parent ,met.nom);
				System.out.println(file.getAbsolutePath());*/
				
				/*meta = in.readObject();
				met = (Metadonnee) meta;
				System.out.println(met.nom);
				file = new File( parent ,met.nom);
				System.out.println(file.getAbsolutePath());*/
				/*transfertDocument(doc,contenu.getName(),in);*/
				//Transfer.transfert(in, new FileOutputStream(file), false);
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
				Transfer.transfert(in, new FileOutputStream(f), false);
			} else {
				f.createNewFile();
				f.setLastModified(m.get(i).dateM);
				Transfer.transfert(in, new FileOutputStream(f), false);
			}
		}
		//Transfer.transfert(new FileInputStream(f), out, false);	
	}
	
	/*public static void recupereDocument(String[] paths, String parent, ObjectInputStream in)
			throws IOException, ClassNotFoundException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		System.out.println("---- Début du transfert......................");
		System.out.println("Affichage du chemin : " + paths);
		System.out.println("Affichage du parent : " + parent);
		
		System.out.println("Date de derniere modification de la metadonnée : " + sdf.format(m.dateM));
		
		
		for (String path : paths) {
			File f = new File(, m.nom);
			// Debug
			System.out.println("Création du fichier à l'emplacement suivant : " + parent + "\\" + path);
			if (f.isDirectory()) {
				parent += "\\" + f.getName();
				transfertDocument(f.list(), f.getName(), in);
				// debug
				System.out.println("Changement de repertoire réussi : " + parent);
			}

			// debug
			System.out.println("\n\nCréation métadonnées ok\n\n");
			Transfer.transfert(in, new FileOutputStream(f), false);
			f.setLastModified(m.dateM);
			System.out.println("Date modif du fichier : " + sdf.format(f.lastModified()));

			System.out.println("\n\nFin du transfert .........................................");
		}*/
	
}
