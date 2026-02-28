package clustering;

import data.Data;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Classe Cluster che rappresenta un cluster.
 * Un cluster è un insieme di indici di esempi.
 */

public class Cluster implements Serializable,Iterable<Integer>, Cloneable{

    /**
     * Attributo clusteredData di tipo Set.
     * Rappresenta gli indici degli esempi del cluster.
     */
	private Set<Integer> clusteredData=new TreeSet<>();
    /**
     * Costruttore predefinito della classe Cluster.
     */
    public Cluster() {
        // Il costruttore predefinito non ha bisogno di fare nulla di specifico.
    }
    /**
     * Aggiunge l'indice di un esempio al cluster.
     * @param id indice dell'esempio
     */
    void addData(int id) {
        clusteredData.add(id);
    }
    /**
     * Restituisce il numero di esempi nel cluster.
     * @return int numero di esempi nel cluster
     */
	int getSize() {
		return clusteredData.size();
	}
    /**
     * Restituisce un iteratore per gli indici degli esempi del cluster.
     */
	@Override
    public Iterator<Integer> iterator() {
        return clusteredData.iterator();
    }
    /**
     * Restituisce il clone del cluster.
     * @return Object
     */
    @Override
    public Object clone() {
        try {
            Cluster copy = (Cluster) super.clone();
            copy.clusteredData = new TreeSet<>(this.clusteredData);
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // Should never happen
        }
    }
    /**
     * Crea un nuovo cluster che è la fusione dei due cluster pre-esistenti
     * @param c cluster da unire
     * @return Cluster cluster risultante dalla fusione
     */
	Cluster mergeCluster(Cluster c) {
        Cluster newC = new Cluster();
        newC.clusteredData.addAll(this.clusteredData);
        newC.clusteredData.addAll(c.clusteredData);
        return newC;
    }
    /**
     * Restituisce una stringa che rappresenta il cluster.
     */
	@Override
    public String toString() {
        String str = "";
        Iterator<Integer> it = clusteredData.iterator();
        while (it.hasNext()) {
            str += it.next();
            if (it.hasNext()) {
                str += ", ";
            }
        }
        return str;
    }
    /**
     * Restituisce una stringa che rappresenta il cluster.
     * @param data dataset
     * @return String rappresentazione del cluster
     */
	String toString(Data data) {
        String str = "";
        Iterator<Integer> it = clusteredData.iterator();
        while (it.hasNext()) {
            int id = it.next();
            str += "<[" + data.getExample(id) + "]>";
        }
        return str;
    }
}
