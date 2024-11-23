package sae.statisalle.modele;

import sae.statisalle.exception.ModuloNegatifException;

import java.util.Random;

/**
 * Classe contenant les méthodes de Diffie-Hellman nécessaire à la
 * génération d'une clé de chiffrement sécurisé.
 */
public class DiffieHellman {

    public static int expoModulaire(int base, int exposant, int modulo) {
        // vérifier si le modulo est valide
        if (modulo <= 0) {
            throw new ModuloNegatifException("Le modulo doit être un nombre positif.");
        }

        // il faut que la base soit compris entre [1, m-1]
        if (base <= 0 || base >= modulo) {
            throw new IllegalArgumentException("La base 'a' doit être un entier strictement positif.");
        }

        if (exposant < 0) {
            base = modInverse(base, modulo); // todo TESTER
            exposant = -exposant;
        }

        int resultat = 1;
        base = base % modulo;
        while (exposant > 0) {
            if ((exposant & 1) == 1) {
                resultat = (resultat * base) % modulo;
            }
            exposant = exposant >> 1;
            base = (base * base) % modulo;
        }

        return resultat;
    }

    public static int modInverse(int a, int m) {
        int m0 = m, t, q;
        int x0 = 0, x1 = 1;
        while (a > 1) {
            q = a / m;
            t = m;
            m = a % m;
            a = t;
            t = x0;
            x0 = x1 - q * x0;
            x1 = t;
        }
        if (x1 < 0) x1 += m0;
        return x1;
    }

    public static boolean estPremier(int p){
        if (p == 0) {
            return false;
        }
        for (int i = 2; i < p; i++) {
            if (p % i == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Génère un nombre premier p dans une plage donnée.
     * @param min Le minimum de la plage.
     * @param max Le maximum de la plage.
     * @return Un nombre premier p dans la plage.
     */
    public static int genererEntierPremier(int min, int max) {
        Random random = new Random();

        // générer un nombre aléatoire entre min et max
        int p = min + random.nextInt(max - min + 1);

        // vérifier si ce nombre est premier
        while (!estPremier(p)) {
            p = min + random.nextInt(max - min + 1);  // réessayer avec un autre nombre
        }

        return p;
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
