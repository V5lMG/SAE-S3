/*
 * TestUnitaireReseau.java    21/10/2024
 * Pas de droit d'auteur ni de copyright
 */
package sae.statisalle;

import org.junit.jupiter.api.*;
import sae.statisalle.exception.MauvaiseConnexionServeur;
import sae.statisalle.modele.Reseau;

import java.io.*;
import java.net.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test unitaire pour la classe Reseau avec gestion explicite des sockets.
 * @author rodrigoxaviertaborda
 * @author valentin munier-genie
 */
public class TestUnitaireReseau {
    private Reseau reseau;
    private ServerSocket serverSocket;
    private Socket clientSocket;

    private static final int PORT_VALIDE = 65432;
    private static final int PORT_INVALIDE = 94321; // Port hors des limites (0-65535)
    private static final String LOCALHOST = "localhost";

    @BeforeEach
    void setUp() {
        reseau = new Reseau();
    }

    @AfterEach
    void tearDown() throws IOException {
        if (clientSocket != null && !clientSocket.isClosed()) {
            clientSocket.close();
        }
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
        }
        reseau.fermerClient();
        reseau.fermerServeur();
    }

    // --------- PARTIE SERVEUR -----------

    @Test
    void testPreparerServeurAvecPortValide() throws IOException {
            int port = 65432;
        reseau.preparerServeur(port);

        try {
            serverSocket = new ServerSocket(port);
            fail("Le port devrait être déjà utilisé par le serveur.");
        } catch (IOException e) {
            // Test réussi, le port est déjà occupé
        }
    }

    @Test
    void testPreparerServeurAvecPortInvalide() {
        int port = 70000; // Hors de la plage 0-65535
        assertThrows(IllegalArgumentException.class, () -> reseau.preparerServeur(port));
    }

    @Test
    void testAttendreConnexionClient() throws IOException, InterruptedException {
        int port = 64321;
        reseau.preparerServeur(port);

        new Thread(() -> {
            try (Socket localClient = new Socket("localhost", port)) {
                OutputStream out = localClient.getOutputStream();
                out.write("Test".getBytes());
                out.flush();
            } catch (IOException ignored) {
            }
        }).start();


        Thread.sleep(500);  // Attendre que la connexion soit établie

        Reseau clientReseau = reseau.attendreConnexionClient();
        assertNotNull(clientReseau,"Le client devrait être connecté."   );
        }

    @Test
    void testRecevoirDonnees() throws IOException {
        int port = 74321;
        reseau.preparerServeur(port);

        new Thread(() -> {
            try {
                clientSocket = new Socket("localhost", port);
                OutputStream out = clientSocket.getOutputStream();
                out.write("Données Test\n".getBytes());
                out.flush();
            } catch (IOException ignored) {
            }
        }).start();

        Reseau clientReseau = reseau.attendreConnexionClient();
        String donnees = clientReseau.recevoirDonnees();
        assertEquals("Données Test", donnees);
    }

    @Test
    void testRecevoirDonneesNull() {
        assertNull(reseau.recevoirDonnees(),
                "La réception de données sans client doit retourner null.");
    }

    @Test
    void testTraiterRequeteCorrecte() {
        String requete = "cle123/DELIM/donneesChiffrees";
        String resultat = reseau.traiterRequete(requete);
        assertNotNull(resultat);
    }

    @Test
    void testTraiterRequeteInvalide() {
        assertThrows(IllegalArgumentException.class, () -> reseau.traiterRequete(null));
        assertThrows(IllegalArgumentException.class, () -> reseau.traiterRequete("Format Incorrect"));
    }

    @Test
    void testEnvoyerReponse() throws IOException {
        int port = 84321;
        reseau.preparerServeur(port);

        new Thread(() -> {
            try {
                clientSocket = new Socket("localhost", port);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String response = in.readLine();
                assertEquals("Réponse Test", response);
            } catch (IOException ignored) {
            }
        }).start();

        Reseau clientReseau = reseau.attendreConnexionClient();
        clientReseau.envoyerReponse("Réponse Test");
    }

    @Test
    void testFermerServeurSansConnexion() {
        assertDoesNotThrow(() -> reseau.fermerServeur());
    }

    // --------- PARTIE CLIENT -----------

    @Test
    void testPreparerClientConnexionReussie() throws MauvaiseConnexionServeur, InterruptedException {
        int port = 94321;

        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(port);
                serverSocket.accept();
            } catch (IOException ignored) {
            }
        }).start();

        // Attendez un peu pour permettre au serveur de démarrer
        Thread.sleep(500);  // Attendez que le serveur soit prêt

        reseau.preparerClient("localhost", port);
        assertTrue(clientSocket != null && clientSocket.isConnected(), "Le client devrait être connecté.");
    }

    @Test
    void testEnvoyerEtRecevoirReponse() throws MauvaiseConnexionServeur {
        int port = 95432;
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(port);
                Socket client = serverSocket.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter out = new PrintWriter(client.getOutputStream(), true);

                String message = in.readLine();
                assertEquals("Message Test", message);

                out.println("Réponse Serveur");
            } catch (IOException ignored) {
            }
        }).start();

        reseau.preparerClient("localhost", port);
        reseau.envoyer("Message Test");
        String response = reseau.recevoirReponse();
        assertEquals("Réponse Serveur", response);
    }

    @Test
    void testRecevoirReponseNull() {
        assertNull(reseau.recevoirReponse());
    }

    @Test
    void testRenvoyerIP() {
        InetAddress adresse = Reseau.renvoyerIP();
        assertNotNull(adresse);
    }

    @Test
    void testFermerClientSansConnexion() {
        assertDoesNotThrow(() -> reseau.fermerClient());
    }
}
