package sae.statisalle.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;

public class Importer {

    @FXML
    void actionRetour(){
        MainControleur.activerAccueil();
    }

    @FXML
    void actionAide(){
        MainControleur.activerAideImporter();
    }

    @FXML
    void actionImporter(){
        // TODO affichage d'un message de confirmation, et importation des fichiers
    }

    @FXML
    void actionChoixFichier(){
        // TODO choisir fichier(s)
    }
}
