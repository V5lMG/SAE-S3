/*
 * Salle.java                 23/10/2024
 * IUT DE RODEZ               Pas de copyrights
 */
package sae.statisalle.modele.objet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * La classe Salle initialise les objets de type Salle.
 * L'objet Salle est constitué de :
 * <ul>
 *     <li>Un identifiant</li>
 *     <li>Un nom</li>
 *     <li>Une capacité</li>
 *     <li>Un indicateur de la présence d'un vidéo projecteur</li>
 *     <li>Un indicateur de la présence d'un écran XXL</li>
 *     <li>Le nombre de machine</li>
 *     <li>Une description</li>
 *     <li>La liste des logiciels</li>
 *     <li>Un indicateur de la présence d'une imprimante</li>
 * </ul>
 * <br>
 * Elle gère également toutes les erreurs relatives à l'instanciation
 * des salles en fonction des contenus des fichiers
 * @author erwan.thierry
 * @author rodrigo.xaviertaborda
 */
public class Salle {
    /* Identifiant de la salle */
    String identifiant;

    /* Nom de la salle*/
    String nom;

    /* Capacité de place de la salle */
    String capacite;

    /* Indique la présence de vidéo projecteur dans la salle */
    String videoProj;

    /* Indique la présence d'écran XXL dans la salle */
    String ecranXXL;

    /* Nombre de machine présente dans la salle */
    String nbMachine;

    /* Type des machines*/
    String typeMachine;

    /* Liste des logiciels utilisable dans la salle */
    String logiciel;

    /* Indique la présence d'imprimante dans la salle */
    String imprimante;

    String pourcentageOccupation;

    /* Liste des réservations */
    private ObservableList<Reservation> listReservation;

    /**
     * Constructeur pour initialiser une salle avec son identifiant,
     * son nom, sa capacité, et les informations concernant les équipements
     * et logiciels présents dans la salle.
     *
     * @param identifiant L'identifiant unique de la salle.
     * @param nom Le nom de la salle.
     * @param capacite La capacité d'accueil de la salle.
     * @param videoProj Indicateur de la présence d'un vidéo projecteur.
     * @param ecranXXL Indicateur de la présence d'un écran XXL.
     * @param nbMachine Le nombre de machines présentes dans la salle.
     * @param typeMachine Le type des machines présentes dans la salle.
     * @param logiciel La liste des logiciels utilisables dans la salle.
     * @param imprimante Indicateur de la présence d'une imprimante.
     */
    public Salle(String identifiant, String nom, String capacite,
                 String videoProj, String ecranXXL, String nbMachine,
                 String typeMachine, String logiciel, String imprimante) {
        this.identifiant = identifiant;
        this.nom = nom;
        this.capacite = capacite;
        this.ecranXXL = ecranXXL;
        this.typeMachine = typeMachine;
        this.videoProj = videoProj;
        this.nbMachine = nbMachine;
        this.logiciel = logiciel;
        this.imprimante = imprimante;
        this.listReservation = FXCollections.observableArrayList();
    }

    /**
     * Renvoie l'identifiant unique de la salle.
     *
     * @return L'identifiant de la salle.
     */
    public String getIdentifiant() {
        return identifiant;
    }

    /**
     * Renvoie le nom de la salle.
     *
     * @return Le nom de la salle.
     */
    public String getNom() {
        return nom;
    }

    /**
     * Renvoie la capacité d'accueil de la salle.
     *
     * @return La capacité de la salle.
     */
    public String getCapacite() {
        return capacite;
    }

    /**
     * Renvoie l'indicateur de la présence d'un vidéo projecteur dans la salle.
     *
     * @return La présence d'un vidéo projecteur.
     */
    public String getVideoProj() {
        return videoProj;
    }

    /**
     * Renvoie l'indicateur de la présence d'un écran XXL dans la salle.
     *
     * @return La présence d'un écran XXL.
     */
    public String getEcranXXL() {
        return ecranXXL;
    }

    /**
     * Renvoie le type des machines présentes dans la salle.
     *
     * @return Le type des machines.
     */
    public String getTypeMachine() {
        return typeMachine;
    }

    /**
     * Renvoie le nombre de machines présentes dans la salle.
     *
     * @return Le nombre de machines.
     */
    public String getNbMachine() {
        return nbMachine;
    }

    /**
     * Renvoie la liste des logiciels utilisables dans la salle.
     *
     * @return La liste des logiciels.
     */
    public String getLogiciel() {
        return logiciel;
    }

    /**
     * Renvoie l'indicateur de la présence d'une imprimante dans la salle.
     *
     * @return La présence d'une imprimante.
     */
    public String getImprimante() {
        return imprimante;
    }

    public ObservableList<Reservation> getReservations() {
        return listReservation;
    }

    //Obtenir le nom de l'employé qui a réservé la salle
    public String getNomEmploye() {
        return listReservation.stream()
                .map(Reservation::getEmployeR)
                .distinct()
                .reduce((a, b) -> a + ", " + b) // Combine les noms distincts
                .orElse("Aucun employé");
    }

    //Obtenir les types d'activités associées à la salle
    public String getTypesActivite() {
        return listReservation.stream()
                .map(Reservation::getActiviteR)
                .distinct()
                .reduce((a, b) -> a + ", " + b) // Combine les types distincts
                .orElse("Aucune activité");
    }

    //Obtenir les types d'activités associées à la salle
    public String getDateR() {
        return listReservation.stream()
                .map(Reservation::getDateR)
                .distinct()
                .reduce((a, b) -> a + ", " + b) // Combine les types distincts
                .orElse("Aucune activité");
    }

    //Obtenir les types d'activités associées à la salle
    public String getHeureDebutR() {
        return listReservation.stream()
                .map(Reservation::getHeureDebut)
                .distinct()
                .reduce((a, b) -> a + ", " + b) // Combine les types distincts
                .orElse("Aucune activité");
    }

    //Obtenir les types d'activités associées à la salle
    public String getHeureFinR() {
        return listReservation.stream()
                .map(Reservation::getHeureFin)
                .distinct()
                .reduce((a, b) -> a + ", " + b) // Combine les types distincts
                .orElse("Aucune activité");
    }

    public String getPourcentageOccupation() {
        return pourcentageOccupation;
    }

    /**
     * Modifie l'identifiant de la salle.
     *
     * @param identifiant Le nouvel identifiant à attribuer à la salle.
     */
    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    /**
     * Modifie le nom de la salle.
     *
     * @param nom Le nouveau nom à attribuer à la salle.
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Modifie la capacité d'accueil de la salle.
     *
     * @param capacite La nouvelle capacité à attribuer à la salle.
     */
    public void setCapacite(String capacite) {
        this.capacite = capacite;
    }

    /**
     * Modifie l'indicateur de la présence d'un vidéo projecteur dans la salle.
     *
     * @param videoProj Le nouvel indicateur de présence du vidéo projecteur.
     */
    public void setVideoProj(String videoProj) {
        this.videoProj = videoProj;
    }

    /**
     * Modifie l'indicateur de la présence d'un écran XXL dans la salle.
     *
     * @param ecranXXL Le nouvel indicateur de présence de l'écran XXL.
     */
    public void setEcranXXL(String ecranXXL) {
        this.ecranXXL = ecranXXL;
    }

    /**
     * Modifie le nombre de machines présentes dans la salle.
     *
     * @param nbMachine Le nouveau nombre de machines à attribuer à la salle.
     */
    public void setNbMachine(String nbMachine) {
        this.nbMachine = nbMachine;
    }

    /**
     * Modifie le type des machines présentes dans la salle.
     *
     * @param typeMachine Le nouveau type de machine à attribuer à la salle.
     */
    public void setTypeMachine(String typeMachine) {
        this.typeMachine = typeMachine;
    }

    /**
     * Modifie la liste des logiciels utilisables dans la salle.
     *
     * @param logiciel La nouvelle liste des logiciels à attribuer à la salle.
     */
    public void setLogiciel(String logiciel) {
        this.logiciel = logiciel;
    }

    /**
     * Modifie l'indicateur de la présence d'une imprimante dans la salle.
     *
     * @param imprimante Le nouvel indicateur de présence de l'imprimante.
     */
    public void setImprimante(String imprimante) {
        this.imprimante = imprimante;
    }

    public void setPourcentageOccupation(String pourcentageOccupation) {
        this.pourcentageOccupation = pourcentageOccupation;
    }
}