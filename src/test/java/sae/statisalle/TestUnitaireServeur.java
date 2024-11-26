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
    void setUp() throws InterruptedException {
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
        Thread.sleep(500); // Attendre que le serveur démarre
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
    void testClePubliqueInvalide() {
        try (Socket client = new Socket(InetAddress.getLocalHost(), 12345)) {
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            // Envoyer une clé publique mal formée
            out.println("CléPubliqueIncorrecte");

            // Vérifier que le serveur gère correctement l'erreur
            String reponse = in.readLine();
            assertNull(reponse, "Le serveur ne devrait pas envoyer de réponse en cas d'erreur de clé publique");
        } catch (Exception e) {
            fail("Erreur lors de la gestion des clés publiques invalides : " + e.getMessage());
        }
    }

    @Test
    void testDeconnexionBrutale() {
        try (Socket client = new Socket(InetAddress.getLocalHost(), 12345)) {
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);

            // Simuler l'envoi d'une clé publique correcte
            out.println("5 ; 23 ; 11");

            // Fermer brutalement la connexion
            client.close();

            // Vérifier que le serveur continue de fonctionner sans erreur
            assertTrue(serveurThread.isAlive(), "Le serveur doit rester opérationnel après une déconnexion brutale");
        } catch (Exception e) {
            fail("Erreur lors de la gestion de la déconnexion brutale : " + e.getMessage());
        }
    }
}
