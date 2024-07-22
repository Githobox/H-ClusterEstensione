package clustering;

import data.Data;
import distance.ClusterDistance;
import distance.InvalidSizeException;

import java.io.*;

/**
 * Classe HierachicalClusterMiner che rappresenta un algoritmo di clustering gerarchico.
 * L'algoritmo di clustering gerarchico costruisce un dendrogramma.
 */

public class HierachicalClusterMiner implements Serializable {
    /**
     * Attributo dendrogram di tipo Dendrogram.
     * Rappresenta il dendrogramma.
     */
    private Dendrogram dendrogram;
    /**
     * Costruttore della classe HierachicalClusterMiner.
     * @param depth profondità del dendrogramma
     * @throws InvalidDepthException eccezione lanciata in caso di profondità non valida
     */
    public HierachicalClusterMiner(int depth) throws InvalidDepthException {
        if (depth < 1)
            throw new InvalidDepthException("La profondità deve essere almeno 1.");
        dendrogram = new Dendrogram(depth);
    }
    /**
     * Crea il livello base (livello 0) del dendrogramma che
     * contiene l’istanza di ClusterSet che rappresenta ogni esempio in un cluster
     * separato; per tutti i livelli successivi del dendrogramma (level &gt;= 1 e level &lt;
     * dendrogram.getDepth()) costruisce l’istanza di ClusterSet che realizza la fusione dei
     * due cluster più vicini nell'istanza del ClusterSet memorizzata al livello level-1 del
     * dendrogramma (usare mergeClosestClusters di ClusterSet); memorizza l’istanza di
     * ClusterSet ottenuta per fusione nel livello level del dendrogramma.
     *
     * @param data Il dataset
     * @param distance La distanza tra i cluster
     * @throws InvalidSizeException se gli esempi hanno dimensioni diverse
     * @throws InvalidDepthException se la profondità del dendrogramma è superiore al numero di esempi nel dataset
     */
    public void mine(Data data, ClusterDistance distance) throws InvalidDepthException, InvalidSizeException {
        if (dendrogram.getDepth() > data.getNumberOfExamples()) {
            throw new InvalidDepthException("La profondità del dendrogramma è superiore al numero di esempi nel dataset.");
        }
        ClusterSet clusterSet = new ClusterSet(data.getNumberOfExamples());
        for (int i = 0; i < data.getNumberOfExamples(); i++) {
            Cluster c = new Cluster();
            c.addData(i);
            clusterSet.add(c);
        }
        dendrogram.setClusterSet(clusterSet, 0);
        for (int level = 1; level < dendrogram.getDepth(); level++) {
            ClusterSet newClusterSet = dendrogram.getClusterSet(level - 1).mergeClosestClusters(distance, data);
            dendrogram.setClusterSet(newClusterSet, level);
        }
    }
    /**
     * Restituisce il dendrogramma.
     * @return dendrogram
     */
    public String toString() {
        return dendrogram.toString();
    }
    /**
     * Restituisce il dendrogramma.
     * @param data Il dataset
     * @return dendrogram
     */
    public String toString(Data data) {
        return dendrogram.toString(data);
    }
    /**
     * Salva un'istanza di HierachicalClusterMiner in un file.
     * @param fileName nome del file
     * @throws FileNotFoundException se il file non è stato trovato
     * @throws IOException se si verifica un errore di I/O
     */
    public void salva(String fileName) throws FileNotFoundException, IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(this);
            out.close();
        }catch (IOException e){
            throw new IOException("[!] Errore di salvataggi");
        }
    }
    /**
     * Carica un'istanza di HierachicalClusterMiner da un file.
     *
     * @param fileName nome del file
     * @return clustering HierachicalClusterMiner
     * @throws FileNotFoundException se il file non è stato trovato
     * @throws IOException se si verifica un errore di I/O
     * @throws ClassNotFoundException se la classe non è stata trovata
     */
    public static HierachicalClusterMiner loadHierachicalClusterMiner(String fileName)
            throws FileNotFoundException, IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            HierachicalClusterMiner clustering = (HierachicalClusterMiner) in.readObject();
            return clustering;
        }
    }
}
