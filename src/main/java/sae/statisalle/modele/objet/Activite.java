/*
 * Activite.java               24/10/2024
 * IUT DE RODEZ               Pas de copyrights
 */
package sae.statisalle.modele.objet;

/**
 * La classe Activite initialise les objets de type Activite.
 * L'objet Activite est composé de :
 * <ul>
 *     <li>Un type d'activité</li>
 * </ul>
 * <br>
 * Elle gère également toutes les erreurs relatives à l'instantiation
 * des Activités en fonction des contenus des fichiers
 * @author robin.montes
 */
public class Activite {

    /* Identifiant de l'activité */
    String idActivite;

    /* Les différents types d'activité */
    String type;

    public Activite(String type, String idActivite) {
        this.idActivite = idActivite;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getIdActivite() {
        return idActivite;
    }

    public void setIdActivite(String idActivite) {
        this.idActivite = idActivite;
    }

    public void setType(String type) {
        this.type = type;
    }
}
