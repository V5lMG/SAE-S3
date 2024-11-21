package sae.statisalle.controleur.aides;

import javafx.fxml.FXML;
import sae.statisalle.controleur.MainControleur;

public class AideConnexion {

    @FXML
    void actionRetour(){
        MainControleur.activerConnexion();
    }
}
