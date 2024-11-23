/*
 * Employe.java               23/10/2024
 * IUT DE RODEZ               Pas de copyrights
 */
package sae.statisalle.modele.objet;

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
