/*
 * Employe.java               23/10/2024
 * IUT DE RODEZ               Pas de copyrights
 */
package sae.statisalle;

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
 * @author robin.montes
 */
public class Employe {
    /*Nom de l'employé*/
    String nom;

    /*Prenom de l'employé*/
    String prenom;

    /*Numéro de téléphone de l'employé*/
    String numTel;

    public Employe(String nom, String prenom, String numTel) {
        this.nom = nom;
        this.prenom = prenom;
        this.numTel = numTel;
    }

    public String getNom() {
        return nom;
    }

    public String getNumTel() {
        return numTel;
    }

    public String getPrenom() {
        return prenom;
    }
}
