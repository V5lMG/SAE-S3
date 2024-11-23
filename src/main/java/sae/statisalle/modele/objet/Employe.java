/*
 * Employe.java               23/10/2024
 * IUT DE RODEZ               Pas de copyrights
 */
package sae.statisalle.modele.objet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * La classe Employe initialise les objets de type Employe.
 * L'objet Employe est composé du :
 * <ul>
 *     <li>Nom</li>
 *     <li>Prenom</li>
 *     <li>Numéros de téléphone</li>
 *     <li>email</li>
 * </ul>
 * <br>
 * Elle gère également toutes les erreurs relatives à l'instantiation
 * des Employés en fonction des contenus des fichiers
 * @author erwan.thierry
 * @author rodrigo.xaviertaborda
 */
public class Employe {
    /* Identifiant de l'employé */
    String idE;

    /* Nom de l'employé */
    String nom;

    /* Prenom de l'employé */
    String prenom;

    /* Numéro de téléphone de l'employé */
    String numTel;

    private ObservableList<Reservation> listReservation;

    public Employe(String idE, String nom, String prenom, String numTel) {
        this.idE = idE;
        this.nom = nom;
        this.prenom = prenom;
        this.numTel = numTel;
        this.listReservation = FXCollections.observableArrayList();
    }

    public String getIdE() {
        return idE;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getNumTel() {
        return numTel;
    }

    public ObservableList<Reservation> getReservations() {
        return listReservation;
    }

//    // 1. Obtenir le temps total de réservation
//    public int getTempsTotalReservation() {
//        return reservations.stream()
//                .mapToInt(Reservation::getDuree)
//                .sum();
//    }

    // 2. Obtenir les noms des salles réservées
    public String getSallesAssociees() {
        return listReservation.stream()
                .map(reservation -> reservation.getSalleR())
                .distinct()
                .reduce((a, b) -> a + ", " + b)
                .orElse("Aucune salle");
    }

    // 3. Obtenir les types d'activités associées
    public String getTypesActivite() {
        return listReservation.stream()
                .map(reservation -> reservation.getActiviteR())
                .distinct()
                .reduce((a, b) -> a + ", " + b) // Combine les types distincts
                .orElse("Aucune activité");
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

    public void setIdE(String idE) {
        this.idE = idE;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setNumTel(String numTel) {
        this.numTel = numTel;
    }
}
