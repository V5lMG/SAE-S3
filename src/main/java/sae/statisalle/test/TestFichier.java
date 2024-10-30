package sae.statisalle.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sae.statisalle.Fichier;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Génération ChatGPT
 */
class TestFichier {

    private Fichier fichier;

    @BeforeEach
    void setUp() throws IOException {
        // Création d'un fichier temporaire pour les tests
        File tempFile = File.createTempFile("testFichier", ".csv");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("Nom,Prenom,Telephone\nJohn,Doe,0123456789\n");
        }
        fichier = new Fichier(tempFile.getAbsolutePath());
    }

    @AfterEach
    void tearDown() {
        // Nettoyage : Supprimer le fichier temporaire
        File tempFile = fichier.getFichierExploite();
        if (tempFile.exists()) tempFile.delete();
    }

    @Test
    void extensionValide() {
        assertTrue(fichier.extensionValide(), "L'extension du fichier devrait être valide.");

        // Test avec une extension invalide
        Fichier fichierInvalide = new Fichier("test.txt");
        assertFalse(fichierInvalide.extensionValide(), "L'extension du fichier devrait être invalide.");
    }

    @Test
    void contenuFichier() {
        List<String> contenu = fichier.contenuFichier();
        assertNotNull(contenu, "Le contenu du fichier ne doit pas être null.");
        assertEquals(2, contenu.size(), "Le fichier devrait contenir deux lignes.");
        assertEquals("Nom,Prenom,Telephone", contenu.get(0), "La première ligne doit correspondre à l'en-tête attendu.");
    }

    @Test
    void nomFichier() {
        String nomFichier = fichier.nomFichier();
        assertTrue(nomFichier.startsWith("testFichier"), "Le nom du fichier devrait correspondre à 'testFichier'.");
    }

    @Test
    void getFichierExploite() {
        File fichierExploite = fichier.getFichierExploite();
        assertNotNull(fichierExploite, "L'objet File ne doit pas être null.");
        assertTrue(fichierExploite.exists(), "Le fichier exploité devrait exister.");
    }

    @Test
    void getTypeFichier() {
        String type = fichier.getTypeFichier();
        assertEquals("Employe", type, "Le type de fichier devrait être 'Employe' d'après le contenu.");

        // Tester un autre fichier
        File tempFileSalle = new File(fichier.getFichierExploite().getParent(), "testSalle.csv");
        try (FileWriter writer = new FileWriter(tempFileSalle)) {
            writer.write("Nom,Capacite\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Fichier fichierSalle = new Fichier(tempFileSalle.getAbsolutePath());
        assertEquals("Salle", fichierSalle.getTypeFichier(), "Le type de fichier devrait être 'Salle'.");

        tempFileSalle.delete(); // Nettoyer
    }
}