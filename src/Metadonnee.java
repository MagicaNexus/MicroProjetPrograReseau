import java.io.Serializable;
import java.text.SimpleDateFormat;

public class Metadonnee implements Serializable {

	public String nom;
	public String chemin;
	public double tailleB;
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    public long dateM;
    public String contenu;
    public byte[] co;
	public Metadonnee(String fileName, String chem, double tail,long date ,String cont,byte[] con) {
		nom = fileName;
		chemin = chem;
		tailleB = tail;
		dateM = date;	
		contenu = cont;
		co = con;
	}
	
	public void setChemin(String chem)
	{
		chemin = chem;
	}
	
	public String toString() {
		String meta;
		 meta  = nom + " est dans l'emplacement suivant : \n" + chemin 
				 + "\n \tDate derniere modification : "+ sdf.format(dateM) 
				 + "\n \tTaille du document : "+ tailleB + " octets\n";
		return meta;	
	}
}
