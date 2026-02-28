package distance;

/**
 * Classe eccezione che si occupa di gestire le dimensioni non valide.
 * Solleva un'eccezione se si prova a calcolare la distanza tra due esempi di dimensioni diverse.
 */
public class InvalidSizeException extends Exception{
    /**
     * Costruttore della classe InvalidSizeException.
     * @param message messaggio di errore
     */
    public InvalidSizeException(String message) {
        super(message);
    }
    /**
     * Costruttore della classe InvalidSizeException senza parametri.
     */
    public InvalidSizeException() {
        super("[!]Dimensione invalida.");
    }
}
