/*
 * PortException.java               29/10/2024
 * Pas de droits d'auteur ni de copyright
 */

package sae.statisalle.exception;

/**
 * Classe gérant les problèmes de port
 * Le port doit être compris entre 0 et 65535
 * @author valentin.munier-genie
 * @author rodrigo.xavier-taborda
 */
public class PortException extends Exception {

    /**
     * Constructeur permettant de récupérer le message de la classe mère
     * @param message le message d'erreur
     */
    public PortException(String message) {
        super(message);
    }
}
