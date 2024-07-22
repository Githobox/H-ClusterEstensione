import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Classe MultiServer che si occupa di gestire la connessione con più client.
 * Il server rimane in ascolto sulla porta 8080 e accetta nuove connessioni.
 * Per ogni connessione accettata viene creato un nuovo thread ServerOneClient
 * che si occuperà di gestire la connessione con il client.
 */
public class MultiServer {
    /**
     * Porta di ascolto del server.
     */
    private int PORT = 8080;
    /**
     * Metodo main della classe MultiServer.
     * @param args argomenti passati da riga di comando
     */
    public static void main(String[] args) {
        MultiServer server = new MultiServer(8080);
        server.run();
    }
    /**
     * Costruttore della classe MultiServer.
     * assegna la porta di ascolto del server.
     * @param port porta di ascolto del server
     */
    public MultiServer(final int port) {
        this.PORT = port;
    }
    /**
     * Metodo run del Multiserver.
     * Il server rimane in ascolto sulla porta 8080 e accetta nuove connessioni.
     * Per ogni connessione accettata viene creato un nuovo thread ServerOneClient
     * che si occuperà di gestire la connessione con il client.
     * In caso di errori durante l'esecuzione del server, viene stampato un messaggio di errore.
     */
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server in ascolto sulla porta " + PORT + "...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nuova connessione accettata da " + clientSocket.getInetAddress());

                ServerOneClient clientHandler = new ServerOneClient(clientSocket);
                clientHandler.start();
            }
        } catch (IOException e) {
            System.out.println("[!] Errori verificati durante l'esecuzione del server");
        }
    }
}
