package clustering;

/**
 * Eccezione sollevata se la profondità con cui è stato istanziato il dendrogramma è superiore al numero di esempi memorizzati nel dataset.
 */
public class InvalidDepthException extends Exception{

    /**
     * Costruttore della classe InvalidDepthException.
     * @param message messaggio di errore
     */
    public InvalidDepthException(String message){
        super(message);
    }
    /**
     * Costruttore della classe InvalidDepthException.
     */
    public InvalidDepthException(){
        super("[!]La profondità del dendrogramma è superiore al numero di esempi memorizzati nel dataset.");
    }

}
