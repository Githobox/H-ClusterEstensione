package database;

/**
 * Classe eccezione che si occupa di gestire gli attributi non numerici.
 * Solleva un'eccezione se ci si trova d'avanti ad attributi non numerici.
 */
public class MissingNumberException extends Exception{
    /**
     * da sollevare se ci si trova d'avanti ad attributi non numerici.
     * @param message messaggio di errore
     */
    public MissingNumberException(String message) {
        super(message);
    }
    /**
     * Costruttore della classe MissingNumberException senza parametri.
     */
    public MissingNumberException() {
        super("[!]Attributi non numerici.");
    }
}
