package sae.statisalle.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import sae.statisalle.modele.objet.Activite;
import sae.statisalle.modele.objet.Employe;
import sae.statisalle.modele.objet.Reservation;
import sae.statisalle.modele.objet.Salle;
import sae.statisalle.modele.Fichier;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class Affichage {

    @FXML
    private Button btnAfficherTableaux;

    @FXML
    private TabPane grandTableau;

    @FXML
    private Text titre;

    // Table de réservations
    @FXML
    private TableView<Reservation> tabReservation;
    @FXML
    private TableColumn<Reservation, String> idReservation;
    @FXML
    private TableColumn<Reservation, String> salleR;
    @FXML
    private TableColumn<Reservation, String> employeR;
    @FXML
    private TableColumn<Reservation, String> activiteR;
    @FXML
    private TableColumn<Reservation, String> dateR;
    @FXML
    private TableColumn<Reservation, String> heureDebutR;
    @FXML
    private TableColumn<Reservation, String> heureFinR;
    @FXML
    private TableColumn<Reservation, String> descriptionR;
    @FXML
    private TableColumn<Reservation, String> nomR;
    @FXML
    private TableColumn<Reservation, String> prenomR;
    @FXML
    private TableColumn<Reservation, String> numTelR;
    @FXML
    private TableColumn<Reservation, String> usageR;
    @FXML
    private Tab feuilleReservation;
    @FXML
    private Tab feuilleSalle;
    @FXML
    private Tab feuilleActivite;
    @FXML
    private Tab feuilleEmploye;
    @FXML
    private ComboBox<String> filtreEmploye;
    @FXML
    private ComboBox<String> filtreActivite;
    @FXML
    private ComboBox<String> filtreSalle;
    @FXML
    private ComboBox<String> filtreJour;
    @FXML
    private ComboBox<String> filtreHeureD;
    @FXML
    private ComboBox<String> filtreHeureF;
    @FXML
    private Text textfiltreEmploye;
    @FXML
    private Text textfiltreActivite;
    @FXML
    private Text textfiltreSalle;
    @FXML
    private Text textfiltreJour;
    @FXML
    private Text textfiltreHeureD;
    @FXML
    private Text textfiltreHeureF;

    // Table des salles
    @FXML
    private TableView<Salle> tabSalle;
    @FXML
    private TableColumn<Salle, String> idSalle;
    @FXML
    private TableColumn<Salle, String> nomS;
    @FXML
    private TableColumn<Salle, String> capaciteS;
    @FXML
    private TableColumn<Salle, String> videoProjS;
    @FXML
    private TableColumn<Salle, String> ecranXXLS;
    @FXML
    private TableColumn<Salle, String> nbrOrdiS;
    @FXML
    private TableColumn<Salle, String> typeS;
    @FXML
    private TableColumn<Salle, String> logicielS;
    @FXML
    private TableColumn<Salle, String> imprimanteS;

    // Table des activités
    @FXML
    private TableView<Activite> tabActivite;
    @FXML
    private TableColumn<Activite, String> idActivite;
    @FXML
    private TableColumn<Activite, String> activiteA;

    // Table des employés
    @FXML
    private TableView<Employe> tabEmploye;
    @FXML
    private TableColumn<Employe, String> idEmploye;
    @FXML
    private TableColumn<Employe, String> nomE;
    @FXML
    private TableColumn<Employe, String> prenomE;
    @FXML
    private TableColumn<Employe, String> numTelE;
    @FXML
    ObservableList<Employe> listEmploye = FXCollections.observableArrayList();
    @FXML
    ObservableList<Activite> listActivite = FXCollections.observableArrayList();

    // Méthodes d'action pour les boutons
    @FXML
    private void actionAide() {
        MainControleur.activerAideAffichage();
    }

    @FXML
    void actionRetour() {
        grandTableau.setVisible(false);
        btnAfficherTableaux.setVisible(true);
        MainControleur.activerAccueil();
    }

    @FXML
    private void chargerDonnees() {
        // Cache le bouton
        btnAfficherTableaux.setVisible(false);

        // Affiche les tableaux
        grandTableau.setVisible(true);

        // Vider les tableaux pour éviter des doublons
        tabEmploye.getItems().clear();
        tabSalle.getItems().clear();
        tabActivite.getItems().clear();
        tabReservation.getItems().clear();

        String URLDossier = "src/main/resources/csv";
        try {
            File dossier = new File(URLDecoder.decode(URLDossier, StandardCharsets.UTF_8));

            // Vérifier si le répertoire contient des fichiers
            if (!dossier.exists() || !dossier.isDirectory()) {
                System.out.println("Le répertoire 'csv' n'existe pas ou n'est pas un dossier.");
                return;
            }

            File[] fichiers = dossier.listFiles((dir, name) -> name.endsWith(".csv"));

            // Vérifier si des fichiers ont été trouvés
            if (fichiers == null || fichiers.length == 0) {
                System.out.println("Aucun fichier CSV trouvé dans le répertoire.");
                return;
            }

            StringBuilder fichiersInvalides = new StringBuilder();

            for (File fichier : fichiers) {
                try {
                    Fichier fichierExploite = new Fichier(fichier.getPath());
                    List<List<String>> contenu = fichierExploite.recupererDonnees();

                    switch (fichierExploite.getTypeFichier()) {
                        case "Employe":
                            for (List<String> ligne : contenu) {
                                if (ligne.size() >= 4) {
                                    listEmploye.add(new Employe(ligne.get(0),
                                            ligne.get(1),
                                            ligne.get(2),
                                            ligne.get(3)));
                                }
                            }
                            remplirComboBoxEmployes();

                            idEmploye.setCellValueFactory(new PropertyValueFactory<>("idE"));
                            nomE.setCellValueFactory(new PropertyValueFactory<>("nom"));
                            prenomE.setCellValueFactory(new PropertyValueFactory<>("prenom"));
                            numTelE.setCellValueFactory(new PropertyValueFactory<>("numTel"));

                            tabEmploye.setItems(listEmploye);
                            break;

                        case "Salle":
                            ObservableList<Salle> listSalle = FXCollections.observableArrayList();
                            for (List<String> ligne : contenu) {
                                if (ligne.size() >= 9) {
                                    listSalle.add(new Salle(ligne.get(0),
                                            ligne.get(1), ligne.get(2),
                                            ligne.get(3), ligne.get(4),
                                            ligne.get(5), ligne.get(6),
                                            ligne.get(7), ligne.get(8)));
                                }
                            }

                            idSalle.setCellValueFactory(new PropertyValueFactory<>("identifiant"));
                            nomS.setCellValueFactory(new PropertyValueFactory<>("nom"));
                            capaciteS.setCellValueFactory(new PropertyValueFactory<>("capacite"));
                            ecranXXLS.setCellValueFactory(new PropertyValueFactory<>("ecranXXL"));
                            typeS.setCellValueFactory(new PropertyValueFactory<>("typeMachine"));
                            videoProjS.setCellValueFactory(new PropertyValueFactory<>("videoProj"));
                            nbrOrdiS.setCellValueFactory(new PropertyValueFactory<>("nbMachine"));
                            logicielS.setCellValueFactory(new PropertyValueFactory<>("logiciel"));
                            imprimanteS.setCellValueFactory(new PropertyValueFactory<>("imprimante"));

                            tabSalle.setItems(listSalle);
                            break;

                        case "Activite":
                            for (List<String> ligne : contenu) {
                                if (ligne.size() == 2) {
                                    listActivite.add(new Activite(ligne.get(0), ligne.get(1)));
                                } else {
                                    System.out.println("Ligne incorrecte dans le fichier Activité : " + ligne);
                                }
                            }
                            remplirComboBoxActivites();

                            idActivite.setCellValueFactory(new PropertyValueFactory<>("type"));
                            activiteA.setCellValueFactory(new PropertyValueFactory<>("idActivite"));

                            tabActivite.setItems(listActivite);
                            break;

                        case "Reservation":
                            ObservableList<Reservation> listReservation = FXCollections.observableArrayList();
                            for (List<String> ligne : contenu) {
                                if (ligne.size() >= 12) {
                                    listReservation.add(new Reservation(
                                            ligne.get(0), ligne.get(1),
                                            ligne.get(2), ligne.get(3),
                                            ligne.get(4), ligne.get(5),
                                            ligne.get(6), ligne.get(7),
                                            ligne.get(8), ligne.get(9),
                                            ligne.get(10), ligne.get(11)));
                                }
                            }

                            idReservation.setCellValueFactory(new PropertyValueFactory<>("idReservation"));
                            salleR.setCellValueFactory(new PropertyValueFactory<>("salleR"));
                            employeR.setCellValueFactory(new PropertyValueFactory<>("employeR"));
                            activiteR.setCellValueFactory(new PropertyValueFactory<>("activiteR"));
                            dateR.setCellValueFactory(new PropertyValueFactory<>("dateR"));
                            heureDebutR.setCellValueFactory(new PropertyValueFactory<>("heureDebut"));
                            heureFinR.setCellValueFactory(new PropertyValueFactory<>("heureFin"));
                            descriptionR.setCellValueFactory(new PropertyValueFactory<>("description"));
                            nomR.setCellValueFactory(new PropertyValueFactory<>("nomIntervenant"));
                            prenomR.setCellValueFactory(new PropertyValueFactory<>("prenomIntervenant"));
                            numTelR.setCellValueFactory(new PropertyValueFactory<>("numTelIntervenant"));
                            usageR.setCellValueFactory(new PropertyValueFactory<>("usage"));

                            tabReservation.setItems(listReservation);
                            break;

                        default:
                            System.out.println("Type de fichier inconnu ou non pris en charge : " + fichier.getName());
                    }
                } catch (Exception e) {
                    System.out.println("Erreur lors du traitement du fichier : " + fichier.getName() + " - " + e.getMessage());
                    fichiersInvalides.append(fichier.getName()).append("\n");
                }
            }

            // si des fichiers invalides ont été détectés, afficher une alerte
            if (!fichiersInvalides.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Fichiers invalides");
                alert.setHeaderText("Certains fichiers n'ont pas pu être chargés");
                alert.setContentText("Les fichiers suivants sont invalides :\n" + fichiersInvalides);

                // ajouter les boutons à l'alerte
                ButtonType supprimerButton = new ButtonType("Supprimer");
                ButtonType ignorerButton = new ButtonType("Ignorer", ButtonBar.ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().setAll(supprimerButton, ignorerButton);

                // gérer la réponse de l'utilisateur
                Optional<ButtonType> resultat = alert.showAndWait();
                if (resultat.isPresent() && resultat.get() == supprimerButton) {
                    try {
                        // supprimer les fichiers invalides
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
    }

    @FXML
    private void afficherFiltre(){
        if (feuilleReservation.isSelected()) {
            filtreEmploye.setVisible(true);
            filtreSalle.setVisible(true);
            filtreActivite.setVisible(true);
            filtreJour.setVisible(true);
            filtreHeureD.setVisible(true);
            filtreHeureF.setVisible(true);
            textfiltreEmploye.setVisible(true);
            textfiltreSalle.setVisible(true);
            textfiltreActivite.setVisible(true);
            textfiltreJour.setVisible(true);
            textfiltreHeureD.setVisible(true);
            textfiltreHeureF.setVisible(true);

        } else {
            filtreEmploye.setVisible(false);
            filtreSalle.setVisible(false);
            filtreActivite.setVisible(false);
            filtreJour.setVisible(false);
            filtreHeureD.setVisible(false);
            filtreHeureF.setVisible(false);
            textfiltreEmploye.setVisible(false);
            textfiltreSalle.setVisible(false);
            textfiltreActivite.setVisible(false);
            textfiltreJour.setVisible(false);
            textfiltreHeureD.setVisible(false);
            textfiltreHeureF.setVisible(false);
        }
    }

    private void remplirComboBoxEmployes() {
        ObservableList<String> nomsEmployes = FXCollections.observableArrayList();
        nomsEmployes.add("Tous"); // Option par défaut
        String nomPrenom = "";

        // Utiliser un HashSet pour éviter les doublons
        HashSet<String> nomsUniques = new HashSet<>();
        for (Employe employe : listEmploye) {
            nomPrenom = employe.getNom() + " " + employe.getPrenom();
            nomsUniques.add(nomPrenom);
        }
        nomsEmployes.addAll(nomsUniques);

        // Mettre à jour la ComboBox
        filtreEmploye.setItems(nomsEmployes);
        filtreEmploye.getSelectionModel().selectFirst(); // Sélectionner "Tous" par défaut
    }

    private void remplirComboBoxActivites() {
        ObservableList<String> nomsActivite = FXCollections.observableArrayList();
        nomsActivite.add("Tous"); // Option par défaut

        // Utiliser un HashSet pour éviter les doublons
        HashSet<String> typeUniques = new HashSet<>();
        for (Activite activite : listActivite) {
            typeUniques.add(activite.getType());
        }
        nomsActivite.addAll(typeUniques);

        // Mettre à jour la ComboBox
        filtreActivite.setItems(nomsActivite);
        filtreActivite.getSelectionModel().selectFirst();
    }

//    private void appliquerFiltreReservation() {
//        String filtreSelectionne = filtreEmploye.getSelectionModel().getSelectedItem();
//
//        if (filtreSelectionne != null && !filtreSelectionne.equals("Tous")) {
//            ObservableList<Reservation> reservationsFiltrees = FXCollections.observableArrayList();
//            for (Reservation reservation : tabReservation.getItems()) {
//                if (reservation.getEmployeR().equals(filtreSelectionne)) {
//                    reservationsFiltrees.add(reservation);
//                }
//            }
//            tabReservation.setItems(reservationsFiltrees);
//        } else {
//            // Réinitialiser la liste des réservations si "Tous" est sélectionné
//            //tabReservation.setItems(FXCollections.observableArrayList(listReservation));
//        }
//    }

}
