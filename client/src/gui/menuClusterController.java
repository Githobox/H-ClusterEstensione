package gui;

import connectionManager.ConnessioneDatabase;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller per la finestra di menuCluster.fxml
 */
public class menuClusterController extends BaseSceneController {
    /**
     * Percorso del file FXML per la scena di risultato.
     */
    private static final String RESULTPATH = "/resources/gui/result.fxml";
    /**
     * Percorso del file FXML per la scena di menuConnect.
     */
    private static final String MENUCONNECTPATH = "/resources/gui/menuUI.fxml";
    /**
     *  TextField per il nome del database.
     */
    @FXML
    private TextField DataBaseTextField;
    /**
     * TextField per il nome della tabella.
     */
    @FXML
    private TextField TabellaTextField;
    /**
     * TextField per l'utente del database.
     */
    @FXML
    private TextField UtenteTextField;
    /**
     * TextField per la password del database.
     */
    @FXML
    private TextField PasswordTextField;
    /**
     * ComboBox per la scelta del tipo di link.
     */
    @FXML
    private ComboBox<String> comboBox;
    /**
     * TextField per il numero di iterazioni.
     */
    @FXML
    private TextField DepthTextField;
    /**
     * TextField per il nome del file.
     */
    @FXML
    private TextField FileNameTextField;
    /**
     * Pulsante per il database.
     */
    @FXML
    private Button DatabaseButton;
    /**
     * Pulsante per il file.
     */
    @FXML
    private Button FileButton;
    /**
     * Pulsante per tornare indietro.
     */
    @FXML
    private Button BackButton;
    /**
     * Oggetto resul che servirà per il cambio di scena.
     */
    private resultController result;
    /**
     * Costruttore predefinito.
     *
     * <p>Questo costruttore viene utilizzato per istanziare un oggetto menuClusterController.
     * Non effettua alcuna operazione specifica, ma è necessario per permettere
     * l'istanziazione della classe e l'accesso alla funzionalità fornita da BaseSceneController.
     */
    public menuClusterController() {
    }
    /**
     * Metodo per inizializzare la ComboBox.
     */
    @FXML
    public void initialize() {
        comboBox.setItems(FXCollections.observableArrayList("Single Link", "Average Link"));
    }
    /**
     * Gestisce l'evento di clic sul pulsante Database.
     * Questo metodo invia i dati del database al server.
     * Effettua il learning dal database e mostra i risultati.
     */
    @FXML
    private void handleDatabase() {
        try {
            String database = DataBaseTextField.getText();
            String tableName = TabellaTextField.getText();
            String utente = UtenteTextField.getText();
            String password = PasswordTextField.getText();
            String iterations = DepthTextField.getText();
            String link = comboBox.getValue();

            // Controlla se i campi sono vuoti
            if (database.isEmpty() || tableName.isEmpty() || utente.isEmpty() || password.isEmpty() || iterations.isEmpty() || link==null) {
                showMessage(Alert.AlertType.ERROR, "Errore", "Assicurati di compilare tutti i campi.");
                return;
            }

            // Inizializza la connessione al database
            connessioneDatabase = new ConnessioneDatabase(this.connessioneSocket.getIp(), database, tableName, utente, password);

            // Scrive la scelta al server (0, 1, o 2)
            this.connessioneSocket.getOutputStream().writeObject(0); // Assuming this is for data loading

            // Scrive il nome della tabella al server
            this.connessioneSocket.getOutputStream().writeObject(tableName);

            // Ottiene la risposta dal server
            String resp = (String) this.connessioneSocket.getInputStream().readObject();
            if ("OK".equals(resp)) {
                this.connessioneSocket.getOutputStream().writeObject(2); // Assuming this is for loading from database
                this.connessioneSocket.getOutputStream().writeObject(Integer.parseInt(iterations));

                if ("Single Link".equals(link)) {
                    this.connessioneSocket.getOutputStream().writeObject(1);
                } else {
                    this.connessioneSocket.getOutputStream().writeObject(2);
                }

                resp = (String) this.connessioneSocket.getInputStream().readObject();

                if ("OK".equals(resp)) {
                    resp = (String) this.connessioneSocket.getInputStream().readObject();
                    if("OK".equals(resp)) {
                        result = new resultController();
                        Stage currentStage = (Stage) DatabaseButton.getScene().getWindow();
                        switchToNewScene(currentStage, result, RESULTPATH);
                    }
                    else {
                        showMessage(Alert.AlertType.ERROR, "Errore", "Errore durante l'elaborazione dati: \n" + resp);
                    }
                } else {
                    showMessage(Alert.AlertType.ERROR, "Errore", "Errore durante l'operazione di connessione al database del server: \n" + resp);
                }
            } else {
                showMessage(Alert.AlertType.ERROR, "Errore", "Errore durante l'operazione di connessione al database del server: " + resp);
            }
        } catch (NumberFormatException e) {
            showMessage(Alert.AlertType.ERROR, "Errore", "Numero di iterazioni non valido.");
        } catch (IOException e) {
            showMessage(Alert.AlertType.ERROR, "Errore", "Errore durante l'operazione del database: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            showMessage(Alert.AlertType.ERROR, "Errore", "Errore durante la lettura della risposta del server: " + e.getMessage());
        }
    }
    /**
     * Gestisce l'evento di clic sul pulsante File.
     * Questo metodo invia i dati del database al server.
     * Effettua il learning da file e mostra i risultati.
     */
    @FXML
    private void handleFile() {
        String resp;
        try {
            String database = DataBaseTextField.getText();
            String tableName = TabellaTextField.getText();
            String utente = UtenteTextField.getText();
            String password = PasswordTextField.getText();
            String filename = FileNameTextField.getText();

            // Controlla se i campi sono vuoti
            if (database.isEmpty() || tableName.isEmpty() || utente.isEmpty() || password.isEmpty() || !filename.endsWith(".dat")) {
                showMessage(Alert.AlertType.ERROR, "Errore", "Assicurati di compilare tutti i campi.\n Assicurati che il file sia un file .dat.");
                return;
            }

            connessioneDatabase = new ConnessioneDatabase(this.connessioneSocket.getIp(), database, tableName, utente, password);

            connessioneSocket.getOutputStream().writeObject(0);
            connessioneSocket.getOutputStream().writeObject(tableName);
            resp = (String) connessioneSocket.getInputStream().readObject();
            if (resp.equals("OK")) {
                connessioneSocket.getOutputStream().writeObject(1);
                connessioneSocket.getOutputStream().writeObject(filename);
                resp = (String) connessioneSocket.getInputStream().readObject();
                if(resp.equals("OK")) {
                    resp = (String) connessioneSocket.getInputStream().readObject();
                    if(resp.equals("OK")) {
                        result = new resultController();
                        Stage currentStage = (Stage) FileButton.getScene().getWindow();
                        switchToNewScene(currentStage, result, RESULTPATH);
                    }
                    else {
                        showMessage(Alert.AlertType.ERROR,"Errore","Errore percorso file: \n" + resp);
                    }

                }
                else {
                    showMessage(Alert.AlertType.ERROR,"Errore","Errore durante l'operazione di connessione al database del server: \n" + resp);
                }

            } else {
                showMessage(Alert.AlertType.ERROR,"Errore","Errore durante l'operazione di connessione al database del server: " + resp);
            }
        } catch (NumberFormatException e) {
            showMessage(Alert.AlertType.ERROR,"Errore","Numero di iterazioni non valido.");
        } catch (IOException e) {
            showMessage(Alert.AlertType.ERROR,"Errore","Errore durante l'operazione del di ritrovamento del file: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Inizializza la connessione al database del server.
     *
     * @param connessioneDatabase Connessione al database
     */
    private String connessioneServerDataBase(ConnessioneDatabase connessioneDatabase) {

        try {
            this.connessioneSocket.getOutputStream().writeObject(0);
            this.connessioneSocket.getOutputStream().writeObject(this.connessioneSocket.getIp());
            this.connessioneSocket.getOutputStream().writeObject(connessioneDatabase.getNomeDatabase());
            this.connessioneSocket.getOutputStream().writeObject(connessioneDatabase.getNomeTabella());
            this.connessioneSocket.getOutputStream().writeObject(connessioneDatabase.getUtenteDatabase());
            this.connessioneSocket.getOutputStream().writeObject(connessioneDatabase.getPasswordDatabase());

            String resp = (String) this.connessioneSocket.getInputStream().readObject();
            if (resp.equals("OK")) {
                showMessage(Alert.AlertType.INFORMATION,"DataSet",(String) this.connessioneSocket.getInputStream().readObject());
            } else {
                showMessage(Alert.AlertType.ERROR,"Errore",resp);
            }
            return resp;
        } catch (IOException | ClassNotFoundException e) {
            showMessage(Alert.AlertType.ERROR,"Errore","Errore durante l'operazione del database: " + e.getMessage());
            return null;
        }
    }
    /**
     * Gestisce l'evento di clic sul pulsante Indietro.
     * Questo metodo torna alla scena di menuConnect.
     */
    @FXML
    private void backToMenuConnect() {
        // Ottieni il riferimento alla scena corrente
        Stage currentStage = (Stage) BackButton.getScene().getWindow();
        // Creazione di un'istanza di menuClusterController
        ConnessioneController connessioneController = new ConnessioneController();
        // Passa alla nuova scena
        switchToNewScene(currentStage, connessioneController, MENUCONNECTPATH);
    }
}
