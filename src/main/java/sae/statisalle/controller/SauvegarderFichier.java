/*
 * SauvegarderFichier.java            13/11/2024
 * IUT DE RODEZ                       Pas de copyrights
 */
package sae.statisalle.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import sae.statisalle.Chiffrement;
import sae.statisalle.Fichier;
import sae.statisalle.Session;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static sae.statisalle.controller.MainControleur.activerAccueil;

/**
 * Contrôleur pour la sauvegarde de fichiers dans l'application StatiSalle.
 * Permet de sélectionner des répertoires de sauvegarde pour chaque fichier,
 * de définir un nom de fichier, puis de sauvegarder le contenu
 * dans un format CSV. Le nombre de fichiers est déterminé par le nombre de
 * séparateurs "/EOF" dans le contenu.
 * <p>
 * Les boutons et champs de texte sont activés en fonction
 * du nombre de fichiers.
 * Chaque fichier peut être sauvegardé dans un dossier différent et les états
 * sont actualisés pour guider l'utilisateur dans la complétion de ses actions.
 */
public class SauvegarderFichier {

    @FXML
    private TextField text1, text2, text3, text4;
    @FXML
    private Button btnFichier1, btnFichier2, btnFichier3, btnFichier4;
    @FXML
    private Button btnEnregistrer;
    @FXML
    private Text fichier1, fichier2, fichier3, fichier4;

    private int nombreDeFichiers;
    private String chemin1, chemin2, chemin3, chemin4;

    /**
     * Initialise la page en comptant les fichiers et
     * affichant les champs nécessaires.
     */
    @FXML
    public void initialiserPage() {
        String contenuFichierCrypte = Session.getContenu();
        String cle = Session.getCle();
        String contenuFichier = Chiffrement.dechiffrementDonnees(contenuFichierCrypte, cle);
        nombreDeFichiers = Fichier.compterMotif(contenuFichier, "/EOF");
        afficherChamps(nombreDeFichiers);
        mettreAJourEtatBoutonEnregistrer();
    }

    /**
     * Active le bouton de sauvegarde si tous les chemins sont complets.
     */
    private void mettreAJourEtatBoutonEnregistrer() {
        boolean tousLesCheminsComplets = true;
        String[] chemins = {
                chemin1,
                chemin2,
                chemin3,
                chemin4};

        for (int i = 0; i < nombreDeFichiers; i++) {
            if (chemins[i] == null || chemins[i].isEmpty()) {
                tousLesCheminsComplets = false;
                break;
            }
        }

        if (tousLesCheminsComplets) {
            btnEnregistrer.setStyle("-fx-background-color: green;");
        } else {
            btnEnregistrer.setStyle("");
        }
    }

    /**
     * Affiche les champs de saisie et boutons selon le nombre de fichiers.
     *
     * @param nombre Le nombre de fichiers à afficher.
     */
    private void afficherChamps(int nombre) {
        TextField[] textFields = {
                text1,
                text2,
                text3,
                text4};

        Button[] boutons = {
                btnFichier1,
                btnFichier2,
                btnFichier3,
                btnFichier4};

        Text[] fichiers = {
                fichier1,
                fichier2,
                fichier3,
                fichier4};

        for (int i = 0; i < 4; i++) {
            boolean visible = (i < nombre);
            textFields[i].setVisible(visible);
            boutons[i].setVisible(visible);
            fichiers[i].setVisible(visible);
        }
    }

    /**
     * Action de sauvegarde des fichiers en fonction des chemins
     * et noms définis.
     */
    @FXML
    private void actionEnregistrer() {
        sauvegarderFichiers();
    }

    /**
     * Sauvegarde chaque fichier en fonction de son chemin et de son contenu.
     */
    private void sauvegarderFichiers() {
        String contenuFichier = Session.getContenu();
        String cle = Session.getCle();
        String contenuDechiffre = Chiffrement.dechiffrementDonnees(contenuFichier, cle);

        String[] chemins = {
                chemin1,
                chemin2,
                chemin3,
                chemin4};
        TextField[] champsTexte = {
                text1,
                text2,
                text3,
                text4};

        try {
            for (int i = 0; i < nombreDeFichiers; i++) {
                if (chemins[i] != null && !champsTexte[i].getText().isEmpty()) {
                    String contenu = contenuDechiffre.split("/EOF")[i];
                    List<String> contenuListe = Arrays.asList(contenu.split("\n"));

                    Fichier.ecritureFichier(contenuListe, chemins[i]
                                            + champsTexte[i].getText()
                                            + ".csv");

                    Fichier.ouvrirDossier(chemins[i]
                                          + champsTexte[i].getText() + ".csv");
                }
            }
            showConfirmation();
            activerAccueil();
        } catch (Exception ex) {
            MainControleur.showAlert("Erreur d'enregistrement",
                    "Une erreur est survenue lors "
                            + "de la sauvegarde des fichiers.");
        }
    }

    /**
     * Affiche une boîte de dialogue de confirmation.
     */
    private void showConfirmation() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Enregistrement");
        alert.setContentText("Tous les fichiers ont été enregistrés "
                             + "avec succès.");
        alert.showAndWait();
    }

    /**
     * Action pour choisir un dossier de sauvegarde pour un fichier.
     *
     * @param retour    Le texte où afficher le chemin.
     * @param champNom  Le champ de texte pour le nom du fichier.
     * @param nombre    Numéro du fichier.
     */
    private void choisirDossier(Text retour, TextField champNom, int nombre) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File dossier = directoryChooser.showDialog(new Stage());

        if (dossier != null) {
            String chemin = dossier.getAbsolutePath() + File.separator;
            String nomFichier = champNom.getText().isEmpty() ?
                    "fichier_exporte.csv" : champNom.getText() + ".csv";
            retour.setText("Fichier " + nombre + " : " + chemin + nomFichier);

            switch (nombre) {
                case 1 -> chemin1 = chemin;
                case 2 -> chemin2 = chemin;
                case 3 -> chemin3 = chemin;
                case 4 -> chemin4 = chemin;
            }
            mettreAJourEtatBoutonEnregistrer();
        }
    }

    @FXML private void actionChoixDossier1() {
        choisirDossier(fichier1, text1, 1);
    }
    @FXML private void actionChoixDossier2() {
        choisirDossier(fichier2, text2, 2);
    }
    @FXML private void actionChoixDossier3() {
        choisirDossier(fichier3, text3, 3);
    }
    @FXML private void actionChoixDossier4() {
        choisirDossier(fichier4, text4, 4);
    }

    @FXML private void nomFichier1() {
        afficherNomFichier(fichier1, chemin1, text1);
    }
    @FXML private void nomFichier2() {
        afficherNomFichier(fichier2, chemin2, text2);
    }
    @FXML private void nomFichier3() {
        afficherNomFichier(fichier3, chemin3, text3);
    }
    @FXML private void nomFichier4() {
        afficherNomFichier(fichier4, chemin4, text4);
    }

    /**
     * Met à jour le texte affiché pour le nom du fichier.
     *
     * @param fichier  Le texte à afficher
     * @param chemin   Le chemin du fichier
     * @param textField Le champ de texte contenant le nom du fichier
     */
    private void afficherNomFichier(Text fichier,
                                    String chemin,
                                    TextField textField) {
        fichier.setText("Fichier : " + (chemin != null ? chemin : "")
                        + textField.getText() + ".csv");
        mettreAJourEtatBoutonEnregistrer();
    }

    /**
     * Retourne à la page d'accueil.
     */
    @FXML
    private void actionRetour() {
        activerAccueil();
    }

    /**
     * Affiche l'aide de l'application.
     */
    @FXML
    private void actionAide() {
        // TODO
    }
}
