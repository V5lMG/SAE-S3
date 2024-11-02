package sae.statisalle;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test pour la classe Fichier.
 *
 * Cette classe vérifie plusieurs aspects du fonctionnement de la classe Fichier,
 * tels que la validité des extensions de fichiers, la récupération du nom du fichier
 * sans l'extension, la lecture du contenu du fichier et le type de fichier détecté.
 *
 * <p>Les tests couvrent les fonctionnalités suivantes :
 * <ul>
 *   <li>Validation de l'extension du fichier           </li>
 *   <li>Récupération du contenu du fichier             </li>
 *   <li>Récupération du nom du fichier sans extension  </li>
 *   <li>Accès au fichier exploité                      </li>
 *   <li>Détection du type de fichier                   </li>
 * </ul>
 * @author erwan.thierry
 */
class TestFichier {

    private Fichier fichierEmploye;

    private Fichier fichierReservation;

    private Fichier fichierSalle;

    private Fichier fichierActvite;

    private String[] cheminFichier = {
            "employes 26_08_24 13_40",
            "reservations 26_08_24 13_40",
            "salles 26_08_24 13_40",
            "activites 26_08_24 13_40"
    };

    private String[] contenuFichier = {
            /* Contenu du fichier employe */
            "Ident;Nom;Prenom;Telephone\n" +
            "E000001;Dupont;Pierre;2614\n" +
            "E000002;Lexpert;Noemie;2614\n" +
            "E000003;Dujardin;Océane;2633\n" +
            "E000004;Durand;Bill;2696\n" +
            "E000005;Dupont;Max;\n" + // Pas de numéro de téléphone
            "E000006;Martin;Martin;2678\n" +
            "E000007;Legrand;Jean-Pierre;2689\n" +
            "E000008;Deneuve;Zoé;2626\n",

            /* Contenu du fichier réservation */
            "Ident;salle;employe;activite;date;heuredebut;heurefin;;;;;\n" +
            "R000001;00000001;E000001;prét;07/10/2024;17h00;19h00;club gym;Legendre;Noémie;0600000000;reunion\n" +
            "R000002;00000004;E000001;réunion;07/10/2024;15h00;18h00;réunion avec client;;;;\n" +
            "R000003;00000004;E000005;réunion;07/10/2024;10h00;11h00;Préparation réunion client;;;;\n" +
            "R000004;00000004;E000002;réunion;08/10/2024;09h00;11h00;;;;;\n" +
            "R000005;00000001;E000003;prét;08/10/2024;17h00;19h00;club gym;Legendre;Noémie;0600000000;AG\n" +
            "R000006;00000008;E000007;autre;09/10/2024;09h00;12h00;tests candidats;;;;\n" +
            "R000007;00000003;E000007;réunion;07/10/2024;15h00;18h00;présentation maquette;;;;\n" +
            "R000008;00000001;E000003;formation;10/10/2024;08h00;18h00;Bureautique;Leroux;Jacques;0600000001;\n" +
            "R000009;00000001;E000003;formation;11/10/2024;08h00;18h00;Bureautique;Leroux;Jacques;0600000001;\n" +
            "R000010;00000003;E000008;réunion;07/10/2024;10h00;12h00;accueil nouveau membre;;;;\n" +
            "R000011;00000008;E000001;autre;10/10/2024;09h00;12h00;tests candidats;;;;\n" +
            "R000012;00000006;E000007;réunion;15/10/2024;09h00;10h00;point avec stagiaire;;;;\n" +
            "R000013;00000008;E000003;entretien;11/10/2024;08h00;17h00;mise à jour logiciels;;;;\n" +
            "R000014;00000009;E000007;entretien;11/10/2024;08h00;17h00;mise à jour logiciels;;;;\n" +
            "R000015;00000006;E000007;réunion;16/10/2024;10h00;11h00;visite tuteur IUT;;;;\n" +
            "R000016;00000006;E000005;réunion;17/10/2024;14h00;15h30;validation maquette;;;;\n" +
            "R000017;00000001;E000008;location;18/10/2024;8h00;13h00;Mairie;Marin;Hector;0666666666;réunion\n" +
            "R000018;00000001;E000005;location;18/10/2024;13h00;19h00;Département;Tournefeuille;Michel;0655555555;réunion\n",

            /* Contenu du fichier salle */
            "Ident;Nom;Capacite;videoproj;ecranXXL;ordinateur;type;logiciels;imprimante\n" +
            "00000001;A6;15;oui;non;4;PC portable;bureautique;non\n" +
            "00000002;salle bleue;18;oui;oui;;;;\n" +
            "00000003;salle ronde;14;oui;non;;;;\n" +
            "00000004;salle Picasso;15;non;non;;;;\n" +
            "00000005;petite salle;7;oui;oui;;;;\n" +
            "00000006;A7;4;non;non;;;;\n" +
            "00000007;salle patio;6;non;non;;;;\n" +
            "00000008;salle Sydney;20;oui;non;16;PC Windows;\"bureautique, java, Intellij\";non\n" +
            "00000009;salle Brisbane;22;oui;non;18;PC Windows;\"bureautique, java, Intellij, photosphop\";oui\n",

            /* Contenu du fichier salle */
            "Ident;Activité\n" +
            "A0000001;réunion\n" +
            "A0000002;formation\n" +
            "A0000003;entretien de la salle\n" +
            "A0000004;prét\n" +
            "A0000005;location\n" +
            "A0000006;autre \n"
    };

    @BeforeEach
    void setUp() throws IOException {
        // Création d'un fichier temporaire pour les tests
        for (int i = 0; i < cheminFichier.length; i++) {
            File tempFile = File.createTempFile(cheminFichier[i],".csv");
            try (FileWriter writer = new FileWriter(tempFile)) {
                writer.write(contenuFichier[i]);
            }
            if (cheminFichier[i].contains("employes")) {
                fichierEmploye = new Fichier(tempFile.getAbsolutePath());
            }
            if (cheminFichier[i].contains("reservations")) {
                fichierReservation = new Fichier(tempFile.getAbsolutePath());
            }
            if (cheminFichier[i].contains("salles")) {
                fichierSalle = new Fichier(tempFile.getAbsolutePath());
            }
            if (cheminFichier[i].contains("activites")) {
                fichierActvite = new Fichier(tempFile.getAbsolutePath());
            }
        }
    }

    @AfterEach
    void tearDown() {
        // Nettoyage : Supprimer le fichier temporaire

        File tempFileActivite = fichierActvite.getFichierExploite();
        if (tempFileActivite.exists()) {
            tempFileActivite.delete();
        }

        File tempFileSalle = fichierSalle.getFichierExploite();
        if (tempFileSalle.exists()) {
            tempFileSalle.delete();
        }

        File tempFileReservation = fichierReservation.getFichierExploite();
        if (tempFileReservation.exists()) {
            tempFileReservation.delete();
        }

        File tempFileEmploye = fichierEmploye.getFichierExploite();
        if (tempFileEmploye.exists()) {
            tempFileEmploye.delete();
        }
    }

    @Test
    void extensionValide() {
        assertTrue(fichierEmploye.extensionValide(), "L'extension du fichier devrait être valide.");
        assertTrue(fichierReservation.extensionValide(), "L'extension du fichier devrait être valide.");
        assertTrue(fichierSalle.extensionValide(), "L'extension du fichier devrait être valide.");
        assertTrue(fichierActvite.extensionValide(), "L'extension du fichier devrait être valide.");
    }

    @Test
    void contenuFichier() {
        List<String> contenuEmploye = fichierEmploye.contenuFichier();
        assertNotNull(contenuEmploye, "Le contenu du fichier ne doit pas être null.");
        assertEquals("Ident;Nom;Prenom;Telephone", contenuEmploye.getFirst(), "La première ligne doit correspondre à l'en-tête attendu.");

        List<String> contenuReservation = fichierReservation.contenuFichier();
        assertNotNull(contenuReservation, "Le contenu du fichier ne doit pas être null.");
        assertEquals("Ident;salle;employe;activite;date;heuredebut;heurefin;;;;;", contenuReservation.getFirst(), "La première ligne doit correspondre à l'en-tête attendu.");

        List<String> contenuSalle = fichierSalle.contenuFichier();
        assertNotNull(contenuSalle, "Le contenu du fichier ne doit pas être null.");
        assertEquals("Ident;Nom;Capacite;videoproj;ecranXXL;ordinateur;type;logiciels;imprimante", contenuSalle.getFirst(), "La première ligne doit correspondre à l'en-tête attendu.");

        List<String> contenuActivite = fichierActvite.contenuFichier();
        assertNotNull(contenuActivite, "Le contenu du fichier ne doit pas être null.");
        assertEquals("Ident;Activité", contenuActivite.getFirst(), "La première ligne doit correspondre à l'en-tête attendu.");
    }

    @Test
    void nomFichier() {
        String nomFichierEmploye = fichierEmploye.nomFichier();
        assertTrue(nomFichierEmploye.startsWith("employes 26_08_24 13_40"), "Le nom du fichier devrait correspondre à 'employe 26_08_24 13_40'.");

        String nomFichierRservation = fichierReservation.nomFichier();
        assertTrue(nomFichierRservation.startsWith("reservations 26_08_24 13_40"), "Le nom du fichier devrait correspondre à 'reservation 26_08_24 13_40'.");

        String nomFichierSalle = fichierSalle.nomFichier();
        assertTrue(nomFichierSalle.startsWith("salles 26_08_24 13_40"), "Le nom du fichier devrait correspondre à 'salles 26_08_24 13_40'.");

        String nomFichierActivite = fichierActvite.nomFichier();
        assertTrue(nomFichierActivite.startsWith("activites 26_08_24 13_40"), "Le nom du fichier devrait correspondre à 'activites 26_08_24 13_40'.");
    }

    @Test
    void getTypeFichier() {
        String typeEmploye = fichierEmploye.getTypeFichier();
        assertEquals("Employe", typeEmploye, "Le type de fichier devrait être 'Employe' d'après le contenu.");

        String typeReservation = fichierReservation.getTypeFichier();
        assertEquals("Reservation", typeReservation, "Le type de fichier devrait être 'Reservation' d'après le contenu.");

        String typeSalle = fichierSalle.getTypeFichier();
        assertEquals("Salle", typeSalle, "Le type de fichier devrait être 'Salle' d'après le contenu.");

        String typeActivite = fichierActvite.getTypeFichier();
        assertEquals("Activite", typeActivite, "Le type de fichier devrait être 'Activite' d'après le contenu.");

    }
}