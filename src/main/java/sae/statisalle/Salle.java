/*
 * Salle.java               23/10/2024
 * IUT DE RODEZ               Pas de copyrights
 */
package sae.statisalle;
import java.util.ArrayList;

/**
 * La classe Salle initialise les objets de type Salle.
 * L'objet Salle est constitué de :
 * <ul>
 *     <li>Un identifiant</li>
 *     <li>Une capacité</li>
 *     <li>Un indicateur de la présence d'un video projecteur</li>
 *     <li>Un indicateur de la présence d'un écran XXL</li>
 *     <li>Le nombre de machine</li>
 *     <li>Une description</li>
 *     <li>La liste des logiciels</li>
 *     <li>Un indicateur de la présence d'une imprimante</li>
 * </ul>
 * <br>
 * Elle gère également toutes les erreurs relatives à l'instantiation
 * des salles en fonction des contenus des fichier
 * @author robin.montes
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

    public Salle(String identifiant, String nom, String capacite, String videoProj, String ecranXXL,
                 String nbMachine, String typeMachine, String logiciel, String imprimante) {
        this.identifiant = identifiant;
        this.nom = nom;
        this.capacite = capacite;
        this.ecranXXL = ecranXXL;
        this.typeMachine = typeMachine;
        this.videoProj = videoProj;
        this.nbMachine = nbMachine;
        this.logiciel = logiciel;
        this.imprimante = imprimante;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public String getNom() {
        return nom;
    }

    public String getCapacite() {
        return capacite;
    }

    public String getVideoProj() {
        return videoProj;
    }

    public String getEcranXXL() {
        return ecranXXL;
    }

    public String getTypeMachine() {
        return typeMachine;
    }

    public String getNbMachine() {
        return nbMachine;
    }

    public String getLogiciel() {
        return logiciel;
    }

    public String getImprimante() {
        return imprimante;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setCapacite(String capacite) {
        this.capacite = capacite;
    }

    public void setVideoProj(String videoProj) {
        this.videoProj = videoProj;
    }

    public void setEcranXXL(String ecranXXL) {
        this.ecranXXL = ecranXXL;
    }

    public void setNbMachine(String nbMachine) {
        this.nbMachine = nbMachine;
    }

    public void setTypeMachine(String typeMachine) {
        this.typeMachine = typeMachine;
    }

    public void setLogiciel(String logiciel) {
        this.logiciel = logiciel;
    }

    public void setImprimante(String imprimante) {
        this.imprimante = imprimante;
    }
}
