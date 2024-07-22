package distance;

import clustering.Cluster;
import data.Data;
import data.Example;

import java.util.Iterator;

/**
 * Classe SingleLinkDistance che implementa l'interfaccia ClusterDistance.
 * La classe SingleLinkDistance si occupa di calcolare la distanza single-link tra due cluster.
 * La distanza single-link tra due cluster è definita come la distanza minima tra tutti gli esempi
 * di un cluster e tutti gli esempi dell'altro cluster.
 */
public class SingleLinkDistance implements ClusterDistance{
    /**
     * Costruttore della classe SingleLinkDistance.
     */
    public SingleLinkDistance() {
       // Costruttore vuoto
   }
    /**
     * Calcola la distanza single-link tra due cluster.
     * @param c1 Il primo cluster
     * @param c2 Il secondo cluster
     * @param d  L'oggetto Data contenente gli esempi
     * @return La distanza single-link tra i due cluster
     * @throws InvalidSizeException se gli esempi hanno dimensioni diverse
     */
    public double distance(Cluster c1, Cluster c2, Data d) throws InvalidSizeException {
        double min = Double.MAX_VALUE;

        Iterator<Integer> it1 = c1.iterator();
        while (it1.hasNext()) {
            int id1 = it1.next();
            Example e1 = d.getExample(id1);

            Iterator<Integer> it2 = c2.iterator();
            while (it2.hasNext()) {
                int id2 = it2.next();
                double distance = e1.distance(d.getExample(id2));

                if (distance < min)
                    min = distance;
            }
        }
        return min;
    }
}
