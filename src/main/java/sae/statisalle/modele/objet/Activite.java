/*
 * Activite.java               24/10/2024
 * IUT DE RODEZ               Pas de copyrights
 */
package sae.statisalle.modele.objet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
 * @author rodrigo.xaviertaborda
 */
public class Activite {

    /* Identifiant de l'activité */
    String idActivite;

    /* Les différents types d'activité */
    String type;

    private ObservableList<Reservation> listReservation ;

    public Activite(String type, String idActivite) {
        this.idActivite = idActivite;
        this.type = type;
        this.listReservation = FXCollections.observableArrayList();
    }

    public String getType() {
        return type;
    }

    public String getIdActivite() {
        return idActivite;
    }

    public ObservableList<Reservation> getReservations() {
        return listReservation;
    }

    // 1. Obtenir les salles associées
    public String getSallesAssociees() {
        return listReservation.stream()
                .map(reservation -> reservation.getSalleR())
                .distinct()
                .reduce((a, b) -> a + ", " + b)
                .orElse("Aucune salle");
    }

    //Obtenir les types d'activités associées à la salle
    public String getHeureDebutR() {
        return listReservation.stream()
                .map(reservation -> reservation.getHeureDebut())
                .distinct()
                .reduce((a, b) -> a + ", " + b) // Combine les types distincts
                .orElse("Aucune activité");
    }

    //Obtenir les types d'activités associées à la salle
    public String getHeureFinR() {
        return listReservation.stream()
                .map(reservation -> reservation.getHeureFin())
                .distinct()
                .reduce((a, b) -> a + ", " + b) // Combine les types distincts
                .orElse("Aucune activité");
    }

//    // 2. Calculer le temps total pour l'activité
//    public int getTempsTotal() {
//        return reservations.stream()
//                .mapToInt(Reservation::getDuree)
//                .sum();
//    }

    // 3. Obtenir les noms des employés participant à l'activité
    public String getEmployeAssocies() {
        return listReservation.stream()
                .map(reservation -> reservation.getEmployeR())
                .distinct()
                .reduce((a, b) -> a + ", " + b)
                .orElse("Aucun employé");
    }

    public void setIdActivite(String idActivite) {
        this.idActivite = idActivite;
    }

    public void setType(String type) {
        this.type = type;
    }
}
