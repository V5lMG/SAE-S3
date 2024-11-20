package sae.statisalle.modele;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * La classe Vigenere gère les opérations de cryptage et décryptage
 * utilisant uniquement l'algorithme de Vigenère.
 */
public class Vigenere {

    private static List<Character> alphabet;

    /**
     * Chiffrement des données avec l'algorithme de Vigenère.
     * @param donnees Données à chiffrer
     * @param cle Clé de chiffrement
     * @return Données chiffrées
     */
    public static String chiffrementDonnees(String donnees, int cle) {
        StringBuilder messChiffre = new StringBuilder();
        creerAlphabet();
        cle = ajusterTailleClef(donnees, cle);

        for (int i = 0; i < donnees.length(); i++) {
            char caractere = donnees.charAt(i);
            int codeDonnees = alphabet.indexOf(caractere);
            int codeCle = cle % alphabet.size();

            if (codeDonnees != -1) {
                int codeCharChiffre = (codeDonnees + codeCle) % alphabet.size();
                messChiffre.append(alphabet.get(codeCharChiffre));
            } else {
                messChiffre.append(caractere);
            }
        }

        return messChiffre.toString();
    }

    /**
     * Déchiffrement des données avec l'algorithme de Vigenère.
     * @param donnees Données à déchiffrer
     * @param cle Clé de déchiffrement
     * @return Données déchiffrées
     */
    public static String dechiffrementDonnees(String donnees, int cle) {
        StringBuilder messDechiffre = new StringBuilder();
        creerAlphabet();
        cle = ajusterTailleClef(donnees, cle);

        for (int i = 0; i < donnees.length(); i++) {
            char caractere = donnees.charAt(i);
            int codeDonnees = alphabet.indexOf(caractere);
            int codeCle = cle % alphabet.size();

            if (codeDonnees != -1) {
                int codeCharDechiffre = (codeDonnees - codeCle + alphabet.size()) % alphabet.size();
                messDechiffre.append(alphabet.get(codeCharDechiffre));
            } else {
                messDechiffre.append(caractere);
            }
        }

        return messDechiffre.toString();
    }

    public static int genererCleAleatoire(String donnees) {
        Random random = new Random();
        int tailleDonnees = donnees.length();
        if (tailleDonnees == 0) {
            throw new IllegalArgumentException("Les données sont vides.");
        }
        return random.nextInt(tailleDonnees) + 1;
    }

    private static void creerAlphabet() {
        if (alphabet != null) return;
        alphabet = new ArrayList<>();
        for (char c = 'a'; c <= 'z'; c++) alphabet.add(c);
        for (char c = 'A'; c <= 'Z'; c++) alphabet.add(c);
        for (char c : "àâäéèêëîïôöùûüçÀÂÄÉÈÊËÎÏÔÖÙÛÜÇ0123456789!@#$%^&*()-_=+[]{}\\|;:'\",.<>?/`~ ".toCharArray()) {
            alphabet.add(c);
        }
    }

    private static int ajusterTailleClef(String donnees, int cle) {
        int tailleDonnees = donnees.length();
        while (cle < tailleDonnees) {
            cle += cle;
        }
        return cle;
    }
}

