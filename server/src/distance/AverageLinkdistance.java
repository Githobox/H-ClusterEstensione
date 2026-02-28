package distance;

import clustering.Cluster;
import data.Data;
import data.Example;

import java.util.Iterator;

/**
 * Classe AverageLinkdistance che implementa l'interfaccia ClusterDistance.
 * La classe AverageLinkdistance si occupa di calcolare la distanza average-link tra due cluster.
 * La distanza average-link tra due cluster è definita come la media delle distanze tra tutti gli esempi
 * di un cluster e tutti gli esempi dell'altro cluster.
 */
public class AverageLinkdistance implements ClusterDistance {

    /**
     * Costruttore predefinito della classe AverageLinkdistance.
     */
    public AverageLinkdistance() {
        // Il costruttore predefinito non ha bisogno di fare nulla di specifico.
    }
    /**
     * Calcola la distanza average-link tra due cluster.
     *
     * @param c1 Il primo cluster
     * @param c2 Il secondo cluster
     * @param d  L'oggetto Data contenente gli esempi
     * @return La distanza average-link tra i due cluster
     * @throws InvalidSizeException se gli esempi hanno dimensioni diverse
     */
    @Override
    public double distance(Cluster c1, Cluster c2, Data d) throws InvalidSizeException {
        double distanzaTotale = 0.0;
        int conteggio = 0;

        Iterator<Integer> it1 = c1.iterator();
        while (it1.hasNext()) {
            int id1 = it1.next();
            Example e1 = d.getExample(id1);

            Iterator<Integer> it2 = c2.iterator();
            while (it2.hasNext()) {
                int id2 = it2.next();
                Example e2 = d.getExample(id2);

                if (e1.size() != e2.size())
                    throw new InvalidSizeException("Gli esempi devono avere la stessa dimensione");

                distanzaTotale += e1.distance(e2);
                conteggio++;
            }
        }
        if (conteggio == 0) {
            throw new InvalidSizeException("Almeno un cluster non contiene elementi");
        }
        return distanzaTotale / conteggio;
    }
}
