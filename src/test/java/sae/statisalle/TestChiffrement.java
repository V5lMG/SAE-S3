/*
 * TestChiffrement.java               11/11/2024
 * IUT DE RODEZ,pas de copyrights
 */
package sae.statisalle;

import java.util.ArrayList;
import java.util.List;
/**
 * CLasse qui teste chaque méthode de la classe Chiffrement
 */
public class TestChiffrement {

    public static void testGenererCleAleatoire() {
        Chiffrement.creerAlphabet();
        List<String> donnees = new ArrayList<>(); // Exemple de données
        donnees.add("Ceci est un exemple de données");
        String cle = Chiffrement.genererCleAleatoire(donnees);
        System.out.println("Clé générée : " + cle);
    }

//    public static void testDefTailleClef() {
//        String donnees = "Bonjour ahaha";  // Exemple de données
//        String cle = Chiffrement.genererCleAleatoire(donnees);
//        System.out.println("cle générer " + cle);
//        Chiffrement.defTailleClef(donnees,cle);
//        System.out.println("Clé adapter à la taille du texte : " + cle);
//    }

    public static void main(String[] args) {
        testGenererCleAleatoire();
        //testDefTailleClef();
    }
}
