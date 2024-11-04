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
import java.util.Date;
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

    public Text textCheminFichier;

    public Text textNomFichier;

    private File fichier;

    Fichier fichierImporter;

    @FXML
    private void initialize() {

        fichier = null;
        actualisationValiditeFichier();
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
            String nomFichier = fichierImporter.getTypeFichier() + "_"
                                + dateDuJour;

            if (Fichier.fichierExiste(nomFichier)) {
                Fichier.ecritureFichier(fichierImporter.contenuFichier(),
                        "/fichier/" + fichierImporter.getTypeFichier()
                                + "_" + dateDuJour);
            } else {
                Fichier fichierExistant = new Fichier(nomFichier);
                fichierExistant.reecritureFichier(fichierImporter.contenuFichier());
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

        // Afficher la boîte de dialogue de sélection de fichier
        fichier = fileChooser.showOpenDialog(stage);
        fichierImporter = new Fichier(fichier.getAbsolutePath());
        actualisationValiditeFichier();
    }

    /**
     * Actualise la validité du fichier sélectionné et met à jour les éléments
     * de l'interface utilisateur en conséquence.
     * <p>
     * Cette méthode vérifie si un fichier a été sélectionné et :
     * <ul>
     *     <li>Active ou désactive le bouton d'importation en fonction de la validité du fichier.</li>
     *     <li>Affiche les informations sur le fichier sélectionné, notamment le chemin et le nom du fichier,
     *         dans les éléments `Text` appropriés.</li>
     *     <li>Modifie le style des textes pour
     *         leur appliquer une couleur noire quand le fichier est sélectionné.</li>
     * </ul>
     * Si aucun fichier n'a été sélectionné, le bouton d'importation est désactivé
     */
    private void actualisationValiditeFichier() {
        btnImporter.setDisable(true);
        // Vérifier si un fichier a été sélectionné
        if (fichier != null) {
            /* Affichage dans la console */
            System.out.println("Fichier sélectionné : " + fichier.getAbsolutePath());

            textCheminFichier.setText("Chemin du fichier choisi : " + fichier.getAbsolutePath());
            textNomFichier.setText("Nom du fichier : " + fichier.getName());

            textCheminFichier.setStyle("-fx-fill: #000000;");
            textNomFichier.setStyle("-fx-fill: #000000;");
            btnImporter.setStyle("-fx-background-color: #4CAF50;");

            btnImporter.setDisable(false);
            // Traiter le fichier comme vous le souhaitez (par exemple, l'envoyer au modèle pour traitement)
        } else {
            btnImporter.setDisable(true);
        }
    }
}
