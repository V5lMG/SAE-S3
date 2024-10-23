/*
 * Salle.java               23/10/2024
 * IUT DE RODEZ               Pas de copyrights
 */
package sae.statisalle;
import java.util.List;

/**
 * La classe Salle initialise les objets de type Salle.
 * Elle permet également de vérifier si une salle existe déjà
 * <br>
 * Elle gère également toutes les erreurs relatives à l'instantiation
 * des salles en fonction des contenues des fichier
 * @author robin.montes
 */
public class Salle {
    /*Identifiant de la salle*/
    String identifiant;

    /*Capacité de place de la salle*/
    int capacite;

    /*Indique la présence de vidéo projecteur dans la salle*/
    boolean videoProj;

    /*Indique la présence d'écran XXL dans la salle*/
    boolean ecranXXL;

    /*Nombre de machine présente dans la salle*/
    int nbMachine;

    /*Description de la salle */
    String description;

    /*Liste des logiciels utilisable dans la salle*/
    List<String> logiciel;

    /*Indique la présence d'imprimante dans la salle*/
    boolean imprimante;

    public Salle(String indentifiant, int capacite, boolean videoProj, boolean ecranXXL, int nbMachine, String description, boolean imprimante) {
        this.identifiant = indentifiant;
        this.capacite = capacite;
        this.videoProj = videoProj;
        this.ecranXXL = ecranXXL;
        this.nbMachine = nbMachine;
        this.description = description;
        this.imprimante = imprimante;
    }
}
