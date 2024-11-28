/*
 * Reservation.java               24/10/2024
 * IUT DE RODEZ               Pas de copyrights
 */
package sae.statisalle.modele.objet;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

/**
 * La classe Reservation représente une réservation effectuée pour une salle avec
 * divers détails tels que l'employé ayant effectué la réservation, l'activité associée,
 * la date, l'heure de début et de fin, ainsi que des informations supplémentaires
 * sur l'intervenant et l'usage de la réservation.
 *
 * <p>
 * Chaque objet Reservation est caractérisé par :
 * <ul>
 *     <li>Un identifiant unique de la réservation</li>
 *     <li>Une salle réservée</li>
 *     <li>Un employé responsable de la réservation</li>
 *     <li>Une activité liée à la réservation</li>
 *     <li>Une date et une plage horaire (heure de début et heure de fin)</li>
 *     <li>Une description de la réservation</li>
 *     <li>Des informations sur l'intervenant (nom, prénom, numéro de téléphone)</li>
 *     <li>L'usage prévu pour la réservation</li>
 * </ul>
 *
 * <p>
 * Cette classe permet de créer, consulter et modifier les informations
 * relatives à une réservation, ainsi que de gérer le formatage des heures
 * de début et de fin si nécessaire.
 * </p>
 *
 * <p>
 * Elle assure également une validation minimale des heures afin de s'assurer
 * qu'elles respectent un format standard (HH:mm).
 * </p>
 *
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
     * Constructeur de la classe Reservation qui initialise une nouvelle
     * réservation avec les détails spécifiés, tels que l'identifiant de la
     * réservation, la salle, l'employé, l'activité, la date, l'heure de début
     * et de fin, la description, les informations de l'intervenant (nom,
     * prénom, numéro de téléphone), ainsi que l'usage de la réservation.
     * Si l'heure de début ou de fin est inférieure à 5 caractères,
     * elle est automatiquement formatée pour ajouter un zéro en début de chaîne.
     *
     * @param idReservation Identifiant unique de la réservation
     * @param salleR Nom de la salle réservée
     * @param employeR Nom de l'employé ayant effectué la réservation
     * @param activiteR Activité associée à la réservation
     * @param dateR Date de la réservation (format attendu : JJ/MM/AAAA)
     * @param heureDebut Heure de début de la réservation (format attendu : HH:mm)
     * @param heureFin Heure de fin de la réservation (format attendu : HH:mm)
     * @param description Brève description de la réservation
     * @param nomIntervenant Nom de l'intervenant associé à la réservation
     * @param prenomIntervenant Prénom de l'intervenant associé à la réservation
     * @param numTelIntervenant Numéro de téléphone de l'intervenant
     * @param usage Usage prévu pour cette réservation
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

    public String getDureeReservation() {
        String heureDebutL = getHeureDebut();
        String heureFinL = getHeureFin();

        LocalTime heureDebut = parseHeure(heureDebutL);
        LocalTime heureFin = parseHeure(heureFinL);

        if (heureDebut != null && heureFin != null) {
            int duree = (int) Duration.between(heureDebut, heureFin).toMinutes();
            int heure = duree / 60;
            int minutes = duree % 60;

            return (heure < 10 ? "0" + heure : heure) + "h" + (minutes < 10 ? "0" + minutes : minutes);
        } else {
            return "Non Valide";
        }
    }

    private LocalTime parseHeure(String heure) {
        try {
            heure = heure.replace('h', ':');
            return LocalTime.parse(heure);
        } catch (DateTimeParseException e) {
            System.out.println("Erreur de format d'heure: " + heure);
            return null;
        }
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
