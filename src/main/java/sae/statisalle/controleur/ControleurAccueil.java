/*
 * ControleurAccueil.java                 14/11/2024
 * IUT DE RODEZ                           Pas de copyrights
 */
package sae.statisalle.controleur;

import javafx.fxml.FXML;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sae.statisalle.modele.Session;

import java.util.Objects;

/**
 * Contrôleur de l'écran d'accueil de l'application.
 * Gère les actions déclenchées par les utilisateurs depuis l'écran principal.
 *
 * @author valentin.munier-genie
 */
public class ControleurAccueil {

    /**
     * Action déclenchée pour passer à l'écran d'importation.
     */
    @FXML
    void actionImporter() {
        MainControleur.activerImporter();
    }

    /**
     * Action déclenchée pour passer à l'écran de connexion
     * et envoyer des données.
     */
    @FXML
    void actionEnvoyer() {
        MainControleur.activerConnexion();
    }

    /**
     * Action déclenchée pour afficher les données importées.
     */
    @FXML
    void actionAfficher() {
        MainControleur.activerAffichage();
    }

    /**
     * Action déclenchée pour quitter l'application.
     */
    @FXML
    void actionQuitter() {
        Platform.exit();
    }

    /**
     * Action déclenchée pour afficher l'aide liée à l'écran d'accueil.
     */
    @FXML
    void actionAide() {
        MainControleur.activerAideAccueil();
    }

    /**
     * Action déclenchée pour afficher une fenêtre de réglages.
     * Permet de modifier l'adresse IP et le port du serveur,
     * avec validation des entrées.
     * Applique les modifications si elles sont valides,
     * redémarre le serveur et ferme la fenêtre.
     */
    @FXML
    private void actionReglage() {
        // création de la fenêtre
        Stage popupStage = new Stage();
        popupStage.setTitle("Réglages Serveur");
        GridPane grid = new GridPane();
        grid.setVgap(8);
        grid.setHgap(10);
        grid.setPadding(new Insets(20));
        grid.setAlignment(Pos.CENTER);
        Image logo = new Image(Objects.requireNonNull(
                               ControleurPopup.class.getResourceAsStream(
                         "/sae/statisalle/img/LogoStatisalle.jpg")));
        popupStage.getIcons().add(logo);

        // champs pour l'IP et le port
        Label ipLabel = new Label("Adresse IP :");
        TextField ipField = new TextField();
        ipField.setPromptText("IP du Serveur");
        grid.add(ipLabel, 0, 0);
        grid.add(ipField, 1, 0);

        Label portLabel = new Label("Port :");
        TextField portField = new TextField();
        portField.setPromptText("Port du Serveur");
        grid.add(portLabel, 0, 1);
        grid.add(portField, 1, 1);

        // boutons Enregistrer et Annuler
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button saveButton = new Button("Enregistrer");
        Button cancelButton = new Button("Annuler");

        saveButton.setStyle(   "-fx-background-color: #60BCFB;");
        cancelButton.setStyle( "-fx-background-color: #CD4043;"
                              +"-fx-text-fill: white;");

        buttonBox.getChildren().addAll(saveButton, cancelButton);

        grid.add(buttonBox, 0, 2, 2, 1);

        saveButton.setOnAction(e -> {
            String ip = ipField.getText().trim();
            String portText = portField.getText().trim();

            try {
                if (!ip.isEmpty() && !isValidIp(ip)) {
                    MainControleur.showAlert(Alert.AlertType.WARNING, "Erreur",
                            "L'adresse IP saisie n'est pas valide !");
                    return;
                }

                if (!portText.isEmpty()) {
                    int port = Integer.parseInt(portText);
                    if (port < 1 || port > 65535) {
                        MainControleur.showAlert(Alert.AlertType.WARNING, "Erreur",
                                "Le port doit être compris entre "
                                        + "1 et 65535 !");
                        return;
                    }
                }

                // si tout est valide, appliquer les modifications
                if (!ip.isEmpty()) {
                    Session.setIpServeur(ip);
                    System.out.println("[SERVEUR] Adresse IP modifiée : " + ip);
                }

                if (!portText.isEmpty()) {
                    Session.setPortServeur(portText);
                    System.out.println("[SERVEUR] Port modifié : " + portText);
                }

                // redémarrer le serveur
                if (!ip.isEmpty() || !portText.isEmpty()) {
                    MainControleur.stopThreadServeur();
                    System.out.println("[MAIN] Serveur stoppé pour changement "
                                       + "de l'IP ou du PORT.");

                    try {
                        Thread.sleep(2000); // pause de 2 secondes
                    } catch (InterruptedException erreur) {
                        System.err.println("[MAIN] Interruption pendant le " +
                                           "délai d'attente : "
                                           + erreur.getMessage());
                        Thread.currentThread().interrupt();
                    }

                    MainControleur.initServeur();
                    System.out.println("[MAIN] Serveur redémarré avec "
                            + "la nouvelle IP.");
                }

                popupStage.close();
            } catch (NumberFormatException ex) {
                MainControleur.showAlert(Alert.AlertType.WARNING, "Erreur",
                        "Le port doit être un nombre valide.");
            }
        });

        // bouton close
        cancelButton.setOnAction(e -> popupStage.close());

        Scene popupScene = new Scene(grid, 400, 250);
        popupStage.setScene(popupScene);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.show();
    }

    /**
     * Vérifie si une adresse IP est valide.
     * @param ip L'adresse IP à vérifier.
     * @return true si l'adresse IP est valide, false sinon.
     */
    private boolean isValidIp(String ip) {
        if (ip == null || ip.isEmpty()) {
            return false;
        }

        if (ip.equals("127.0.0.1")
                || ip.equalsIgnoreCase("localhost")) {
            return true;
        }

        String regex = "^([0-9]{1,3}\\.){3}[0-9]{1,3}$";
        if (!ip.matches(regex)) {
            return false;
        }

        for (String part : ip.split("\\.")) {
            int num;
            try {
                num = Integer.parseInt(part);
            } catch (NumberFormatException e) {
                return false;
            }
            if (num < 0 || num > 255) {
                return false;
            }
        }

        return true;
    }
}