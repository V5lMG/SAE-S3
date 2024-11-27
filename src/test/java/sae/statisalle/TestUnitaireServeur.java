/*
 * TestUnitaireServeur.java            21/10/2024
 * IUT DE RODEZ                        Pas de copyrights
 */
package sae.statisalle;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sae.statisalle.modele.objet.Serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de tests unitaires pour la classe Serveur.
 * @author valentin.munier-genie
 * @author rodrigo xavier-taborda
 */
public class TestUnitaireServeur {

    private Serveur serveur;
    private Thread serveurThread;

    @BeforeEach
    void setUp() throws InterruptedException {
        serveur = new Serveur();
        serveurThread = new Thread(() -> {
            try {
                serveur.demarrer(65432, null);
                serveur.accepterClients();
            } catch (Exception e) {
                System.err.println("Erreur lors du démarrage du serveur : "
                        + e.getMessage());
            }
        });
        serveurThread.start();
        // attendre que le serveur démarre
        Thread.sleep(500);
    }

    @AfterEach
    void tearDown() {
        serveur.fermerServeur();
        try {
            // Attendre que le thread du serveur se termine proprement
            serveurThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Test
    void testDemarrageAvecIPValide() {
        try {
            Serveur serveurAvecIP = new Serveur();
            // test avec localhost
            serveurAvecIP.demarrer(55557, "127.0.0.1");
            assertNotNull(serveurAvecIP.renvoyerIP(), "L'adresse IP "
                    + "ne doit pas être null");
            serveurAvecIP.fermerServeur();
        } catch (Exception e) {
            fail("Erreur lors du démarrage du serveur avec une adresse IP "
                    + "spécifique : " + e.getMessage());
        }
    }

    @Test
    void testEchangeDeCles() {
        try (Socket client = new Socket(InetAddress.getLocalHost(),
                65432)) {
            PrintWriter out = new PrintWriter(client.getOutputStream(),
                    true);
            BufferedReader in = new BufferedReader(new InputStreamReader
                    (client.getInputStream()));

            // simuler l'envoi de la clé publique du client
            String cleClient = "5 ; 23 ; 11";
            out.println(cleClient);

            // reçoit la clé publique du serveur
            String cleServeur = in.readLine();
            assertNotNull(cleServeur, "La clé publique du serveur ne "
                    + "doit pas être null");
            assertTrue(cleServeur.matches("\\d+ ; \\d+ ; \\d+"),
                    "La clé publique du serveur doit respecter le "
                            + "format attendu");
        } catch (Exception e) {
            fail("Erreur lors de l'échange de clés avec le serveur : "
                    + e.getMessage());
        }
    }

    @Test
    void testClePubliqueInvalide() {
        try (Socket client = new Socket(InetAddress.getLocalHost(),
                65432)) {
            PrintWriter out = new PrintWriter(client.getOutputStream(),
                    true);
            BufferedReader in = new BufferedReader(new InputStreamReader
                    (client.getInputStream()));

            // Envoyer une clé publique mal formée
            out.println("CléPubliqueIncorrecte");

            // Vérifier que le serveur gère correctement l'erreur
            String reponse = in.readLine();
            assertNull(reponse, "Le serveur ne devrait pas envoyer "
                    + "de réponse en cas d'erreur de clé publique");
        } catch (Exception e) {
            fail("Erreur lors de la gestion des clés publiques invalides : "
                    + e.getMessage());
        }
    }

    @Test
    void testDeconnexionBrutale() {
        try (Socket client = new Socket(InetAddress.getLocalHost(),
                65432)) {
            PrintWriter out = new PrintWriter(client.getOutputStream(),
                    true);

            // Simuler l'envoi d'une clé publique correcte
            out.println("5 ; 23 ; 11");

            // Fermer brutalement la connexion
            client.close();

            // Vérifier que le serveur continue de fonctionner sans erreur
            assertTrue(serveurThread.isAlive(), "Le serveur doit "
                    + "rester opérationnel après une déconnexion brutale");
        } catch (Exception e) {
            fail("Erreur lors de la gestion de la déconnexion brutale : "
                    + e.getMessage());
        }
    }

    @Test
    void testRenvoyerIP() {
        InetAddress ip = serveur.renvoyerIP();
        assertNotNull(ip, "L'IP retournée ne doit pas être null");
    }


    @Test
    void testFermerResourcesProprement() throws IOException {
        // Création d'un ServerSocket et d'un Socket client
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            // Création d'un thread pour simuler un client se connectant au serveur
            Thread clientThread = new Thread(() -> {
                try {
                    new Socket("localhost", serverSocket.getLocalPort());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            clientThread.start();

            // Accepter la connexion du client
            Socket clientSocket = serverSocket.accept();

            // Instancier le Serveur
            Serveur serveur = new Serveur();

            // Initialisation des flux
            serveur.demarrer(55588, "127.0.0.1");
            serveur.accepterClients();

            // Vérification que fermer ne lève pas d'exception
            assertDoesNotThrow(serveur::fermer, "La méthode fermer ne doit pas lever d'exception.");

            // Vérification que le socket est bien fermé
            assertTrue(clientSocket.isClosed(), "Le clientSocket doit être fermé.");
        }
    }

    @Test
    void testFermerSansRessources() {
        // Création d'une instance de Serveur sans initialisation de flux
        Serveur serveur = new Serveur();

        // Vérification que fermer ne lève pas d'exception même sans ressources
        assertDoesNotThrow(serveur::fermer, "La méthode fermer ne doit pas lever d'exception lorsqu'aucune ressource n'est initialisée.");
    }
}
