package sae.statisalle.modele;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * La classe Vigenere gère les opérations de cryptage et décryptage
 * utilisant uniquement l'algorithme de Vigenère.
 * @author valentin.munier-genie
 * @author mathias cambon
 * @author rodrigo xavier-taborda
 */
public class Vigenere {

    /**
     * Liste des caractères utilisés comme alphabet
     * pour le chiffrement/déchiffrement.
     * (public pour les tests)
     */
    public static List<Character> alphabet;

    /**
     * Chiffre une chaîne de données en utilisant une clé numérique.
     *
     * @param donnees la chaîne de données à chiffrer.
     * @param cle la clé utilisée pour le chiffrement,
     *            exprimée sous forme de BigInteger.
     * @return une chaîne représentant les données chiffrées.
     */
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
                BigInteger codeCharChiffre = codeDonneesBI.add(codeCle)
                                                          .mod(tailleAlphabet);
                messChiffre.append(alphabet.get(codeCharChiffre.intValue()));
            } else {
                messChiffre.append(caractere);
            }

            // décale la clé
            cleAjustee = cleAjustee.divide(BigInteger.TEN);
        }
        return messChiffre.toString();
    }

    /**
     * Déchiffre une chaîne de données en utilisant une clé de chiffrement.
     *
     * @param donnees la chaîne de données chiffrées à déchiffrer.
     * @param cle la clé de chiffrement utilisée pour décrypter les données,
     *            exprimée sous forme de BigInteger.
     * @return une chaîne représentant les données déchiffrées.
     */
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
                BigInteger codeCharDechiffre = codeDonneesBI.subtract(codeCle)
                                                          .mod(tailleAlphabet);

                // gestion des indices négatifs dans modulo
                if (codeCharDechiffre.compareTo(BigInteger.ZERO) < 0) {
                    codeCharDechiffre = codeCharDechiffre.add(tailleAlphabet);
                }

                messDechiffre.append(alphabet.get(
                                                codeCharDechiffre.intValue()));
            } else {
                messDechiffre.append(caractere);
            }

            // décale la clé
            cleAjustee = cleAjustee.divide(BigInteger.TEN);
        }
        return messDechiffre.toString();
    }

    /**
     * Création de l'alphabet contenant tous les caractères imprimables
     * disponibles sur un clavier standard.
     */
    public static void creerAlphabet() {

        // -----------
        // ATTENTION, ne pas MODIFIER l'aplphabet sous peine de casser
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

    /**
     * Ajuste la taille d'une clé en fonction de la longueur des données.
     *
     * @param donnees la chaîne de données à laquelle la clé doit être ajustée.
     * @param cle la clé d'origine, exprimée en tant que BigInteger.
     * @return une clé ajustée sous forme de BigInteger,
     *         ayant la même longueur que la chaîne donnees.
     *
     * @throws NumberFormatException si la clé ajustée contient des caractères
     *                               invalides pour une conversion en BigInt.
     */
    public static BigInteger ajusterTailleClef(String donnees, BigInteger cle) {
        // si les données ou la clé sont nulles ou vides, retourne 0
        if (donnees == null || donnees.isEmpty() || cle == null) {
            return BigInteger.ZERO;
        }

        String cleStr = cle.toString();
        StringBuilder cleAjustee = new StringBuilder();

        int longueurDonnees = donnees.length();
        int longueurCle = cleStr.length();

        // répète ou tronque la clé pour correspondre à la longueur des données
        while (cleAjustee.length() < longueurDonnees) {
            cleAjustee.append(cleStr);
        }

        // tronque la clé à la longueur exacte des données
        cleAjustee.setLength(longueurDonnees);

        return new BigInteger(cleAjustee.toString());
    }

}

