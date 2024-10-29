/*
 * FichierTransfertException.java               29/10/2024
 * Pas de droits d'auteur ni de copyright
 */

package sae.statisalle.exception;

/**
 * Classe gérant les problèmes lors de l'envoi ou de la réception de fichier
 * @author valentin.munier-genie
 * @author rodrigo.xavier-taborda
 */

public class FichierTransfertException extends Exception {

    /**
     * Constructeur permettant de récupérer le message de la classe mère
     * @param message le message d'erreur
     */
    public FichierTransfertException(String message) {
        super(message);
    }

}
