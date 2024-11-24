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

    /* Liste des réservations */
    private ObservableList<Reservation> listReservation;

    /**
     * Constructeur pour initialiser un nouvel employé avec son identifiant,
     * son nom, son prénom et son numéro de téléphone.
     *
     * @param idE L'identifiant unique de l'employé.
     * @param nom Le nom de l'employé.
     * @param prenom Le prénom de l'employé.
     * @param numTel Le numéro de téléphone de l'employé.
     */
    public Employe(String idE, String nom, String prenom, String numTel) {
        this.idE = idE;
        this.nom = nom;
        this.prenom = prenom;
        this.numTel = numTel;
        this.listReservation = FXCollections.observableArrayList();
    }

    /**
     * Renvoie l'identifiant unique de l'employé.
     *
     * @return L'identifiant de l'employé.
     */
    public String getIdE() {
        return idE;
    }

    /**
     * Renvoie le nom de l'employé.
     *
     * @return Le nom de l'employé.
     */
    public String getNom() {
        return nom;
    }

    /**
     * Renvoie le prénom de l'employé.
     *
     * @return Le prénom de l'employé.
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * Renvoie le numéro de téléphone de l'employé.
     *
     * @return Le numéro de téléphone de l'employé.
     */
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

    /**
     * Modifie l'identifiant de l'employé.
     *
     * @param idE Le nouvel identifiant à attribuer à l'employé.
     */
    public void setIdE(String idE) {
        this.idE = idE;
    }

    /**
     * Modifie le nom de l'employé.
     *
     * @param nom Le nouveau nom à attribuer à l'employé.
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Modifie le prénom de l'employé.
     *
     * @param prenom Le nouveau prénom à attribuer à l'employé.
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * Modifie le numéro de téléphone de l'employé.
     *
     * @param numTel Le nouveau numéro de téléphone à attribuer à l'employé.
     */
    public void setNumTel(String numTel) {
        this.numTel = numTel;
    }
}
