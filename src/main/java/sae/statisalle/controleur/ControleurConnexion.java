/*
 * Connexion.java              31/10/2024
 * Pas de droits d'auteur ni de copyright
 */
package sae.statisalle.controleur;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import sae.statisalle.modele.Client;
import sae.statisalle.modele.Session;

import java.io.IOException;
import java.net.InetAddress;

/**
 * Contrôleur de la connexion pour l'application StatiSalle.
 * Gère les actions liées à la connexion réseau entre le client
 * et le serveur. Vérifie la validité de l'adresse IP et tente
 * d'établir la connexion.
 *
 * @author valentin.munier-genie
 */
public class ControleurConnexion {

    /** Instance de la classe Client. */
    private final Client client = new Client();

    @FXML
    private TextField textIp;

    @FXML
    private TextField textPort;

    @FXML
    private Button btnConnexion;

    @FXML
    private Button btnAfficherIp;

    /**
     * Initialise l'état de la scène de connexion et
     * désactive le bouton de connexion par défaut.
     * Suivre les modifications des champs de texte
     * pour activer ou désactiver le bouton.
     */
    @FXML
    void initialize() {
        btnConnexion.setDisable(true);
        textIp.textProperty().addListener((observable, oldValue, newValue) ->
                verifierLesChamps());
        textPort.textProperty().addListener((observable, oldValue, newValue) ->
                verifierLesChamps());
    }

    /**
     * Vérifie si les champs d'adresse IP et de port sont remplis
     * et active ou désactive le bouton de connexion en conséquence.
     */
    private void verifierLesChamps() {
        boolean isFilled = !textIp.getText().isEmpty()
                && !textPort.getText().isEmpty();

        if (isFilled) {
            btnConnexion.setStyle("-fx-background-color: #4CAF50;"); // vert
            btnConnexion.setDisable(false);
        } else {
            btnConnexion.setStyle("-fx-background-color: #D3D3D3;"); // défaut
            btnConnexion.setDisable(true);
        }
    }

    /**
     * Retourne à l'écran d'accueil de l'application.
     */
    @FXML
    void actionRetour() {
        MainControleur.activerAccueil();
    }

    /**
     * Tente d'établir une connexion avec le serveur en utilisant
     * l'adresse IP et le port fournis par l'utilisateur.
     * Affiche une alerte en cas d'erreur de connexion.
     */
    @FXML
    void actionConnexion() {
        String ip = textIp.getText();
        String portText = textPort.getText();
        int port;

        // validation de l'adresse IP
        if (!isValidIPv4(ip)) {
            showAlert(AlertType.ERROR, "Erreur de connexion",
                    "L'adresse IP n'est pas valide. " +
                            "Veuillez saisir une adresse IPv4.");
            return;
        }

        // validation du port
        try {
            port = Integer.parseInt(portText);
            if (port < 0 || port > 65535) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Erreur de connexion",
                    "Le numéro de port n'est pas valide. " +
                            "Veuillez saisir un port entre 0 et 65535.");
            return;
        }

        // tentative de connexion
        try {
            client.connecter(ip, port);
            Session.setClient(client);

            showAlert(AlertType.INFORMATION, "Connexion réussie",
                    "Connexion établie avec l'adresse IP : "
                            + ip + " : " + portText);
            MainControleur.activerEnvoyer();
        } catch (IOException e) {
            showAlert(AlertType.ERROR, "Erreur de connexion",
                    "Impossible de se connecter au serveur.");
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Affiche l'écran d'aide pour la connexion.
     */
    @FXML
    void actionAide() {
        MainControleur.activerAideConnexion();
    }

    /**
     * Affiche l'adresse IP de la machine locale dans le bouton
     * btnAfficherIp.
     */
    @FXML
    void actionAfficherIp() {
        Client clientIP = new Client();
        InetAddress ip = clientIP.renvoyerIP();
        if (ip != null) {
            String ipAddress = ip.getHostAddress();
            btnAfficherIp.setText(ipAddress);
        } else {
            btnAfficherIp.setText("Pas de connexion");
            showAlert(AlertType.INFORMATION,"Impossible d'afficher l'IP",
                             "Absence de connexion a un réseau.");
            System.err.println("Erreur lors de la récupération "
                               + "de l'adresse IP.");
        }
    }

    /**
     * Vérifie si une adresse IP est au format IPv4.
     *
     * @param ip l'adresse IP à vérifier.
     * @return true si l'adresse IP est valide, false sinon.
     */
    private boolean isValidIPv4(String ip) {
        // Règle regex pour valider les adresses IPv4
        String ipv4Pattern = "^(localhost|((25[0-5]|2[0-4][0-9]|" +
                "[0-1]?[0-9]{1,2})\\.){3}(25[0-5]|2[0-4][0-9]|" +
                "[0-1]?[0-9]{1,2}))$";
        return ip.matches(ipv4Pattern);
    }

    /**
     * Affiche une alerte pour informer l'utilisateur
     * d'une situation spécifique.
     *
     * @param alertType le type de l'alerte.
     * @param title le titre de l'alerte.
     * @param message le message de l'alerte.
     */
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
