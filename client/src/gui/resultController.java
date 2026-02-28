package gui;

import connectionManager.ConnessioneSocket;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
/**
 * Classe resultController: Controller associato alla finestra di visualizzazione dei risultati del clustering.
 * Questa classe si occupa di gestire la visualizzazione dei risultati del clustering e di fornire all'utente la possibilità
 * di ripetere l'iterazione, salvare i risultati su file e tornare al menu principale.
 */
public class resultController extends BaseSceneController {
    /**
     * Percorso del file FXML per la scena di menuCluster.
     */
    private static final String CLUSTERPATH = "/resources/gui/menuCluster.fxml";
    /**
     * TextArea per visualizzare i risultati del clustering.
     */
    @FXML
    private TextArea resultTextArea;
    /**
     * Pulsante per tornare al menu principale.
     */
    @FXML
    private Button BackButton;
    /**
     * Campo di testo per il numero di iterazioni.
     */
    @FXML
    private TextField IterateTextField;
    /**
     * ComboBox per selezionare il tipo di link.
     */
    @FXML
    private ComboBox<String> comboBox;
    /**
     * Campo di testo per il nome del file.
     */
    @FXML
    private TextField SaveNameTextField;
    /**
     * Inizializa il contenuto della ComboBox.
     */
    @FXML
    public void initialize() {
        comboBox.setItems(FXCollections.observableArrayList("Single Link", "Average Link"));
    }
    /**
     * Costruttore predefinito.
     *
     * <p>Questo costruttore viene utilizzato per istanziare un oggetto resultController.
     * Non effettua alcuna operazione specifica, ma è necessario per permettere
     * l'istanziazione della classe e l'accesso alla funzionalità fornita da BaseSceneController.
     */
    public resultController() {}
    /**
     * Metodo per aggiornare il contenuto della TextArea dei risultati.
     * @param result Stringa da inserire nella TextArea.
     */
    private void updateResultTextArea(String result) {
            resultTextArea.clear();
            resultTextArea.appendText(result);
    }
    /**
     * Metodo per stampare i risultati del clustering.
     * Questo metodo legge i risultati del clustering dal server e li stampa nella TextArea dei risultati.
     * @param connessioneSocket ConnessioneSocket: connessione al server.
     */
    public void stampaRisultato(ConnessioneSocket connessioneSocket) {
        String result; // Variabile per il risultato
        try {
            // Verifica se la connessione al server è stata passata correttamente
            if (connessioneSocket == null || connessioneSocket.getSocket() == null || !connessioneSocket.getSocket().isConnected()) {
                showMessage(Alert.AlertType.ERROR,"Errore","Connessione al server non inizializzata correttamente");
            }
                result = (String) connessioneSocket.getInputStream().readObject();
                // Aggiorna la TextArea dei risultati
                updateResultTextArea("Cluster Generati\n" + result + "\n");
                // Assegna il valore di tempIterations a iterations
        } catch (IOException | ClassNotFoundException | IllegalStateException e) {
            showMessage(Alert.AlertType.ERROR,"Errore","Errore nella connessione al server"+e.getMessage());
        }
    }
    /**
     * Metodo associato all'azione di click del RepeatButton.
     * Questo metodo ripete l'iterazione del clustering.
     * @throws IOException Eccezione di I/O.
     * @throws ClassNotFoundException  Eccezione di classe non trovata.
     */
    @FXML
    private void repeatIteration() throws IOException, ClassNotFoundException {
        String resp;
        String link = comboBox.getValue();
        // Verifica se il campo di testo delle iterazioni è vuoto
        if(IterateTextField.getText().isEmpty() || link == null){
            showMessage(Alert.AlertType.ERROR,"Errore","Devi compilare tutti i campi");
        }
        else{
            // Invia il numero di iterazioni al server
            this.connessioneSocket.getOutputStream().writeObject(2);
            this.connessioneSocket.getOutputStream().writeObject(Integer.parseInt(IterateTextField.getText()));
            if ("Single Link".equals(link)) {
                this.connessioneSocket.getOutputStream().writeObject(1);
            } else {
                this.connessioneSocket.getOutputStream().writeObject(2);
            }
              resp = (String) this.connessioneSocket.getInputStream().readObject();
              if("OK".equals(resp))
              {
                  resp = (String) this.connessioneSocket.getInputStream().readObject();
                  updateResultTextArea("Iterazione ripetuta correttamente:\n"+resp+"\n");
              }
              else {
                  showMessage(Alert.AlertType.ERROR,"Errore","Errore durante l'elaborazione dati: \n"+resp);
              }
        }
    }
    /**
     * Metodo associato all'azione di click del SaveButton.
     * Questo metodo salva i risultati del clustering su file.
     * @throws IOException Eccezione di I/O.
     * @throws ClassNotFoundException Eccezione di classe non trovata.
     */
    @FXML
    private void saveOnFile() throws IOException, ClassNotFoundException {
        String filename = SaveNameTextField.getText();

        // Crea il nome del file
        if(!filename.endsWith(".dat")){
            showMessage(Alert.AlertType.ERROR,"Errore","Inserisci un nome valido per il file");
        }
        else{
            // Dice al server di salvare i risultati su file
            this.connessioneSocket.getOutputStream().writeObject(3);
            // Invia il nome del file al server
            this.connessioneSocket.getOutputStream().writeObject(filename);
            // Riceve la conferma dal server
            if(((String)connessioneSocket.getInputStream().readObject()).equals("OK")){
                // Aggiorna la TextArea dei risultati
                showMessage(Alert.AlertType.INFORMATION,"Salvataggio","File salvato correttamente");
            }
            else{
                showMessage(Alert.AlertType.ERROR,"Errore","Errore durante il salvataggio del file");
            }
        }
    }
    /**
     * Metodo associato all'azione di click del BackButton.
     * Questo metodo torna al menu principale.
     */
    @FXML
    private void backToMenuCluster() {
        // Ottieni il riferimento alla scena corrente
        Stage currentStage = (Stage) BackButton.getScene().getWindow();
        // Creazione di un'istanza di menuClusterController
        menuClusterController menuController = new menuClusterController();
        // Passa alla nuova scena
        switchToNewScene(currentStage, menuController, CLUSTERPATH);
    }
}
