package data;

/**
 * Classe eccezione che si occupa di gestire attributi senza dati.
 * Solleva un'eccezione se ci si trova d'avanti ad attributi senza dati.
 */
public class NoDataException extends Exception{
    /**
     * Costruttore della classe NoDataException.
     * @param message messaggio di errore
     */
    public NoDataException(String message) {
        super(message);
    }
    /**
     * Costruttore della classe NoDataException senza parametri.
     */
    public NoDataException() {
        super("[!]Attributi senza dati.");
    }

}
