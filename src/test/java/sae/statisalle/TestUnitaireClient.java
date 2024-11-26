/*
 * TestUnitaireClient.java            24/10/2024
 * IUT DE RODEZ                        Pas de copyrights
 */

package sae.statisalle;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sae.statisalle.modele.objet.Client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de tests unitaires pour la classe Serveur.
 */
public class TestUnitaireClient {

    private Client client;
    private ServerSocket serveurTest;
    private Thread serveurThread;

    @BeforeEach
    void setUp() throws Exception {
        client = new Client();

        // Démarrage d'un serveur fictif pour les tests
        serveurTest = new ServerSocket(65432);
        serveurThread = new Thread(() -> {
            try (Socket socket = serveurTest.accept();
                 PrintWriter out = new PrintWriter(socket.getOutputStream(),
                         true);
                 BufferedReader in = new BufferedReader(new InputStreamReader
                         (socket.getInputStream()))) {

                String clePubliqueClient = in.readLine();
                if (clePubliqueClient != null) {
                    // Simule une clé publique du serveur
                    out.println("7 ; 23 ; 11");
                }

                String donneesRecues = in.readLine();
                if (donneesRecues != null) {
                    out.println("Réponse du serveur");
                }
            } catch (Exception e) {
                System.err.println("[SERVEUR TEST] Erreur : " + e.getMessage());
            }
        });
        serveurThread.start();
    }

    @AfterEach
    void tearDown() throws Exception {
        client.fermer();
        serveurTest.close();
        // Attendre que le thread serveur se termine
        serveurThread.join();
    }

    @Test
    void testConnexionEtDeconnexion() {
        assertDoesNotThrow(() -> client.connecter("127.0.0.1", 65432), "La connexion au serveur doit réussir");
        client.fermer();
    }

    @Test
    void testEnvoiEtReceptionClePublique() {
        assertDoesNotThrow(() -> client.connecter("127.0.0.1", 65432), "La connexion au serveur doit réussir");

        client.envoyerClePublic("5 ; 23 ; 11");
        String clePubliqueServeur = client.recevoirClePublic();

        assertNotNull(clePubliqueServeur, "La clé publique reçue du serveur ne doit pas être null");
        assertTrue(clePubliqueServeur.matches("\\d+ ; \\d+ ; \\d+"),
                "La clé publique reçue doit respecter le format attendu");
    }

    @Test
    void testReceptionDonneesInvalide() {
        assertDoesNotThrow(() -> client.connecter("127.0.0.1", 65432), "La connexion au serveur doit réussir");

        client.fermer(); // Fermer avant de recevoir
        String donneesReçues = client.recevoir();

        assertNull(donneesReçues, "Aucune donnée ne doit être reçue après la fermeture de la connexion");
    }

    @Test
    void testGestionErreurConnexion() {
        assertThrows(Exception.class, () -> client.connecter("192.0.2.0", 12345),
                "Une connexion à une adresse IP inexistante doit échouer");
    }

    @Test
    void testRenvoyerIP() {
        InetAddress ip = client.renvoyerIP();
        assertNotNull(ip, "L'IP retournée ne doit pas être null");
    }
}
