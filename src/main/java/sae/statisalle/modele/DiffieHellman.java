/*
 * DiffieHellman.java          30/10/2024
 * Pas de droits d'auteur ni de copyright
 */
package sae.statisalle.modele;

import sae.statisalle.exception.ModuloNegatifException;

import java.util.Random;

/**
 * La classe DiffieHellman contient les méthodes nécessaires pour
 * implémenter l'algorithme de Diffie-Hellman utilisé dans la génération
 * de clés de chiffrement sécurisées. Elle inclut des méthodes pour
 * l'exponentiation modulaire, la génération de nombres premiers et de
 * générateurs pour les groupes multiplicatifs.
 *
 * @author valentin.munier-genie
 * @author rodrigo xavier-taborda
 */
public class DiffieHellman {

    /**
     * Calcule l'exponentiation modulaire,
     * c'est-à-dire (base^exposant) % modulo,
     * de manière efficace en utilisant
     * l'algorithme d'exponentiation rapide.
     * <br>
     * Si l'exposant est négatif, l'inverse modulaire de la base sera utilisé.
     * Cette méthode est souvent utilisée en cryptographie (exemple RSA).
     *
     * @param base La base de l'exponentiation. Doit être un entier positif et
     *             strictement inférieur au modulo.
     * @param exposant L'exposant de l'exponentiation.
     *                 Peut être positif ou négatif.
     * @param modulo   Le modulo sous lequel l'opération est effectuée.
     *                 Doit être un entier strictement positif.
     * @return Le résultat de (base^exposant) % modulo.
     * @throws ModuloNegatifException si le modulo est négatif ou nul.
     * @throws IllegalArgumentException si la base est hors de
     *                                  l'intervalle [1, modulo - 1].
     */
    public static int expoModulaire(int base, int exposant, int modulo) {
        // vérifier si le modulo est valide
        if (modulo <= 0) {
            throw new ModuloNegatifException("Le modulo doit être un"
                                             + " nombre positif.");
        }

        if (modulo == 1) {
            return 0;
        }

        if (base == 1) {
            return 1;
        }

        // il faut que la base soit compris entre [1, m-1]
        if (base < 1 || base >= modulo) {
            throw new IllegalArgumentException("La base 'a' doit être un "
                                              + "entier strictement positif.");
        }

        // si l'exposant est négatif, on utilise l'inverse modulaire de la base
        if (exposant < 0) {
            base = modInverse(base, modulo);
            exposant = -exposant;
        }

        int resultat = 1;
        // s'assurer que la base est dans l'intervalle [0, modulo-1]
        base = base % modulo;

        while (exposant > 0) {
            if ((exposant & 1) == 1) {
                resultat = (resultat * base) % modulo; // si exposant impair
            }
            exposant = exposant >> 1; // division par 2 de l'exposant
            base = (base * base) % modulo; // calcul de base^2 % modulo
        }

        return resultat;
    }

    /**
     * Calcule l'inverse modulaire d'un entier donné dans un modulo spécifique.
     * L'inverse modulaire d'un entier a sous un modulo m est un entier x
     * tel que (a * x) % m == 1.
     * <p>
     * Cette méthode utilise l'algorithme
     * étendu d'Euclide pour trouver l'inverse modulaire.
     * Elle suppose que a et m sont premiers
     * entre eux (PGCD(a, m) = 1).
     *
     * @param a l'entier pour lequel on veut calculer l'inverse modulaire.
     * @param m le modulo sous lequel l'inverse est calculé.
     * @return l'inverse modulaire de a sous m.
     * @throws IllegalArgumentException si l'inverse modulaire n'existe pas
     *         (quand a et m ne sont pas premiers entre eux).
     */
    public static int modInverse(int a, int m) {
        int m0 = m, t, q;
        int x0 = 0;
        int x1 = 1;

        // si a et m ne sont pas premiers, l'inverse modulaire n'existe pas.
        if (pgcd(a, m) != 1) {
            throw new IllegalArgumentException("L'inverse modulaire n'existe "
                    + "pas quand a et m ne sont "
                    + "pas premiers entre eux.");
        }

        // cas trivial: inverse modulaire de n mod 1 est 0
        if (m == 1) {
            return 0;
        }

        while (a > 1) {
            // calcul de q (quotient) et t (reste)
            q = a / m;
            t = m;
            m = a % m;
            a = t;
            t = x0;

            // mise à jour de x0 et x1
            x0 = x1 - q * x0;
            x1 = t;
        }

        // si x1 est négatif, on ajoute m pour obtenir l'inverse positif
        if (x1 < 0) {
            x1 += m0;
        }

        return x1;
    }

    /**
     * Calcule le plus grand commun diviseur (PGCD) de deux entiers a et b
     * en utilisant l'algorithme d'Euclide.
     * <p>
     * L'algorithme d'Euclide permet de trouver le PGCD de deux entiers en
     * répétant l'opération de division avec reste jusqu'à ce que le reste
     * soit nul. Le PGCD est alors le dernier diviseur non nul.
     *
     * @param a Le premier entier.
     * @param b Le deuxième entier.
     * @return Le PGCD de a et b.
     * @throws IllegalArgumentException si a ou b est égal à 0, car le PGCD
     *         n'est pas défini pour 0.
     */
    private static int pgcd(int a, int b) {
        while (b != 0) {
            int t = b;
            b = a % b;
            a = t;
        }
        return a;
    }

    /**
     * Vérifie si un nombre est un nombre premier.
     * Un nombre premier est un entier strictement
     * supérieur à 1 qui n'est divisible que par 1
     * et par lui-même.
     *
     * @param p le nombre entier à vérifier.
     * @return true si le nombre est premier, false sinon.
     */
    public static boolean estPremier(int p) {
        boolean result = true;
        if (p == 0 || p == 1) {
            result = false;
        } else {
            for (int i = 2; i < p; i++) {
                if (p % i == 0) {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Génère un nombre premier p dans une plage donnée.
     * @param min Le minimum de la plage.
     * @param max Le maximum de la plage.
     * @return Un nombre premier p dans la plage.
     */
    public static int genererEntierPremier(int min, int max) {
        Random random = new Random();
        int p = min + random.nextInt(max - min + 1);

        while (!estPremier(p)) {
            // réessayer avec un autre nombre
            p = min + random.nextInt(max - min + 1);
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
            throw new IllegalArgumentException("Le nombre 'p' doit être "
                                               + "un nombre premier.");
        }

        // Cas trivial pour p = 2
        if (p == 2) {
            return 1; // Le seul élément du groupe est 1
        }

        // Tester les candidats pour g
        for (int g = 2; g < p; g++) {
            boolean estGenerateur = true;

            // Vérifier si g^k mod p produit des résultats distincts
            // pour k dans [1, p-1]
            for (int k = 1; k < p - 1; k++) {
                int resultat = expoModulaire(g, k, p);
                if (resultat == 1 && k < (p - 1)) {
                    // Si g^k mod p == 1 avant k = p-1,
                    // ce n'est pas un générateur.
                    estGenerateur = false;
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
