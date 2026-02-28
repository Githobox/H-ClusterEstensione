import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.EOFException;
import java.net.Socket;
import clustering.HierachicalClusterMiner;
import clustering.InvalidDepthException;
import data.Data;
import data.NoDataException;
import distance.ClusterDistance;
import distance.InvalidSizeException;
import distance.SingleLinkDistance;
import distance.AverageLinkdistance;

/**
 * Classe ServerOneClient che si occupa di gestire la connessione con un singolo client.
 * Il server rimane in ascolto sulla porta 8080 e accetta nuove connessioni.
 * Per ogni connessione accettata viene creato un nuovo thread ServerOneClient
 * che si occuperà di gestire la connessione con il client.
 * In caso di errori durante l'esecuzione del server, viene stampato un messaggio di errore.
 */

public class ServerOneClient extends Thread{
    /**
     * Socket per la comunicazione con il client.
     */
    Socket socket;
    /**
     * Stream di output per inviare dati al client.
     */
    ObjectOutputStream out;
    /**
     * Stream di input per ricevere dati dal client.
     */
    ObjectInputStream in;
    /**
     * Nome della tabella.
     */
    String tableName = "";
    /**
     * Oggetto HierachicalClusterMiner per effettuare il clustering.
     */
    HierachicalClusterMiner clustering = null;

    /**
     * Costruttore della classe ServerOneClient.
     * Inizializza gli stream di input e output per la comunicazione con il client.
     * @param s socket per la comunicazione con il client
     * @throws IOException in caso di errore durante la comunicazione con il client
     */
    public ServerOneClient(Socket s) throws IOException {
        socket = s;
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }

    /**
     * Metodo run del ServerOneClient.
     * In base alla scelta del client, il server esegue un'operazione diversa.
     * case 0: caricamentoDati();
     * case 1: caricaDaFile();
     * case 2: caricaDatabase();
     * case 3: salvataggioFile();
     */
    public void run() {
        try {
            while (true) {
                int scelta = (int) in.readObject();
                System.out.println("Scelta: " + scelta);
                switch (scelta) {
                    case 0:
                        caricamentoDati();
                        break;
                    case 1:
                        caricaDaFile();
                        break;
                    case 2:
                        caricaDatabase();
                        break;
                    case 3:
                        salvataggioFile();
                        break;
                    case 4:
                        System.out.println("Chiusura connessione.");
                        close();
                        return;
                    default:
                        System.out.println("Opzione non valida.");
                }
            }
        } catch (EOFException e) {
            System.out.println("Connessione chiusa dal client.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Errore durante la comunicazione con il client: " + e.getMessage());
        } finally {
            close();
        }
    }

    /**
     * Metodo per il caricamento dei dati.
     * Il server riceve il nome della tabella dal client e risponde con un messaggio di conferma.
     * Se il caricamento dei dati è avvenuto con successo, il server risponde con un messaggio di conferma.
     * Altrimenti, risponde con un messaggio di errore.
     * @throws IOException errore di I/O
     * @throws ClassNotFoundException errore classe non trovata
     */
    private void caricamentoDati() throws IOException, ClassNotFoundException {
        System.out.println("Caricamento dati");
        tableName = (String) in.readObject();
        System.out.println("Nome tabella: " + tableName);
        out.writeObject("OK");
        boolean success = true;
        if (success) {
            out.writeObject("OK");
        } else {
            out.writeObject("Errore caricamento dati");
        }
    }

    /**
     * Metodo per il caricamento del database.
     * Il server riceve la profondità e la scelta della distanza dal client.
     * In base alla scelta della distanza, il server crea un oggetto ClusterDistance.
     * Il server crea un oggetto HierachicalClusterMiner e crea il dendrogramma.
     * Il server stampa il risultato.
     * @throws IOException errore di I/O
     * @throws ClassNotFoundException errore classe non trovata
     */
    private void caricaDatabase() throws IOException, ClassNotFoundException {
        int depth = (int) in.readObject();
        System.out.println("Profondità: " + depth);

        int distanceChoice = (int) in.readObject();
        System.out.println("Scelta distanza: " + distanceChoice);

        ClusterDistance distance = null;
        if (distanceChoice == 1) {
            distance = new SingleLinkDistance();
        } else if (distanceChoice == 2) {
            distance = new AverageLinkdistance();
        } else {
            System.out.println("[!] Scelta non valida. Utilizzerà la Single Link Distance di default.");
            distance = new SingleLinkDistance();
        }

        try {
            clustering = new HierachicalClusterMiner(depth);
        } catch (InvalidDepthException e) {
            out.writeObject("[!] Errore durante la creazione del dendrogramma, la profondità è maggiore del numero di esempi nel dataset.");
            return;
        }

        try {
            clustering.mine(new Data(tableName), distance);
        } catch (InvalidDepthException | InvalidSizeException | NoDataException e) {
            System.err.println("[!] Errore durante la creazione del dendrogramma: " + e.getMessage());
            e.printStackTrace();
            out.writeObject("[!] Errore durante la creazione del dendrogramma.");
            return;
        }

        // Stampa del risultato (simulato)
        try {
            out.writeObject("OK");
            out.writeObject(clustering.toString(new Data(tableName)));
        } catch (IOException e) {
            System.err.println("[!] Errore durante la stampa del risultato: " + e.getMessage());
            e.printStackTrace();
        } catch (NoDataException e) {
            System.err.println("[!] Errore nel recupero dei dati: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Metodo per il caricamento del file.
     * Il server riceve il nome del file dal client e carica il dendrogramma.
     * Il server stampa il risultato.
     * @throws IOException errore di I/O
     * @throws ClassNotFoundException errore classe non trovata
     */
    private void caricaDaFile() throws IOException, ClassNotFoundException {
        try {
            String fileName = (String) in.readObject();
            clustering = HierachicalClusterMiner.loadHierachicalClusterMiner("DataStore\\" + fileName);
        } catch (IOException | ClassNotFoundException e) {
            out.writeObject("[!] Errore durante il caricamento del file.");
            return;
        }

        try {
            out.writeObject("OK");
            System.out.println(clustering.toString(new Data(tableName)));
            out.writeObject(clustering.toString(new Data(tableName)));
        } catch (NoDataException e) {
            System.out.println("[!] Errore nel recupero dei dati.");
            e.printStackTrace();
        }
    }

    /**
     * Metodo per il salvataggio del file.
     * Il server riceve il nome del file dal client e salva il dendrogramma.
     * @throws IOException errore di I/O
     * @throws ClassNotFoundException errore classe non trovata
     */
    private void salvataggioFile() throws IOException, ClassNotFoundException {
        String saveFileName = (String) in.readObject();
        try {
            clustering.salva("DataStore\\" + saveFileName);
            out.writeObject("OK");
        } catch (IOException e) {
            out.writeObject("[!] Errore durante il salvataggio dei dati");
            e.printStackTrace();
        }
    }

    /**
     * Metodo per la chiusura delle risorse.
     * Chiude gli stream di input e output e il socket.
     * In caso di errori durante la chiusura delle risorse, viene stampato un messaggio di errore.
     */
    private void close() {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            System.err.println("Errore nella chiusura delle risorse: " + e.getMessage());
        }
    }
}
