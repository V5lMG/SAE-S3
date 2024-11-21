package sae.statisalle.controleur.aides;

import javafx.fxml.FXML;
import sae.statisalle.controleur.MainControleur;

public class AideAffichage {

    @FXML
    void actionRetour(){
        MainControleur.activerAffichage();
    }
}
