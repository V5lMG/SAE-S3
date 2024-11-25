/*
 * TestUnitaireDiffieHellman.java             22/11/2024
 * Pas de droit d'auteur ni de copyright
 */
package sae.statisalle;

import org.junit.jupiter.api.Test;
import sae.statisalle.exception.ModuloNegatifException;
import sae.statisalle.modele.DiffieHellman;

import static org.junit.jupiter.api.Assertions.*;

public class TestUnitaireDiffieHellman {

    /**
     * Test de la méthode expoModulaire.
     * Vérifie que l'exponentiation modulaire fonctionne pour des cas de base.
     */
    @Test
    public void testExpoModulaire() {
        // Cas 1 : Test avec des valeurs classiques
        assertEquals(3, DiffieHellman.expoModulaire(2, 3, 5), "2^3 mod 5 doit être égal à 3");

        // Cas 2 : Test avec exposant négatif
        assertEquals(3, DiffieHellman.expoModulaire(2, -1, 5), "L'inverse de 2 mod 5 devrait donner 3");

        // Cas 3 : Test avec un exposant nul
        assertEquals(1, DiffieHellman.expoModulaire(2, 0, 5), "2^0 mod 5 doit être égal à 1");

        // Cas 4 : Test avec un modulo égal à 1
        assertEquals(0, DiffieHellman.expoModulaire(3, 5, 1), "Tout nombre mod 1 doit donner 0");

        // Cas 5 : Test avec une base invalide (supérieure ou égale au modulo)
        assertThrows(IllegalArgumentException.class, () -> {
            // Remplacer la base invalide (5) par une valeur correcte (par exemple 6)
            DiffieHellman.expoModulaire(6, 2, 5);
        }, "La base doit être dans l'intervalle [1, modulo-1]");

        // Cas 6 : Test avec un modulo négatif
        assertThrows(ModuloNegatifException.class, () -> {
            DiffieHellman.expoModulaire(2, 3, -5);
        }, "Le modulo doit être un nombre positif");
    }

    /**
     * TODO
     */
    @Test
    public void testExpoModulaireBase1() {
        assertEquals(1, DiffieHellman.expoModulaire(1, 10, 7), "1^10 mod 7 doit être égal à 1");
        assertEquals(1, DiffieHellman.expoModulaire(1, -5, 7), "1^(-5) mod 7 doit être égal à 1");
    }

    /**
     * TODO
     */
    @Test
    public void testModInversePasPremiers() {
        // Exemple avec a = 6 et m = 9 (ces deux nombres ne sont pas premiers entre eux)
        assertThrows(IllegalArgumentException.class, () -> {
            DiffieHellman.modInverse(6, 9);
        }, "L'inverse modulaire n'existe pas quand a et m ne sont pas premiers entre eux.");
    }

    // Cas 3 : Test inverse modulaire avec un petit exemple
    @Test
    public void testModInversePetit() {
        assertEquals(5, DiffieHellman.modInverse(3, 7), "L'inverse modulaire de 3 mod 7 doit être 5");
    }

    // Cas 4 : Test d'un nombre composé (pas premier)
    @Test
    public void testEstPremierComposé() {
        assertFalse(DiffieHellman.estPremier(9), "9 n'est pas un nombre premier");
        assertFalse(DiffieHellman.estPremier(15), "15 n'est pas un nombre premier");
    }

    // Cas 5 : Vérification avec des nombres premiers plus grands
    @Test
    public void testEstPremierGrand() {
        assertTrue(DiffieHellman.estPremier(104729), "104729 est un nombre premier");
        assertTrue(DiffieHellman.estPremier(100003), "100003 est un nombre premier");
    }

    // Cas 6 : Test générer un générateur avec un petit nombre premier (p = 2)
    @Test
    public void testGenererGenerateurPetit() {
        assertEquals(1, DiffieHellman.genererGenerateur(2), "Le générateur pour p=2 doit être 1");
    }

    // Cas 7 : Test générer un générateur pour un nombre non premier (devrait échouer)
    @Test
    public void testGenererGenerateurNonPremier() {
        assertThrows(IllegalArgumentException.class, () -> {
            DiffieHellman.genererGenerateur(4); // 4 n'est pas premier
        }, "Le générateur ne doit pas être calculé si p n'est pas un nombre premier");
    }

    // Cas 8 : Test générer un générateur avec un nombre premier plus grand (p = 23)
    @Test
    public void testGenererGenerateurGrand() {
        int p = 23;
        int g = DiffieHellman.genererGenerateur(p);
        assertTrue(g > 1 && g < p, "Le générateur g doit être dans l'intervalle [1, p-1]");
    }

    // Cas 9 : Test avec une base invalide pour expoModulaire
    @Test
    public void testExpoModulaireBaseInvalide() {
        assertThrows(IllegalArgumentException.class, () -> {
            DiffieHellman.expoModulaire(0, 3, 5);
        }, "La base doit être strictement supérieure à 1 et inférieure au modulo");
    }

    // Cas 10 : Test expoModulaire avec un exposant égal à 0
    @Test
    public void testExpoModulaireExposantZero() {
        assertEquals(1, DiffieHellman.expoModulaire(2, 0, 5), "Tout nombre élevé à 0 doit être 1");
    }

    // Cas 11 : Test expoModulaire avec un modulo égal à 1
    @Test
    public void testExpoModulaireModuloUn() {
        assertEquals(0, DiffieHellman.expoModulaire(3, 5, 1), "Tout nombre modulo 1 doit être 0");
    }

    // Cas 12 : Test de l'exponentiation modulaire avec un grand exposant
    @Test
    public void testExpoModulaireGrandExponent() {
        assertEquals(2, DiffieHellman.expoModulaire(2, 100, 7), "2^100 mod 7 doit être égal à 2");
    }

    // Cas 13 : Test expoModulaire pour une base et exposant négatif
    @Test
    public void testExpoModulaireBaseNegative() {
        assertEquals(5, DiffieHellman.expoModulaire(3, -1, 7), "3^(-1) mod 7 doit être égal à 5");
    }

    /**
     * Test de la méthode modInverse.
     * Vérifie que l'inverse modulaire est correctement calculé.
     */
    @Test
    public void testExpoModulaireExposantNegatif() {
        // Cas 1 : Test avec un exposant négatif et une base valide
        // 2^(-3) mod 5 => On doit d'abord calculer l'inverse modulaire de 2 mod 5 (qui est 3) et puis effectuer 3^3 mod 5
        assertEquals(2, DiffieHellman.expoModulaire(2, -3, 5), "2^(-3) mod 5 doit être égal à 2");

        // Cas 2 : Test avec un exposant négatif et une base valide
        // 3^(-1) mod 7 => L'inverse modulaire de 3 mod 7 est 5, donc le résultat attendu est 5
        assertEquals(5, DiffieHellman.expoModulaire(3, -1, 7), "3^(-1) mod 7 doit être égal à 5");

        // Cas 4 : Test avec une base dont l'inverse modulaire est difficile à calculer à la main
        // 5^(-2) mod 11 => On doit d'abord calculer l'inverse modulaire de 5 mod 11 (qui est 9) et ensuite calculer 9^2 mod 11
        assertEquals(4, DiffieHellman.expoModulaire(5, -2, 11), "5^(-2) mod 11 doit être égal à 4");

        // Cas 5 : Test avec un exposant 0 (cas de base)
        // 7^0 mod 11 => Tout nombre élevé à 0 est égal à 1, donc le résultat attendu est 1
        assertEquals(1, DiffieHellman.expoModulaire(7, 0, 11), "7^0 mod 11 doit être égal à 1");
    }


    /**
     * Test de la méthode estPremier.
     * Vérifie que la méthode détecte correctement les nombres premiers.
     */
    @Test
    public void testEstPremier() {
        // Cas 1 : Test avec un nombre premier
        assertTrue(DiffieHellman.estPremier(7), "7 est un nombre premier");

        // Cas 2 : Test avec un nombre non premier
        assertFalse(DiffieHellman.estPremier(8), "8 n'est pas un nombre premier");

        // Cas 3 : Test avec un nombre très grand
        assertTrue(DiffieHellman.estPremier(104729), "104729 est un nombre premier");

        // Cas 4 : Test avec 1, qui n'est pas un nombre premier
        assertFalse(DiffieHellman.estPremier(1), "1 n'est pas un nombre premier");
    }

    /**
     * Test de la méthode genererEntierPremier.
     * Vérifie que la génération de nombres premiers fonctionne dans une plage donnée.
     */
    @Test
    public void testGenererEntierPremier() {
        // Cas 1 : Plage normale
        int premier = DiffieHellman.genererEntierPremier(10, 50);
        assertTrue(DiffieHellman.estPremier(premier), "Le nombre généré doit être premier");

        // Cas 2 : Plage avec un nombre premier dans la plage
        premier = DiffieHellman.genererEntierPremier(100, 200);
        assertTrue(DiffieHellman.estPremier(premier), "Le nombre généré doit être premier");

        // Cas 3 : Cas avec une petite plage, générant un nombre premier
        premier = DiffieHellman.genererEntierPremier(1, 10);
        assertTrue(DiffieHellman.estPremier(premier), "Le nombre généré doit être premier");
    }

    /**
     * Test de la méthode genererGenerateur.
     * Vérifie que la génération d'un générateur pour un nombre premier est correcte.
     */
    @Test
    public void testGenererGenerateur() {
        // Cas 1 : Test pour un nombre premier simple
        int p = 7; // Nombre premier
        int g = DiffieHellman.genererGenerateur(p);
        assertTrue(DiffieHellman.estPremier(p), "p doit être premier");
        assertTrue(g > 1 && g < p, "Le générateur g doit être dans l'intervalle [1, p-1]");

        // Cas 2 : Test avec un nombre premier plus grand
        p = 23;
        g = DiffieHellman.genererGenerateur(p);
        assertTrue(DiffieHellman.estPremier(p), "p doit être premier");
        assertTrue(g > 1 && g < p, "Le générateur g doit être dans l'intervalle [1, p-1]");

        // Cas 3 : Test pour un nombre non premier (devrait échouer)
        assertThrows(IllegalArgumentException.class, () -> {
            DiffieHellman.genererGenerateur(15); // 15 n'est pas un nombre premier
        }, "Le générateur ne doit pas être calculé si p n'est pas un nombre premier");
    }
}
