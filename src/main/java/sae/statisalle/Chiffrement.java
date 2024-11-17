/*
 * Chiffrement.java               06/11/2024
 * IUT DE RODEZ,pas de copyrights
 */

package sae.statisalle;

import sae.statisalle.exception.ModuloNegatifException;

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
 * @author valentin.munier-genie
 */
public class Chiffrement {

    /* Nombre de caractere dans les données*/
    private static int tailleDonnees;

    /*Contient les caractères utilisés pour le chiffrement*/
    private static List<Character> alphabet;

    public static void main (String[] args) {
        String donnee = "test de chiffrement";
        String cle = genererCleAleatoire(donnee);
        System.out.println(cle);
        System.out.println("donné non crypté : " + donnee);
        String donneeCrypte = chiffrementDonnees(donnee, cle);
        System.out.println("donné crypté : " + donneeCrypte);
        String donneeDecrypte = dechiffrementDonnees(donneeCrypte, cle);
        System.out.println("donné décrypté : " + donneeDecrypte);
    }

    /**
     * Méthode de chiffrement des données,
     * en utilisant l'algorithme de Vigenère.
     * Pour cela, il faut utiliser une clé qui va nous permettre de chiffrer
     * et déchiffrer les données.
     * @param cle Cle de chiffrement que nous utiliserons pour chiffrer le
     *            message
     * @param donnees Données à crypter sous forme de String
     * @return Le message chiffré sous forme de String
     */
    public static String chiffrementDonnees(String donnees, String cle) {
        StringBuilder messChiffre = new StringBuilder();

        int codeDonnees,
                codeCle,
                codeCharChiffre;

        // Initialisation de l'alphabet (par exemple : alphabet complet A-Z + a-z + chiffres, etc.)
        creerAlphabet();

        // Ajuster la clé pour qu'elle ait la bonne longueur
        cle = defTailleClef(donnees, cle);

        // Parcours des caractères de la chaîne
        for (int i = 0; i < donnees.length(); i++) {
            char caractere = donnees.charAt(i);

            codeDonnees = alphabet.indexOf(caractere);
            codeCle = alphabet.indexOf(cle.charAt(i % cle.length()));

            if (codeDonnees != -1 && codeCle != -1) {
                codeCharChiffre = (codeDonnees + codeCle) % alphabet.size();
                messChiffre.append(alphabet.get(codeCharChiffre));
            } else {
                // Conserve le caractère si non trouvé dans l'alphabet
                messChiffre.append(caractere);
            }
        }

        return messChiffre.toString();
    }

    /**
     * Méthode de déchiffrement des données,
     * en utilisant l'algorithme de Vigenère.
     * Cette méthode utilise la même clé que pour le chiffrement afin de décrypter
     * les données d'origine.
     * @param donnees Données à décrypter sous forme de String
     * @param cle Clé de déchiffrement que nous utiliserons pour déchiffrer le message
     * @return Le message déchiffré sous forme de String
     */
    public static String dechiffrementDonnees(String donnees, String cle) {
        StringBuilder messDechiffre = new StringBuilder();

        int codeDonnees,
                codeCle,
                codeCharDechiffre;

        // Initialisation de l'alphabet
        creerAlphabet();

        // Ajuster la clé pour qu'elle ait la bonne longueur
        cle = defTailleClef(donnees, cle);

        // Parcours des caractères de la chaîne
        for (int i = 0; i < donnees.length(); i++) {
            char caractere = donnees.charAt(i);

            codeDonnees = alphabet.indexOf(caractere);
            codeCle = alphabet.indexOf(cle.charAt(i % cle.length()));

            if (codeDonnees != -1 && codeCle != -1) {
                // Calculer l'index du caractère d'origine en utilisant la soustraction
                codeCharDechiffre = (codeDonnees - codeCle + alphabet.size()) % alphabet.size();
                messDechiffre.append(alphabet.get(codeCharDechiffre));
            } else {
                // Conserve le caractère si non trouvé dans l'alphabet
                messDechiffre.append(caractere);
            }
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

        // Ajoute les numéros de 0 à 9
        char[] numeros = {
                '0','1', '2', '3', '4', '5', '6', '7', '8', '9'
        };
        for (char n : numeros) {
            alphabet.add(n);
        }

        //Ajout de l'espace de l'aphabet.
        alphabet.add(' ');

    }

    /**
     * Adapte la taille de la clé pour qu'elle corresponde à la taille du message entier.
     * @param donnees Les données sous forme de chaîne de caractères
     * @param cle La clé initiale
     * @return La clé ajustée à la taille des données
     */
    public static String defTailleClef(String donnees, String cle) {
        int tailleDonnees = donnees.length(); // Taille totale des données
        StringBuilder cleAjustee = new StringBuilder();

        // Répéter la clé jusqu'à atteindre la taille des données
        while (cleAjustee.length() < tailleDonnees) {
            cleAjustee.append(cle);
        }

        // Tronquer la clé à la taille exacte nécessaire
        return cleAjustee.substring(0, tailleDonnees);
    }

    /**
     * Génère une clé aléatoire de taille aléatoire entre 1 et la longueur des données.
     * @param donnees Les données sous forme de chaîne de caractères
     * @return La clé générée sous forme de String.
     */
    public static String genererCleAleatoire(String donnees) {
        if (alphabet == null || alphabet.isEmpty()) {
            creerAlphabet();  // Initialiser l'alphabet si ce n'est pas déjà fait
        }

        int tailleDonnees = donnees.length();  // Longueur totale des données

        if (tailleDonnees == 0) {
            throw new IllegalArgumentException("Les données sont vides. Impossible de générer une clé.");
        } else {
            Random random = new Random();
            int longueurCle = random.nextInt(tailleDonnees) + 1;  // Taille de la clé entre 1 et tailleDonnees
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
     * Calcule l'inverse modulaire de `a` modulo `m`, c'est-à-dire le nombre `x` tel que
     * (a * x) % m == 1.
     * Cette méthode utilise une recherche exhaustive pour trouver l'inverse modulaire de `a` mod `m`.
     * Si l'inverse n'existe, une exception `IllegalArgumentException` est levée.
     *
     * @param a Le nombre dont on veut calculer l'inverse modulaire. Doit être un entier positif.
     * @param m Le modulo. Doit être un entier positif.
     * @return L'inverse modulaire de `a` modulo `m`.
     * @throws IllegalArgumentException Si l'inverse modulaire de `a` modulo `m` n'existe pas.
     */
    public static int modInverse(int a, int m) {
        int m0 = m, t, q;
        int x0 = 0, x1 = 1;

        if (m == 1) {
            return 0;
        }

        while (a > 1) {
            q = a / m;
            t = m;
            m = a % m;
            a = t;
            t = x0;
            x0 = x1 - q * x0;
            x1 = t;
        }

        if (x1 < 0) {
            x1 += m0;
        }

        return x1;
    }

    /**
     * Calcule l'exponentiation modulaire : a^exposant mod modulo.
     * Si l'exposant est négatif, cette méthode calcule d'abord l'inverse modulaire de la base `a`,
     * puis élève cet inverse à la valeur absolue de l'exposant.
     *
     * @param base La base de l'exponentiation. Ce peut être un nombre positif ou négatif.
     * @param exposant L'exposant. Peut être positif ou négatif.
     * @param modulo Le module. Doit être un entier positif.
     * @return Le résultat de a^exposant mod modulo.
     * @throws ModuloNegatifException Si le modulo est inférieur ou égal à 0.
     * @throws IllegalArgumentException Si l'inverse modulaire n'existe pas pour `a` et `modulo`.
     */
    public static int expoModulaire(int base, int exposant, int modulo) {
        // Vérifier si le modulo est valide
        if (modulo <= 0) {
            throw new ModuloNegatifException("Le modulo doit être un nombre positif.");
        }

        /*Vérification de la primalité du modulo*/
        if (!estPremier(modulo)) {
            throw new IllegalArgumentException("Le modulo 'm' doit être un nombre premier.");
        }

        //Il faut que la base soit compris entre [1, m-1]
        if (base <= 0 || base >= modulo) {
            throw new IllegalArgumentException("La base 'a' doit être un entier strictement positif.");
        }

        if (exposant < 0) {
            base = modInverse(base, modulo);
            exposant = -exposant;
        }

        int resultat = 1;

        while (exposant > 0) {
            if (exposant % 2 == 1) {
                resultat = (resultat * base) % modulo;
            }
            base = (base * base) % modulo;
            exposant = exposant / 2;
        }

        return resultat;
    }

    /**
     * Implémente l'échange de clés Diffie-Hellman pour générer une clé partagée
     * entre deux parties (par exemple, A et B).
     *
     *
     * <p>La méthode retourne la clé partagée calculée entre A et B après l'échange.</p>
     *
     * @param p Le nombre premier utilisé dans l'échange de clés.
     * @param g La base utilisée dans l'échange de clés.
     * @return La clé partagée calculée entre Alice et Bob.
     * @throws IllegalArgumentException Si p n'est pas un nombre premier,
     *                                  ou si g est supérieur ou égal à p.
     */
    public static int cleDiffieHellman(int p, int g){
        int clePubliqueA = 0;
        int cleSecretePartagee = 0;
        int a = 0; // à déterminer par l'utilisateur
        try{
            clePubliqueA = expoModulaire(g, a, p);
            //envoi de clePubliqueA via le réseau
            //recevoir b via le réseau
            int b = 0;
            cleSecretePartagee = expoModulaire(b, a, p);
        } catch (ModuloNegatifException e){
            System.out.println(e.getMessage());
        }
        return cleSecretePartagee;
    }
}