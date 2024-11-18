package sae.statisalle.controller.aides;

import javafx.fxml.FXML;
import sae.statisalle.controller.MainControleur;

public class AideEnvoyer {

    @FXML
    void actionRetour(){
        MainControleur.activerEnvoyer();
    }
}
