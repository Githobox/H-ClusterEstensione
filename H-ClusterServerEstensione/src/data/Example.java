package data;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Classe che rappresenta un esempio.
 * Un esempio è una sequenza di valori reali.
 * Gli esempi sono utilizzati per rappresentare i punti nello spazio.
 */

public class Example implements Iterable<Double>{
    /**
     * Attributo example di tipo List<Double>.
     */
    private List<Double> example;
    /**
     * Inizializza example come vettore di dimensione lenght.
     */
    public Example () {
        example=new LinkedList<>();
    }
    /**
     * Restituisce un iteratore per example.
     * @return example.iterator() iteratore per example
     */
    public Iterator<Double> iterator() {
        return example.iterator();
    }
    /**
     * Modifica example inserendo v in posizione index.
     * @param v valore da inserire
     */
    public void add(Double v) {
        example.add(v);
    }
    /**
     * Restituisce il valore in posizione index.
     * @param index posizione
     * @return example.get(index) valore in posizione index
     */
    Double get(int index) {
        return example.get(index);
    }
    /**
     * Restituisce la dimensione di example.
     * @return example.size() dimensione di example
     */
    public int size() {
        return example.size();
    }
    /**
     * Calcola la distanza euclidea tra this.example e new.example.
     * @param newE esempio da confrontare
     * @return dist distanza euclidea tra this.example e new.example
     */
    public double distance(Example newE) {
        double dist = 0;
        Iterator<Double> it1 = this.iterator();
        Iterator<Double> it2 = newE.iterator();

        while (it1.hasNext() && it2.hasNext()) {
            double val1 = it1.next();
            double val2 = it2.next();
            dist += Math.pow(val1 - val2, 2);
        }
        return dist;
    }
    /**
     * La stringa che rappresenta il contenuto di example.
     * @return s stringa che rappresenta il contenuto di example
     */
    public String toString(){
        String s = "";
        for (Double aDouble : example) {
            s += aDouble + " ";
        }
        return s.trim();
    }

}
