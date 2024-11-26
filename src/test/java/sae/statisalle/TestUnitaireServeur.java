/*
 * TestUnitaireServeur.java            24/10/2024
 * IUT DE RODEZ                        Pas de copyrights
 */
package sae.statisalle;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sae.statisalle.modele.objet.Serveur;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test unitaire pour la classe Serveur.
 */
public class TestUnitaireServeur {

    private Serveur serveur;
    private Thread serveurThread;

    @BeforeEach
    void setUp() {
        serveur = new Serveur();
        serveurThread = new Thread(() -> {
            try {
                serveur.demarrer(12345, null);
                serveur.accepterClients();
            } catch (Exception e) {
                System.err.println("Erreur lors du démarrage du serveur : " + e.getMessage());
            }
        });
        serveurThread.start();
    }

    @AfterEach
    void tearDown() {
        serveur.fermerServeur();
        try {
            serveurThread.join(); // Attendre que le thread du serveur se termine proprement
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Test
    void testConnexionClient() {
        try (Socket client = new Socket(InetAddress.getLocalHost(), 12345)) {
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            // Envoyer un message au serveur
            String message = "Test de connexion";
            out.println(message);

            // Lire la réponse du serveur
            String reponse = in.readLine();
            assertNotNull(reponse, "La réponse du serveur ne doit pas être null");
            assertTrue(reponse.contains("Données bien envoyées"),
                    "La réponse du serveur doit confirmer la réception des données");
        } catch (Exception e) {
            fail("Erreur lors de la connexion au serveur : " + e.getMessage());
        }
    }

    @Test
    void testEchangeDeCles() {
        try (Socket client = new Socket(InetAddress.getLocalHost(), 12345)) {
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            // Simuler l'envoi de la clé publique du client
            String cleClient = "5 ; 23 ; 11"; // Clé publique au format attendu
            out.println(cleClient);

            // Recevoir la clé publique du serveur
            String cleServeur = in.readLine();
            assertNotNull(cleServeur, "La clé publique du serveur ne doit pas être null");
            assertTrue(cleServeur.matches("\\d+ ; \\d+ ; \\d+"),
                    "La clé publique du serveur doit respecter le format attendu");
        } catch (Exception e) {
            fail("Erreur lors de l'échange de clés avec le serveur : " + e.getMessage());
        }
    }

    @Test
    void testTraitementRequete() {
        try (Socket client = new Socket(InetAddress.getLocalHost(), 12345)) {
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            // Simuler l'échange de clés (pré-requis)
            String cleClient = "5 ; 23 ; 11";
            out.println(cleClient);
            in.readLine(); // Clé publique du serveur (non vérifiée ici)

            // Envoyer une requête chiffrée simulée
            String requeteChiffree = "Données chiffrées simulées";
            out.println(requeteChiffree);

            // Lire la réponse du serveur
            String reponse = in.readLine();
            assertNotNull(reponse, "La réponse du serveur ne doit pas être null");
            assertTrue(reponse.startsWith("Données bien envoyées"),
                    "La réponse du serveur doit inclure la confirmation des données reçues");
        } catch (Exception e) {
            fail("Erreur lors du traitement de la requête par le serveur : " + e.getMessage());
        }
    }
}
