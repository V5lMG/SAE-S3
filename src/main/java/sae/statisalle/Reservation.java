/*
 * Reservation.java               24/10/2024
 * IUT DE RODEZ               Pas de copyrights
 */
package sae.statisalle;

import java.text.DateFormat;

/**
 * La classe Reservation initialise les objets de type Reservation.
 * L'objet Reservation est composé de :
 * <ul>
 *     <li>Un identifiant de réservation</li>
 *     <li>Une date</li>
 *     <li>Un Creneau(Cf classe Creneau)</li>
 * </ul>
 *
 * <br>
 * Elle gère également toutes les erreurs relatives à l'instantiation
 * des reservations en fonction des contenus des fichier
 * @author robin.montes
 */
public class Reservation {
    /*Identifiant de la reservation*/
    String idReservation;

    /*Date de la réservation*/
    DateFormat date;

    /*Créneau de réservation*/
    Creneau creneau; //intuile approximativement

    public Reservation(String idReservation, DateFormat date, Creneau creneau) {
        this.idReservation = idReservation;
        this.date = date;
        this.creneau = creneau;
    }
}
