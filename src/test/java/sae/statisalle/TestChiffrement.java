/*
 * TestChiffrement.java               11/11/2024
 * IUT DE RODEZ,pas de copyrights
 */
package sae.statisalle;

import org.junit.jupiter.api.Test;
import sae.statisalle.exception.ModuloNegatifException;
import sae.statisalle.modele.Chiffrement;


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
        Chiffrement.creerAlphabet();

        String donnees = "Exemple de texte à chiffrer.";
        String cle = Chiffrement.genererCleAleatoire(donnees);

        // Vérification que la clé générée n'est pas nulle
        assertNotNull(cle, "La clé générée ne doit pas être null.");

        // Vérification que la longueur de la clé est valide
        assertTrue(cle.length() > 0 && cle.length() <= donnees.length(),
                "La clé générée doit avoir une longueur comprise entre 1 et la longueur des données.");

        // Vérification que la clé est composée uniquement de caractères de l'alphabet
        for (char c : cle.toCharArray()) {
            assertTrue(Chiffrement.alphabet.contains(c),
                    "La clé doit contenir uniquement des caractères présents dans l'alphabet.");
        }
    }

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
    /*
    public static boolean testChiffrementDechiffrementDonnees(String cheminFichier) {
        Fichier fichier = new Fichier(cheminFichier);
        List<String> contenuOriginal = fichier.contenuFichier();

        if (contenuOriginal.isEmpty()) {
            System.err.println("Erreur : Le fichier est vide ou introuvable.");
            return false;
        }

        // Affiche le contenu original
        System.out.println("Contenu original :");
        System.out.println(String.join("\n", contenuOriginal));

        // Générer une clé aléatoire pour le test
        String cle = Chiffrement.genererCleAleatoire(contenuOriginal);
        System.out.println("Clé générée : " + cle);

        // Chiffrer le contenu avec la clé générée
        String contenuChiffre = Chiffrement.chiffrementDonnees(fichier, cle);
        System.out.println("Contenu chiffré :");
        System.out.println(contenuChiffre);


        // Créer un nouveau fichier temporaire avec le contenu chiffré
        Fichier fichierTemp = new Fichier("C:\\Users\\mathi\\Documents\\BUTInfo2emeAnnee\\temp.csv");
        fichierTemp.reecritureFichier(Arrays.asList(contenuChiffre.split("\n")));

        // Déchiffrer le contenu chiffré en utilisant la même clé
        String contenuDechiffre = Chiffrement.dechiffrementDonnees(fichierTemp, cle);
        System.out.println("Contenu déchiffré :");
        System.out.println(contenuDechiffre);

        // Comparer le contenu déchiffré avec le contenu original
        String contenuOriginalNettoye = String.join("\n", contenuOriginal).replaceAll("\r\n", "").trim();
        String contenuDechiffreNettoye = contenuDechiffre.replaceAll("\r\n", "").trim();
        boolean resultat = contenuDechiffreNettoye.equals(contenuOriginalNettoye);

        if (resultat) {
            System.out.println("Le chiffrement et le déchiffrement ont réussi. Le contenu est identique !");
        } else {
            System.err.println("Échec du test : le contenu déchiffré est différent du contenu original.");
        }

        return resultat;

    }
    */

    @Test
    void estPremierValide() {
        for (int j : entierPremier) {
            assertTrue(Chiffrement.estPremier(j));
        }
    }

    @Test
    void estPremierNonValide() {
        for(int i = 0; i < entierPremier.length; i++){
            assertFalse(Chiffrement.estPremier(entierNonPremier[i]));
        }
    }

    /* Test des cas valides pour expoModulaire */
    @Test
    void expoModulaireValide() {
        assertEquals(11, Chiffrement.expoModulaire(11, 13, 19), "Le calcul doit retourner 11");
        assertEquals(11, Chiffrement.expoModulaire(67, 88, 83), "Le calcul doit retourner 11");
        assertEquals(284, Chiffrement.expoModulaire(999, 1290, 1021), "Le calcul doit retourner 284");
        assertEquals(1, Chiffrement.expoModulaire(2, -3, 7), "Le calcul doit retourner 1");
        assertEquals(5, Chiffrement.expoModulaire(3, -2, 11), "Le calcul doit retourner 5");
    }


    /* Test des cas invalides pour modulo négatif */
    @Test
    void expoModulaireModuloNegatifException() {
        assertThrows(ModuloNegatifException.class, () -> Chiffrement.expoModulaire(4, 13, -2),
                "Un modulo négatif doit lever ModuloNegatifException");
        assertThrows(ModuloNegatifException.class, () -> Chiffrement.expoModulaire(67, 88, -1000),
                "Un modulo négatif doit lever ModuloNegatifException");
        assertThrows(ModuloNegatifException.class, () -> Chiffrement.expoModulaire(6190, 1290, -1021),
                "Un modulo négatif doit lever ModuloNegatifException");
        assertThrows(ModuloNegatifException.class, () -> Chiffrement.expoModulaire(-1234, -73, -7),
                "Un modulo négatif doit lever ModuloNegatifException");
    }

    /* Test des cas invalides pour IllegalArgumentException (a ou modulo non valides) */
    @Test
    void expoModulaireIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> Chiffrement.expoModulaire(-4, 13, 2),
                "Un 'a' non valide doit lever IllegalArgumentException");
        assertThrows(IllegalArgumentException.class, () -> Chiffrement.expoModulaire(0, 88, 5),
                "Un 'a' non valide doit lever IllegalArgumentException");
        assertThrows(IllegalArgumentException.class, () -> Chiffrement.expoModulaire(1, 13, 6),
                "Un modulo pas premier doit lever IllegalArgumentException");
        assertThrows(IllegalArgumentException.class, () -> Chiffrement.expoModulaire(67, 88, 5000),
                "Un modulo pas premier doit lever IllegalArgumentException");
    }

    public static void main(String[] args) {
       /* // Chemin vers le fichier que vous voulez tester
        String cheminFichierTest = "C:\\Users\\mathi\\Documents\\BUTInfo2emeAnnee\\salles 26_08_24 13_40.csv";

        // Appel de la méthode de test pour vérifier le chiffrement et le déchiffrement
        boolean estReussi = testChiffrementDechiffrementDonnees(cheminFichierTest);

        // Afficher le résultat du test dans la console
        if (estReussi) {
            System.out.println("Test réussi : le contenu déchiffré est identique au contenu original.");
        } else {
            System.out.println("Test échoué : le contenu déchiffré diffère du contenu original.");
        }
        // Remplacer par test unitaire JUnit pour terminer la classe TestChiffrement */
    }
}
