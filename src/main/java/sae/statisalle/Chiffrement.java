/*
 * Chiffrement.java               06/11/2024
 * IUT DE RODEZ,pas de copyrights
 */

package sae.statisalle;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;

/**
 * La classe chiffrement gère les opérations de cryptage
 * sur des fichiers de type Comma Separated Values (csv). Elle permet de crypter
 * le contenu d'un fichier csv pour sécuriser les données à l'intérieur.
 * <br>
 * Les fichiers pris en charge doivent avoir une extension .csv ou .CSV.
 * Nous utiliserons le chiffrement avec la methode de Vignère pour chiffrer le
 * contenu du fichier. Ensuite, nous utiliserons la méthode de Diffie-Helman
 * @author Montes Robin
 * @author Xavier-Taborda Rodrigo
 * @author Cambon Mathias
 */
public class Chiffrement {

    private static List<String> donnees;

    private static String cle = null;

    /* Nombre de caractere dans les données*/
    private static int tailleDonnees;

    /* Contient le total des caractères que nous utilisons pour le chiffrement*/
    private static List<Character> alphabet;

    /**
     * Méthode de chiffrement des données,
     * en utilisant l'algorithme de Vigenère.
     * Pour cela, il faut utiliser une clé qui va nous permettre de chiffrer
     * et déchiffrer les données.
     *
     * @param cle     : Cle de chiffrement que nous utiliserons pour chiffre le
     *                message
     * @param fichier : Fichier qui contient les données à crypter
     */
    public static String chiffrementDonnees(Fichier fichier,
                                            String cle) {
        String ligne = "";
        StringBuilder messChiffre = new StringBuilder();

        char charDonnees,
                charCle;

        int codeDonnees,
                codeCle,
                codeCharChiffre;

        creerAlphabet();
        donnees = fichier.contenuFichier();
        defTailleClef(donnees, cle);
        genererCleAleatoire(donnees);



        for (int i = 0; i < tailleDonnees - 1; i++) {
            ligne = donnees.get(i);
            for(int j = 0; j < ligne.length(); j++){
                charDonnees = ligne.charAt(j);
                charCle = cle.charAt(j);
                codeDonnees = charDonnees;
                codeCle = charCle;
                codeCharChiffre = (codeDonnees + codeCle % 26);
                messChiffre = new StringBuilder(messChiffre.append(codeCharChiffre));
            }
        }
        return messChiffre.toString();
    }

    public static List<Character> creerAlphabet() {
        // Crée une nouvelle liste pour stocker les caractères
        alphabet = new ArrayList<>();

        // Ajoute les lettres de l'alphabet (minuscules et majuscules)
            for (char c = 'a'; c <= 'z'; c++) {
            alphabet.add(c);
            alphabet.add(Character.toUpperCase(c));
        }

        // Ajoute les caractères accentués courants
        char[] accents = {
                'à', 'â', 'ä', 'é', 'è', 'ê', 'ë', 'î', 'ï', 'ô', 'ö', 'ù', 'û', 'ü', 'ç', 'œ', 'æ'
        };
            for (char c : accents) {
            alphabet.add(c);
        }

        //Ajout de l'espace de l'aphabet.
        alphabet.add(' ');

        return alphabet;
    }

    /**
     * Méthode de déchiffrement des données,
     * en utilisant l'algorithme de Vigenère
     * Pour cela, il faut utiliser une clé qui va nous permettre de chiffrer
     * et déchiffrer les données.
     */
    public static void dechiffrementDonnees(Fichier fichier,
                                            String cle) {
    }

    /**
     * Adapte la taille de la clef de chiffrement en fonction de la taille du
     * message.
     *
     * @param donnees : les données présentes dans le fichier
     */
    public static String defTailleClef(List<String> donnees, String cle) {
        tailleDonnees = donnees.size();
        /*Nombre de caractères dans la clé en fonction de la taille des données*/
        int tailleCle = cle.length();
        StringBuilder cleBuilder = new StringBuilder(cle);
        for (int i = 0; i < tailleDonnees - 1; i++) {
            if (i == tailleCle - 1) {
                i = 0;
            }
            cleBuilder.append(cleBuilder.charAt(i));
        }
        cle = cleBuilder.toString();
        return cle;
    }

    /**
     * Génère une clé aléatoire de taille aléatoire entre 1 et la longueur des données.
     * @param donnees La chaîne de données qui détermine la longueur maximale de la clé.
     * @return La clé générée sous forme de String.
     */
    public static String genererCleAleatoire(List<String> donnees) {
        Random random = new Random();

        // le +1 garantit au moins une lettre
        int longueurCle = random.nextInt(donnees.size()) + 1;

        String cle = "";

        // Générer la clé aléatoire avec des lettres contenues dans la liste alphabet
        // jusqu'à atteindre la longueur souhaitée
        for (int i = 0; i < longueurCle; i++) {
            char lettre = (char) ('a' + random.nextInt(alphabet.size()));
            cle = cle + lettre;
        }
        return cle;
    }




    /**
    
    // 3. Chiffrement d'une ligne de texte
    public String chiffrerLigne(String ligne, String cle) {
        StringBuilder resultat = new StringBuilder();
        for (int i = 0; i < ligne.length(); i++) {
            char caractere = ligne.charAt(i);
            char cleChar = cle.charAt(i);

            int indexCaractere = ALPHABET.indexOf(caractere);
            int indexCle = ALPHABET.indexOf(cleChar);

            if (indexCaractere == -1 || indexCle == -1) {
                resultat.append(caractere); // Si le caractère n'est pas dans l'alphabet, on le laisse inchangé
            } else {
                int indexChiffre = (indexCaractere + indexCle) % ALPHABET.length();
                resultat.append(ALPHABET.charAt(indexChiffre));
            }
        }
        return resultat.toString();
    }

    // 5. Chiffrement du contenu complet d'un fichier
    public List<String> chiffrerContenu(List<String> contenu) {
        int longueurContenu = contenu.stream().mapToInt(String::length).sum(); // Calcul de la longueur totale du contenu
        String cleAjustee = ajusterCle(this.cle, longueurContenu); // Ajuste la clé
        List<String> contenuChiffre = new ArrayList<>();

        int indexCle = 0; // Pour parcourir la clé
        for (String ligne : contenu) {
            String cleLigne = cleAjustee.substring(indexCle, indexCle + ligne.length());
            contenuChiffre.add(chiffrerLigne(ligne, cleLigne));
            indexCle += ligne.length();
        }
        return contenuChiffre;
    }
     */
}
