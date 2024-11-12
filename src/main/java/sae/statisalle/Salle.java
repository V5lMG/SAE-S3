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
    /*Identifiant de la salle*/
    String identifiant;

    /*Nom de la salle*/
    String nom;

    /*Capacité de place de la salle*/
    int capacite;

    /*Indique la présence de vidéo projecteur dans la salle*/
    boolean videoProj;

    /*Indique la présence d'écran XXL dans la salle*/
    boolean ecranXXL;

    /*Nombre de machine présente dans la salle*/
    int nbMachine;

    /*Type des machines*/
    String typeMachine;

    /*Liste des logiciels utilisable dans la salle*/
    ArrayList<String> logiciel;

    /*Indique la présence d'imprimante dans la salle*/
    boolean imprimante;

    public Salle(String identifiant, String nom, int capacite, boolean ecranXXL, String typeMachine, boolean videoProj, int nbMachine, ArrayList<String> logiciel, boolean imprimante) {
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

    public int getCapacite() {
        return capacite;
    }

    public boolean isVideoProj() {
        return videoProj;
    }

    public boolean isEcranXXL() {
        return ecranXXL;
    }

    public int getNbMachine() {
        return nbMachine;
    }

    public String getTypeMachine() {
        return typeMachine;
    }

    public ArrayList<String> getLogiciel() {
        return logiciel;
    }

    public boolean isImprimante() {
        return imprimante;
    }
}
