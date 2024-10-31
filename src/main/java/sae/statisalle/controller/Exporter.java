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
 * Classe du controller Exporter,
 * Qui permet de faire le lien entre la vue Exporter et son modèle
 * @author MathiasCambon
 */

public class Exporter {

    @FXML
    void actionRetour(){
        MainControleur.activerAccueil();
    }

    @FXML
    void actionAide(){
        MainControleur.activerAideExporter();
    }

    @FXML
    void actionExporter(){
        // TODO message d'affichage "exportation réalisée" ou "erreur"
    }

    @FXML
    void actionChoixDonnees() {
        // Création d'une instance de FileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir les données à exporter");

        // Filtre pour les types de fichiers appropriés (par exemple, fichiers CSV)
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers CSV", "*.csv"));

        // Obtenir le stage principal
        Stage stage = MainControleur.getFenetrePrincipale();

        // Afficher la boîte de dialogue pour la sélection du fichier
        File fichier = fileChooser.showOpenDialog(stage);

        // Vérifier si un fichier a été sélectionné
        if (fichier != null) {
            System.out.println("Fichier sélectionné pour exportation : " + fichier.getAbsolutePath());
            // Traiter le fichier ici, par exemple, l'envoyer pour l'exportation
        } else {
            System.out.println("Aucun fichier sélectionné pour exportation.");
        }
    }
}
