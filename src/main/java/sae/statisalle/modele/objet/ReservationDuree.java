/*
 * ReservationDuree.java               24/11/2024
 * IUT DE RODEZ               Pas de copyrights
 */
package sae.statisalle.modele.objet;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

/**
 * Classe objet qui calcule la durée d'une réservation
 */
public class ReservationDuree {
    private String idReservation;
    private String salle;
    private String employe;
    private String activite;
    private String date;
    private String heureDebut;
    private String heureFin;
    private String duree;

    // Constructeur
    public ReservationDuree(String idReservation, String salle, String employe, String activite,
                            String date, String heureDebut, String heureFin) {
        this.idReservation = idReservation;
        this.salle = salle;
        this.employe = employe;
        this.activite = activite;
        this.date = date;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.duree = setDuree(this.heureDebut,this.heureFin);
    }

    // Getters
    public String getIdReservation() { return idReservation; }
    public String getSalle() { return salle; }
    public String getEmploye() { return employe; }
    public String getActivite() { return activite; }
    public String getDate() { return date; }
    public String getHeureDebut() { return heureDebut; }
    public String getHeureFin() { return heureFin; }
    public String getDuree() { return duree; }

    private String setDuree(String heureDebut, String heureFin) {

        LocalTime heureDebutL = parseHeure(heureDebut);
        LocalTime heureFinL = parseHeure(heureFin);

        if (heureDebutL != null && heureFinL != null) {
            int duree = (int) Duration.between(heureDebutL, heureFinL).toMinutes();
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
}
