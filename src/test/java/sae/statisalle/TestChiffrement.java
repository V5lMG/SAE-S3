/*
 * TestChiffrement.java               24/10/2024
 * IUT DE RODEZ                       Pas de copyrights
 */
package sae.statisalle;

import sae.statisalle.modele.DiffieHellman;
import sae.statisalle.modele.Vigenere;

import java.math.BigInteger;

/**
 * Classe de test pour illustrer le fonctionnement de l'échange de clés
 * Diffie-Hellman et l'utilisation du chiffrement et déchiffrement
 * par l'algorithme de Vigenère.
 *
 * @author valentin.munier-genie
 */
public class TestChiffrement {

    /**
     * Point d'entrée du programme, effectuant les étapes suivantes :
     * <ul>
     * <li>Simulation de l'échange de clés Diffie-Hellman entre Alice
     * et Bob.</li>
     * <li>Vérification que la clé partagée calculée par Alice et Bob
     * est identique.</li>
     * <li>Utilisation de cette clé partagée pour chiffrer et déchiffrer
     * un message en utilisant l'algorithme de chiffrement de Vigenère.</li>
     * </ul>
     *
     * @param args arguments passés en ligne de commande (non utilisés)
     */
    public static void main(String[] args) {
        // Paramètres de Diffie-Hellman
        int p = 23; // Nombre premier
        int g = 5;  // Générateur

        // Clés privées (Alice et Bob)
        int a = 6;  // Clé privée d'Alice
        int b = 15; // Clé privée de Bob

        // Étape 1: Alice calcule g^a mod p
        int gaModP = DiffieHellman.expoModulaire(g, a, p);
        System.out.println("g^a mod p (Alice) : " + gaModP);

        // Étape 2: Bob calcule g^b mod p
        int gbModP = DiffieHellman.expoModulaire(g, b, p);
        System.out.println("g^b mod p (Bob) : " + gbModP);

        // Alice envoie gaModP à Bob, et Bob envoie gbModP à Alice
        // Étape 3: Alice calcule (g^b)^a mod p
        BigInteger clePartageeAlice = BigInteger.valueOf(
                DiffieHellman.expoModulaire(gbModP, a, p));
        System.out.println("Clé partagée calculée par Alice : "
                + clePartageeAlice);

        // Étape 4: Bob calcule (g^a)^b mod p
        BigInteger clePartageeBob = BigInteger.valueOf(
                DiffieHellman.expoModulaire(gaModP, b, p));
        System.out.println("Clé partagée calculée par Bob : "
                + clePartageeBob);

        // Vérification que les deux clés partagées sont identiques
        if (clePartageeAlice.equals(clePartageeBob)) {
            System.out.println("Les clés partagées sont identiques : "
                    + clePartageeAlice);
        } else {
            System.out.println("Les clés partagées ne correspondent pas.");
        }

        String donneesChiffrees = Vigenere.chiffrementDonnees("Test",
                clePartageeAlice);
        System.out.println("Donnees: " + donneesChiffrees);
        String donneesDechiffrees = Vigenere.dechiffrementDonnees(
                donneesChiffrees, clePartageeBob);
        System.out.println("Dechiffrees: " + donneesDechiffrees);
    }
}
