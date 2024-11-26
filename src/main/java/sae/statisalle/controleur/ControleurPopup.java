/*
 * ControleurPopup.java            24/10/2024
 * IUT DE RODEZ                    Pas de copyrights
 */
package sae.statisalle.controleur;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import sae.statisalle.modele.Fichier;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * La classe ControleurPopup est responsable
 * de la gestion des popups dans l'application.
 * Elle permet d'afficher des fenêtres de confirmation
 * ou de visualisation liées à l'importation
 * et au traitement des fichiers reçus.
 * Elle gère également l'enregistrement
 * des fichiers dans un répertoire spécifique
 * si l'utilisateur choisit de charger les données.
 *
 * @author Valentin Munier-Génie
 */
public class ControleurPopup {

    /**
     * Affiche une popup permettant à l'utilisateur
     * de choisir une action après avoir reçu un
     * ou plusieurs fichiers.
     * La popup propose trois options :
     * charger les fichiers dans l'application,
     * visualiser les fichiers reçus
     * ou annuler l'action.
     * <br>
     * Si l'utilisateur choisit de charger les fichiers,
     * ces derniers sont enregistrés dans
     * un répertoire spécifique.
     * En cas d'erreur de création du répertoire ou
     * d'enregistrement des fichiers, une exception est levée et
     * un message d'erreur est affiché.
     * Si l'utilisateur choisit de visualiser les fichiers,
     * une popup de visualisation des données s'ouvre avec les fichiers reçus.
     *
     * @param donnees Les données reçues sous forme de chaîne de caractères,
     *                représentant les fichiers à traiter.
     */
    public static void afficherPopupFichierRecu(String donnees) {

        Alert popup = new Alert(Alert.AlertType.CONFIRMATION);

        Image logo = new Image(Objects.requireNonNull(
                               ControleurPopup.class.getResourceAsStream(
                         "/sae/statisalle/img/LogoStatisalle.jpg")));
        Stage stage = (Stage) popup.getDialogPane().getScene().getWindow();
        stage.getIcons().add(logo);

        popup.setTitle("Fichier Reçu");
        popup.setHeaderText("Vous avez reçu un (ou plusieurs) fichier(s).");
        popup.setContentText("Que souhaitez-vous faire ?");

        ButtonType boutonCharger = new ButtonType("Charger dans "
                                                  + "l'application");
        ButtonType boutonVisualiser = new ButtonType("Visualiser");
        ButtonType boutonAnnuler = new ButtonType("Annuler",
                ButtonBar.ButtonData.CANCEL_CLOSE);

        popup.getButtonTypes().setAll(boutonCharger,
                                      boutonVisualiser,
                                      boutonAnnuler);

        Optional<ButtonType> resultat = popup.showAndWait();

        String reponseAvecRetourLigne = donnees.replace("/N", "\n")
                                               .replace("/R", "\r");

        String[] fichiers = reponseAvecRetourLigne.split("/EOF");

        if (resultat.isPresent()) {
            if (resultat.get() == boutonVisualiser) {
                afficherPopupVisualiser(reponseAvecRetourLigne);
            } else if (resultat.get() == boutonCharger) {
                String dateDuJour = new SimpleDateFormat("ddMMyyyy")
                                                           .format(new Date());

                try {
                    // vérifier et créer le répertoire
                    File dossier =
                            new File("src/main/resources/csv/");
                    if (!dossier.exists() && !dossier.mkdirs()) {
                        throw new IOException("Erreur lors de la "
                                              + "création du répertoire.");
                    }

                    // parcourir chaque fichier extrait et les sauvegarder
                    for (String fichier : fichiers) {
                        List<String> donneesEnListe =
                                Arrays.asList(fichier.split("\n"));

                        String nomFichier = Fichier.getTypeDepuisContenu(
                                donneesEnListe
                        ) + "_" + dateDuJour + ".csv";

                        // créer ou réécrire le fichier avec les données reçues
                        Fichier.ecritureFichier(donneesEnListe,
                                    "src/main/resources/csv/"
                                                + nomFichier);
                        System.out.println("Les données ont été sauvegardées "
                                           + "dans : " + nomFichier);
                    }

                    // renvoie l'utilisateur vers l'affichage des données
                    MainControleur.activerAffichage();
                } catch (IOException e) {
                    MainControleur.showAlert(Alert.AlertType.WARNING, "Erreur d'importation",
                            "Impossible de charger les données "
                                    + "dans l'application : "
                                    + e.getMessage());
                }
            } else {
                System.out.println("Action annulée.");
                popup.close();
            }
        }
    }

    /**
     * Affiche une popup pour visualiser
     * les données reçues sous forme de texte.
     * @param donnees Les données reçues sous forme de chaîne de caractères
     */
    private static void afficherPopupVisualiser(String donnees) {
        Dialog<ButtonType> popupVisualiser = new Dialog<>();
        popupVisualiser.setTitle("Visualisation des données");
        popupVisualiser.setHeaderText("Données reçues");

        // création du contenu de la popup
        String contenu = donnees.replace("/EOF", "\n\n");
        TextArea zoneTexte = new TextArea(contenu);
        zoneTexte.setEditable(false);
        zoneTexte.setWrapText(true);

        ButtonType boutonRetour = new ButtonType("Retour",
                                                 ButtonBar.ButtonData.OK_DONE);
        popupVisualiser.getDialogPane().getButtonTypes().add(boutonRetour);

        popupVisualiser.getDialogPane().setContent(zoneTexte);

        // gestion du retour
        popupVisualiser.showAndWait().ifPresent(result -> {
            if (result == boutonRetour) {
                afficherPopupFichierRecu(donnees);
            }
        });
    }
}
