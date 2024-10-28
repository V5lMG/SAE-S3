package sae.statisalle;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        File tempFile = File.createTempFile("activites 26_08_24 13_40",".csv");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("Ident;Activité\n" +
                    "A0000001;réunion\n" +
                    "A0000002;formation\n" +
                    "A0000003;entretien de la salle\n" +
                    "A0000004;prêt\n" +
                    "A0000005;location\n" +
                    "A0000006;autre ");
        }
        fichier = new Fichier(tempFile.getAbsolutePath());
    }

    @AfterEach
    void tearDown() {
        // Nettoyage : Supprimer le fichier temporaire
        File tempFile = fichier.getFichierExploite();
        if (tempFile.exists()) {
            tempFile.delete();
        }
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
        assertEquals(7, contenu.size(), "Le fichier devrait contenir sept lignes.");
        assertEquals("Ident;Activité", contenu.get(0), "La première ligne doit correspondre à l'en-tête attendu.");
    }

    @Test
    void nomFichier() {
        String nomFichier = fichier.nomFichier();
        assertTrue(nomFichier.startsWith("activites 26_08_24 13_40"), "Le nom du fichier devrait correspondre à 'activites 26_08_24 13_40'.");
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
        assertEquals("Activite", type, "Le type de fichier devrait être 'Employe' d'après le contenu.");

        // Tester un autre fichier
        File tempFileSalle = new File(fichier.getFichierExploite().getParent(), "salles 26_08_24 13_40.csv");
        try (FileWriter writer = new FileWriter(tempFileSalle)) {
            writer.write("Ident;Nom;Capacite;videoproj;ecranXXL;ordinateur;type;logiciels;imprimante");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Fichier fichierSalle = new Fichier(tempFileSalle.getAbsolutePath());
        assertEquals("Salle", fichierSalle.getTypeFichier(), "Le type de fichier devrait être 'Salle'.");

        tempFileSalle.delete(); // Nettoyer
    }
}