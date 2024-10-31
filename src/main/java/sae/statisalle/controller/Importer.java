/*
 * Exporter.java               24/10/2024
 * IUT DE RODEZ               Pas de copyrights
 */
package sae.statisalle.controller;

import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

/**
 * Classe du controller Importer,
 * Qui permet de faire le lien entre la vue Importer et son modèle
 * @author MathiasCambon
 */


public class Importer {

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
        // TODO affichage d'un message de confirmation, et importation des fichiers
    }

    @FXML
    private void actionChoixFichier() {
        // Création d'une instance de FileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir un fichier");

        // Ajouter des filtres d'extension si nécessaire
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Fichiers CSV", "*.csv"),
                new FileChooser.ExtensionFilter("Tous les fichiers", "*.*")
        );

        // Obtenir le stage principal à partir de MainControleur
        Stage stage = MainControleur.getFenetrePrincipale();

        // Afficher la boîte de dialogue de sélection de fichier
        File fichier = fileChooser.showOpenDialog(stage);

        // Vérifier si un fichier a été sélectionné
        if (fichier != null) {
            System.out.println("Fichier sélectionné : " + fichier.getAbsolutePath());
            // Traiter le fichier comme vous le souhaitez (par exemple, l'envoyer au modèle pour traitement)
        } else {
            System.out.println("Aucun fichier sélectionné.");
        }
    }
}
