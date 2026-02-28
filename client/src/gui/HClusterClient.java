package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * HClusterClient è la classe principale per l'applicazione client.
 * Questa classe avvia l'applicazione client e carica la scena di connessione.
 */
public class HClusterClient extends Application{
    /**
     * Metodo di avvio del programma.
     * @param args argomenti passati da riga di comando.
     */
    public static void main(String[] args) {
        launch();
    }
    /**
     * Costruttore predefinito.
     *
     * <p>Questo costruttore è necessario per consentire l'istanziazione della classe HClusterClient
     * e l'avvio dell'applicazione JavaFX.
     */
    public HClusterClient() {}
    /**
     * Metodo che inizializza l'interfaccia grafica.
     * @param stage parametro iniziale
     */
    @Override
    public void start(Stage stage) {
        try {
            // Carica la scena di connessione
            FXMLLoader fxmlLoader = new FXMLLoader(HClusterClient.class.getResource("/resources/gui/menuUI.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            // Imposta i settaggi della finestra
            stage.setTitle("H-Cluster Client");
            Image icon = new Image(getClass().getResourceAsStream("/resources/images/icona.png"));
            stage.getIcons().add(icon);
            stage.setResizable(true);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            System.err.println("Errore caricamento FXML");
            e.printStackTrace();
        }
    }
}
