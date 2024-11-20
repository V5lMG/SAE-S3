package sae.statisalle;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import sae.statisalle.exception.MauvaiseConnexionServeur;
import sae.statisalle.modele.Chiffrement;
import sae.statisalle.modele.Reseau;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe Reseau.
 */
public class TestUnitaireReseau {

    private static Reseau serveur;
    private static Thread serveurThread;
    private static int portTest; // Port dynamique pour éviter les conflits
    private static final String LOCALHOST = "127.0.0.1";

    private static int obtenirPortLibre() {
        try (java.net.ServerSocket socket = new java.net.ServerSocket(0)) {
            return socket.getLocalPort();
        } catch (IOException e) {
            fail("Impossible de trouver un port libre : " + e.getMessage());
            return -1;
        }
    }

    @BeforeAll
    static void initialiserServeur() {
        serveur = new Reseau();
        portTest = obtenirPortLibre();
        serveurThread = new Thread(() -> {
            try {
                serveur.preparerServeur(portTest);
                Reseau clientHandler = serveur.attendreConnexionClient();
                if (clientHandler != null) {
                    String donnees = clientHandler.recevoirDonnees();
                    clientHandler.envoyerReponse(donnees);
                }
            } catch (IOException e) {
                System.err.println("Erreur serveur : " + e.getMessage());
            } finally {
                serveur.fermerServeur();
            }
        });
        serveurThread.start();
    }

    @AfterAll
    static void arreterServeur() {
        serveur.fermerServeur();
        try {
            if (serveurThread != null) {
                serveurThread.join(1000);
            }
        } catch (InterruptedException e) {
            System.err.println("Erreur lors de l'arrêt du serveur : "
                    + e.getMessage());
        }
    }

    @Test
    void testPreparationServeur() {
        int portTest = obtenirPortLibre();
        Reseau serveurTest = new Reseau();
        assertDoesNotThrow(() -> serveurTest.preparerServeur(portTest),
                "La préparation du serveur ne doit pas lever d'exception.");
        serveurTest.fermerServeur();
    }

    @Test
    @Timeout(10)
    void testConnexionClientServeur() {
        int portTest = obtenirPortLibre();
        Reseau serveur = new Reseau();
        Reseau client = new Reseau();

        Thread threadServeur = new Thread(() -> {
            try {
                serveur.preparerServeur(portTest);
                Reseau clientHandler = serveur.attendreConnexionClient();
                if (clientHandler != null) {
                    String messageRecu = clientHandler.recevoirDonnees();
                    clientHandler.envoyerReponse(messageRecu);
                }
            } catch (IOException e) {
                fail("Erreur du serveur : " + e.getMessage());
            } finally {
                serveur.fermerServeur();
            }
        });

        try {
            threadServeur.start();
            Thread.sleep(500);

            client.preparerClient(LOCALHOST, portTest);
            String message = "Bonjour serveur";
            client.envoyer(message);

            String reponse = client.recevoirReponse();
            assertNotNull(reponse, "La réponse ne doit pas être null.");
            assertTrue(reponse.contains("/DELIM/") || !reponse.equals(message),
                    "La réponse doit contenir les données transformées par le serveur.");
        } catch (MauvaiseConnexionServeur | InterruptedException e) {
            fail("La connexion client-serveur a échoué : " + e.getMessage());
        } finally {
            client.fermerClient();
        }

        try {
            threadServeur.join();
        } catch (InterruptedException e) {
            fail("Le thread serveur a été interrompu : " + e.getMessage());
        }
    }

    @Test
    @Timeout(10)
    void testAttendreConnexionClient() {
        int portTest = obtenirPortLibre();
        Reseau serveur = new Reseau();
        Thread threadServeur = new Thread(() -> {
            try {
                serveur.preparerServeur(portTest);
                serveur.attendreConnexionClient();
            } catch (IOException e) {
                fail("Erreur lors de l'attente d'une connexion client : "
                        + e.getMessage());
            } finally {
                serveur.fermerServeur();
            }
        });

        Reseau client = new Reseau();
        try {
            threadServeur.start();
            Thread.sleep(500);
            assertDoesNotThrow(() -> client.preparerClient(LOCALHOST, portTest),
                    "La connexion au serveur ne doit pas lever d'exception.");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            client.fermerClient();
        }

        try {
            threadServeur.join();
        } catch (InterruptedException e) {
            fail("Le thread serveur a été interrompu : " + e.getMessage());
        }
    }

    @Test
    void testReceptionDonneesNull() {
        Reseau serveurTest = new Reseau();
        serveurTest.fermerServeur();

        assertNull(serveurTest.getFluxEntree(),
                "Le flux d'entrée doit être null lorsque le serveur est fermé.");

        assertDoesNotThrow(() -> {
            String result = serveurTest.recevoirDonnees();
            assertNull(result, "recevoirDonnees() doit retourner null lorsque le "
                    + "flux d'entrée est null.");
        }, "recevoirDonnees() ne doit pas lever d'exception si le flux est null.");
    }

    @Test
    void testTraiterRequete() {
        Reseau serveur = new Reseau();

        String cleValide = "clé123";
        String donneesChiffrees = Chiffrement.chiffrementDonnees("message", cleValide);
        String requeteValide = cleValide + "/DELIM/" + donneesChiffrees;

        assertDoesNotThrow(() -> {
            String resultat = serveur.traiterRequete(requeteValide);
            assertEquals("message", resultat, "Les données déchiffrées doivent "
                    + "correspondre au message original.");
        });

        assertThrows(IllegalArgumentException.class,
                () -> serveur.traiterRequete(null),
                "Une requête null doit lever une IllegalArgumentException.");

        String requeteMalFormateeSansDelim = cleValide + donneesChiffrees;
        assertThrows(IllegalArgumentException.class,
                () -> serveur.traiterRequete(requeteMalFormateeSansDelim),
                "Une requête sans délimiteur doit lever une IllegalArgumentException.");

        String requeteMalFormateeAvecTropDeDelim = cleValide + "/DELIM/"
                + donneesChiffrees + "/DELIM/extra";
        assertThrows(IllegalArgumentException.class,
                () -> serveur.traiterRequete(requeteMalFormateeAvecTropDeDelim),
                "Une requête avec plusieurs délimiteurs doit lever une "
                        + "IllegalArgumentException.");
    }

    @Test
    void testPreparationClientInvalide() {
        Reseau client = new Reseau();

        MauvaiseConnexionServeur exception = assertThrows(
                MauvaiseConnexionServeur.class,
                () -> client.preparerClient("adresse_invalide", obtenirPortLibre()),
                "Une connexion avec une adresse invalide doit lever une exception.");
        assertTrue(exception.getMessage().contains("Impossible de se connecter"));

        IllegalArgumentException exception2 = assertThrows(
                IllegalArgumentException.class,
                () -> client.preparerClient(LOCALHOST, 99999),
                "Une connexion avec un port hors limites doit lever une exception.");
        assertTrue(exception2.getMessage().contains("port out of range"));
    }

    @Test
    void testRenvoyerIP() {
        assertDoesNotThrow(() -> {
            assertNotNull(Reseau.renvoyerIP(),
                    "L'adresse IP locale ne doit pas être null.");
        }, "La méthode renvoyerIP ne doit pas lever d'exception.");
    }
}
