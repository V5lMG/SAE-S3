/*
 * TestUnitaireVigenere.java
 * Refonte complète des tests pour Vigenere.java
 */

package sae.statisalle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sae.statisalle.modele.Vigenere;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe qui teste chaque méthode de la classe Vigenere.
 * Couvre les cas normaux, limites, et erreurs potentielles.
 */
public class TestUnitaireVigenere {

    @BeforeEach
    public void setUp() {
        Vigenere.creerAlphabet();
    }

    /**
     * Test du chiffrement simple avec des données valides et une clé courte.
     */
    @Test
    public void testChiffrementDonneesSimple() {
        String donnees = "Bonjour123";
        BigInteger cle = new BigInteger("123");
        String resultatAttendu = "}n6~n-HÇ%4";

        String resultat = Vigenere.chiffrementDonnees(donnees, cle);
        assertEquals(resultatAttendu, resultat, "Le chiffrement simple doit correspondre au résultat attendu.");
    }

    /**
     * Test du chiffrement avec des caractères inconnus (qui ne font pas partie de l'alphabet).
     */
    @Test
    public void testChiffrementAvecCaractereInconnu() {
        String donnees = "Hello, World! 🚀";
        BigInteger cle = new BigInteger("456");
        String resultatAttendu = "Û7v4}GôAy&_nPÙ\uD83D\uDE80";

        String resultat = Vigenere.chiffrementDonnees(donnees, cle);
        assertEquals(resultatAttendu, resultat, "Les caractères inconnus doivent rester inchangés.");
    }

    /**
     * Test du déchiffrement simple avec une clé courte.
     */
    @Test
    public void testDechiffrementDonneesSimple() {
        String donneesChiffrees = "}n6~n-HÇ%4";
        BigInteger cle = new BigInteger("123");
        String resultatAttendu = "Bonjour123";

        String resultat = Vigenere.dechiffrementDonnees(donneesChiffrees, cle);
        assertEquals(resultatAttendu, resultat, "Le déchiffrement doit reproduire les données originales.");
    }

    /**
     * Test du déchiffrement avec des caractères inconnus.
     */
    @Test
    public void testDechiffrementAvecCaractereInconnu() {
        String donneesChiffrees = "Û7v4}GôAy&_nPÙ\uD83D\uDE80";
        BigInteger cle = new BigInteger("456");
        String resultatAttendu = "Hello, World! 🚀";

        String resultat = Vigenere.dechiffrementDonnees(donneesChiffrees, cle);
        assertEquals(resultatAttendu, resultat, "Les caractères inconnus doivent rester inchangés après déchiffrement.");
    }

    /**
     * Test du chiffrement et déchiffrement pour vérifier la cohérence.
     */
    @Test
    public void testChiffrementEtDechiffrement() {
        String donnees = "Exemple de texte avec Vigenere.";
        BigInteger cle = new BigInteger("987654");

        // Chiffrement
        String donneesChiffrees = Vigenere.chiffrementDonnees(donnees, cle);
        assertNotEquals(donnees, donneesChiffrees, "Les données chiffrées doivent être différentes des originales.");

        // Déchiffrement
        String donneesDechiffrees = Vigenere.dechiffrementDonnees(donneesChiffrees, cle);
        assertEquals(donnees, donneesDechiffrees, "Le déchiffrement doit reproduire les données originales.");
    }

    /**
     * Test pour ajuster la taille de la clé (avec BigInteger).
     */
    @Test
    public void testAjusterTailleClefBigInteger() {
        // Cas 1 : clé plus courte que les données
        String donnees = "Bonjour";
        BigInteger cle = new BigInteger("123");
        BigInteger cleAjustee = Vigenere.ajusterTailleClef(donnees, cle);
        assertEquals(new BigInteger("1231231"), cleAjustee, "La clé ajustée doit répéter '123'.");

        // Cas 2 : clé de la même taille que les données
        cle = new BigInteger("1234567");
        cleAjustee = Vigenere.ajusterTailleClef(donnees, cle);
        assertEquals(new BigInteger("1234567"), cleAjustee, "La clé ajustée doit rester identique si elle correspond déjà à la taille.");

        // Cas 3 : clé plus longue que les données
        cle = new BigInteger("123456789012");
        cleAjustee = Vigenere.ajusterTailleClef(donnees, cle);
        assertEquals(new BigInteger("1234567"), cleAjustee, "La clé ajustée doit être tronquée à la taille des données.");

        // Cas 4 : données vides
        donnees = "";
        cle = new BigInteger("123");
        cleAjustee = Vigenere.ajusterTailleClef(donnees, cle);
        assertEquals(BigInteger.ZERO, cleAjustee, "Si les données sont vides, la clé ajustée doit être 0.");
    }

    /**
     * Test de la création de l'alphabet pour s'assurer qu'il est bien formé.
     */
    @Test
    public void testCreerAlphabet() {
        Vigenere.creerAlphabet();

        for (char c = 'a'; c <= 'z'; c++) {
            assertTrue(Vigenere.alphabet.contains(c), "L'alphabet doit contenir la lettre : " + c);
            assertTrue(Vigenere.alphabet.contains(Character.toUpperCase(c)), "L'alphabet doit contenir la lettre : " + Character.toUpperCase(c));
        }

        char[] chiffres = "0123456789".toCharArray();
        for (char c : chiffres) {
            assertTrue(Vigenere.alphabet.contains(c), "L'alphabet doit contenir le chiffre : " + c);
        }

        char[] symboles = "!@#$%^&*()-=_+[]{}|;:'\",.<>?/\\~`".toCharArray();
        for (char c : symboles) {
            assertTrue(Vigenere.alphabet.contains(c), "L'alphabet doit contenir le symbole : " + c);
        }
    }
}
