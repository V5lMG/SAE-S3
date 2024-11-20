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

    @Test
    public void testGenerateurAvecNombrePremierValide() {
        int p = 7; // 7 est un nombre premier
        int g = Vigenere.genererGenerateur(p);

        // Vérifie que g est un générateur pour le groupe (Z/pZ)*.
        assertTrue(estGenerateurValide(g, p), "Le générateur trouvé doit être valide.");
    }

    @Test
    public void testGenerateurAvecAutreNombrePremier() {
        int p = 11; // 11 est un nombre premier
        int g = Vigenere.genererGenerateur(p);

        // Vérifie que g est un générateur pour le groupe (Z/pZ)*.
        assertTrue(estGenerateurValide(g, p), "Le générateur trouvé doit être valide.");
    }

    @Test
    public void testGenerateurAvecPetitNombrePremier() {
        int p = 5; // 5 est un nombre premier
        int g = Vigenere.genererGenerateur(p);

        // Vérifie que g est un générateur pour le groupe (Z/pZ)*.
        assertTrue(estGenerateurValide(g, p), "Le générateur trouvé doit être valide.");
    }

    @Test
    public void testGenerateurAvecNombreNonPremier() {
        int p = 8; // 8 n'est pas un nombre premier
        assertThrows(IllegalArgumentException.class, () -> {
            Vigenere.genererGenerateur(p);
        }, "Un nombre non premier doit lancer une exception.");
    }

    @Test
    public void testGenerateurAvecPetitNombreNonPremier() {
        int p = 4; // 4 n'est pas un nombre premier
        assertThrows(IllegalArgumentException.class, () -> {
            Vigenere.genererGenerateur(p);
        }, "Un nombre non premier doit lancer une exception.");
    }

    @Test
    public void testGenerateurAvecNombrePremierLimite() {
        int p = 2; // 2 est un nombre premier, mais trivial pour ce groupe
        int g = Vigenere.genererGenerateur(p);

        // Vérifie que g est un générateur pour le groupe (Z/pZ)*.
        assertTrue(estGenerateurValide(g, p), "Le générateur trouvé doit être valide.");
    }

    @Test
    public void testAucunGenerateurTrouve() {
        int p = 13; // Cas limite où aucun générateur ne devrait manquer
        int g = Vigenere.genererGenerateur(p);

        assertTrue(estGenerateurValide(g, p), "Il doit toujours trouver un générateur valide pour un nombre premier.");
    }

    /**
     * Vérifie si g est un générateur valide pour le groupe (Z/pZ)*.
     */
    private boolean estGenerateurValide(int g, int p) {
        // Créer un ensemble pour vérifier l'unicité des valeurs g^k mod p
        Set<Integer> resultats = new HashSet<>();
        for (int k = 1; k < p; k++) {
            int valeur = Vigenere.expoModulaire(g, k, p);
            resultats.add(valeur);
        }
        // Un générateur valide doit produire p-1 valeurs distinctes
        return resultats.size() == (p - 1);
    }

}
