/*
 * Reservation.java               24/10/2024
 * IUT DE RODEZ               Pas de copyrights
 */
package sae.statisalle.modele.objet;

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
 * des reservations en fonction des contenus des fichiers
 * @author erwan.thierry
 */
public class Reservation {

    /* Identifiant de la reservation */
    String idReservation;

    /* Date de la réservation */
    String salleR;

    /* Identifiant de la reservation */
    String employeR;

    /* Date de la réservation */
    String activiteR;

    /* Identifiant de la reservation */
    String dateR;

    /* Date de la réservation */
    String heureDebut;

    /* Identifiant de la reservation */
    String heureFin;

    /* Date de la réservation */
    String description;

    /* Identifiant de la reservation */
    String nomIntervenant;

    /* Date de la réservation */
    String prenomIntervenant;

    /* Identifiant de la reservation */
    String numTelIntervenant;

    /* Date de la réservation */
    String usage;

    /**
     * Constructeur de la classe Reservation
     * @param idReservation Identifiant de la réservation
     * @param salleR Salle réservée
     * @param employeR Employé ayant effectué la réservation
     * @param activiteR Activité liée à la réservation
     * @param dateR Date de la réservation
     * @param heureDebut Heure de début de la réservation
     * @param heureFin Heure de fin de la réservation
     * @param description Description de la réservation
     * @param nomIntervenant Nom de l'intervenant
     * @param prenomIntervenant Prénom de l'intervenant
     * @param numTelIntervenant Numéro de téléphone de l'intervenant
     * @param usage Usage de la réservation
     */
    public Reservation(String idReservation, String salleR, String employeR,
                       String activiteR, String dateR, String heureDebut,
                       String heureFin, String description,
                       String nomIntervenant, String prenomIntervenant,
                       String numTelIntervenant, String usage) {

        this.idReservation = idReservation;
        this.salleR = salleR;
        this.employeR = employeR;
        this.activiteR = activiteR;
        this.dateR = dateR;
        if (heureDebut.length() < 5) {
            heureDebut = "0" + heureDebut;
        }

        if (heureFin.length() < 5) {
            heureFin = "0" + heureFin;
        }

        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.description = description;
        this.nomIntervenant = nomIntervenant;
        this.prenomIntervenant = prenomIntervenant;
        this.numTelIntervenant = numTelIntervenant;
        this.usage = usage;
    }

    /* ---------------------------------------------- */
    /* Getters pour chaque attribut de la réservation */
    /* ---------------------------------------------- */

    public String getIdReservation() {
        return idReservation;
    }

    public String getSalleR() {
        return salleR;
    }

    public String getEmployeR() {
        return employeR;
    }

    public String getActiviteR() {
        return activiteR;
    }

    public String getDateR() {
        return dateR;
    }

    public String getHeureDebut() {
        return heureDebut;
    }

    public String getHeureFin() {
        return heureFin;
    }

    public String getDescription() {
        return description;
    }

    public String getNomIntervenant() {
        return nomIntervenant;
    }

    public String getPrenomIntervenant() {
        return prenomIntervenant;
    }

    public String getNumTelIntervenant() {
        return numTelIntervenant;
    }

    public String getUsage() {
        return usage;
    }

    /* ----------------------------------------------------- */
    /* Setters pour modifier les attributs de la réservation */
    /* ----------------------------------------------------- */

    public void setIdReservation(String idReservation) {
        this.idReservation = idReservation;
    }

    public void setSalleR(String salleR) {
        this.salleR = salleR;
    }

    public void setEmployeR(String employeR) {
        this.employeR = employeR;
    }

    public void setActiviteR(String activiteR) {
        this.activiteR = activiteR;
    }

    public void setDateR(String dateR) {
        this.dateR = dateR;
    }

    public void setHeureDebut(String heureDebut) {
        this.heureDebut = heureDebut;
    }

    public void setHeureFin(String heureFin) {
        this.heureFin = heureFin;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setNomIntervenant(String nomIntervenant) {
        this.nomIntervenant = nomIntervenant;
    }

    public void setPrenomIntervenant(String prenomIntervenant) {
        this.prenomIntervenant = prenomIntervenant;
    }

    public void setNumTelIntervenant(String numTelIntervenant) {
        this.numTelIntervenant = numTelIntervenant;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }
}
