package sae.statisalle.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import sae.statisalle.modele.objet.Activite;
import sae.statisalle.modele.objet.Employe;
import sae.statisalle.modele.objet.Reservation;
import sae.statisalle.modele.objet.Salle;
import sae.statisalle.modele.Fichier;

import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Affichage {

    @FXML
    private Button btnAfficherTableaux;

    @FXML
    private TabPane grandTableau;

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
    private ComboBox<String> filtreEmploye;
    @FXML
    private ComboBox<String> filtreActivite;
    @FXML
    private ComboBox<String> filtreSalle;
    @FXML
    private ComboBox<String> filtreDate;
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
    private Text textfiltreDate;
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
    @FXML
    ObservableList<Salle> listSalle = FXCollections.observableArrayList();
    @FXML
    ObservableList<Reservation> listReservation = FXCollections.observableArrayList();


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
                                    listEmploye.add(new Employe(ligne.get(0),
                                            ligne.get(1),
                                            ligne.get(2),
                                            ligne.get(3)));
                                }
                            }
                            idEmploye.setCellValueFactory(new PropertyValueFactory<>("idE"));
                            nomE.setCellValueFactory(new PropertyValueFactory<>("nom"));
                            prenomE.setCellValueFactory(new PropertyValueFactory<>("prenom"));
                            numTelE.setCellValueFactory(new PropertyValueFactory<>("numTel"));
                            tabEmploye.setItems(listEmploye);
                        }
                        case "Salle" -> {
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
                        }
                        case "Activite" -> {
                            for (List<String> ligne : contenu) {
                                if (ligne.size() == 2) {
                                    listActivite.add(new Activite(ligne.get(0), ligne.get(1)));
                                } else {
                                    System.out.println("Ligne incorrecte dans le fichier Activité : " + ligne);
                                }
                            }
                            idActivite.setCellValueFactory(new PropertyValueFactory<>("type"));
                            activiteA.setCellValueFactory(new PropertyValueFactory<>("idActivite"));
                            tabActivite.setItems(listActivite);
                        }
                        case "Reservation" -> {
                            for (List<String> ligne : contenu) {
                                if (ligne.size() >= 12) {
                                    // Créer une réservation
                                    Reservation reservation = new Reservation(
                                            ligne.get(0), ligne.get(1),
                                            ligne.get(2), ligne.get(3),
                                            ligne.get(4), ligne.get(5),
                                            ligne.get(6), ligne.get(7),
                                            ligne.get(8), ligne.get(9),
                                            ligne.get(10), ligne.get(11)
                                    );

                                    // Trouver le nom de l'employé
                                    for (Employe employe : listEmploye) {
                                        if (employe.getIdE().equals(reservation.getEmployeR())) {
                                            reservation.setEmployeR(employe.getNom() + " " + employe.getPrenom());
                                            break;
                                        }
                                    }

                                    // Associer les noms depuis les listes avec des boucles
                                    // Trouver le nom de la salle
                                    for (Salle salle : listSalle) {
                                        if (salle.getIdentifiant().equals(reservation.getSalleR())) {
                                            reservation.setSalleR(salle.getNom());
                                            break;
                                        }
                                    }

                                    // Ajouter la réservation à la liste
                                    listReservation.add(reservation);
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
                        }
                        default ->
                                System.out.println("Type de fichier inconnu ou non pris en charge : " + fichier.getName());
                    }
                    remplirComboBoxSalles();
                    remplirComboBoxEmployes();
                    remplirComboBoxDates();
                    remplirComboBoxActivites();
                    remplirComboBoxHeuresD();
                    remplirComboBoxHeuresF();
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

        // Vérifiez si l'onglet actif est Réservation et affichez les filtres
        if (grandTableau.getSelectionModel().getSelectedItem() == feuilleReservation) {
            afficherFiltre();
        }
    }

    @FXML
    private void afficherFiltre() {
        // création d'une liste contenant tous les filtres
        List<Node> filtres = Arrays.asList(
                filtreEmploye, filtreSalle, filtreActivite, filtreDate, filtreHeureD, filtreHeureF,
                textfiltreEmploye, textfiltreSalle, textfiltreActivite, textfiltreDate, textfiltreHeureD, textfiltreHeureF
        );

        // Détermine la visibilité en fonction de l'état de la checkbox
        boolean visible = feuilleReservation.isSelected();

        // Applique la visibilité à chaque composant si celui-ci n'est pas null
        filtres.forEach(composantFiltre -> {
            //Vérification
            if (composantFiltre != null) {
                composantFiltre.setVisible(visible);
            }
        });
    }

    private void remplirComboBox(ComboBox<String> comboBox, Set<String> valeurs) {
        ObservableList<String> items = FXCollections.observableArrayList();
        items.add("Tous");
        items.addAll(valeurs);
        comboBox.setItems(items);
        comboBox.getSelectionModel().selectFirst();
    }

    private void remplirComboBoxEmployes() {
        Set<String> nomsUniques = new HashSet<>();
        if (!listEmploye.isEmpty()){
            for (Employe employe : listEmploye) {
                nomsUniques.add(employe.getNom() + " " + employe.getPrenom());
            }
        } else {
            for (Reservation employe : listReservation) {
                nomsUniques.add(employe.getEmployeR());
            }
        }

        remplirComboBox(filtreEmploye, nomsUniques);
    }

    private void remplirComboBoxActivites() {
        Set<String> typesUniques = new HashSet<>();
        for (Reservation activite : listReservation) {
            typesUniques.add(activite.getActiviteR());
        }

        remplirComboBox(filtreActivite, typesUniques);
    }

    private void remplirComboBoxSalles() {
        Set<String> nomsUniques = new HashSet<>();
        if(!listSalle.isEmpty()) {
            for (Salle salle : listSalle) {
                nomsUniques.add(salle.getNom());
            }
        } else {
            for (Reservation salle : listReservation) {
                nomsUniques.add(salle.getSalleR());
            }
        }
        remplirComboBox(filtreSalle, nomsUniques);
    }

    private void remplirComboBoxDates() {
        Set<String> datesUniques = new HashSet<>();
        for (Reservation date : listReservation) {
            datesUniques.add(date.getDateR());
        }
        remplirComboBox(filtreDate, datesUniques);
    }

    private void remplirComboBoxHeuresD() {
        Set<String> heuresUniques = new HashSet<>();
        for (Reservation heuresD : listReservation) {
            heuresUniques.add(heuresD.getHeureDebut());
        }

        remplirComboBox(filtreHeureD, heuresUniques);
    }

    private void remplirComboBoxHeuresF() {
        Set<String> heuresUniques = new HashSet<>();
        for (Reservation heuresF : listReservation) {
            heuresUniques.add(heuresF.getHeureDebut());
        }

        remplirComboBox(filtreHeureF, heuresUniques);
    }

    @FXML
    private void initialize() {

        // Lier les ComboBox avec leur valueProperty pour appliquer les filtres
        filtreEmploye.valueProperty().addListener((observable, oldValue, newValue) -> appliquerFiltres());
        filtreActivite.valueProperty().addListener((observable, oldValue, newValue) -> appliquerFiltres());
        filtreSalle.valueProperty().addListener((observable, oldValue, newValue) -> appliquerFiltres());
        filtreDate.valueProperty().addListener((observable, oldValue, newValue) -> appliquerFiltres());
        filtreHeureD.valueProperty().addListener((observable, oldValue, newValue) -> appliquerFiltres());
        filtreHeureF.valueProperty().addListener((observable, oldValue, newValue) -> appliquerFiltres());
    }

    private void appliquerFiltres() {
        // Récupérer les filtres sélectionnés
        String employeFiltre = filtreEmploye.getValue();
        String activiteFiltre = filtreActivite.getValue();
        String salleFiltre = filtreSalle.getValue();
        String dateFiltre = filtreDate.getValue();
        String heureDebutFiltre = filtreHeureD.getValue();
        String heureFinFiltre = filtreHeureF.getValue();

        // Appliquer les filtres sur les réservations
        ObservableList<Reservation> reservationsFiltrees = FXCollections.observableArrayList();

        for (Reservation reservation : listReservation) {
            boolean matchesFiltre = employeFiltre == null || employeFiltre.equals("Tous") ||
                    reservation.getEmployeR().equalsIgnoreCase(employeFiltre);

            // Vérifier chaque filtre
            if (activiteFiltre != null && !activiteFiltre.equals("Tous") &&
                    !reservation.getActiviteR().equalsIgnoreCase(activiteFiltre)) {
                matchesFiltre = false;
            }
            if (salleFiltre != null && !salleFiltre.equals("Tous") &&
                    !reservation.getSalleR().equalsIgnoreCase(salleFiltre)) {
                matchesFiltre = false;
            }
            if (dateFiltre != null && !dateFiltre.equals("Tous") &&
                    !reservation.getDateR().equals(dateFiltre)) {
                matchesFiltre = false;
            }
            if (heureDebutFiltre != null && !heureDebutFiltre.equals("Tous") &&
                    !reservation.getHeureDebut().equals(heureDebutFiltre)) {
                matchesFiltre = false;
            }
            if (heureFinFiltre != null && !heureFinFiltre.equals("Tous") &&
                    !reservation.getHeureFin().equals(heureFinFiltre)) {
                matchesFiltre = false;
            }

            // Ajouter la réservation si elle correspond aux filtres
            if (matchesFiltre) {
                reservationsFiltrees.add(reservation);
            }
        }

        // Mettre à jour la table avec les résultats filtrés
        tabReservation.setItems(reservationsFiltrees);
    }

}
