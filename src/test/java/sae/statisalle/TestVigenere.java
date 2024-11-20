/*
 * TestVigenere.java               11/11/2024
 * IUT DE RODEZ,pas de copyrights
 */
package sae.statisalle;

import sae.statisalle.modele.Chiffrement;
import sae.statisalle.modele.Fichier;

import java.util.Arrays;
import java.util.List;

/**
 * Classe qui teste chaque méthode de la classe Vigenere.
 * @author Robin Montes
 * @author Mathias Cambon
 * @author rodrigoxaviertaborda
 * @author valentin.munier-genie
 */
public class TestVigenere {

    /**
     * Teste les processus de chiffrement et de déchiffrement des données à l'aide
     * d'une clé générée aléatoirement pour un fichier donné.
     * La méthode lit le contenu du fichier, le chiffre avec une clé générée,
     * puis le déchiffre pour vérifier si le contenu déchiffré correspond
     * au contenu original.
     * @param cheminFichier le chemin du fichier à tester, qui contient les
     *                      données à chiffrer et à déchiffrer
     * @return {@code true} si le contenu déchiffré est identique au contenu
     *         original, {@code false} sinon.
     */
    public static boolean testChiffrementDechiffrementDonnees(String cheminFichier) {
        Fichier fichier = new Fichier(cheminFichier);
        List<String> contenuOriginalList = fichier.contenuFichier(); // Assurez-vous que cette méthode retourne le contenu du fichier en String
        String contenuOriginal = String.join("\n", contenuOriginalList);

        if (contenuOriginal.isEmpty()) {
            System.err.println("Erreur : Le fichier est vide ou introuvable.");
            return false;
        }

        // Affiche le contenu original
        System.out.println("Contenu original :");
        System.out.println(contenuOriginal);

        // Générer une clé aléatoire pour le test
        int cle = Chiffrement.genererCleAleatoire(contenuOriginal);
        System.out.println("Clé générée : " + cle);

        // Chiffrer le contenu avec la clé générée
        String contenuChiffre = Chiffrement.chiffrementDonnees(contenuOriginal, cle);
        System.out.println("Contenu chiffré :");
        System.out.println(contenuChiffre);

        // Créer un nouveau fichier temporaire avec le contenu chiffré
        Fichier fichierTemp = new Fichier("src/test/java/fichierTest/temp.csv");
        fichierTemp.reecritureFichier(Arrays.asList(contenuChiffre.split("\n")));

        // Déchiffrer le contenu chiffré en utilisant la même clé
        String contenuDechiffre = Chiffrement.dechiffrementDonnees(contenuChiffre, cle);
        System.out.println("Contenu déchiffré :");
        System.out.println(contenuDechiffre);

        // Comparer le contenu déchiffré avec le contenu original
        String contenuOriginalNettoye = contenuOriginal.replaceAll("\r\n", "").trim();
        String contenuDechiffreNettoye = contenuDechiffre.replaceAll("\r\n", "").trim();
        boolean resultat = contenuDechiffreNettoye.equals(contenuOriginalNettoye);

        if (resultat) {
            System.out.println("Le chiffrement et le déchiffrement ont réussi. Le contenu est identique !");
        } else {
            System.err.println("Échec du test : le contenu déchiffré est différent du contenu original.");
        }

        return resultat;
    }

    public static void main(String[] args) {
        // chemin du fichier de test
        String cheminFichierTest = "src/test/java/fichierTest/reservations.csv";

        // Appel de la méthode de test pour vérifier le chiffrement et le déchiffrement
        boolean estReussi = testChiffrementDechiffrementDonnees(cheminFichierTest);
        if (estReussi) {
            System.out.println("Test réussi : le contenu déchiffré est identique au contenu original.");
        } else {
            System.out.println("Test échoué : le contenu déchiffré diffère du contenu original.");
        }
    }
}
