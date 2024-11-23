/*
 * ControleurImporter.java          24/10/2024
 * IUT DE RODEZ                     Pas de copyrights
 */
package sae.statisalle.controleur;

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
 * Permet de faire le lien entre la vue Importer et le modèle
 * @author robin.montes
 * @author mathias.cambon
 * @author valentin.munier-genie
 * @author erwan.thierry
 */
public class ControleurImporter {

    @FXML
    public Button btnFichier, btnImporter;

    @FXML
    public Text cheminFichier, nomFichier, texteCheminFichier, texteNomFichier;

    /**
     * Liste contenant les chemins des fichiers sélectionnés pour l'envoi.
     * Chaque chemin représente un fichier que l'utilisateur souhaite
     * envoyer au serveur.
     */
    private List<String> cheminsDesFichiers;

    Fichier fichierImporter;

    /**
     * Initialise l'état de l'interface, désactivant le bouton d'envoi.
     */
    @FXML
    void initialize() {
        btnImporter.setDisable(true);
    }

    /**
     * Cette méthode est appelée lors de l'action de retour.
     * Elle permet de retourner à la page d'accueil.
     */
    @FXML
    void actionRetour() {
        MainControleur.activerAccueil();
    }

    /**
     * Cette méthode est appelée lors de l'action d'aide.
     * Elle permet d'afficher la page d'aide pour l'importation de données.
     */
    @FXML
    void actionAide() {
        MainControleur.activerAideImporter();
    }

    /**
     * Cette méthode est appelée lors de l'importation de fichiers.
     * Elle gère la création, l'écriture ou la réécriture de fichiers CSV
     * à partir des fichiers importés.
     */
    @FXML
    void actionImporter() {
        String dateDuJour = new SimpleDateFormat("ddMMyyyy")
                            .format(new Date());

        for (String chemin : cheminsDesFichiers) {
            fichierImporter = new Fichier(chemin);

            String nomFichier = fichierImporter.getTypeFichier() + "_"
                    + dateDuJour + ".csv";

            // vérifier si le répertoire existe, sinon le créer
            File dossier = new File("src/main/resources/csv/");
            if (!dossier.exists()) {
                boolean created = dossier.mkdirs();
                if (!created) {
                    System.out.println("Erreur : Impossible de créer le "
                                       + "répertoire.");
                    return;
                }
            }

            // Si le fichier n'existe pas, on le crée. Sinon, on le réécrit.
            if (!Fichier.fichierExiste(nomFichier)) {
                Fichier.ecritureFichier(fichierImporter.contenuFichier(),
                        "src/main/resources/csv/"
                                + fichierImporter.getTypeFichier()
                                + "_" + dateDuJour + ".csv");
            } else {
                Fichier fichierExistant = new Fichier(nomFichier);
                fichierExistant.reecritureFichier(
                        fichierImporter.contenuFichier());
            }
        }

        System.out.println("Le fichier ou les fichiers ont bien "
                           + "été importé(s)");
        MainControleur.activerAccueil();

        Alert information = new Alert(Alert.AlertType.INFORMATION);
        information.setTitle("Validation");
        information.setHeaderText(null);
        information.setContentText("Le/Les fichier(s) a/ont bien été "
                                   + "importé(s)");
        information.showAndWait();
    }

    /**
     * Cette méthode est appelée lors de l'importation de fichiers.
     * Elle gère la création, l'écriture ou la réécriture de fichiers CSV
     * à partir des fichiers importés.
     */
    @FXML
    private void actionChoixFichier() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir un fichier");

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Fichiers CSV", "*.csv")
        );

        // obtenir le stage principal à partir de MainControleur
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
            btnImporter.setStyle("-fx-background-color: #4CAF50;" +
                                 "-fx-text-fill: #000000;");
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
