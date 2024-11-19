/*
 * TestUnitaireReseau.java    21/10/2024
 * Pas de droit d'auteur ni de copyright
 */
package sae.statisalle;

import org.junit.jupiter.api.*;
import sae.statisalle.exception.MauvaiseConnexionServeur;
import sae.statisalle.modele.Chiffrement;
import sae.statisalle.modele.Reseau;

import java.io.*;
import java.net.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test unitaire pour la classe Reseau.
 * Elle couvre les principales méthodes et cas d'usage.
 */
public class TestUnitaireReseau {

    private Reseau serveur;
    private Reseau client;
    private ServerSocket serverSocket;
    private Socket clientSocket;

    private static final int PORT_VALIDE = 65432;
    private static final int PORT_INVALIDE = 70000;
    private static final String LOCALHOST = "localhost";

    @BeforeEach
    void setUp() {
        serveur = new Reseau();
        client = new Reseau();
    }

    @AfterEach
    void tearDown() {
        serveur.fermerServeur();
        client.fermerClient();

        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la fermeture du serverSocket : " + e.getMessage());
        }
    }

    // --------- PARTIE SERVEUR -----------

    @Test
    void testPreparerServeurAvecPortValide() throws IOException {
        serveur.preparerServeur(PORT_VALIDE);
        try (ServerSocket testSocket = new ServerSocket(PORT_VALIDE)) {
            fail("Le port devrait être occupé par le serveur.");
        } catch (IOException e) {
            // Le port est occupé, test réussi.
        }
    }

    @Test
    void testPreparerServeurAvecPortInvalide() {
        assertThrows(IllegalArgumentException.class, () -> serveur.preparerServeur(PORT_INVALIDE));
    }

    @Test
    void testAttendreConnexionClient() throws IOException, InterruptedException {
        serveur.preparerServeur(PORT_VALIDE);

        // Simuler un client qui se connecte
        new Thread(() -> {
            try (Socket localClient = new Socket(LOCALHOST, PORT_VALIDE)) {
                OutputStream out = localClient.getOutputStream();
                out.write("Test".getBytes());
                out.flush();
            } catch (IOException ignored) {
            }
        }).start();

        Thread.sleep(500);  // Laisser le temps au client de se connecter
        Reseau clientReseau = serveur.attendreConnexionClient();
        assertNotNull(clientReseau, "Le client devrait être connecté.");
    }

    @Test
    void testRecevoirDonnees() throws IOException {
        serveur.preparerServeur(PORT_VALIDE);

        // Simuler un client envoyant des données
        new Thread(() -> {
            try (Socket localClient = new Socket(LOCALHOST, PORT_VALIDE)) {
                OutputStream out = localClient.getOutputStream();
                out.write("Données Test\n".getBytes());
                out.flush();
            } catch (IOException ignored) {
            }
        }).start();

        Reseau clientReseau = serveur.attendreConnexionClient();
        String donnees = clientReseau.recevoirDonnees();
        assertEquals("Données Test", donnees, "Les données reçues devraient correspondre.");
    }

    @Test
    void testTraiterRequeteCorrecte() {
        String requete = "cle123/DELIM/donneesChiffrees";
        String resultat = serveur.traiterRequete(requete);
        assertNotNull(resultat, "Le traitement d'une requête correcte devrait réussir.");
    }

    @Test
    void testTraiterRequeteInvalide() {
        assertThrows(IllegalArgumentException.class, () -> serveur.traiterRequete(null));
        assertThrows(IllegalArgumentException.class, () -> serveur.traiterRequete("Format Incorrect"));
    }

    @Test
    void testEnvoyerReponse() throws IOException {
        serveur.preparerServeur(PORT_VALIDE);

        // Simuler un client recevant une réponse
        new Thread(() -> {
            try (Socket localClient = new Socket(LOCALHOST, PORT_VALIDE)) {
                BufferedReader in = new BufferedReader(new InputStreamReader(localClient.getInputStream()));
                String reponse = in.readLine();
                assertEquals("Réponse Test", reponse, "La réponse envoyée par le serveur devrait être reçue par le client.");
            } catch (IOException ignored) {
            }
        }).start();

        Reseau clientReseau = serveur.attendreConnexionClient();
        clientReseau.envoyerReponse("Réponse Test");
    }

    // --------- PARTIE CLIENT -----------

    @Test
    void testPreparerClientConnexionReussie() throws MauvaiseConnexionServeur, InterruptedException {
        // Démarrer un thread pour préparer le serveur
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(PORT_VALIDE);
                clientSocket = serverSocket.accept(); // Attendre la connexion du client
            } catch (IOException ignored) {
            }
        }).start();

        // Attendre suffisamment de temps pour que le serveur démarre
        Thread.sleep(1000);

        // Préparer le client
        client.preparerClient(LOCALHOST, PORT_VALIDE);

        assertTrue(client.estConnecte(), "Le client devrait être connecté au serveur.");
    }

    @Test
    void testEnvoyerEtRecevoirReponse() throws MauvaiseConnexionServeur, InterruptedException {
        // Démarrer un thread pour simuler un serveur
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(PORT_VALIDE);
                Socket clientSocket = serverSocket.accept();

                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                String messageRecu = in.readLine();
                assertNotNull(messageRecu, "Le message reçu par le serveur ne devrait pas être nul.");

                String[] parts = messageRecu.split("/DELIM/");
                assertEquals(2, parts.length, "Le message reçu devrait contenir une clé et des données séparées par /DELIM/.");

                String cle = parts[0];
                String donneesChiffrees = parts[1];

                // Utiliser le même alphabet pour déchiffrer les données
                Chiffrement.creerAlphabet();
                String messageDechiffre = Chiffrement.dechiffrementDonnees(donneesChiffrees, cle);
                assertEquals("Message Test", messageDechiffre, "Le serveur devrait recevoir le message déchiffré.");

                out.println("Réponse Serveur");
            } catch (IOException ignored) {
            }
        }).start();

        Thread.sleep(1000);

        // Préparer les données du client
        Chiffrement.creerAlphabet();
        String cle = Chiffrement.genererCleAleatoire("Message Test");
        String messageChiffre = Chiffrement.chiffrementDonnees("Message Test", cle);
        String messageAEnvoyer = cle + "/DELIM/" + messageChiffre;

        client.preparerClient(LOCALHOST, PORT_VALIDE);
        client.envoyer(messageAEnvoyer);

        String reponse = client.recevoirReponse();
        assertEquals("Réponse Serveur", reponse, "Le client devrait recevoir la réponse du serveur.");
    }
}
