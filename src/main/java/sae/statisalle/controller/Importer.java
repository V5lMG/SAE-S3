/*
 * Importer.java               24/10/2024
 * IUT DE RODEZ               Pas de copyrights
 */
package sae.statisalle.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sae.statisalle.modele.Fichier;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Classe du controller Importer,
 * Qui permet de faire le lien entre la vue Importer et son modèle
 * @author Mathias CAMBON
 *         Robin MONTES
 *         Erwan THIERRY
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

    Fichier fichierImporter;

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
        String dateDuJour = new SimpleDateFormat("ddMMyyyy")
                .format(new Date());

        for (String chemin : cheminsDesFichiers) {
            fichierImporter = new Fichier(chemin);

            String nomFichier = fichierImporter.getTypeFichier() + "_"
                    + dateDuJour + ".csv";

            // Vérifier si le répertoire existe, sinon le créer
            File dossier = new File("src/main/resources/csv/");
            if (!dossier.exists()) {
                boolean created = dossier.mkdirs(); // Crée les répertoires si nécessaires
                if (!created) {
                    System.out.println("Erreur : Impossible de créer le répertoire.");
                    return;  // Arrêter si la création échoue
                }
            }

            // Si le fichier n'existe pas, on le crée. Sinon, on le réécrit.
            if (!Fichier.fichierExiste(nomFichier)) {
                Fichier.ecritureFichier(fichierImporter.contenuFichier(),
                        "src/main/resources/csv/" + fichierImporter.getTypeFichier()
                                + "_" + dateDuJour + ".csv");
            } else {
                Fichier fichierExistant = new Fichier(nomFichier);
                fichierExistant.reecritureFichier(fichierImporter.contenuFichier());
            }
        }

        // Confirmation d'importation réussie
        System.out.println("Le fichier ou les fichiers ont bien été importé(s)");
        MainControleur.activerAccueil();

        // Pop-up d'information pour confirmer l'importation réussie
        Alert information = new Alert(Alert.AlertType.INFORMATION);
        information.setTitle("Validation");
        information.setHeaderText(null);
        information.setContentText("Le/Les fichier(s) a/ont bien été importé(s)");
        information.showAndWait();
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
            btnImporter.setStyle("-fx-background-color: #4CAF50;-fx-text-fill: #000000;");
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
