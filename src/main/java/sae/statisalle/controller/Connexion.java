package sae.statisalle.controller;

import javafx.fxml.FXML;

public class Connexion {

    @FXML
    void actionRetour(){
        MainControleur.activerAccueil();
    }

    @FXML
    void actionConnexion(){
        MainControleur.activerEnvoyer();
        // TODO afficher message de confirmation de connexion ou d'erreur
    }

    @FXML
    void actionAide(){
        MainControleur.activerAideConnexion();
    }
}
