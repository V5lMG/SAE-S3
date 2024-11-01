package sae.statisalle.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sae.statisalle.Reseau;
import sae.statisalle.Session;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO javadoc
 *
 * @author valentin.munier-genie
 */
public class Envoyer {

    private Reseau reseau;

    private List<String> cheminsDesFichiers;

    @FXML
    private Text cheminFx;

    @FXML
    private Text nomFx;

    @FXML
    private Button btnEnvoyer;

    @FXML
    private Text ipFx;

    @FXML
    void actionRetour() {
        MainControleur.activerAccueil();
    }

    @FXML
    void actionAide() {
        MainControleur.activerAideEnvoyer();
    }

    @FXML
    void initialize() {
        btnEnvoyer.setDisable(true);
    }

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
            System.out.println("Fichiers sélectionnés : \n" + chemins);

            btnEnvoyer.setStyle("-fx-background-color: #4CAF50;");
            btnEnvoyer.setDisable(false);
        } else if (fichiers != null) {
            System.out.println("Vous devez sélectionner au maximum 4 fichiers.");
            showAlert(Alert.AlertType.ERROR, "Trop de fichier", "Vous devez sélectionner au maximum 4 fichiers.");
        } else {
            System.out.println("Aucun fichier sélectionné.");
            showAlert(Alert.AlertType.ERROR, "Pas de fichier", "Aucun fichier sélectionné.");
        }
    }

    /**
     * Affiche une alerte pour informer l'utilisateur d'une situation spécifique.
     * @param alertType le type de l'alerte.
     * @param title le titre de l'alerte.
     * @param message le message de l'alerte.
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void actionEnvoyer() {
        try {
            reseau = Session.getReseau();
            List<File> fichiers = new ArrayList<>();

            // Ajouter les chemins des fichiers sélectionnés
            for (String cheminFichier : cheminsDesFichiers) {
                fichiers.add(new File(cheminFichier));
            }

            // Envoyer les fichiers
            for (File fichier : fichiers) {
                reseau.envoyer(fichier.getPath());

                // Recevoir une réponse du serveur
                String reponse = reseau.recevoirReponse();
                reseau.utiliserReponse(reponse);
            }

            System.out.println("Tous les fichiers ont été envoyés !");

        } catch (IllegalArgumentException e) {
            System.err.println("Erreur d'envoi : " + e.getMessage());
            e.printStackTrace();

        } catch (Exception e) {
            System.err.println("Erreur inattendue : " + e.getMessage());
            e.printStackTrace();

        } finally {
            if (reseau != null) {
                reseau.fermerClient();
            }
        }
    }
}
