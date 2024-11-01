/*
 * TestReseau.java            21/10/2024
 * Pas de droit d'auteur ni de copyright
 */
package sae.statisalle;

import sae.statisalle.exception.MauvaiseConnexionServeur;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Classe de test pour la classe Reseau.
 * Cette classe lance un serveur et un client pour tester les fonctionnalités
 * d'envoi et de réception de données sur le réseau.
 * Elle permet de vérifier le bon fonctionnement des méthodes de la
 * classe Reseaudans un environnement réseau simulé.
 * <p>
 * Le serveur écoute sur un port spécifique et le client se connecte à ce port
 * pour envoyer et recevoir des messages.
 * </p>
 * @author valentin.munier-genie
 * @author rodrigo.xavier-taborda
 */
public class TestReseau {

    /**
     * Port d'écoute utilisé par le serveur pour
     * accepter les connexions clients.
     */
    private static final int PORT = 12345;

    /** Nom d'hôte ou adresse IP du serveur (ici configuré en local). */
    private static final String HOST = "localhost";

    /** Chemin d'accès au fichier de test à envoyer
     * pour tester les échanges de données.
     */
    private static final String TEST_FILE_PATH = "testFile.csv";

    /**
     * Point d'entrée principal du programme de test.
     * Ce programme lance un serveur et un client, le client envoie
     * un fichier au serveur pour tester la communication réseau.
     *
     * @param args Arguments de la ligne de commande (non utilisés).
     * @throws IOException Si une erreur survient lors de la connexion
     *                     ou de l'envoi de données.
     */
    public static void main(String[] args) throws IOException {
        try {
            // Créer un fichier de test
            Path path = Path.of(TEST_FILE_PATH);
            Files.writeString(path, "Contenu de test pour le fichier.");

            // Appel des méthodes de test
            envoieEtReceptionDunFichier();
            Reseau.renvoyerIP();

        } catch (IOException e) {
            System.err.println("Erreur lors de la création ou de l'écriture " +
                               "du fichier de test : " + e.getMessage());

        } finally {
            // Suppression du fichier après les tests
            Files.deleteIfExists(Path.of(TEST_FILE_PATH));
        }
    }

    /**
     * Teste le fonctionnement de la communication
     * entre un serveur et un client.
     * La méthode gère également la création et
     * la suppression d'un fichier de test
     * utilisé pour vérifier le fonctionnement de
     * l'envoi et de la réception de données.
     */
    public static void envoieEtReceptionDunFichier() {
        Reseau serveur = new Reseau();

        Thread serveurThread = new Thread(() -> {
            try {
                serveur.preparerServeur(PORT);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            serveur.attendreConnexionClient();
            String requete = serveur.recevoirDonnees();
            if (requete != null) {
                String reponse = serveur.traiterRequete(requete);
                serveur.envoyerReponse(reponse);
            }
            serveur.fermerServeur();
        });
        serveurThread.start();

        // Créer et démarrer le client
        Reseau client = new Reseau();
        try {
            client.preparerClient(HOST, PORT);
        } catch (MauvaiseConnexionServeur e) {
            throw new RuntimeException(e);
        }

        // Envoyer un fichier de test
        client.envoyer(TEST_FILE_PATH);
        String reponseServeur = client.recevoirReponse();

        if (reponseServeur != null) {
            client.utiliserReponse(reponseServeur);
        }

        client.fermerClient();
    }
}