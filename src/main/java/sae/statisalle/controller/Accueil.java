/*
 * Accueil.java                 14/11/2024
 * IUT DE RODEZ                 Pas de copyrights
 */
package sae.statisalle.controller;

import javafx.fxml.FXML;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import sae.statisalle.modele.Reseau;
import sae.statisalle.modele.Session;

/**
 * TODO
 * @author valentin munier-genie
 */
public class Accueil {

    @FXML
    void actionImporter() {
        MainControleur.activerImporter();
    }

    @FXML
    void actionEnvoyer() {
        MainControleur.activerConnexion();
    }

    @FXML
    void actionAfficher() {
        MainControleur.activerAffichage();
    }

    @FXML
    void actionQuitter() {
        Platform.exit();
    }

    @FXML
    void actionAide() {
        MainControleur.activerAideAccueil();
    }

    /**
     * Action déclenchée pour ouvrir une fenêtre de réglages du serveur.
     * Cette fenêtre permet de définir l'adresse IP et le port du serveur.
     * Les informations saisies sont validées avant d'être enregistrées.
     */
    @FXML
    private void actionReglage() {
        // Création de la fenêtre popup
        Stage popupStage = new Stage();
        popupStage.setTitle("Réglages Serveur");

        // Création des champs pour l'IP et le port
        GridPane grid = new GridPane();
        grid.setVgap(8);
        grid.setHgap(10);

        TextField ipField = new TextField();
        ipField.setPromptText("IP du Serveur");
        grid.add(ipField, 0, 0);

        TextField portField = new TextField();
        portField.setPromptText("Port du Serveur");
        grid.add(portField, 0, 1);

        Button saveButton = new Button("Enregistrer");
        saveButton.setOnAction(e -> {
            String ip = ipField.getText();
            String portText = portField.getText();
            try {
                if (ip.isEmpty()) {
                    MainControleur.showAlert("Erreur", "L'IP doit être remplie !");
                    return;
                }
                int port = Integer.parseInt(portText);
                if (port < 1 || port > 65535) {
                    MainControleur.showAlert("Erreur", "Le port doit être entre 1 et 65535 !");
                    return;
                }

                MainControleur.stopServeur();
                Session.setIpServeur(ip);
                Session.setPortServeur(portText);

                MainControleur.initServeur();

                popupStage.close();

                System.out.println("[SERVEUR] IP configurée manuellement : " + ip + ":" + portText);
            } catch (NumberFormatException ex) {
                MainControleur.showAlert("Erreur", "Le port doit être un nombre valide.");
            }
        });
        grid.add(saveButton, 0, 2);
        Scene popupScene = new Scene(grid, 300, 200);
        popupStage.setScene(popupScene);
        popupStage.show();
    }
}
