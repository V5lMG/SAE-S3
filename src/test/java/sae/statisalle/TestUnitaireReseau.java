/*
 * TestUnitaireReseau.java    21/10/2024
 * Pas de droit d'auteur ni de copyright
 */
package sae.statisalle;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.net.ServerSocket;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test unitaire pour la classe Reseau.
 *
 * @author valentin.munier-genie
 * @author rodrigo.xavier-taborda
 */
class TestUnitaireReseau {

    /** Simule le serveur pour les tests de communication réseau. */
    private Reseau reseauServeur;
    /** Simule le client pour les tests de communication réseau. */
    private Reseau reseauClient;

    /** Chemin du fichier de test*/
    private final String TEST_FILE_PATH = "FichierDeTest.csv";

    /** Initialisation des ressources avant chaque test. */
    @BeforeEach
    void setUp() {
        reseauServeur = new Reseau();
        reseauClient = new Reseau();
        try {
            Files.writeString(Paths.get(TEST_FILE_PATH), "Contenu du fichier de test");
        } catch (IOException e) {
            fail("Échec lors de la création du fichier de test : " + e.getMessage());
        }
    }

    /** Nettoyage des ressources après chaque test. */
    @AfterEach
    void nettoyage() {
        reseauServeur.fermerServeur();
        reseauClient.fermerClient();

        try {
            Files.deleteIfExists(Paths.get(TEST_FILE_PATH));
        } catch (IOException e) {
            fail("Échec lors de la supprésion du fichier de test : " + e.getMessage());
        }
    }

    /**
     * Teste le démarrage normal du serveur.
     */
    @Test
    void testPreparerServeurSuccess() {
        assertDoesNotThrow(() -> {
            reseauServeur.preparerServeur(12345);
        }, "Le serveur n'a pas pu démarrer sur un port valide.");
    }

    /**
     * Teste le démarrage du serveur sur un port déjà utilisé et invalide.
     */
    @Test
    void testPreparerServeurPortDejaUtiliseEtInvalide() throws IOException {

        // port déjà utilisé
        ServerSocket serverSocket = new ServerSocket(12345);
        assertThrows(IOException.class, () -> {
            reseauServeur.preparerServeur(12345);
        });

        // port invalid
        assertThrows(IllegalArgumentException.class, () -> {
            reseauServeur.preparerServeur(-1);
        });

        // port invalid
        assertThrows(IllegalArgumentException.class, () -> {
            reseauServeur.preparerServeur(-1);
        });

        serverSocket.close();
    }

    @Test
    void testAttendreConnexionClient() {
        new Thread(() -> {
            try {
                reseauServeur.preparerServeur(12345);
                reseauServeur.attendreConnexionClient();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();

        assertDoesNotThrow(() -> {
            reseauClient.preparerClient("localhost", 12345);
            reseauClient.envoyer(TEST_FILE_PATH);
        });

        reseauServeur.fermerServeur();
        reseauClient.fermerClient();
    }

    @Test
    void recevoirDonnees() {
        new Thread(() -> {
            try {
                reseauServeur.preparerServeur(12345);
                reseauServeur.attendreConnexionClient();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();

        // connexion du client et envoi des données
        assertDoesNotThrow(() -> {
            reseauClient.preparerClient("localhost", 12345);
            reseauClient.envoyer(TEST_FILE_PATH);
        });

        // recevoir les données et vérifier
        String donnees = reseauServeur.recevoirDonnees();
        assertEquals("Contenu du fichier de test", donnees);

        // fermeture des connexions
        reseauServeur.fermerServeur();
        reseauClient.fermerClient();
    }

    @Test
    void testTraiterRequeteValide() {
        String requete = "test requetes valide";
        String reponse = reseauServeur.traiterRequete(requete);

        assertEquals("Message reçu : test requetes valide", reponse, "La réponse ne correspond pas à celle attendue.");
    }

    @Test
    void testEnvoyerReponseValide() {
        Reseau reseau = new Reseau();

        // utiliser StringWriter pour capturer la sortie envoyée
        StringWriter stringWriter = new StringWriter();
        reseau.fluxSortie = new PrintWriter(stringWriter, true);

        // definir une réponse et l'envoyer
        String reponse = "test réponse valide";
        reseau.envoyerReponse(reponse);

        // verifier que la réponse envoyée est bien capturée dans StringWriter
        assertEquals("test réponse valide\n", stringWriter.toString(), "La réponse envoyée ne correspond pas.");
    }

    @Test
    void fermerServeur() {
        assertDoesNotThrow(reseauServeur::fermerServeur);
    }

    @Test
    void preparerClient() {
        try {
            reseauServeur.preparerServeur(12345);
            new Thread(reseauServeur::attendreConnexionClient).start();

            // Préparation du client
            assertDoesNotThrow(() -> reseauClient.preparerClient("localhost", 12345),
                    "Le client doit pouvoir se connecter au serveur.");

            assertNotNull(reseauClient.fluxSortie, "Le flux de sortie du client ne doit pas être nul.");
            assertNotNull(reseauClient.fluxEntree, "Le flux d'entrée du client ne doit pas être nul.");

        } catch (IOException e) {
            fail("Erreur lors de la préparation du serveur ou du client : " + e.getMessage());
        }
        reseauServeur.fermerServeur();
        reseauClient.fermerClient();
    }

    @Test
    void envoyer() throws IOException {
        Reseau serveur = new Reseau();
        Reseau client = new Reseau();

        serveur.preparerServeur(12345);
        new Thread(serveur::attendreConnexionClient).start();

        client.preparerClient("localhost", 12345);

        // Envoie d'un fichier
        client.envoyer(TEST_FILE_PATH);

        // Vérification côté serveur
        assertEquals("Contenu du fichier de test", serveur.recevoirDonnees(),
                "Le contenu reçu doit correspondre au contenu envoyé.");

        reseauServeur.fermerServeur();
        reseauClient.fermerClient();
    }

    @Test
    void recevoirReponse() throws IOException {

        reseauServeur.preparerServeur(12345);
        new Thread(reseauServeur::attendreConnexionClient).start();

        reseauClient.preparerClient("localhost", 12345);

        // envoi d'une réponse de test
        String reponseTest = "Réponse de test";
        reseauServeur.envoyerReponse(reponseTest);

        // vérification côté client
        assertEquals(reponseTest, reseauClient.recevoirReponse(),
                "La réponse reçue doit correspondre à la réponse envoyée.");

        reseauServeur.fermerServeur();
        reseauClient.fermerClient();
    }

    @Test
    void utiliserReponse() {
        Reseau client = new Reseau();

        // Capturer la sortie standard pour vérifier l'affichage
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String reponseTest = "Réponse de test";
        client.utiliserReponse(reponseTest);

        // Vérification que la réponse a été affichée correctement
        assertEquals("Réponse du serveur : Réponse de test\n", outContent.toString(),
                "La réponse affichée doit correspondre à celle attendue.");

        // Restaurer la sortie standard
        System.setOut(originalOut);
    }

    @Test
    void fermerClient() {
        assertDoesNotThrow(reseauClient::fermerClient);
    }

    @Test
    void afficherIP() {
        try {
            InetAddress ip = Reseau.afficherIP();

            assertNotNull(ip, "L'adresse IP ne doit pas être nulle.");

            System.out.println("Test afficherIP : Adresse IP locale trouvée -> " + ip.getHostAddress());
        } catch (UnknownHostException e) {
            fail("Échec du test afficherIP en raison d'une erreur : " + e.getMessage());
        }
    }
}