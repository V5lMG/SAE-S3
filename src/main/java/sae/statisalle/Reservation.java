/*
 * Reservation.java               24/10/2024
 * IUT DE RODEZ               Pas de copyrights
 */
package sae.statisalle;

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



    //TODO Faire les modifications pour correspondre aux attentes
    //Ident;salle;employe;activite;date;heuredebut;heurefin;H;I;J;K;L
//    Fichier des réservations
//    Signification des colonnes H à L
//
//    Colonne H :
//    Si l'activité est une réunion  => objet de la réunion
//    Si l'activité est un prêt ou une location  => organisme bénéfiaire du prêt ou auquel la salle est louée
//    Si l'activité est une formation => la thématique de la formation
//    Si l'activité est un entretien => nature de l'entretien
//    Si l'activité est "autre"  => un texte libre de description de cette activité
//
//    Colonnes I à L :
//    Elles sont présentes seulement si l'activité est un prêt ou une location
//    Puisque le prêt ou la location concernent un organisme extérieur, il est intéressant
//    de pouvoir enregistrer les coordonnées d'un interlocuteur (à contacter si besoin),
//    donc une personne faisant partie de la structure bénéfiaire du prêt ou de la location
//
//    Colonne I  et J  => nom et prénom de l'interlocuteur
//    Colonne K        => numéro de téléphone de l'interlocuteur
//    Colonne L        => usage qui sera fait de la salle, dans le cadre du prêt ou de la location
    public Reservation(String idReservation, String salleR           , String employeR         , String activiteR  , String dateR,
                       String heureDebut       , String heureFin         , String description, String nomIntervenant,
                       String prenomIntervenant, String numTelIntervenant, String usage) {
        this.idReservation = idReservation;
        this.salleR = salleR;
        this.employeR = employeR;
        this.activiteR = activiteR;
        this.dateR = dateR;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.description = description;
        this.nomIntervenant = nomIntervenant;
        this.prenomIntervenant = prenomIntervenant;
        this.numTelIntervenant = numTelIntervenant;
        this.usage = usage;
    }

    public String getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(String idReservation) {
        this.idReservation = idReservation;
    }

    public String getSalleR() {
        return salleR;
    }

    public void setSalleR(String salleR) {
        this.salleR = salleR;
    }

    public String getEmployeR() {
        return employeR;
    }

    public void setEmployeR(String employeR) {
        this.employeR = employeR;
    }

    public String getActiviteR() {
        return activiteR;
    }

    public void setActiviteR(String activiteR) {
        this.activiteR = activiteR;
    }

    public String getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(String heureDebut) {
        this.heureDebut = heureDebut;
    }

    public String getDateR() {
        return dateR;
    }

    public void setDateR(String dateR) {
        this.dateR = dateR;
    }

    public String getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(String heureFin) {
        this.heureFin = heureFin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNomIntervenant() {
        return nomIntervenant;
    }

    public void setNomIntervenant(String nomIntervenant) {
        this.nomIntervenant = nomIntervenant;
    }

    public String getPrenomIntervenant() {
        return prenomIntervenant;
    }

    public void setPrenomIntervenant(String prenomIntervenant) {
        this.prenomIntervenant = prenomIntervenant;
    }

    public String getNumTelIntervenant() {
        return numTelIntervenant;
    }

    public void setNumTelIntervenant(String numTelIntervenant) {
        this.numTelIntervenant = numTelIntervenant;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }
}
