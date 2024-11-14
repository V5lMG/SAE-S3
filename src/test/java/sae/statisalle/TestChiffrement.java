/*
 * TestChiffrement.java               11/11/2024
 * IUT DE RODEZ,pas de copyrights
 */
package sae.statisalle;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
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
            1, 3, 5, 7, 11, 13, 17, 19,
            23, 29, 31, 37, 41, 43, 47,
            53, 59, 61, 67, 71, 73, 79,
            83, 89, 97,1223, 12907
    };

    /*Tableau composer uniquement d'entier non premier*/
    private int[] entierNonPremier = {
            0, 4, 6, 8, 9, 10, 12, 20,
            22, 25, 26, 28, 30, 40, 45,
            46, 48, 50, 52, 55, 58, 60,
            70, 90, 900, 1000, 10000
    };

    /*Resultat d'exponentiation modulaire*/
    private int[] resultatExpo = {
            11, 17, 6190
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

    public static boolean testChiffrementDechiffrementDonnees(String cheminFichier) {
        Fichier fichier = new Fichier(cheminFichier);
        List<String> contenuOriginal = fichier.contenuFichier();

        if (contenuOriginal.isEmpty()) {
            System.err.println("Erreur : Le fichier est vide ou introuvable.");
            return false;
        }

        // Générer une clé aléatoire pour le test
        String cle = Chiffrement.genererCleAleatoire(contenuOriginal);

        // Chiffrer le contenu avec la clé générée
        String contenuChiffre = Chiffrement.chiffrementDonnees(fichier, cle);

        // Créer un nouveau fichier temporaire avec le contenu chiffré
        Fichier fichierTemp = new Fichier("/Users/rodrigoxaviertaborda/Documents/SAE/temp.csv");
        fichierTemp.reecritureFichier(Arrays.asList(contenuChiffre.split("\n")));

        // Déchiffrer le contenu chiffré en utilisant la même clé
        String contenuDechiffre = Chiffrement.dechiffrementDonnees(fichierTemp, cle);

        // Comparer le contenu déchiffré avec le contenu original
        boolean resultat = contenuDechiffre.equals(String.join("\n", contenuOriginal));

        if (resultat) {
            System.out.println("Le chiffrement et le déchiffrement ont réussi. Le contenu est identique !");
        } else {
            System.err.println("Échec du test : le contenu déchiffré est différent du contenu original.");
        }

        return resultat;
    }


    @Test
    void estPremier() {
        for(int i = 0; i < entierPremier.length; i++){
            assertTrue(Chiffrement.estPremier(entierPremier[i]));
            assertFalse(Chiffrement.estPremier(entierNonPremier[i]));
        }
    }

    @Test
    void expoModulaire() {
        for (int i = 0; i < resultatExpo.length; i++) {
            assertEquals(resultatExpo[i], Chiffrement.expoModulaire(11, 13, entierPremier[i]));
        }
    }

    public static void main(String[] args) {
        // Chemin vers le fichier que vous voulez tester
        String cheminFichierTest = "/Users/rodrigoxaviertaborda/Documents/SAE/salles 26_08_24 13_40.csv";

        // Appel de la méthode de test pour vérifier le chiffrement et le déchiffrement
        boolean estReussi = testChiffrementDechiffrementDonnees(cheminFichierTest);

        // Afficher le résultat du test dans la console
        if (estReussi) {
            System.out.println("Test réussi : le contenu déchiffré est identique au contenu original.");
        } else {
            System.out.println("Test échoué : le contenu déchiffré diffère du contenu original.");
        }
        //testGenererCleAleatoire();
    }
}
