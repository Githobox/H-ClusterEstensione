package data;

import database.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Classe Data che rappresenta il dataset.
 * La classe Data contiene una lista di oggetti di tipo Example.
 * Ogni oggetto Example rappresenta un esempio del dataset.
 */
public class Data {
    /**
     * Attributo data di tipo List<Example>.
     * Rappresenta il dataset.
     */
    private List<Example> data = new ArrayList<>();
    /**
     * Attributo numberOfExamples di tipo int.
     * Rappresenta il numero di esempi nel dataset.
     */
    private int numberOfExamples;
    /**
     * Costruttore della classe Data.
     * Inizializza l'attributo data con gli esempi della tabella tableName.
     * Inizializza l'attributo numberOfExamples con il numero di esempi.
     * @param tableName nome della tabella
     * @throws NoDataException se la tabella non contiene dati
     */
    public Data(String tableName) throws NoDataException {
        DbAccess db = new DbAccess();
        try {
            db.initConnection();
            TableData tableData = new TableData(db);
            data = tableData.getDistinctTransazioni(tableName);
            numberOfExamples = data.size();
        } catch (SQLException | EmptySetException | MissingNumberException | DatabaseConnectionException e) {
            throw new NoDataException("Errore durante il recupero dei dati dalla tabella: " + e.getMessage());
        } finally {
            if (db != null) {
                db.closeConnection();
            }
        }
        if (numberOfExamples == 0) {
            throw new NoDataException("La tabella non contiene dati.");
        }
    }
    /**
     * Restituisce il numero di esempi memorizzati in data.
     * @return numberOfExamples
     */
    public int getNumberOfExamples() {
        return numberOfExamples;
    }
    /**
     * Restituisce l'esempio memorizzato in data[exampleIndex].
     *
     * @param exampleIndex indice dell'esempio
     * @return data[exampleIndex]
     */
    public Example getExample(int exampleIndex) {
        return data.get(exampleIndex);
    }
    /**
     * Matrice Triangolare Superiore delle distanze Euclideee calcolate tra gli esempi memorizzati in data.
     * Tale matrice va avvalorata usando il  metodo distance() di Example.
     * @return distMatrix
     */
    public double[][] distance() {
        int n = data.size();
        double[][] distMatrix = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                if (i == j) {
                    distMatrix[i][j] = 0.0; // Distance from a point to itself is zero
                } else {
                    double dist = data.get(i).distance(data.get(j));
                    distMatrix[i][j] = dist;
                    distMatrix[j][i] = dist; // Ensuring symmetry
                }
            }
        }

        return distMatrix;
    }
    /**
     * Crea una stringa in cui memorizza gli esempi memorizzati nell'attributo data, opportunamente enumerati.
     * @return sb.toString()
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numberOfExamples; i++) {
            sb.append(i).append(": [ ").append(data.get(i).toString()).append("]\n");
        }
        return sb.toString();
    }
    /**
     * Metodo main di test della classe Data.
     * @param args argomenti della riga di comando
     */
    public static void main(String args[]) {
        try {
            Data trainingSet = new Data("tableName");
            System.out.println(trainingSet);
            double[][] distanceMatrix = trainingSet.distance();
            System.out.println("Distance matrix:\n");
            for (int i = 0; i < distanceMatrix.length; i++) {
                for (int j = 0; j < distanceMatrix.length; j++)
                    System.out.print(distanceMatrix[i][j] + "\t");
                System.out.println("");
            }
        } catch (NoDataException e) {
            e.printStackTrace();
        }
    }
}
