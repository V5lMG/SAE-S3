package sae.statisalle.controller.aides;

import javafx.fxml.FXML;
import sae.statisalle.controller.MainControleur;

public class AideAffichage {

    @FXML
    void actionRetour(){
        MainControleur.activerAffichage();
    }
}
