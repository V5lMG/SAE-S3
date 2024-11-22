package sae.statisalle.modele;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * La classe Vigenere gère les opérations de cryptage et décryptage
 * utilisant uniquement l'algorithme de Vigenère.
 */
public class Vigenere {

    private static List<Character> alphabet;

    public static String chiffrementDonnees(String donnees, BigInteger cle) {
        StringBuilder messChiffre = new StringBuilder();
        creerAlphabet();
        BigInteger cleAjustee = ajusterTailleClef(donnees, cle);
        BigInteger tailleAlphabet = BigInteger.valueOf(alphabet.size());

        for (int i = 0; i < donnees.length(); i++) {
            char caractere = donnees.charAt(i);
            int codeDonnees = alphabet.indexOf(caractere);

            if (codeDonnees != -1) {
                BigInteger codeCle = cleAjustee.mod(tailleAlphabet);
                BigInteger codeDonneesBI = BigInteger.valueOf(codeDonnees);
                BigInteger codeCharChiffre = codeDonneesBI.add(codeCle).mod(tailleAlphabet);
                messChiffre.append(alphabet.get(codeCharChiffre.intValue()));
            } else {
                messChiffre.append(caractere);
            }

            // Décale la clé
            cleAjustee = cleAjustee.divide(BigInteger.TEN);
        }
        return messChiffre.toString();
    }

    public static String dechiffrementDonnees(String donnees, BigInteger cle) {
        StringBuilder messDechiffre = new StringBuilder();
        creerAlphabet();
        BigInteger cleAjustee = ajusterTailleClef(donnees, cle);
        BigInteger tailleAlphabet = BigInteger.valueOf(alphabet.size());

        for (int i = 0; i < donnees.length(); i++) {
            char caractere = donnees.charAt(i);
            int codeDonnees = alphabet.indexOf(caractere);

            if (codeDonnees != -1) {
                BigInteger codeCle = cleAjustee.mod(tailleAlphabet);
                BigInteger codeDonneesBI = BigInteger.valueOf(codeDonnees);
                BigInteger codeCharDechiffre = codeDonneesBI.subtract(codeCle).mod(tailleAlphabet);

                // Gestion des indices négatifs dans modulo
                if (codeCharDechiffre.compareTo(BigInteger.ZERO) < 0) {
                    codeCharDechiffre = codeCharDechiffre.add(tailleAlphabet);
                }

                messDechiffre.append(alphabet.get(codeCharDechiffre.intValue()));
            } else {
                messDechiffre.append(caractere);
            }

            // Décale la clé
            cleAjustee = cleAjustee.divide(BigInteger.TEN);
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

    /**
     * Création de l'alphabet contenant tous les caractères imprimables
     * disponibles sur un clavier standard.
     */
    public static void creerAlphabet() {

        // -----------
        // ATTENTION, ne pas MODIFIER l'aplhpabet sous peine de casser
        //            tous les tests.
        // -----------

        alphabet = new ArrayList<>();

        // lettres minuscules et majuscules
        for (char c = 'a'; c <= 'z'; c++) {
            alphabet.add(c);
            alphabet.add(Character.toUpperCase(c));
        }

        // lettres accentuées courantes
        char[] accents = {
                'à', 'â', 'ä', 'é', 'è', 'ê', 'ë',
                'î', 'ï', 'ô', 'ö', 'ù', 'û', 'ü',
                'ç', 'À', 'Â', 'Ä', 'É', 'È', 'Ê',
                'Ë', 'Î', 'Ï', 'Ô', 'Ö', 'Ù', 'Û',
                'Ü', 'Ç'
        };
        for (char c : accents) {
            alphabet.add(c);
        }

        // chiffres
        for (char c = '0'; c <= '9'; c++) {
            alphabet.add(c);
        }

        // symboles courants et caractères de ponctuation
        char[] symboles = {
                '!', '@', '#', '$', '%', '^', '&',
                '*', '(', ')', '-', '_', '=', '+',
                '[', ']', '{', '}', '\\', '|', ':',
                ';', '\'', '"', '<', '>', ',', '.',
                '?', '/'
        };
        for (char c : symboles) {
            alphabet.add(c);
        }

        // espace et autres caractères spécifiques
        alphabet.add(' ');
        alphabet.add('`');
        alphabet.add('~');
    }

    public static BigInteger ajusterTailleClef(String donnees, BigInteger cle) {
        int tailleDonnees = donnees.length();
        String cleStr = String.valueOf(cle);
        StringBuilder cleAjustee = new StringBuilder();

        // Répéter la clé jusqu'à atteindre la taille des données
        while (cleAjustee.length() < tailleDonnees) {
            cleAjustee.append(cleStr);
        }

        // Tronquer la clé à la taille exacte nécessaire
        String cleFinale = cleAjustee.substring(0, tailleDonnees);

        // Convertir la clé ajustée en BigInteger
        return new BigInteger(cleFinale);
    }
}

