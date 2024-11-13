/*
 * TestChiffrement.java               11/11/2024
 * IUT DE RODEZ,pas de copyrights
 */
package sae.statisalle;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

/**
 * CLasse qui teste chaque méthode de la classe Chiffrement
 * @author Robin Montes
 * @author Mathias Cambon
 * @author rodrigoxaviertaborda
 */
public class TestChiffrement {

    /*Tableau composer uniquement d'entier premier*/
    private int[] entierPremier = {
            2, 3, 5, 7, 11, 13, 17, 19,
            23, 29, 31, 37, 41, 43, 47,
            53, 59, 61, 67, 71, 73, 79,
            83, 89, 97,1223, 12907
    };

    /*Tableau composer uniquement d'entier non premier*/
    private int[] entierNonPremier = {
            1, 4, 6, 8, 9, 10, 12, 20,
            22, 25, 26, 28, 30, 40, 45,
            46, 48, 50, 52, 55, 58, 60,
            70, 90, 900, 1000, 10000
    };

    /*
    public static void testGenererCleAleatoire() {
        Chiffrement.creerAlphabet();
        List<String> donnees = new ArrayList<>(); // Exemple de données
        donnees.add("Ceci");
        donnees.add("Salut");
        String cle = Chiffrement.genererCleAleatoire(donnees);
        System.out.println("Clé générée : " + cle);
    }
    */
    /*
    public static void testDefTailleClef() {
       Chiffrement.creerAlphabet();
       List<String> donnees = new ArrayList<>(); // Exemple de données
       donnees.add("Ceci");
       donnees.add("Salut");
       String cle = Chiffrement.genererCleAleatoire(donnees);
       System.out.println("cle générer : " + cle);
       String cle2 = Chiffrement.defTailleClef(donnees,cle);
       System.out.println("Clé adapter à la taille du texte : " + cle2);
    }
    */

    public static void testChiffrementDonnees() {

        List<String> donnees = new ArrayList<>();

        donnees.add("coucou");
        donnees.add("mon bebs");

        Fichier fichier = new Fichier("test.csv");

        fichier.reecritureFichier(donnees);

        // Génère une clé aléatoire et ajuste sa longueur
        String cle = Chiffrement.genererCleAleatoire(donnees);
        cle = Chiffrement.defTailleClef(donnees, cle);

        // Chiffre les données du texte
        String resultatChiffre = Chiffrement.chiffrementDonnees(fichier, cle);

        Fichier fichierChiffre = new Fichier("temp_chiffre.csv");
        String resultatDechiffrer = Chiffrement.dechiffrementDonnees(fichier, cle);

        // Vérifie que le texte chiffré est différent du texte original
        if (!donnees.equals(resultatChiffre)) {
            System.out.println("Le chiffrement fonctionne correctement.");
        } else {
            System.out.println("Échec du chiffrement : le texte chiffré est identique au texte original.");
        }
    }


    @Test
    void estPremier() {
        for(int i = 0; i < entierPremier.length; i++){
            assertTrue(Chiffrement.estPremier(entierPremier[i]));
            assertFalse(Chiffrement.estPremier(entierNonPremier[i]));
        }
    }

    public static void main(String[] args) {
        testChiffrementDonnees();
        //testGenererCleAleatoire();
    }
}
