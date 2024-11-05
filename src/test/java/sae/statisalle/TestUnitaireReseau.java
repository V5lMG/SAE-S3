/*
 * TestUnitaireReseau.java    21/10/2024
 * Pas de droit d'auteur ni de copyright
 */
package sae.statisalle;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sae.statisalle.exception.MauvaiseConnexionServeur;

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

    private Reseau reseauServeur;
    private Reseau reseauClient;
    private final String TEST_FILE_PATH = "FichierDeTest.csv";

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

    @AfterEach
    void nettoyage() {
        reseauServeur.fermerServeur();
        reseauClient.fermerClient();
        try {
            Files.deleteIfExists(Paths.get(TEST_FILE_PATH));
        } catch (IOException e) {
            fail("Échec lors de la suppression du fichier de test : " + e.getMessage());
        }
    }

    @Test
    void testPreparerServeurSuccess() {
        assertDoesNotThrow(() -> reseauServeur.preparerServeur(12345), "Le serveur n'a pas pu démarrer sur un port valide.");
    }

    @Test
    void testPreparerServeurPortInvalide() {
        assertThrows(IllegalArgumentException.class, () -> reseauServeur.preparerServeur(70000), "Un port invalide doit lever une exception.");
        assertThrows(IllegalArgumentException.class, () -> reseauServeur.preparerServeur(-1), "Un port invalide doit lever une exception.");
    }

    @Test
    void testPreparerServeurPortDejaUtilise() throws IOException {
        ServerSocket serverSocket = new ServerSocket(12346);
        assertThrows(IOException.class, () -> reseauServeur.preparerServeur(12346), "Un port déjà utilisé doit lever une IOException.");
        serverSocket.close();
    }

    @Test
    void testPreparerClientErreurConnexion() {
        assertThrows(MauvaiseConnexionServeur.class, () -> reseauClient.preparerClient("localhost", 12347), "Connexion impossible doit lever MauvaiseConnexionServeur.");
    }

    @Test
    void testRecevoirDonneesErreur() {
        // Forcer la fermeture du flux avant la réception pour provoquer une IOException
        reseauServeur.fluxEntree = new BufferedReader(new StringReader("Test"));
        reseauServeur.fermerServeur();
        assertNull(reseauServeur.recevoirDonnees(), "La réception des données sur un flux fermé doit retourner null.");
    }

    @Test
    void testEnvoyerErreurFichierInvalide() {
        assertThrows(IllegalArgumentException.class, () -> reseauClient.envoyer("FichierInexistant.csv"), "L'envoi d'un fichier inexistant doit lever une exception.");
    }

    @Test
    void testEnvoyerErreurSurFlux() throws IOException {
        reseauClient.fluxSortie = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream())));
        reseauClient.fermerClient();  // Ferme les flux pour provoquer une erreur
        assertDoesNotThrow(() -> reseauClient.envoyer(TEST_FILE_PATH), "L'envoi après fermeture des flux ne doit pas provoquer d'exception.");
    }

    @Test
    void testEnvoyerReponseErreur() {
        reseauServeur.fluxSortie = null;  // Simule un flux de sortie null
        assertDoesNotThrow(() -> reseauServeur.envoyerReponse("Test"), "Envoi d'une réponse sans flux de sortie ne doit pas provoquer d'exception.");
    }

    @Test
    void testRecevoirReponseErreur() {
        reseauClient.fluxEntree = null;  // Simule un flux d'entrée null
        assertEquals("Erreur lors de la réception de la réponse.", reseauClient.recevoirReponse(), "Réception sans flux d'entrée doit retourner un message d'erreur.");
    }

    @Test
    void testRenvoyerIPErreur() {
        InetAddress ip = Reseau.renvoyerIP();
        assertNotNull(ip, "L'adresse IP ne doit pas être nulle même si une erreur survient.");
    }

    @Test
    void testFermerServeurErreur() {
        assertDoesNotThrow(() -> reseauServeur.fermerServeur(), "La fermeture du serveur sans connexion ne doit pas provoquer d'exception.");
    }

    @Test
    void testFermerClientErreur() {
        assertDoesNotThrow(() -> reseauClient.fermerClient(), "La fermeture du client sans connexion ne doit pas provoquer d'exception.");
    }

    @Test
    void testUtiliserReponseErreur() {
        reseauClient.utiliserReponse(null);
        assertEquals("", "", "La méthode utiliserReponse ne doit rien afficher si la réponse est null.");
    }

}