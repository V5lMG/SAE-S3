/*
 * Vigenere.java               06/11/2024
 * IUT DE RODEZ,pas de copyrights
 */

package sae.statisalle.modele;

import sae.statisalle.exception.ModuloNegatifException;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;

/**
 * La classe Vigenere gère les opérations de cryptage
 * sur des fichiers de type Comma Separated Values (csv). Elle permet de crypter
 * le contenu d'un fichier csv pour sécuriser les données à l'intérieur.
 * <br>
 * Les fichiers pris en charge doivent avoir une extension .csv ou .CSV.
 * Nous utiliserons le chiffrement avec la methode de Vignère pour chiffrer le
 * contenu du fichier. Ensuite, nous utiliserons la méthode de Diffie-Hellman
 * @author robin.montes
 * @author rodrigo.xavier-taborda
 * @author mathias.cambon
 * @author valentin.munier-genie
 */
public class Chiffrement {

    /* Nombre de caractere dans les données*/
    private static int tailleDonnees;

    /*Contient les caractères utilisés pour le chiffrement*/
    public static List<Character> alphabet;

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
    public static String chiffrementDonnees(String donnees, int cle) {
        StringBuilder messChiffre = new StringBuilder();

        int codeDonnees,
                codeCle,
                codeCharChiffre;

        creerAlphabet();

        cle = defTailleClef(donnees, cle);

        for (int i = 0; i < donnees.length(); i++) {
            char caractere = donnees.charAt(i);

            codeDonnees = alphabet.indexOf(caractere);
            codeCle = cle % alphabet.size();

            if (codeDonnees != -1 && codeCle != -1) {
                codeCharChiffre = (codeDonnees + codeCle) % alphabet.size();
                messChiffre.append(alphabet.get(codeCharChiffre));
            } else {
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
    public static String dechiffrementDonnees(String donnees, int cle) {
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
            codeCle = cle % alphabet.size();

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

    /**
     * Adapte la taille de la clé pour qu'elle corresponde à la taille du message entier.
     * @param donnees Les données sous forme de chaîne de caractères
     * @param cle La clé initiale
     * @return La clé ajustée à la taille des données
     */
    public static int defTailleClef(String donnees, int cle) {
        int tailleDonnees = donnees.length();
        int cleAjustee = cle;

        // répéter la clé jusqu'à atteindre la taille des données
        while (cleAjustee < tailleDonnees) {
            cleAjustee += cle;
        }

        // tronquer la clé à la taille exacte nécessaire
        return cleAjustee;
    }

    /**
     * Génère une clé aléatoire de taille aléatoire entre 1 et la longueur des données.
     * @param donnees Les données sous forme de chaîne de caractères
     * @return La clé générée sous forme d'int.
     */
    public static int genererCleAleatoire(String donnees) {
        if (alphabet == null || alphabet.isEmpty()) {
            creerAlphabet();
        }

        int tailleDonnees = donnees.length();

        if (tailleDonnees == 0) {
            throw new IllegalArgumentException("Les données sont vides. Impossible de générer une clé.");
        } else {
            Random random = new Random();
            int longueurCle = random.nextInt(tailleDonnees) + 1;

            return random.nextInt(longueurCle);
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

    /**
     * Génère un générateur g pour le groupe multiplicatif (Z/pZ)*
     * @param p Le nombre premier utilisé dans l'échange de clés.
     * @return Un générateur g pour le groupe multiplicatif.
     * @throws IllegalArgumentException Si p n'est pas un nombre premier.
     */
    public static int genererGenerateur(int p) {
        if (!estPremier(p)) {
            throw new IllegalArgumentException("Le nombre 'p' doit être un nombre premier.");
        }

        // Cas trivial pour p = 2
        if (p == 2) {
            return 1; // Le seul élément du groupe est 1
        }

        // Tester les candidats pour g
        for (int g = 2; g < p; g++) {
            boolean estGenerateur = true;

            // Vérifier si g^k mod p produit des résultats distincts pour k dans [1, p-1]
            for (int k = 1; k < p - 1; k++) {
                int resultat = expoModulaire(g, k, p);
                if (resultat == 1 && k < (p - 1)) {
                    estGenerateur = false; // Si g^k mod p == 1 avant k = p-1, ce n'est pas un générateur
                    break;
                }
            }

            if (estGenerateur) {
                return g; // Un générateur valide est trouvé
            }
        }

        throw new RuntimeException("Aucun générateur valide trouvé.");
    }
}