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

    private Fichier fichierActivite;

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
                fichierActivite = new Fichier(tempFile.getAbsolutePath());
            }
        }
    }

    @AfterEach
    void tearDown() {
        // Nettoyage : Supprimer le fichier temporaire

        File tempFileActivite = fichierActivite.getFichierExploite();
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
        assertTrue(fichierEmploye.extensionValide());
        assertTrue(fichierReservation.extensionValide());
        assertTrue(fichierSalle.extensionValide());
        assertTrue(fichierActivite.extensionValide());
    }

    @Test
    void contenuFichier() {
        List<String> contenuEmploye = fichierEmploye.contenuFichier();
        assertFalse(contenuEmploye.isEmpty());
        assertEquals(9, contenuEmploye.size());

        List<String> contenuReservation = fichierReservation.contenuFichier();
        assertFalse(contenuReservation.isEmpty());
        assertEquals(19, contenuReservation.size());

        List<String> contenuSalle = fichierSalle.contenuFichier();
        assertFalse(contenuSalle.isEmpty());
        assertEquals(10, contenuSalle.size());

        List<String> contenuActivite = fichierActivite.contenuFichier();
        assertFalse(contenuActivite.isEmpty());
        assertEquals(7, contenuActivite.size());
    }

    @Test
    void nomFichier() {
        String nomFichierEmploye = fichierEmploye.nomFichier();
        assertTrue(nomFichierEmploye.startsWith("employes 26_08_24 13_40"));

        String nomFichierReservation = fichierReservation.nomFichier();
        assertTrue(nomFichierReservation.startsWith("reservations 26_08_24 13_40"));

        String nomFichierSalle = fichierSalle.nomFichier();
        assertTrue(nomFichierSalle.startsWith("salles 26_08_24 13_40"));

        String nomFichierActivite = fichierActivite.nomFichier();
        assertTrue(nomFichierActivite.startsWith("activites 26_08_24 13_40"));
    }

    @Test
    void getFichierExploite() {
        assertNotNull(fichierEmploye.getFichierExploite());
        assertNotNull(fichierReservation.getFichierExploite());
        assertNotNull(fichierSalle.getFichierExploite());
        assertNotNull(fichierActivite.getFichierExploite());
    }

    @Test
    void getTypeFichier() {
        assertEquals("Employe", fichierEmploye.getTypeFichier());
        assertEquals("Reservation", fichierReservation.getTypeFichier());
        assertEquals("Salle", fichierSalle.getTypeFichier());
        assertEquals("Activite", fichierActivite.getTypeFichier());
    }

    @Test
    void reecritureFichier() {
        // Nouveau contenu pour le fichier d'employés
        List<String> nouveauContenu = List.of(
                "Ident;Nom;Prenom;Telephone",
                "E000009;Dupont;Marie;1234",
                "E000010;Martin;Paul;5678"
        );

        // Réécrire le fichier d'employés
        fichierEmploye.reecritureFichier(nouveauContenu);

        // Vérifier que le fichier a été réécrit correctement
        List<String> contenuReecrit = fichierEmploye.contenuFichier();
        assertEquals(nouveauContenu.size(), contenuReecrit.size());
        for (int i = 0; i < nouveauContenu.size(); i++) {
            assertEquals(nouveauContenu.get(i), contenuReecrit.get(i));
        }
    }

    @Test
    void ecritureFichier() {
        // Nouveau contenu pour le fichier de réservations
        List<String> nouveauContenu = List.of(
                "Ident;salle;employe;activite;date;heuredebut;heurefin",
                "R000019;00000002;E000002;réunion;15/10/2024;10h00;11h00"
        );

        // Chemin pour le fichier de sortie
        String cheminFichierSortie = "test_ecriture_reservation.csv";

        Fichier.ecritureFichier(nouveauContenu, cheminFichierSortie);

        Fichier fichierEcrit = new Fichier(cheminFichierSortie);
        List<String> contenuEcrit = fichierEcrit.contenuFichier();

        assertEquals(nouveauContenu.size(), contenuEcrit.size());
        for (int i = 0; i < nouveauContenu.size(); i++) {
            assertEquals(nouveauContenu.get(i), contenuEcrit.get(i));
        }

        new File(cheminFichierSortie).delete();
    }

    @Test
    void fichierExiste() {
        assertTrue(Fichier.fichierExiste(fichierEmploye.getFichierExploite().getAbsolutePath()));
        assertTrue(Fichier.fichierExiste(fichierReservation.getFichierExploite().getAbsolutePath()));
        assertTrue(Fichier.fichierExiste(fichierSalle.getFichierExploite().getAbsolutePath()));
        assertTrue(Fichier.fichierExiste(fichierActivite.getFichierExploite().getAbsolutePath()));

        assertFalse(Fichier.fichierExiste("non_existant_fichier.csv"));
    }

    @Test
    void ecrireFichier() {
        List<String> nouveauContenu = List.of(
                "Ident;Nom;Capacite;videoproj;ecranXXL;ordinateur;type;logiciels;imprimante",
                "00000010;salle Test;20;oui;non;2;PC;logiciels;oui"
        );

        // Chemin pour le fichier de sortie
        String cheminFichierSortie = "test_ecriture_salle.csv";

        Fichier.ecritureFichier(nouveauContenu, cheminFichierSortie);

        Fichier fichierEcrit = new Fichier(cheminFichierSortie);
        List<String> contenuEcrit = fichierEcrit.contenuFichier();

        assertEquals(nouveauContenu.size(), contenuEcrit.size());
        for (int i = 0; i < nouveauContenu.size(); i++) {
            assertEquals(nouveauContenu.get(i), contenuEcrit.get(i));
        }

        new File(cheminFichierSortie).delete();
    }

    @Test
    void ouvrirDossier() {
        assertNotNull(fichierEmploye.getFichierExploite());
        assertNotNull(fichierSalle.getFichierExploite());
        assertNotNull(fichierActivite.getFichierExploite());
        assertNotNull(fichierReservation.getFichierExploite());
    }
}