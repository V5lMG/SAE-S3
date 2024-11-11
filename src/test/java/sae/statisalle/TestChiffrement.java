/*
 * TestChiffrement.java               11/11/2024
 * IUT DE RODEZ,pas de copyrights
 */
package sae.statisalle;

/**
 * CLasse qui teste chaque méthode de la classe Chiffrement
 */
public class TestChiffrement {

    public static void testGenererCleAleatoire() {
        String donnees = "Ceci est un exemple de données";  // Exemple de données
        String cle = Chiffrement.genererCleAleatoire(donnees);
        System.out.println("Clé générée : " + cle);
    }
    public static void main(String[] args) {
        testGenererCleAleatoire();
    }
}
