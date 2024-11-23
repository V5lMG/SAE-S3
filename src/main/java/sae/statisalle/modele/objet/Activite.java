/*
 * Activite.java               24/10/2024
 * IUT DE RODEZ                Pas de copyrights
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
 * @author erwan.thierry
 */
public class Activite {

    /* Identifiant de l'activité */
    String idActivite;

    /* Les différents types d'activité */
    String type;

    /**
     * Constructeur pour initialiser une nouvelle
     * activité avec son type et son identifiant.
     *
     * @param type Le type de l'activité.
     * @param idActivite L'identifiant unique de l'activité.
     */
    public Activite(String type, String idActivite) {
        this.idActivite = idActivite;
        this.type = type;
    }

    /**
     * Renvoie le type de l'activité.
     *
     * @return Le type de l'activité.
     */
    public String getType() {
        return type;
    }

    /**
     * Renvoie l'identifiant unique de l'activité.
     *
     * @return L'identifiant de l'activité.
     */
    public String getIdActivite() {
        return idActivite;
    }

    /**
     * Modifie l'identifiant de l'activité.
     *
     * @param idActivite Le nouvel identifiant à attribuer à l'activité.
     */
    public void setIdActivite(String idActivite) {
        this.idActivite = idActivite;
    }

    /**
     * Modifie le type de l'activité.
     *
     * @param type Le nouveau type à attribuer à l'activité.
     */
    public void setType(String type) {
        this.type = type;
    }
}
