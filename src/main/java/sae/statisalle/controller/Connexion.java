/*
 * Connexion.java              31/10/2024
 * Pas de droits d'auteur ni de copyright
 */
package sae.statisalle.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import sae.statisalle.Reseau;
import sae.statisalle.Session;
import sae.statisalle.exception.MauvaiseConnexionServeur;

import java.net.InetAddress;

/**
 * Contrôleur de la connexion pour l'application StatiSalle.
 * Gère les actions liées à la connexion réseau entre le client
 * et le serveur. Vérifie la validité de l'adresse IP et tente
 * d'établir la connexion.
 *
 * @author valentin.munier-genie
 */
public class Connexion {

    /** Instance de la classe Reseau. */
    private Reseau reseau = new Reseau();

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
            Session.setAdresseIp(ip);
            MainControleur.showAlert("Erreur de connexion",
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
            MainControleur.showAlert("Erreur de connexion",
                    "Le numéro de port n'est pas valide. " +
                            "Veuillez saisir un port entre 0 et 65535.");
            return;
        }

        // tentative de connexion
        try {
            reseau.preparerClient(ip, port);
            Session.setAdresseIp(ip + ":" + portText);
            Session.setReseau(reseau);

            MainControleur.activerEnvoyer();
        } catch (MauvaiseConnexionServeur e) {
            MainControleur.showAlert("Erreur de connexion",
                    "Impossible de se connecter au serveur : "
                            + e.getMessage());
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
        InetAddress ip = Reseau.renvoyerIP();
        if (ip != null) {
            String ipAddress = ip.getHostAddress();
            btnAfficherIp.setText(ipAddress);
        } else {
            btnAfficherIp.setText("Pas de connexion");
            MainControleur.showAlert("Impossible d'afficher l'IP",
                    "Absence de connexion a un réseau.");
            System.err.println("Erreur lors de la récupération de l'adresse IP.");
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
}
