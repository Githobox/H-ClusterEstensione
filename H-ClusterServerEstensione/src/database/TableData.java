package database;

import data.Example;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe TableData che si occupa di gestire i dati contenuti in una tabella.
 * La classe TableData contiene un oggetto di tipo DbAccess.
 * Inizializza db con il database passato come parametro.
 * Interroga la tabella con nome table nel database e restituisce la lista di
 * Example memorizzata nella tabella.
 */
public class TableData {
    /**
     * Attributo db di tipo DbAccess.
     */
    private DbAccess db;

    /**
     * Inizializza db con il database passato come parametro.
     * @param db database
     */
    public TableData(DbAccess db) {
        this.db = db;
    }
    /**
     * Interroga la tabella con nome table nel database e restituisce la lista di
     * Example memorizzata nella tabella.
     * Solleva e propaga un'istanza di:
     * SLQException in caso di errore nella interrogazione, EmptySetException in
     * caso di tabella vuota, MissingNumberException in presenza di attributi non
     * numerici.
     *
     * @param table nome della tabella
     * @return transazioni lista di Example memorizzata nella tabella
     * @throws SQLException Ecccezione lanciata in caso di errore nella interrogazione
     * @throws EmptySetException Eccezione lanciata in caso di tabella vuota
     * @throws MissingNumberException  Eccezione lanciata in presenza di attributi non numerici
     * @throws DatabaseConnectionException Eccezione lanciata in caso di errore nella connessione al database
     */
    public List<Example> getDistinctTransazioni(String table)
            throws SQLException, EmptySetException, MissingNumberException, DatabaseConnectionException {

        TableSchema tableSchema = new TableSchema(db, table);
        Statement st = null;
        ResultSet rs = null;
        try {
            st = db.getConnection().createStatement();
            rs = st.executeQuery("SELECT * FROM " + table + ";");

            if (!rs.next())
                throw new EmptySetException("Il resultSet è vuoto.");

            List<Example> transazioni = new ArrayList<>();

            // Itera sui risultati del ResultSet
            do {
                Example e = new Example();
                for (int i = 0; i < tableSchema.getNumberOfAttributes(); i++) {
                    if (tableSchema.getColumn(i).isNumber()) {
                        e.add(rs.getDouble(i + 1));
                    } else {
                        throw new MissingNumberException("La tabella contiene attributi non numerici.");
                    }
                }
                transazioni.add(e);
            } while (rs.next());

            return transazioni;
        } finally {
            // Chiude lo Statement e il ResultSet
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }
}
