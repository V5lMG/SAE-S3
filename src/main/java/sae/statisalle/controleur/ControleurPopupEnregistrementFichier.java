package sae.statisalle.controleur;

import javafx.scene.control.*;
import sae.statisalle.modele.Fichier;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ControleurPopupEnregistrementFichier {

    /**
     * Affiche une popup avec des boutons d'action pour gérer un fichier reçu.
     */
    public static void afficherPopupFichierRecu(String donnees) {

        Alert popup = new Alert(Alert.AlertType.CONFIRMATION);
        popup.setTitle("Fichier Reçu");
        popup.setHeaderText("Vous avez reçu un (ou plusieurs) fichier(s).");
        popup.setContentText("Que souhaitez-vous faire ?");

        ButtonType boutonCharger = new ButtonType("Charger dans l'application");
        ButtonType boutonVisualiser = new ButtonType("Visualiser");
        ButtonType boutonAnnuler = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);

        popup.getButtonTypes().setAll(boutonCharger, boutonVisualiser, boutonAnnuler);

        Optional<ButtonType> resultat = popup.showAndWait();

        String reponseAvecRetourLigne = donnees.replace("/N", "\n")
                                               .replace("/R", "\r");

        String[] fichiers = reponseAvecRetourLigne.split("/EOF");

        if (resultat.isPresent()) {
            if (resultat.get() == boutonVisualiser) {
                afficherPopupVisualiser(reponseAvecRetourLigne);
            } else if (resultat.get() == boutonCharger) {
                String dateDuJour = new SimpleDateFormat("ddMMyyyy").format(new Date());

                try {
                    // vérifier et créer le répertoire
                    File dossier = new File("src/main/resources/csv/");
                    if (!dossier.exists() && !dossier.mkdirs()) {
                        throw new IOException("Erreur lors de la création du répertoire.");
                    }

                    // parcourir chaque fichier extrait et les sauvegarder
                    for (String fichier : fichiers) {
                        List<String> donneesEnListe = Arrays.asList(fichier.split("\n"));

                        String nomFichier = Fichier.getTypeDepuisContenu(
                                donneesEnListe
                        ) + "_" + dateDuJour + ".csv";

                        // créer ou réécrire le fichier avec les données reçues
                        Fichier.ecritureFichier(donneesEnListe, "src/main/resources/csv/" + nomFichier);
                        System.out.println("Les données ont été sauvegardées dans : " + nomFichier);
                    }

                    // renvoie l'utilisateur vers l'affichage des données
                    MainControleur.activerAffichage();
                } catch (IOException e) {
                    MainControleur.showAlert("Erreur d'importation", "Impossible de charger les données dans l'application : " + e.getMessage());
                }
            } else {
                System.out.println("Action annulée.");
                popup.close();
            }
        }
    }

    private static void afficherPopupVisualiser(String donnees) {
        Dialog<ButtonType> popupVisualiser = new Dialog<>();
        popupVisualiser.setTitle("Visualisation des données");
        popupVisualiser.setHeaderText("Données reçues");

        // création du contenu de la popup
        String contenu = donnees.replace("/EOF", "\n\n");
        TextArea zoneTexte = new TextArea(contenu);
        zoneTexte.setEditable(false);
        zoneTexte.setWrapText(true);

        ButtonType boutonRetour = new ButtonType("Retour", ButtonBar.ButtonData.OK_DONE);
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
