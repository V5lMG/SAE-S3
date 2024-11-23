/*
 * AideAccueil.java                   14/11/2024
 * IUT DE RODEZ                       Pas de copyrights
 */
package sae.statisalle.controleur.aides;

import javafx.fxml.FXML;
import sae.statisalle.controleur.MainControleur;

/**
 * Contrôleur de l'écran d'aide lié à la page d'accueil.
 * Permet de naviguer vers l'écran principal depuis l'aide.
 */
public class AideAccueil {

    /**
     * Gère l'action de retour à l'écran d'affichage principal.
     */
    @FXML
    void actionRetour(){
        MainControleur.activerAccueil();
    }

}
