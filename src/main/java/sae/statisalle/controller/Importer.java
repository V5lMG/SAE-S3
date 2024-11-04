/*
 * Importer.java               24/10/2024
 * IUT DE RODEZ               Pas de copyrights
 */
package sae.statisalle.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sae.statisalle.Fichier;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Classe du controller Importer,
 * Qui permet de faire le lien entre la vue Importer et son modèle
 * @author MathiasCambon
 *         Robin Montes
 */


public class Importer {

    public Button btnFichier;

    public Button btnImporter;

    public Text cheminFichier;

    public Text nomFichier;

    public Text texteCheminFichier;

    public Text texteNomFichier;

    /**
     * Liste contenant les chemins des fichiers sélectionnés pour l'envoi.
     * Chaque chemin représente un fichier que l'utilisateur souhaite
     * envoyer au serveur.
     */
    private List<String> cheminsDesFichiers;

    private File fichier;

    Fichier fichierImporter;

    @FXML
    private void initialize() {
        fichier = null;
    }

    @FXML
    void actionRetour() {
        MainControleur.activerAccueil();
    }

    @FXML
    void actionAide() {
        MainControleur.activerAideImporter();
    }

    @FXML
    void actionImporter() {
        Alert envoyer = new Alert (Alert.AlertType.CONFIRMATION);
        envoyer.setTitle("Importer");
        envoyer.setHeaderText(null);
        envoyer.setContentText("Voulez vous vraiment importer ce/ces fichier(s) ?");
        Optional<ButtonType> result = envoyer.showAndWait();

        if (result.get() == ButtonType.OK){


            String dateDuJour = new SimpleDateFormat("ddMMyyyy")
                                    .format(new Date());

            for (String chemin : cheminsDesFichiers) {

                fichierImporter = new Fichier(chemin);

                String nomFichier = fichierImporter.getTypeFichier() + "_"
                                    + dateDuJour + ".csv";

                if (!Fichier.fichierExiste(nomFichier)) {
                    Fichier.ecritureFichier(fichierImporter.contenuFichier(),
                            fichierImporter.getTypeFichier()
                                    + "_" + dateDuJour);
                } else {
                    Fichier fichierExistant = new Fichier(nomFichier);
                    fichierExistant.reecritureFichier(fichierImporter.contenuFichier());
                }
            }
            System.out.println("Le fichier ou les fichiers ont bien été importé(s)");
        } else if (result.get() == ButtonType.CANCEL){
            envoyer.close();
        } else {
            System.out.println("Aucun fichier sélectionné.");
            System.out.print("Erreur : le ou les fichiers n'ont pas pu être importé(s)");
        }
        // TODO affichage d'un message de confirmation, et importation des fichiers
    }

    @FXML
    private void actionChoixFichier() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir un fichier");

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Fichiers CSV", "*.csv")
        );

        // Obtenir le stage principal à partir de MainControleur
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

            texteCheminFichier.setText(chemins.toString());
            texteNomFichier.setText(noms.toString());

            texteCheminFichier.setStyle("-fx-fill: #000000;");
            texteNomFichier.setStyle("-fx-fill: #000000;");
            cheminFichier.setStyle("-fx-fill: #000000;");
            nomFichier.setStyle("-fx-fill: #000000;");
            btnImporter.setStyle("-fx-background-color: #4CAF50;");
            btnImporter.setDisable(false);

            System.out.println("Fichiers sélectionnés : \n" + chemins);

        } else if (fichiers != null) {
            System.out.println("Vous devez sélectionner "
                    + "au maximum 4 fichiers.");
        } else {
            System.out.println("Aucun fichier sélectionné.");
        }
    }
}
