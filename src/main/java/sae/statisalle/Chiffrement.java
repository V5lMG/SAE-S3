/*
 * Chiffrement.java               06/11/2024
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

    /* Nombre de caractere dans les données*/
    private static int tailleDonnees;

    /**
     * Méthode de chiffrement des données,
     * en utilisant l'algorithme de Vigenère.
     * Pour cela, il faut utiliser une clé qui va nous permettre de chiffrer
     * et déchiffrer les données.
     * @param cle : Cle de chiffrement que nous utiliserons pour chiffre le
     *              message
     * @param fichier : Fichier qui contient les données à crypter
     */
    public static String chiffrementDonnees(Fichier fichier,
                                          String cle) {
        String messChiffre = "";
        char charDonnees,
                charCle;
        int codeDonnees,
                codeCle,
                codeCharChiffre;
        donnees = fichier.contenuFichier().toString();
        defTailleClef(donnees, cle);


        for(int i = 0; i < tailleDonnees; i++){
            charDonnees = donnees.charAt(i);
            charCle = cle.charAt(i);
            codeDonnees = charDonnees;
            codeCle = charCle;
            codeCharChiffre = (codeDonnees - codeCle);
            messChiffre = messChiffre += codeCharChiffre;
        }
        return messChiffre;
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
    public static void defTailleClef(String donnees, String cle){
        tailleDonnees = donnees.length();
        /*Nombre de caractere dans la clé adatper en fonction de la taille des données*/
        int tailleCle = cle.length();
        for(int i = 0; i < tailleDonnees - 1; i++){
            if(i == tailleCle - 1){
                i = 0;
            }
            cle += cle.charAt(i);
        }
    }

    /**
     * Générer une clé aléatoire de taille aléatoire
     */
    public static String genererCle(String donnees, int longueurMaxCle){
        //TODO
        longueurMaxCle = donnees.length();
        return ""; //STUB
    }
}
