/*
 * Importer.java               24/10/2024
 * IUT DE RODEZ               Pas de copyrights
 */
package sae.statisalle.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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
            System.out.print("Erreur : le ou les fichiers n'ont pas pu être importé(s)");
        }
        // TODO affichage d'un message de confirmation, et importation des fichiers
    }

    @FXML
    private void actionChoixFichier() {
        // Création d'une instance de FileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir un fichier");

        // Ajouter des filtres d'extension si nécessaire
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Fichiers CSV", "*.csv")
        );

        // Obtenir le stage principal à partir de MainControleur
        Stage stage = MainControleur.getFenetrePrincipale();

        // Afficher la boîte de dialogue de sélection de fichier
        File fichier = fileChooser.showOpenDialog(stage);

        // Vérifier si un fichier a été sélectionné
        if (fichier != null) {
            fichierImporter = new Fichier(fichier.getAbsolutePath());
            System.out.println("Fichier sélectionné : " + fichier.getAbsolutePath());
            // Traiter le fichier comme vous le souhaitez (par exemple, l'envoyer au modèle pour traitement)
        } else {
            System.out.println("Aucun fichier sélectionné.");
        }
    }
}
