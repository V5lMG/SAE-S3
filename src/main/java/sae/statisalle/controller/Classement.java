/*
 * Classement.java              21/11/2024
 * Pas de droits d'auteur ni de copyright
 */
package sae.statisalle.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * Contrôleur de la connexion pour l'application StatiSalle.
 * Gère les actions liées à la connexion réseau entre le client
 * et le serveur. Vérifie la validité de l'adresse IP et tente
 * d'établir la connexion.
 * @author valentin.munier-genie
 */
public class Classement {


    @FXML
    private TextField textIp;

    @FXML
    private TextField textPort;

    @FXML
    private Button btnConnexion;

    @FXML
    private Button btnAfficherIp;


    /**
     * Retourne à l'écran d'accueil de l'application.
     */
    @FXML
    void actionRetour() {
        MainControleur.activerAccueil();
    }

    /**
     * Affiche l'écran d'aide pour la connexion.
     */
    @FXML
    void actionAide() {
        MainControleur.activerAideConnexion();
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
