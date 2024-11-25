/*
 * LireFichier.java               22/11/2024
 * IUT DE RODEZ               Pas de copyrights
 */
package sae.statisalle.modele;

import javafx.collections.ObservableList;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import javafx.scene.image.Image;
import javafx.stage.Stage;
import sae.statisalle.controleur.ControleurPopup;
import sae.statisalle.modele.objet.Activite;
import sae.statisalle.modele.objet.Employe;
import sae.statisalle.modele.objet.Reservation;
import sae.statisalle.modele.objet.Salle;

import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Classe qui charge les fichiers csv dans des list afin de les affichés dans
 * les tableux.
 * Et qui créer des associations entre les classes objet Reservation,Salle,
 * Employe,Activite
 * @author erwan.thierry
 * @author rodrigo.xaviertaborda
 */
public class LireFichier {

    // Méthode pour charger les données depuis les fichiers CSV
    public static void chargerDonneesCSV(String chemin, ObservableList<Employe> listEmploye, ObservableList<Salle> listSalle, ObservableList<Activite> listActivite, ObservableList<Reservation> listReservation) {

        String URLDossier = "src/main/resources/csv";
        try {
            File dossier = new File(URLDecoder.decode(URLDossier, StandardCharsets.UTF_8));

            if (!dossier.exists() || !dossier.isDirectory()) {
                System.out.println("Le répertoire 'csv' n'existe pas ou n'est pas un dossier.");
                return;
            }

            File[] fichiers = dossier.listFiles((dir, name) -> name.endsWith(".csv"));

            if (fichiers == null || fichiers.length == 0) {
                System.out.println("Aucun fichier CSV trouvé dans le répertoire.");
                return;
            }

            // Trier les fichiers pour donner la priorité à "Salle"
            Arrays.sort(fichiers, (f1, f2) -> {
                if (f1.getName().contains("Salle") && !f2.getName().contains("Salle")) {
                    return -1;
                } else if (!f1.getName().contains("Salle") && f2.getName().contains("Salle")) {
                    return 1;
                } else {
                    return 0;
                }
            });

            StringBuilder fichiersInvalides = new StringBuilder();

            for (File fichier : fichiers) {
                try {
                    Fichier fichierExploite = new Fichier(fichier.getPath());
                    List<List<String>> contenu = fichierExploite.recupererDonnees();

                    switch (fichierExploite.getTypeFichier()) {
                        case "Employe" -> {
                            for (List<String> ligne : contenu) {
                                if (ligne.size() >= 4) {
                                    listEmploye.add(new Employe(ligne.get(0), ligne.get(1), ligne.get(2), ligne.get(3)));
                                }
                            }
                        }
                        case "Salle" -> {
                            for (List<String> ligne : contenu) {
                                if (ligne.size() >= 9) {
                                    listSalle.add(new Salle(ligne.get(0), ligne.get(1), ligne.get(2), ligne.get(3), ligne.get(4), ligne.get(5), ligne.get(6), ligne.get(7), ligne.get(8)));
                                }
                            }
                        }
                        case "Activite" -> {
                            for (List<String> ligne : contenu) {
                                if (ligne.size() == 2) {
                                    listActivite.add(new Activite(ligne.get(0), ligne.get(1)));
                                } else {
                                    System.out.println("Ligne incorrecte dans le fichier Activité : " + ligne);
                                }
                            }
                        }
                        case "Reservation" -> {
                            for (List<String> ligne : contenu) {
                                if (ligne.size() >= 12) {
                                    Reservation reservation = new Reservation(
                                            ligne.get(0), ligne.get(1), ligne.get(2), ligne.get(3), ligne.get(4),
                                            ligne.get(5), ligne.get(6), ligne.get(7), ligne.get(8), ligne.get(9),
                                            ligne.get(10), ligne.get(11)
                                    );

                                    // Récupérer les informations supplémentaires
                                    for (Employe employe : listEmploye) {
                                        if (employe.getIdE().equals(reservation.getEmployeR())) {
                                            reservation.setEmployeR(employe.getNom() + " " + employe.getPrenom());
                                        }
                                    }

                                    for (Salle salle : listSalle) {
                                        if (salle.getIdentifiant().equals(reservation.getSalleR())) {
                                            reservation.setSalleR(salle.getNom());
                                        }
                                    }

                                    listReservation.add(reservation);
                                }
                            }
                        }
                        default -> System.out.println("Type de fichier inconnu ou non pris en charge : " + fichier.getName());
                    }

                } catch (Exception e) {
                    System.out.println("Erreur lors du traitement du fichier : " + fichier.getName() + " - " + e.getMessage());
                    fichiersInvalides.append(fichier.getName()).append("\n");
                }
            }

            if (!fichiersInvalides.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Fichiers invalides");
                Image logo = new Image(Objects.requireNonNull(
                        ControleurPopup.class.getResourceAsStream(
                                "/sae/statisalle/img/LogoStatisalle.jpg")));
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(logo);
                alert.setHeaderText("Certains fichiers n'ont pas pu être chargés");
                alert.setContentText("Les fichiers suivants sont invalides :\n" + fichiersInvalides);

                ButtonType supprimerButton = new ButtonType("Supprimer");
                ButtonType ignorerButton = new ButtonType("Ignorer", ButtonBar.ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().setAll(supprimerButton, ignorerButton);

                Optional<ButtonType> resultat = alert.showAndWait();
                if (resultat.isPresent() && resultat.get() == supprimerButton) {
                    try {
                        for (String nomFichier : fichiersInvalides.toString().split("\n")) {
                            File fichierADelete = new File("src/main/resources/csv/" + nomFichier.trim());
                            if (fichierADelete.exists() && fichierADelete.isFile()) {
                                if (fichierADelete.delete()) {
                                    System.out.println("Fichier supprimé : " + fichierADelete.getName());
                                } else {
                                    System.out.println("Impossible de supprimer : " + fichierADelete.getName());
                                }
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Erreur lors de la suppression des fichiers : " + e.getMessage());
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Erreur générale : " + e.getMessage());
        }

        for (Reservation reservation : listReservation) {
            // Associer les réservations aux salles
            for (Salle salle : listSalle) {
                if (salle.getNom().equals(reservation.getSalleR())) {
                    salle.getReservations().add(reservation);
                    break;
                }
            }

            // Associer les réservations aux employés
            for (Employe employe : listEmploye) {
                if (employe.getIdE().equals(reservation.getEmployeR())) {
                    employe.getReservations().add(reservation);
                    break;
                }
            }

            // Associer les réservations aux activités
            for (Activite activite : listActivite) {
                if (activite.getIdActivite().equals(reservation.getActiviteR())) {
                    activite.getReservations().add(reservation);
                    break;
                }
            }
        }
    }
}