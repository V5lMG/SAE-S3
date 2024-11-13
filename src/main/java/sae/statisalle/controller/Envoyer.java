/*
 * Envoyer.java                 30/10/2024
 * IUT DE RODEZ                 Pas de copyrights
 */
package sae.statisalle.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sae.statisalle.Reseau;
import sae.statisalle.Session;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Contrôleur pour gérer l'envoi de fichiers dans l'application StatiSalle.
 * Permet de sélectionner des fichiers à envoyer et de gérer l'interface.
 *
 * @author Valentin MUNIER-GENIE
 * @author Erwan THIERRY
 */
public class Envoyer {

    /**
     * Instance de la classe Reseau utilisée pour gérer la connexion
     * et les communications avec le serveur.
     */
    private Reseau reseau;

    /**
     * Liste contenant les chemins des fichiers sélectionnés pour l'envoi.
     * Chaque chemin représente un fichier que l'utilisateur souhaite
     * envoyer au serveur.
     */
    private List<String> cheminsDesFichiers;

    @FXML
    private Text cheminFx;

    @FXML
    private Text nomFx;

    @FXML
    private Text cheminFichier;

    @FXML
    private Text nomFichier;

    @FXML
    private Text adresseIP;

    @FXML
    private Text ipFx;

    @FXML
    private Button btnEnvoyer;

    /**
     * Gère l'action de retour à l'écran d'accueil de l'application.
     */
    @FXML
    void actionRetour() {
        MainControleur.activerAccueil();
    }

    /**
     * Gère l'action d'affichage de l'aide relative à l'envoi de fichiers.
     */
    @FXML
    void actionAide() {
        MainControleur.activerAideEnvoyer();
    }

    /**
     * Initialise l'état de l'interface, désactivant le bouton d'envoi.
     */
    @FXML
    void initialize() {
        btnEnvoyer.setDisable(true);
    }

    /**
     * Ouvre un sélecteur de fichiers pour
     * choisir des fichiers à envoyer.
     * Met à jour l'interface avec les chemins
     * et noms des fichiers sélectionnés.
     */
    @FXML
    private void actionChoixFichier() {
        ipFx.setText(Session.getAdresseIp());

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir des fichiers");

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Fichiers CSV", "*.csv")
        );

        Stage stage = MainControleur.getFenetrePrincipale();
        List<File> fichiers = fileChooser.showOpenMultipleDialog(stage);

        if (fichiers != null && fichiers.size() <= 4) {
            StringBuilder chemins = new StringBuilder();
            StringBuilder noms = new StringBuilder();
            cheminsDesFichiers = new ArrayList<>();

            for (File fichier : fichiers) {
                cheminsDesFichiers.add(fichier.getPath());
                chemins.append(fichier.getAbsolutePath()).append("\n");
                noms.append(fichier.getName()).append("\n");
            }

            cheminFx.setText(chemins.toString());
            nomFx.setText(noms.toString());

            cheminFx.setStyle("-fx-fill: #000000;");
            nomFx.setStyle("-fx-fill: #000000;");
            cheminFichier.setStyle("-fx-fill: #000000;");
            nomFichier.setStyle("-fx-fill: #000000;");
            ipFx.setStyle("-fx-fill: #000000;");
            adresseIP.setStyle("-fx-fill: #000000;");
            btnEnvoyer.setStyle("-fx-background-color: #4CAF50;");
            btnEnvoyer.setDisable(false);

            System.out.println("Fichiers sélectionnés : \n" + chemins);
        } else if (fichiers != null) {
            System.out.println("Vous devez sélectionner "
                               + "au maximum 4 fichiers.");
            MainControleur.showAlert("Trop de fichier",
                    "Vous devez sélectionner au maximum 4 fichiers.");
        } else {
            System.out.println("Aucun fichier sélectionné.");
            MainControleur.showAlert("Pas de fichier",
                    "Aucun fichier sélectionné.");
        }
    }

    /**
     * Gère l'envoi des fichiers sélectionnés au serveur.
     * Cumule le contenu de chaque fichier en une seule chaîne de caractères,
     * séparée par un délimiteur /EOF, puis envoie le tout en un seul envoi.
     */
    @FXML
    void actionEnvoyer() {
        try {
            reseau = Session.getReseau();
            StringBuilder contenuTotal = new StringBuilder();

            for (String cheminFichier : cheminsDesFichiers) {
                File fichier = new File(cheminFichier);

                if (!fichier.exists()) {
                    throw new IllegalArgumentException("Le fichier "
                            + "n'existe pas : " + cheminFichier);
                }
                if (!fichier.isFile()) {
                    throw new IllegalArgumentException("Ce n'est pas un "
                            + "fichier valide : " + cheminFichier);
                }

                StringBuilder contenu = new StringBuilder();
                try (BufferedReader lectureFichier =
                             new BufferedReader(
                                     new FileReader(
                                             fichier.getAbsolutePath()))) {
                    String ligne;
                    while ((ligne = lectureFichier.readLine()) != null) {
                        contenu.append(ligne).append("\n");
                    }
                } catch (IOException e) {
                    System.out.println("Erreur lors de la lecture du fichier "
                            + ": " + e.getMessage());
                }

                contenuTotal.append(contenu).append("/EOF");
            }

            reseau.envoyer(contenuTotal.toString());
            String reponse = reseau.recevoirReponse();

            reseau.utiliserReponse(reponse);
            afficherConfirmationEtRetour();

        } catch (IllegalArgumentException e) {
            System.err.println("Erreur d'envoi : " + e.getMessage());
            MainControleur.showAlert("Erreur d'envoi",
                    "Une erreur est survenue lors de l'envoi : "
                            + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erreur inattendue : " + e.getMessage());
            MainControleur.showAlert("Erreur inattendue",
                    "Une erreur inattendue est survenue : "
                            + e.getMessage());
        } finally {
            if (reseau != null) {
                reseau.fermerClient();
            }
        }
    }

    /**
     * Affiche une alerte de confirmation et
     * redirige vers l'écran de connexion si l'IP n'est pas localhost.
     */
    private void afficherConfirmationEtRetour() {
        // récup l'ip et enlever le port
        String ipDestinataire = "localhost";
        String ipSource = Session.getAdresseIp().split(":")[0];

        // Si l'IP est localhost, ne pas quitter la page d'envoi
        if (ipSource.equals(ipDestinataire)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Envoi réussi");
            alert.setHeaderText(null);
            alert.setContentText("Les fichiers ont été envoyés avec succès.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Envoi réussi");
            alert.setHeaderText(null);
            alert.setContentText("Les fichiers ont été envoyés avec succès.");

            alert.setOnHidden(evt -> MainControleur.activerConnexion());
            alert.showAndWait();
        }
    }
}