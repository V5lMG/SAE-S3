/*
 * Creneau.java               24/10/2024
 * IUT DE RODEZ               Pas de copyrights
 */
package sae.statisalle;

import java.text.DateFormat;

/**
 * La classe Creneau initialise les objets de type Creneau.
 * L'objet Creneau est composé de :
 * <ul>
 *     <li>Une heure de début</li>
 *     <li>Une heure de fin</li>
 * </ul>
 * <br>
 * Elle gère également toutes les erreurs relatives à l'instantiation
 * des Créneaux en fonction des contenus des fichiers
 * @author robin.montes
 */
public class Creneau {
    /*Heure de début du créneau de réservation*/
    DateFormat heureDebut;

    /*Heure de fin du créneau de réservation*/
    DateFormat heureFin;

    public Creneau(DateFormat heureDebut, DateFormat heureFin) {
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
    }
}
