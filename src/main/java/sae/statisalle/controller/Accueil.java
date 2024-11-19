/*
 * Accueil.java                 14/11/2024
 * IUT DE RODEZ                 Pas de copyrights
 */
package sae.statisalle.controller;

import javafx.fxml.FXML;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sae.statisalle.modele.Session;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

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

        // Création de la grille pour l'agencement
        GridPane grid = new GridPane();
        grid.setVgap(8);
        grid.setHgap(10);
        grid.setPadding(new Insets(20));
        grid.setAlignment(Pos.CENTER);

        // Champs pour l'IP et le port
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

        // Boutons Enregistrer et Annuler
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button saveButton = new Button("Enregistrer");
        Button cancelButton = new Button("Annuler");
        buttonBox.getChildren().addAll(saveButton, cancelButton);

        grid.add(buttonBox, 0, 2, 2, 1);

        saveButton.setOnAction(e -> {
            String ip = ipField.getText().trim();
            String portText = portField.getText().trim();

            try {
                // Vérification de l'adresse IP uniquement si un nouveau champ est saisi
                if (!ip.isEmpty() && !isValidIp(ip)) {
                    MainControleur.showAlert("Erreur", "L'adresse IP saisie n'est pas valide !");
                    return;
                }

                // Vérification du port uniquement si un nouveau champ est saisi
                if (!portText.isEmpty()) {
                    int port = Integer.parseInt(portText);
                    if (port < 1 || port > 65535) {
                        MainControleur.showAlert("Erreur", "Le port doit être compris entre 1 et 65535 !");
                        return;
                    }
                }

                // Si tout est valide, appliquer les modifications
                if (!ip.isEmpty()) {
                    Session.setIpServeur(ip);
                    System.out.println("[SERVEUR] Adresse IP modifiée : " + ip);
                }

                if (!portText.isEmpty()) {
                    Session.setPortServeur(portText);
                    System.out.println("[SERVEUR] Port modifié : " + portText);
                }

                // Redémarrer le serveur uniquement si au moins un champ a été modifié
                if (!ip.isEmpty() || !portText.isEmpty()) {
                    MainControleur.stopServeur();
                    System.out.println("[MAIN] Serveur stoppé par la configuration de l'IP.");
                    MainControleur.initServeur();
                    System.out.println("[MAIN] Serveur redémarré par la config de l'IP.");
                }

                popupStage.close();
            } catch (NumberFormatException ex) {
                MainControleur.showAlert("Erreur", "Le port doit être un nombre valide.");
            }
        });

        // Fermeture de la fenêtre si Annuler est cliqué
        cancelButton.setOnAction(e -> popupStage.close());

        // Création et affichage de la scène
        Scene popupScene = new Scene(grid, 400, 250);
        popupStage.setScene(popupScene);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.show();
    }

    /**
     * Vérifie si une adresse IP est valide et non déjà utilisée par une interface réseau de la machine.
     * @param ip L'adresse IP à vérifier.
     * @return true si l'adresse IP est valide et non liée, false sinon.
     */
    private boolean isValidIp(String ip) {
        try {
            // Vérifie que l'IP n'est ni nulle ni vide
            if (ip == null || ip.isEmpty()) {
                return false;
            }

            // Vérifie que l'IP est au bon format
            String[] parts = ip.split("\\.");
            if (parts.length != 4) {
                return false;
            }
            for (String part : parts) {
                int num = Integer.parseInt(part);
                if (num < 0 || num > 255) return false;
            }

            // Vérifie si l'IP est celle de localhost
            if (ip.equals("127.0.0.1") || ip.equals("localhost")) {
                return true;
            }

            // Vérifie que l'IP n'est pas déjà utilisée par une interface de la machine
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    if (address.getHostAddress().equals(ip)) {
                        return false; // L'IP est déjà liée
                    }
                }
            }

            return true;
        } catch (NumberFormatException | SocketException e) {
            return false;
        }
    }
}
