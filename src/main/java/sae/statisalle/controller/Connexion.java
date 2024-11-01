package sae.statisalle.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import sae.statisalle.Reseau;
import sae.statisalle.Session;
import sae.statisalle.exception.MauvaiseConnexionServeur;

/**
 * Contrôleur de la connexion pour l'application StatiSalle.
 * Gère les actions liées à la connexion réseau entre le client et le serveur.
 * Vérifie la validité de l'adresse IP et tente d'établir la connexion.
 *
 * @author valentin.munier-genie
 */
public class Connexion {

    private Reseau reseau = new Reseau();

    @FXML
    private TextField textIp;

    @FXML
    private TextField textPort;

    @FXML
    private Button btnConnexion;

    @FXML
    void initialize() {
        btnConnexion.setDisable(true);

        // Suivre les modifications des champs de texte
        textIp.textProperty().addListener((observable, oldValue, newValue) ->
                verifierLesChamps());
        textPort.textProperty().addListener((observable, oldValue, newValue) ->
                verifierLesChamps());
    }

    private void verifierLesChamps() {
        // Vérifier si tous les champs sont remplis
        boolean isFilled = !textIp.getText().isEmpty()
                           && !textPort.getText().isEmpty();

        // Activer et désactiver le bouton en fonction de la vérification
        if (isFilled) {
            btnConnexion.setStyle("-fx-background-color: #4CAF50;"); // vert
            btnConnexion.setDisable(false);
        } else {
            btnConnexion.setStyle("-fx-background-color: #D3D3D3;"); // couleur par défaut
            btnConnexion.setDisable(true);
        }
    }

    @FXML
    void actionRetour() {
        MainControleur.activerAccueil();
    }

    @FXML
    void actionConnexion() {
        String ip = textIp.getText();
        String portText = textPort.getText();
        int port;

        // Validation de l'adresse IP
        if (!isValidIPv4(ip)) {
            Session.setAdresseIp(ip);
            showAlert(AlertType.ERROR, "Erreur de connexion",
                    "L'adresse IP n'est pas valide. " +
                            "Veuillez saisir une adresse IPv4.");
            return;
        }

        // Validation du port
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

        // Tentative de connexion
        try {
            reseau.preparerClient(ip, port);
            // Si la connexion est réussie, enregistrer l'adresse IP
            // dans la session au format requis et l'objet reseau.
            Session.setAdresseIp(ip + ":" + portText);
            Session.setReseau(reseau);


            showAlert(AlertType.INFORMATION, "Connexion réussie",
                    "Connexion établie avec l'adresse IP : "
                             + ip + " : " + portText);
            MainControleur.activerEnvoyer();
        } catch (MauvaiseConnexionServeur e) {
            // Affiche une alerte d'erreur sans changer de page
            showAlert(AlertType.ERROR, "Erreur de connexion",
                    "Impossible de se connecter au serveur : "
                     + e.getMessage());
        }
    }

    @FXML
    void actionAide() {
        MainControleur.activerAideConnexion();
    }

    /**
     * Vérifie si une adresse IP est au format IPv4.
     * @param ip l'adresse IP à vérifier.
     * @return true si l'adresse IP est valide, false sinon.
     */
    private boolean isValidIPv4(String ip) {
        // Règle regex générée sur un site web
        String ipv4Pattern = "^(localhost|((25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})\\.){3}(25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2}))$";
        return ip.matches(ipv4Pattern);
    }

    /**
     * Affiche une alerte pour informer l'utilisateur
     * d'une situation spécifique.
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
