/*
 * TestUnitaireVigenere.java               11/11/2024
 * IUT DE RODEZ, pas de copyrights
 */
package sae.statisalle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sae.statisalle.exception.ModuloNegatifException;
import sae.statisalle.modele.Vigenere;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe qui teste chaque méthode de la classe Vigenere
 * @author Robin Montes
 * @author Mathias Cambon
 * @author rodrigoxaviertaborda
 * @author valentin.munier-genie
 */
public class TestUnitaireVigenere {

    /* Tableau composé uniquement d'entiers premiers */
    private int[] entierPremier = {
            1, 3, 5, 7, 11, 13, 17, 19,
            23, 29, 31, 37, 41, 43, 47,
            53, 59, 61, 67, 71, 73, 79,
            83, 89, 97, 1223, 12907
    };

    /* Tableau composé uniquement d'entiers non premiers */
    private int[] entierNonPremier = {
            0, 4, 6, 8, 9, 10, 12, 20,
            22, 25, 26, 28, 30, 40, 45,
            46, 48, 50, 52, 55, 58, 60,
            70, 90, 900, 1000, 10000
    };

    /* Liste de a pour le calcul de l'exponentiation modulaire */
    private int[] a = {
            4, 67, 6190, -1234
    };

    /* Liste d'exposant pour calculer l'exponentiation modulaire */
    private int[] exposant = {
            13, 88, 1290, -73
    };

    /* Liste de modulo valide pour calculer l'exponentiation modulaire */
    private int[] moduloValide = {
            3, 8, 1021, 7
    };

    /* Liste de modulo invalide pour calculer l'exponentiation modulaire */
    private int[] moduloInvalide  = {
            -2, -1000, -1021, -7
    };

    /* Résultat d'exponentiation modulaire */
    private int[] resultatExpo = {
            1, 1, 737, 5
    };

    @BeforeEach
    public void setUp() {
        Vigenere.creerAlphabet();
    }

    @Test
    public void testChiffrementDonneesSimple() {
        String donnees = "test numéro 1";
        String cle = "WÇ";

        // Résultat attendu calculé manuellement avec votre alphabet
        String resultatAttendu = "17Ç.v]3+(\"ËÙB";

        String resultat = Vigenere.chiffrementDonnees(donnees, cle);
        assertEquals(resultatAttendu, resultat, "Le chiffrement simple avec clé doit correspondre.");
    }

    @Test
    public void testChiffrementAvecCaractereInconnu() {
        String donnees = "HELLO ×";
        String cle = "w3E\"* w";

        // Résultat attendu : caractères inconnus conservés
        String resultatAttendu = "î#qGB.×";

        String resultat = Vigenere.chiffrementDonnees(donnees, cle);
        assertEquals(resultatAttendu, resultat, "Les caractères inconnus doivent rester inchangés.");
    }

    @Test
    void testDechiffrementDonneesAvecMessageValide() {
        // Données test
        String messageChiffre = "17Ç.v]3+(\"ËÙB";
        String cle = "WÇ"; // Exemple de clé
        String messageAttendu = "test numéro 1"; // Message attendu après déchiffrement

        // Appel de la méthode de déchiffrement
        String messageDechiffre = Vigenere.dechiffrementDonnees(messageChiffre, cle);

        // Assertion pour vérifier le résultat
        assertEquals(messageAttendu, messageDechiffre,
                "Le message déchiffré ne correspond pas au message attendu.");
    }

    @Test
    void testDechiffrementDonneesAvecCaracteresInconnu() {
        // Données test
        String messageChiffre = "î#qGB.×";
        String cle = "w3E\"* w";
        String messageAttendu = "HELLO ×"; // Message attendu après déchiffrement

        // Appel de la méthode de déchiffrement
        String messageDechiffre = Vigenere.dechiffrementDonnees(messageChiffre, cle);

        // Assertion pour vérifier le résultat
        assertEquals(messageAttendu, messageDechiffre,
                "Le message déchiffré ne correspond pas au message attendu.");
    }

    @Test
    void testGenererCleAleatoire() {
        // Initialisation de l'alphabet directement dans le test
        Vigenere.creerAlphabet();

        String donnees = "Exemple de texte à chiffrer.";
        String cle = Vigenere.genererCleAleatoire(donnees);

        // Vérification que la clé générée n'est pas nulle
        assertNotNull(cle, "La clé générée ne doit pas être null.");

        // Vérification que la longueur de la clé est valide
        assertTrue(cle.length() > 0 && cle.length() <= donnees.length(),
                "La clé générée doit avoir une longueur comprise entre 1 et la longueur des données.");

        // Vérification que la clé est composée uniquement de caractères de l'alphabet
        for (char c : cle.toCharArray()) {
            assertTrue(Vigenere.alphabet.contains(c),
                    "La clé doit contenir uniquement des caractères présents dans l'alphabet.");
        }
    }

    @Test
    public void testDefTailleClef() {
        // Cas 1 : Clé plus courte que les données
        String donnees = "Bonjour";
        String cle = "abc";
        String cleAjustee = Vigenere.defTailleClef(donnees, cle);
        assertEquals("abcabca", cleAjustee,
                "La clé ajustée doit répéter 'abc' pour correspondre à la taille des données.");

        // Cas 2 : Clé de la même taille que les données
        donnees = "Bonjour";
        cle = "abcdefg";
        cleAjustee = Vigenere.defTailleClef(donnees, cle);
        assertEquals("abcdefg", cleAjustee,
                "La clé ajustée doit rester identique si sa taille correspond déjà à celle des données.");

        // Cas 3 : Clé plus longue que les données
        donnees = "Bonjour";
        cle = "abcdefghijkl";
        cleAjustee = Vigenere.defTailleClef(donnees, cle);
        assertEquals("abcdefg", cleAjustee,
                "La clé ajustée doit être tronquée pour correspondre à la taille des données.");

        // Cas 4 : Données vides
        donnees = "";
        cle = "abc";
        cleAjustee = Vigenere.defTailleClef(donnees, cle);
        assertEquals("", cleAjustee,
                "Si les données sont vides, la clé ajustée doit être une chaîne vide.");
    }

    /**
     * Teste si l'alphabet créé contient les lettres minuscules et majuscules.
     * On pourrait faire de même pour les chiffres, lettres accentuées...
     * Mais on ne le fait pas pour éviter de surcharger la classe de test.
     */
    @Test
    void testCreerAlphabetContientMinusculesEtMajuscules() {
        Vigenere.creerAlphabet();
        List<Character> alphabet = Vigenere.alphabet;

        for (char c = 'a'; c <= 'z'; c++) {
            assertTrue(alphabet.contains(c), "L'alphabet doit contenir la lettre minuscule : " + c);
            assertTrue(alphabet.contains(Character.toUpperCase(c)), "L'alphabet doit contenir la lettre majuscule : " + Character.toUpperCase(c));
        }
    }
}