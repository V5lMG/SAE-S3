/*
 * Fichier.java               06/11/2024
 * IUT DE RODEZ               Pas de copyrights
 */

package sae.statisalle;

// import java.util.ArrayList;

/**
 * La classe chiffrement gère les opérations de cryptage
 * sur des fichiers de type Comma Separated Values (csv). Elle permet de crypter
 * le contenu d'un fichier csv pour sécuriser les données à l'intérieur.
 * <br>
 * Les fichiers pris en charge doivent avoir une extension .csv ou .CSV.
 * Nous utiliserons le chiffrement avec la methode de Vignère pour chiffrer le
 * contenu du fichier. Ensuite, nous utiliserons la méthode de Diffie-Helman
 * @author Montes Robin
 * @author Xavier-Taborda Rodrigo
 */
public class Chiffrement {

    public static String donnees;

    /*La clef à partir de laquelle le chiffrement est effectué*/
    private static String cle = "BUTINFORMATIQUE";

    /**
     * Méthode de chiffrement des données,
     * en utilisant l'algorithme de Vigenère.
     * Pour cela, il faut utiliser une clé qui va nous permettre de chiffrer
     * et déchiffrer les données.
     * @param cle : Cle de chiffrement que nous utiliserons pour chiffre le
     *              message
     * @param fichier : Fichier qui contient les données à crypter
     */
    public static void chiffrementDonnees(Fichier fichier,
                                          String cle) {
        donnees = fichier.contenuFichier().toString();
        tailleClef(donnees);



    }

    /**
     * Méthode de déchiffrement des données,
     * en utilisant l'algorithme de Vigenère
     * Pour cela, il faut utiliser une clé qui va nous permettre de chiffrer
     * et déchiffrer les données.
     */
    public static void dechiffrementDonnees(Fichier fichier,
                                            String cle) {

    }

    /**
     * Adapte la taille de la clef de chiffrement en fonction de la taille du
     * message.
     * @param donnees : les données présentes dans le fichier
     */
    public static String tailleClef(String donnees){
        int tailleDonnees = donnees.length();
        int tailleCle = cle.length();
        for(int i = 0; i < tailleDonnees; i++){
            if(i == tailleCle - 1){
                i = 0;
                cle += cle.charAt(i);
            }
        }
        return cle;
    }

    /**
     * Générer une clé aléatoire de taille aléatoire
     */
    public static String genererCle(String donnees, int longueurMaxCle){
        longueurMaxCle = donnees.length();
        return cle;
    }
}
