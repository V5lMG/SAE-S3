package sae.statisalle.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;

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
        // TODO message d'affichage "exportation réalisée" ou " erreur "
    }

    @FXML
    void actionChoixDonnees(){
        // TODO pouvoir choisir les données
    }

}
