/*
 * ModuloNegatifException.java                  30/10/2024
 * Pas de droits d'auteur ni de copyright
 */
package sae.statisalle.exception;

/**
 * Exception personnalisée levée lorsqu'une opération de modulo avec un
 * diviseur négatif est effectuée.
 */
public class ModuloNegatifException extends RuntimeException {

    /**
     * Constructeur pour créer une nouvelle instance de l'exception
     * ModuloNegatifException avec un message spécifique.
     * @param message Le message d'erreur à afficher lorsque l'exception est
     *                levée.
     */
    public ModuloNegatifException(String message) {
        super(message);
    }
}
