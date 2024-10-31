package sae.statisalle.controller;

import javafx.fxml.FXML;
import javafx.application.Platform;

public class Accueil {

    @FXML
    void actionImporter(){
        MainControleur.activerImporter();
    }

    @FXML
    void actionExporter(){
        MainControleur.activerExporter();
    }

    @FXML
    void actionEnvoyer(){
        MainControleur.activerEnvoyer();
    }

    @FXML
    void actionQuitter(){
        Platform.exit();
    }

    @FXML
    void actionAide(){
        MainControleur.activerAideAccueil();
    }
}
