package clustering;

import data.Data;

import java.io.Serializable;

/**
 * Classe Dendrogram che rappresenta un dendrogramma.
 * Un dendrogramma è una sequenza di ClusterSet.
 */
public class Dendrogram implements Serializable{
    /**
     * Attributo tree di tipo ClusterSet[].
     */
    private ClusterSet tree[];
    /**
     * Crea un vettore di dimensione depth con cui inizializza tree.
     * @param depth dimensione del vettore
     */
    Dendrogram(int depth) {
        tree = new ClusterSet[depth];
    }
    /**
     * memorizza c nella posizione level di tree
     */
    void setClusterSet(ClusterSet c, int level){
        tree[level] = c;
    }
    /**
     * restituisce tree[level]
     * @param level livello
     * @return tree[level]
     */
    ClusterSet getClusterSet(int level){
        return tree[level];
    }
    /**
     * la profondità del dendrogamma (ossia la dimensione di tree)
     * @return int
     */
    int getDepth(){
        return tree.length;
    }
    /**
     * restituisce una stringa che rappresenta il dendrogramma
     * @return v stringa che rappresenta il dendrogramma
     */
    public String toString() {
        String v="";
        for (int i=0;i<tree.length;i++)
        v+=("level"+i+":\n"+tree[i].toString()+"\n");
        return v;
    }
    /**
     * restituisce una stringa che rappresenta il dendrogramma
     * @param data oggetto di tipo Data
     * @return v stringa che rappresenta il dendrogramma
     */
    public String toString(Data data) {
        String v="";
        for (int i=0;i<tree.length;i++)
        v+=("level"+i+":\n"+tree[i].toString(data)+"\n");
        return v;
    }
}
