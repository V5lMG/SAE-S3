/*
 * TestReseau.java            21/10/2024
 * Pas de droit d'auteur ni de copyright
 */

package sae.statisalle;

import sae.statisalle.exception.MauvaiseConnexionServeur;
import sae.statisalle.modele.Reseau;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Classe de test de fonctionnement pour la classe Reseau
 * @author rodrigoxaviertaborda
 * @author valentin munier-genie
 */
public class TestReseau {

    private static final int PORT = 54321;
    private static final String HOST = "localhost";
    private static final String TEST_FILE_PATH = "testFile.csv";

    public static void main(String[] args) {
        try {
            preparerFichierDeTest();
            testEnvoieEtReception();
            testResolutionIP();
        } catch (IOException e) {
            System.err.println("Erreur lors des tests réseau : " + e.getMessage());
        } finally {
            nettoyerFichierDeTest();
        }
    }

    private static void preparerFichierDeTest() throws IOException {
        Path path = Path.of(TEST_FILE_PATH);
        Files.writeString(path, "Contenu de test pour le fichier.");
        System.out.println("[TEST] Fichier de test créé.");
    }

    private static void nettoyerFichierDeTest() {
        try {
            Files.deleteIfExists(Path.of(TEST_FILE_PATH));
            System.out.println("[TEST] Fichier de test supprimé.");
        } catch (IOException e) {
            System.err.println("[TEST] Erreur lors de la suppression du fichier : " + e.getMessage());
        }
    }

    private static void testEnvoieEtReception() {
        System.out.println("[TEST] Démarrage du test d'envoi et réception.");
        Reseau serveur = new Reseau();

        Thread serveurThread = new Thread(() -> {
            try {
                serveur.preparerServeur(PORT);
                Reseau connexionClient = serveur.attendreConnexionClient();
                if (connexionClient != null) {
                    String requete = connexionClient.recevoirDonnees();
                    if (requete != null) {
                        String reponse = connexionClient.traiterRequete(requete);
                        connexionClient.envoyerReponse(reponse);
                    }
                }
            } catch (IOException e) {
                System.err.println("[SERVEUR] Erreur dans le serveur : " + e.getMessage());
            } finally {
                serveur.fermerServeur();
            }
        });
        serveurThread.start();

        // pause pour permettre au serveur de démarrer
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Reseau client = new Reseau();
        try {
            client.preparerClient(HOST, PORT);
            client.envoyer(TEST_FILE_PATH);
            String reponse = client.recevoirReponse();
            System.out.println("[CLIENT] Réponse du serveur : " + reponse);
        } catch (MauvaiseConnexionServeur e) {
            System.err.println("[CLIENT] Erreur lors de la connexion : " + e.getMessage());
        } finally {
            client.fermerClient();
        }
    }

    private static void testResolutionIP() {
        System.out.println("[TEST] Démarrage du test de résolution IP.");
        try {
            Reseau.renvoyerIP();
        } catch (Exception e) {
            System.err.println("[TEST] Erreur lors de la résolution IP : " + e.getMessage());
        }
    }
}