package sae.statisalle.controller;

import javafx.fxml.FXML;

public class Envoyer {

    @FXML
    void actionRetour(){
        MainControleur.activerConnexion();
    }

    @FXML
    void actionAide(){
        MainControleur.activerAideEnvoyer();
    }

    @FXML
    void actionEnvoyer(){
        // TODO affichage d'un message "fichiers envoyés" ou "erreur"
    }
}
