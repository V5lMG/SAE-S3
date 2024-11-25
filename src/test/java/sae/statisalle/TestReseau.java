/*
 * TestReseau.java            24/11/2024
 * Pas de droit d'auteur ni de copyright
 */
package sae.statisalle;

import sae.statisalle.modele.objet.Serveur;
import sae.statisalle.modele.objet.Client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Classe de test de fonctionnement pour les classes Serveur et Client
 * @author valentin.munier-genie
 */
public class TestReseau {

    private static final int PORT = 65000;
    private static final String HOST = "localhost";
    private static final String TEST_FILE_PATH = "testFile.csv";

    public static void main(String[] args) {
        try {
            preparerFichierDeTest();
            testEnvoiEtReception();
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

    private static void testEnvoiEtReception() {
        System.out.println("[TEST] Démarrage du test d'envoi et réception.");

        Serveur serveur = new Serveur();
        Thread serveurThread = new Thread(() -> {
            try {
                serveur.demarrer(PORT, HOST);
                serveur.accepterClients();
            } catch (IOException e) {
                System.err.println("[SERVEUR] Erreur : " + e.getMessage());
            } finally {
                serveur.fermerServeur();
            }
        });
        serveurThread.start();

        // Pause pour permettre au serveur de démarrer
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Client client = new Client();
        try {
            client.connecter(HOST, PORT);

            // Simulation d'échange de clés Diffie-Hellman
            String clePubliqueClient = "12345 ; 23 ; 5"; // Exemple de clé publique
            client.envoyerClePublic(clePubliqueClient);

            String clePubliqueServeur = client.recevoirClePublic();
            System.out.println("[CLIENT] Clé publique reçue du serveur : " + clePubliqueServeur);

            // Envoi de données
            String contenuFichier = Files.readString(Path.of(TEST_FILE_PATH));
            System.out.println("[CLIENT] Envoi des données : " + contenuFichier);
            client.envoyer(contenuFichier);

            // Réception de la réponse du serveur
            String reponse = client.recevoir();
            System.out.println("[CLIENT] Réponse du serveur : " + reponse);

        } catch (IOException e) {
            System.err.println("[CLIENT] Erreur lors de la connexion : " + e.getMessage());
        } finally {
            client.fermer();
        }
    }

    private static void testResolutionIP() {
        System.out.println("[TEST] Démarrage du test de résolution IP.");

        Serveur serveur = new Serveur();
        try {
            System.out.println("[SERVEUR] Adresse IP : " + serveur.renvoyerIP());
        } catch (Exception e) {
            System.err.println("[TEST] Erreur lors de la résolution IP : " + e.getMessage());
        }

        Client client = new Client();
        try {
            System.out.println("[CLIENT] Adresse IP : " + client.renvoyerIP());
        } catch (Exception e) {
            System.err.println("[TEST] Erreur lors de la résolution IP : " + e.getMessage());
        }
    }
}
