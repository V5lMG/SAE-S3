/*
 * TestChiffrement.java               11/11/2024
 * IUT DE RODEZ,pas de copyrights
 */
package sae.statisalle;

import org.junit.jupiter.api.Test;
import sae.statisalle.exception.ModuloNegatifException;
import sae.statisalle.modele.Vigenere;
import sae.statisalle.modele.Fichier;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CLasse qui teste chaque méthode de la classe Chiffrement
 * @author Robin Montes
 * @author Mathias Cambon
 * @author rodrigoxaviertaborda
 */
public class TestUnitaireVigenere {

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

    /*Liste de a pour le calcul de l'exponentiation modulaire*/
    private int[] a = {
            4, 67, 6190, -1234
    };

    /*Liste d'exposant pour calculer l'exponentiation modulaire*/
    private int[] exposant = {
            13, 88, 1290, -73
    };

    /*Liste de modulo pour calculer l'exponentiation modulaire*/
    private int[] moduloValide = {
            3, 8, 1021, 7
    };

    /*Liste de modulo invalide pour calculer l'exponentiation modulaire*/
    private int[] moduloInvalide  = {
            -2, -1000, -1021, -7
    };

    /*Resultat d'exponentiation modulaire*/
    private int[] resultatExpo = {
            1, 1, 737, 5
    };

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


    // ---------------------Diffie-Helmann--------------------------------------


    @Test
    void estPremierValide() {
        for (int j : entierPremier) {
            assertTrue(Vigenere.estPremier(j));
        }
    }

    @Test
    void estPremierNonValide() {
        for(int i = 0; i < entierPremier.length; i++){
            assertFalse(Vigenere.estPremier(entierNonPremier[i]));
        }
    }

    /* Test des cas valides pour expoModulaire */
    @Test
    void expoModulaireValide() {
        assertEquals(11, Vigenere.expoModulaire(11, 13, 19), "Le calcul doit retourner 11");
        assertEquals(11, Vigenere.expoModulaire(67, 88, 83), "Le calcul doit retourner 11");
        assertEquals(284, Vigenere.expoModulaire(999, 1290, 1021), "Le calcul doit retourner 284");
        assertEquals(1, Vigenere.expoModulaire(2, -3, 7), "Le calcul doit retourner 1");
        assertEquals(5, Vigenere.expoModulaire(3, -2, 11), "Le calcul doit retourner 5");
    }


    /* Test des cas invalides pour modulo négatif */
    @Test
    void expoModulaireModuloNegatifException() {
        assertThrows(ModuloNegatifException.class, () -> Vigenere.expoModulaire(4, 13, -2),
                "Un modulo négatif doit lever ModuloNegatifException");
        assertThrows(ModuloNegatifException.class, () -> Vigenere.expoModulaire(67, 88, -1000),
                "Un modulo négatif doit lever ModuloNegatifException");
        assertThrows(ModuloNegatifException.class, () -> Vigenere.expoModulaire(6190, 1290, -1021),
                "Un modulo négatif doit lever ModuloNegatifException");
        assertThrows(ModuloNegatifException.class, () -> Vigenere.expoModulaire(-1234, -73, -7),
                "Un modulo négatif doit lever ModuloNegatifException");
    }

    /* Test des cas invalides pour IllegalArgumentException (a ou modulo non valides) */
    @Test
    void expoModulaireIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> Vigenere.expoModulaire(-4, 13, 2),
                "Un 'a' non valide doit lever IllegalArgumentException");
        assertThrows(IllegalArgumentException.class, () -> Vigenere.expoModulaire(0, 88, 5),
                "Un 'a' non valide doit lever IllegalArgumentException");
        assertThrows(IllegalArgumentException.class, () -> Vigenere.expoModulaire(1, 13, 6),
                "Un modulo pas premier doit lever IllegalArgumentException");
        assertThrows(IllegalArgumentException.class, () -> Vigenere.expoModulaire(67, 88, 5000),
                "Un modulo pas premier doit lever IllegalArgumentException");
    }

}
