package sae.statisalle.controller;

import javafx.fxml.FXML;
import javafx.application.Platform;

public class Accueil {

    @FXML
    void actionImporter() { MainControleur.activerImporter(); }

    @FXML
    void actionEnvoyer(){
        MainControleur.activerConnexion();
    }

    @FXML
    void actionAfficher(){ MainControleur.activerAffichage(); }

    @FXML
    void actionQuitter(){
        Platform.exit();
    }

    @FXML
    void actionAide(){
        MainControleur.activerAideAccueil();
    }
}
