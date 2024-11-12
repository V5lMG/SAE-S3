package sae.statisalle.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import sae.statisalle.Session;

import java.io.File;

import static sae.statisalle.controller.MainControleur.activerAccueil;

public class SauvegarderFichier {

    @FXML
    private TextField text1;
    @FXML
    private TextField text2;
    @FXML
    private TextField text3;
    @FXML
    private TextField text4;
    @FXML
    private Button btnFichier1;
    @FXML
    private Button btnFichier2;
    @FXML
    private Button btnFichier3;
    @FXML
    private Button btnFichier4;
    @FXML
    private Text fichier1;
    @FXML
    private Text fichier2;
    @FXML
    private Text fichier3;
    @FXML
    private Text fichier4;

    private int nombreDeFichiers;
    private String chemin1;
    private String chemin2;
    private String chemin3;
    private String chemin4;

    @FXML
    public void initialiserPage() {
        String contenuFichier = Session.getContenu();
        nombreDeFichiers = compterMotif(contenuFichier, "/EOF");
        afficherChamps(nombreDeFichiers);
    }

    private int compterMotif(String texte, String motif) {
        int compteurFichier = 0;
        int index = 0;
        while ((index = texte.indexOf(motif, index)) != -1) {
            compteurFichier++;
            index += motif.length();
        }
        return compteurFichier;
    }

    private void afficherChamps(int nombre) {
        text1.setVisible(nombre >= 1);
        btnFichier1.setVisible(nombre >= 1);
        fichier1.setVisible(nombre >= 1);

        text2.setVisible(nombre >= 2);
        btnFichier2.setVisible(nombre >= 2);
        fichier2.setVisible(nombre >= 2);

        text3.setVisible(nombre >= 3);
        btnFichier3.setVisible(nombre >= 3);
        fichier3.setVisible(nombre >= 3);

        text4.setVisible(nombre >= 4);
        btnFichier4.setVisible(nombre >= 4);
        fichier4.setVisible(nombre >= 4);
    }

    @FXML
    private void actionEnvoyer() {
        sauvegarderFichiers();
    }

    @FXML
    private void sauvegarderFichiers() {
        try {
            // récupération noms et chemins
            if (nombreDeFichiers >= 1 && !text1.getText().isEmpty()) {
                String chemin1 = btnFichier1.getText();
                String nom1 = text1.getText();

                // TODO créer un fichier CSV avec ce contenu
            }
            if (nombreDeFichiers >= 2 && !text2.getText().isEmpty()) {
                String chemin2 = btnFichier2.getText();
                String nom2 = text2.getText();

                // TODO créer un fichier CSV avec ce contenu
            }
            if (nombreDeFichiers >= 3 && !text3.getText().isEmpty()) {
                String chemin3 = btnFichier3.getText();
                String nom3 = text3.getText();

                // TODO créer un fichier CSV avec ce contenu
            }
            if (nombreDeFichiers >= 4 && !text4.getText().isEmpty()) {
                String chemin4 = btnFichier4.getText();
                String nom4 = text4.getText();

                // TODO créer un fichier CSV avec ce contenu
            }

            showConfirmation("Enregistrement", "Tous les fichiers"
                             + " ont été enregistrés avec succès.");
            activerAccueil();
        } catch (Exception ex) {
            MainControleur.showAlert("Erreur d'enregistrement",
                    "Une erreur est survenue lors de la sauvegarde "
                            + "des fichiers.");
        }
    }

    private void showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void actionChoixDossier1() {
        choisirDossier(fichier1, text1, 1);
    }

    @FXML
    private void actionChoixDossier2() {
        choisirDossier(fichier2, text2, 2);
    }

    @FXML
    private void actionChoixDossier3() {
        choisirDossier(fichier3, text3, 3);
    }

    @FXML
    private void actionChoixDossier4() {
        choisirDossier(fichier4, text4, 4);
    }

    @FXML
    private void nomFichier1() {
        String cheminVerifie = chemin1 == null ? "" : chemin1;
        fichier1.setText("Fichier : " + cheminVerifie + text1.getText() + ".csv");
    }

    @FXML
    private void nomFichier2() {
        String cheminVerifie = chemin2 == null ? "" : chemin2;
        fichier2.setText("Fichier : " + cheminVerifie + text2.getText() + ".csv");
    }

    @FXML
    private void nomFichier3() {
        String cheminVerifie = chemin3 == null ? "" : chemin3;
        fichier3.setText("Fichier : " + cheminVerifie + text3.getText() + ".csv");
    }

    @FXML
    private void nomFichier4() {
        String cheminVerifie = chemin4 == null ? "" : chemin4;
        fichier4.setText("Fichier : " + cheminVerifie + text4.getText() + ".csv");
    }

    private void choisirDossier(Text retour, TextField champNom, int nombre) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File dossier = directoryChooser.showDialog(new Stage());
        if (dossier != null) {
            String chemin = dossier.getAbsolutePath() + File.separator;
            String nomFichier = champNom.getText().isEmpty() ? "fichier_exporte.csv" : champNom.getText() + ".csv";
            retour.setText("Fichier " + nombre + " : " + chemin + nomFichier);

            switch (nombre) {
                case 1 -> chemin1 = chemin;
                case 2 -> chemin2 = chemin;
                case 3 -> chemin3 = chemin;
                case 4 -> chemin4 = chemin;
            }
        }
    }

    @FXML
    private void actionRetour() {
        activerAccueil();
    }

    @FXML
    private void actionAide() {
        // Code pour afficher l'aide
    }
}