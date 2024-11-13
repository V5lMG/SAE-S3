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
 * contenu du fichier. Ensuite, nous utiliserons la méthode de Diffie-Hellman
 * @author Montes Robin
 * @author Xavier-Taborda Rodrigo
 * @author Cambon Mathias
 */
public class Chiffrement {

    /* Nombre de caractere dans les données*/
    private static int tailleDonnees;

    /*Contient les caractères utilisés pour le chiffrement*/
    private static List<Character> alphabet;


    /**
     * Méthode de chiffrement des données,
     * en utilisant l'algorithme de Vigenère.
     * Pour cela, il faut utiliser une clé qui va nous permettre de chiffrer
     * et déchiffrer les données.
     * @param cle Cle de chiffrement que nous utiliserons pour chiffre le
     *            message
     * @param fichier Fichier qui contient les données à crypter
     * @return Le message chiffré sous forme de String
     */
    public static String chiffrementDonnees(Fichier fichier, String cle) {
        StringBuilder messChiffre = new StringBuilder();

        int codeDonnees,
                codeCle,
                codeCharChiffre;

        // Initialisation de l'alphabet
        creerAlphabet();

        // Charger le contenu du fichier
        List<String> donnees = fichier.contenuFichier();
        tailleDonnees = donnees.size();

        // Générer une clé pour les tests uniquement
        cle = genererCleAleatoire(donnees); // uniquement pour les tests le temps d'avoir l'algo sur Diffie-Hellman

        // Ajuster la clé pour qu'elle ait la bonne longueur
        cle = defTailleClef(donnees, cle);

        for (String ligne : donnees) {
            for(int j = 0; j < ligne.length(); j++){
                codeDonnees = alphabet.indexOf(ligne.charAt(j));
                codeCle = alphabet.indexOf(cle.charAt(j % cle.length()));
                if (codeDonnees != -1 && codeCle != -1) {
                    codeCharChiffre = (codeDonnees + codeCle) % alphabet.size();
                    messChiffre.append(alphabet.get(codeCharChiffre));
                } else {
                    // Conserve le caractère si non trouvé
                    messChiffre.append(ligne.charAt(j));
                }
            }
            // Ajoute un retour à la ligne après chaque ligne de données chiffrée
            messChiffre.append("\n");
        }
        return messChiffre.toString();
    }

    /**
     * Méthode de déchiffrement des données,
     * en utilisant l'algorithme de Vigenère.
     * Cette méthode utilise la même clé que pour le chiffrement afin de décrypter
     * les données d'origine.
     * @param cle cle de déchiffrement que nous utiliserons pour déchiffrer le
     *            message
     * @param fichier fichier qui contient les données à décrypter
     * @return Le message déchiffré sous forme de String
     */
    public static String dechiffrementDonnees(Fichier fichier, String cle) {
        StringBuilder messDechiffre = new StringBuilder();

        int codeDonnees,
                codeCle,
                codeCharDechiffre;

        // Initialisation de l'alphabet
        creerAlphabet();

        // Charger le contenu du fichier
        List<String> donnees = fichier.contenuFichier();
        tailleDonnees = donnees.size();

        // Ajuster la clé pour qu'elle ait la bonne longueur
        cle = defTailleClef(donnees, cle);

        for (String ligne : donnees) {
            for (int j = 0; j < ligne.length(); j++) {
                codeDonnees = alphabet.indexOf(ligne.charAt(j));
                codeCle = alphabet.indexOf(cle.charAt(j % cle.length()));

                if (codeDonnees != -1 && codeCle != -1) {
                    // Calculer l'index du caractère d'origine en utilisant la soustraction
                    codeCharDechiffre = (codeDonnees - codeCle + alphabet.size()) % alphabet.size();
                    messDechiffre.append(alphabet.get(codeCharDechiffre));
                } else {
                    // Conserve le caractère si non trouvé
                    messDechiffre.append(ligne.charAt(j));
                }
            }
            // Ajoute un retour à la ligne après chaque ligne de données déchiffrée
            messDechiffre.append("\n");
        }
        return messDechiffre.toString();
    }

    /**
     * Création de notre alphabet avec l'ensemble des caractères que l'on peut
     * retrouve à l'intérieur des fichiers csv
     */
    public static void creerAlphabet() {
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

    }

    /**
     * Adapte la taille de la clé pour qu'elle corresponde à la taille du message entier.
     * @param donnees Les données présentes dans le fichier
     * @param cle La clé initiale
     * @return La clé ajustée à la taille des données
     */
    public static String defTailleClef(List<String> donnees, String cle) {
        tailleDonnees = 0;
        for (String ligne : donnees) {
            tailleDonnees += ligne.length();  // Ajoute la longueur de chaque ligne
        }

        StringBuilder cleAjustee = new StringBuilder();

        while (cleAjustee.length() < tailleDonnees) {
            cleAjustee.append(cle);
        }

        // Tronquer la clé à la taille exacte nécessaire
        return cleAjustee.substring(0, tailleDonnees);
    }

    /**
     * Génère une clé aléatoire de taille aléatoire entre 1 et la longueur des données.
     * @return La clé générée sous forme de String.
     */
    public static String genererCleAleatoire(List<String> donnees) {
        tailleDonnees = 0;
        for (String ligne : donnees) {
            tailleDonnees += ligne.length();  // Ajoute la longueur de chaque ligne
        }

        if (tailleDonnees == 0) {
            throw new IllegalArgumentException("La liste des données est vide. Impossible de générer une clé.");
        } else {
            Random random = new Random();
            int longueurCle = random.nextInt(tailleDonnees) + 1;
            StringBuilder cleBuilder = new StringBuilder();

            for (int i = 0; i < longueurCle; i++) {
                char lettre = alphabet.get(random.nextInt(alphabet.size())); // Choisir une lettre aléatoire dans l'alphabet
                cleBuilder.append(lettre);
            }
            return cleBuilder.toString();
        }
    }

    // ---------------------Diffie-Helmann--------------------------------------

    /**
     * Methode permettant de tester si un nombre est premier ou non
     * Pour Diffie-Hellman
     */
    public static boolean estPremier(int p){
        if(p == 0){
            return false;
        }
        for(int i = 2; i < p; i++){
            if(p % i == 0){
                return false;
            }
        }
        return true;
    }

    /**
     * Méthode effectuant le calcule d'une exponentiation modulaire en fonction
     *
     * @param a c'est la base sur laquelle il faut appliquer l'exposant
     * @param exposant exposant du calcul
     * @param modulo cela représente la clé publique partagée entre les deux
     *               utilisateurs autrement dit (p).
     * @return le resultat de l'exponentielle modulaire.
     */
    public static int expoModulaire(int a, int exposant, int modulo){
        int resultat = 1;
        while(exposant > 0){
            if(exposant % 2 == 1){
                resultat = (resultat * a) % modulo;
            }
            a = (a * a) % modulo;
            exposant = exposant / 2;
        }
        return resultat;
    }

    /**
     * Création de la clé à partir de la méthode de Diffie-Hellman
     */
    public static String cleDiffieHellman(int p, int b){
        
        return ""; //stub
    }


}