package distance;

import clustering.Cluster;
import data.Data;
/**
 * Interfaccia ClusterDistance che si occupa di calcolare la distanza tra due cluster.
 * La classe ClusterDistance contiene un metodo distance che restituisce la distanza tra due cluster.
 */
public interface ClusterDistance {
	/**
	 * Metodo distance che restituisce la distanza tra due cluster.
	 * @param c1 primo cluster
	 * @param c2 secondo cluster
	 * @param d oggetto di tipo Data
	 * @return distanza tra due cluster
	 * @throws InvalidSizeException eccezione lanciata in caso di dimensioni non valide
	 */
	public double distance(Cluster c1, Cluster c2, Data d) throws InvalidSizeException;
}
