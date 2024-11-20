package sae.statisalle.modele;

import sae.statisalle.exception.ModuloNegatifException;

/**
 * Classe pour combiner Vigenère et Diffie-Hellman.
 */
public class DiffieHellman {

    public static int cleDiffieHellman(int p, int g) {
        if (!estPremier(p)) {
            throw new IllegalArgumentException("p doit être un nombre premier.");
        }
        int a = 3;
        int b = 5;
        int clePubliqueA = expoModulaire(g, a, p);
        int clePubliqueB = expoModulaire(g, b, p);
        return expoModulaire(clePubliqueB, a, p);
    }

    public static boolean estPremier(int p) {
        if (p < 2) return false;
        for (int i = 2; i * i <= p; i++) {
            if (p % i == 0) return false;
        }
        return true;
    }

    public static int expoModulaire(int base, int exposant, int modulo) {
        if (modulo <= 0) {
            throw new ModuloNegatifException("Modulo doit être positif.");
        }
        if (exposant < 0) {
            base = modInverse(base, modulo);
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
}
