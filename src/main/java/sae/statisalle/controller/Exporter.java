/*
 * Exporter.java                24/10/2024
 * IUT DE RODEZ                 Pas de copyrights
 */
package sae.statisalle.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

/**
 * Classe du controller Exporter, qui fait le lien entre
 * la vue Exporter et son modèle.
 * @author MathiasCambon
 * @author Montes Robin
 */
public class Exporter {

    /**
     * Retourne à l'écran d'accueil de l'application.
     */
    @FXML
    void actionRetour(){
        MainControleur.activerAccueil();
    }

    /**
     * Affiche l'écran d'aide pour l'exportation.
     */
    @FXML
    void actionAide(){
        MainControleur.activerAideExporter();
    }

    /**
     * Gère l'action d'exportation des fichiers.
     * Affiche une alerte de confirmation avant d'exporter.
     */
    @FXML
    void actionExporter(){
        Alert envoyer = new Alert (Alert.AlertType.CONFIRMATION);
        envoyer.setTitle("Exporter");
        envoyer.setHeaderText(null);
        envoyer.setContentText("Voulez vous vraiment exporter ce/ces "
                               + "fichier(s) ?");
        Optional<ButtonType> result = envoyer.showAndWait();

        if (result.get() == ButtonType.OK){

            String dateDuJour = new SimpleDateFormat("ddMMyyyy")
                    .format(new Date());

            /* TODO Fichier.reecritureFichier(fichierImporter.contenuFichier(),
                    "/fichier/" + fichierImporter.getTypeFichier()
                            + "_" + dateDuJour);*/

            System.out.println("Le fichier ou les fichiers ont bien été "
                               + "exporté(s)");
        } else if (result.get() == ButtonType.CANCEL){
            envoyer.close();
        } else {
            System.out.print("Erreur : le ou les fichiers n'ont pas pu être "
                             + "exporté(s)");
        }
    }

    /**
     * Ouvre une boîte de dialogue pour choisir les données à exporter.
     * Traite le fichier sélectionné pour l'exportation.
     */
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
