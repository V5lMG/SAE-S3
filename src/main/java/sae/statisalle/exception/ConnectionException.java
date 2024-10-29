/*
 * ConnectionException.java               29/10/2024
 * Pas de droits d'auteur ni de copyright
 */

package sae.statisalle.exception;

/**
 * Classe gérant les problèmes de connexion au serveur
 * @author valentin.munier-genie
 * @author rodrigo.xavier-taborda
 */

public class ConnectionException extends Exception {

    /**
     * Constructeur permettant de récupérer le message de la classe mère
     * @param message le message d'erreur
     */
    public ConnectionException(String message) {
        super(message);
    }
}
