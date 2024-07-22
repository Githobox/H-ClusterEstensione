package clustering;

import data.Data;
import distance.ClusterDistance;
import distance.InvalidSizeException;

import java.io.Serializable;

/**
 * Classe ClusterSet che rappresenta un insieme di cluster.
 * Un ClusterSet è un insieme di cluster.
 */
class ClusterSet implements Serializable{

	/**
	 * Attributo C di tipo Cluster[].
	 * Rappresenta l'insieme di cluster.
	 */
	private Cluster C[];
	/**
	 * Attributo lastClusterIndex di tipo int.
	 * Rappresenta l'indice dell'ultimo cluster.
	 */
	private int lastClusterIndex=0;
	/**
	 * Costruttore della classe ClusterSet.
	 * Inizializza l'attributo C con un array di k cluster.
	 * @param k numero di cluster
	 */
	ClusterSet(int k){
		C=new Cluster[k];
	}
	/**
	 * Aggiunge un cluster all'insieme di cluster.
	 * @param  c cluster da aggiungere
	 */
	void add(Cluster c){
		for(int j=0;j<lastClusterIndex;j++)
			if(c==C[j]) // to avoid duplicates
				return;
		C[lastClusterIndex]=c;
		lastClusterIndex++;
	}
	/**
	 * Restituisce il cluster in posizione i.
	 * @param i indice del cluster
	 * @return Cluster cluster in posizione i
	 */
	Cluster get(int i){
		return C[i];
	}
	/**
	 * Restituisce il numero di cluster memorizzati in C.
	 * @return str stringa contenente i cluster memorizzati in C
	 */
	public String toString(){
		String str="";
		for(int i=0;i<C.length;i++){
			if (C[i]!=null){
				str+="cluster"+i+":"+C[i].toString()+"\n";

			}
		}
		return str;
	}
	/**
	 * Determina la coppia di cluster più simili (usando il metodo distance di ClusterDistance e li fonde in un unico cluster).
	 * Crea una nuova istanza di ClusterSet che contiene tutti i cluster dell'oggetto this a meno dei due cluster fusi al posto dei quali inserisce il cluster risultante dalla fusione.
	 * N.B. l'oggetto ClusterSet risultante memorizza un numero di cluster che è pari al numero di cluster memorizzato nell'oggetto this meno 1.
	 * distance: oggettto per il calcolo della distanza tra due cluster.
	 * data: oggetto istanza che rappresenta il dataset in cui si sta calcolando l'oggetto istanza di ClusterSet.
	 * @param distance l'oggetto per il calcolo della distanza tra due cluster
	 * @param data l'oggetto istanza che rappresenta il dataset in cui si sta calcolando l'oggetto istanza di ClusterSet
	 * @return ClusterSet l'oggetto istanza di ClusterSet risultante dalla fusione dei due cluster piu' vicini
	 * @throws InvalidSizeException se gli esempi hanno dimensioni diverse
	 */
	ClusterSet mergeClosestClusters(ClusterDistance distance, Data data) throws InvalidSizeException{
		ClusterSet newClusterSet=new ClusterSet(C.length-1);
		//trova i due cluster pi� vicini
		int i1=0;
		int i2=1;
		double minDist=distance.distance(C[i1], C[i2], data);
		for(int i=0;i<C.length;i++)
			for(int j=i+1;j<C.length;j++){
				double dist=distance.distance(C[i], C[j], data);
				if(dist<minDist){
					i1=i;
					i2=j;
					minDist=dist;
				}
			}
		//fonde i due cluster piu' vicini
		Cluster newC=C[i1].mergeCluster(C[i2]);
		newClusterSet.add(newC);
		//aggiunge i cluster non fusi
		for(int i=0;i<C.length;i++)
			if(i!=i1 && i!=i2)
				newClusterSet.add(C[i]);
		return newClusterSet;
	}
	/**
	 * Restituisce la Stringa che rappresenta l'oggetto istanza di ClusterSet.
	 * @param data oggetto istanza che rappresenta il dataset in cui si sta calcolando l'oggetto istanza di ClusterSet
	 * @return str stringa che rappresenta l'oggetto istanza di ClusterSet
	 */
	String toString(Data data){
		String str="";
		for(int i=0;i<C.length;i++){
			if (C[i]!=null){
				str+="cluster"+i+":"+C[i].toString(data)+"\n";

			}
		}
		return str;
	}
}
