package sae.statisalle.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import sae.statisalle.modele.objet.*;

import java.io.IOException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import java.util.*; // TODO ne jamais mettre d'étoiles

/**
 * Contrôleur qui gère la consultation des données et des filtres de recherche sont applicables sur les reservations.
 * Ce contrôleur applique des filtres sur :
 * <ul>
 *     <li>Le nom des salles</li>
 *     <li>Le nom de l'employé que a reservé la salle</li>
 *     <li>Les dates de réservation</li>
 *     <li>Les créneaux horaires</li>
 * </ul>
 * et retourne les données filtrées à la vue.
 *
 * @author robin.montes
 * @author mathias.cambon
 */
public class Affichage {

    @FXML
    private Button btnAfficherTableaux;

    @FXML
    private Button btnClassement;

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
    private ComboBox<String> filtreDateDebut;
    @FXML
    private ComboBox<String> filtreDateFin;
    @FXML
    private ComboBox<String> filtreHeureD;
    @FXML
    private ComboBox<String> filtreHeureF;
    @FXML
    private Button reinitialiserFiltre;
    @FXML
    private Text textfiltreEmploye;
    @FXML
    private Text textfiltreActivite;
    @FXML
    private Text textfiltreSalle;
    @FXML
    private Text textfiltreDateDebut;
    @FXML
    private Text textfiltreDateFin;
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

    @FXML
    private void actionAide() {
        MainControleur.activerAideAffichage();
    }

    @FXML
    private void actionClassement() {
        MainControleur.activerActionAnalyse();
    }

    @FXML
    void actionRetour() {
        // Rendre les filtres invisibles
        filtreEmploye.setVisible(false);
        filtreSalle.setVisible(false);
        filtreActivite.setVisible(false);
        filtreDateDebut.setVisible(false);
        filtreDateFin.setVisible(false);
        filtreHeureD.setVisible(false);
        filtreHeureF.setVisible(false);
        reinitialiserFiltre.setVisible(false);

        // Rendre les textes de filtre invisibles
        textfiltreEmploye.setVisible(false);
        textfiltreSalle.setVisible(false);
        textfiltreActivite.setVisible(false);
        textfiltreDateDebut.setVisible(false);
        textfiltreDateFin.setVisible(false);
        textfiltreHeureD.setVisible(false);
        textfiltreHeureF.setVisible(false);
        grandTableau.setVisible(false);
        btnAfficherTableaux.setVisible(true);
        MainControleur.activerAccueil();
    }

    @FXML
    private void chargerDonnees() throws IOException {

        masquerFiltres();

        btnAfficherTableaux.setVisible(false);

        grandTableau.setVisible(true);

        tabEmploye.getItems().clear();
        tabSalle.getItems().clear();
        tabActivite.getItems().clear();
        tabReservation.getItems().clear();

        // Appel de la méthode centralisée pour charger les fichiers
        LireFichier.chargerDonneesCSV("src/main/resources/csv", listEmploye, listSalle, listActivite, listReservation);

        // Configurez les tables après avoir chargé les données
        idEmploye.setCellValueFactory(new PropertyValueFactory<>("idE"));
        nomE.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomE.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        numTelE.setCellValueFactory(new PropertyValueFactory<>("numTel"));
        tabEmploye.setItems(listEmploye);

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

        idActivite.setCellValueFactory(new PropertyValueFactory<>("type"));
        activiteA.setCellValueFactory(new PropertyValueFactory<>("idActivite"));
        tabActivite.setItems(listActivite);

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

        if (grandTableau.getSelectionModel().getSelectedItem() == feuilleReservation) {
            afficherFiltre();
        }

        remplirComboBoxSalles();
        remplirComboBoxEmployes();
        remplirComboBoxDates();
        remplirComboBoxActivites();
        remplirComboBoxHeuresD();
        remplirComboBoxHeuresF();

        mettreAJourFiltreHeureFin();
        mettreAJourFiltreHeureDebut();

        mettreAJourFiltreDateDebut();
        mettreAJourFiltreDateFin();

        // Afficher le bouton réinitialiser filtre après le chargement des données
        reinitialiserFiltre.setVisible(true);
    }

    @FXML
    private void afficherFiltre() {
        // création d'une liste contenant tous les filtres
        List<Node> filtres = Arrays.asList(
                filtreEmploye, filtreSalle, filtreActivite, filtreDateDebut, filtreDateFin, filtreHeureD, filtreHeureF,
                textfiltreEmploye, textfiltreSalle, textfiltreActivite, textfiltreDateDebut, textfiltreDateFin, textfiltreHeureD,
                textfiltreHeureF, reinitialiserFiltre
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

        List<String> valeursTrier = new ArrayList<>(valeurs);
        Collections.sort(valeursTrier); // Trie par ordre alphabétique

        items.addAll(valeursTrier);
        comboBox.setItems(items);
        comboBox.getSelectionModel().selectFirst();
    }

    private void remplirComboBoxEmployes() {
        Set<String> nomsUniques = new HashSet<>();
        if (!listEmploye.isEmpty()) {
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
        if (!listSalle.isEmpty()) {
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
        remplirComboBox(filtreDateDebut, datesUniques);
        remplirComboBox(filtreDateFin, datesUniques);
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
            heuresUniques.add(heuresF.getHeureFin());
        }

        remplirComboBox(filtreHeureF, heuresUniques);
    }

    @FXML
    private void initialize() {

        // Sélectionner l'onglet "Réservation" par défaut
        grandTableau.getSelectionModel().select(feuilleReservation);

        // Mettre à jour le filtre d'heure de fin dès le début
        mettreAJourFiltreHeureFin();

        // Masquer les filtres au démarrage
        masquerFiltres();

        // Lier les ComboBox avec leur valueProperty pour appliquer les filtres
        filtreEmploye.valueProperty().addListener((observable, oldValue, newValue) -> appliquerFiltres());
        filtreActivite.valueProperty().addListener((observable, oldValue, newValue) -> appliquerFiltres());
        filtreSalle.valueProperty().addListener((observable, oldValue, newValue) -> appliquerFiltres());
        filtreDateDebut.valueProperty().addListener((observable, oldValue, newValue) -> appliquerFiltres());
        filtreDateFin.valueProperty().addListener((observable, oldValue, newValue) -> appliquerFiltres());
        filtreHeureD.valueProperty().addListener((observable, oldValue, newValue) -> appliquerFiltres());
        filtreHeureF.valueProperty().addListener((observable, oldValue, newValue) -> appliquerFiltres());
    }

    private void masquerFiltres() {
        filtreEmploye.setVisible(false);
        textfiltreEmploye.setVisible(false);
        filtreSalle.setVisible(false);
        textfiltreSalle.setVisible(false);
        filtreActivite.setVisible(false);
        textfiltreActivite.setVisible(false);
        filtreDateDebut.setVisible(false);
        filtreDateFin.setVisible(false);
        textfiltreDateDebut.setVisible(false);
        textfiltreDateFin.setVisible(false);
        filtreHeureD.setVisible(false);
        textfiltreHeureD.setVisible(false);
        filtreHeureF.setVisible(false);
        textfiltreHeureF.setVisible(false);
        reinitialiserFiltre.setVisible(false);
    }

    @FXML
    private void reinitialiserFiltre() {
        // Ajouter "Tous" dans les listes de filtres (si ce n'est pas déjà fait)
        if (filtreSalle != null) {
            if (!filtreSalle.getItems().contains("Tous")) {
                filtreSalle.getItems().addFirst("Tous");
            }
            filtreSalle.getSelectionModel().select("Tous");  // Sélectionner "Tous" par défaut
        }

        if (filtreEmploye != null) {
            if (!filtreEmploye.getItems().contains("Tous")) {
                filtreEmploye.getItems().addFirst("Tous");
            }
            filtreEmploye.getSelectionModel().select("Tous");  // Sélectionner "Tous" par défaut
        }

        if (filtreDateDebut != null) {
            if (!filtreDateDebut.getItems().contains("Tous")) {
                filtreDateDebut.getItems().addFirst("Tous");
            }
            filtreDateDebut.getSelectionModel().select("Tous");  // Sélectionner "Tous" par défaut
        }

        if (filtreDateFin != null) {
            if (!filtreDateFin.getItems().contains("Tous")) {
                filtreDateFin.getItems().addFirst("Tous");
            }
            filtreDateFin.getSelectionModel().select("Tous");  // Sélectionner "Tous" par défaut
        }

        if (filtreActivite != null) {
            if (!filtreActivite.getItems().contains("Tous")) {
                filtreActivite.getItems().addFirst("Tous");
            }
            filtreActivite.getSelectionModel().select("Tous");  // Sélectionner "Tous" par défaut
        }

        if (filtreHeureD != null) {
            if (!filtreHeureD.getItems().contains("Tous")) {
                filtreHeureD.getItems().addFirst("Tous");
            }
            filtreHeureD.getSelectionModel().select("Tous");  // Sélectionner "Tous" par défaut
        }

        if (filtreHeureF != null) {
            if (!filtreHeureF.getItems().contains("Tous")) {
                filtreHeureF.getItems().addFirst("Tous");
            }
            filtreHeureF.getSelectionModel().select("Tous");  // Sélectionner "Tous" par défaut
        }

        // Réaffecter les listes complètes aux tableaux
        if (tabSalle != null) tabSalle.setItems(listSalle);
        if (tabEmploye != null) tabEmploye.setItems(listEmploye);
        if (tabActivite != null) tabActivite.setItems(listActivite);
        if (tabReservation != null) tabReservation.setItems(listReservation);

        // Afficher un message de confirmation ou notifier l'utilisateur
        System.out.println("Filtres réinitialisés avec succès.");
    }


    private void mettreAJourFiltreHeureDebut() {
        // Extraire les heures de début uniques des réservations
        Set<String> heuresDebutUniques = new HashSet<>();

        for (Reservation reservation : listReservation) {
            heuresDebutUniques.add(reservation.getHeureDebut());
        }

        // Convertir en liste et trier les heures de début
        List<String> heuresDebutListe = new ArrayList<>(heuresDebutUniques);
        Collections.sort(heuresDebutListe);

        // Ajouter l'option "Tous" en tête de liste
        heuresDebutListe.addFirst("Tous");

        // Mettre à jour le filtre d'heure de début avec la liste d'heures
        filtreHeureD.setItems(FXCollections.observableArrayList(heuresDebutListe));
    }


    private void mettreAJourFiltreHeureFin() {
        // Extraire les heures de fin uniques des réservations
        Set<String> heuresFinUniques = new HashSet<>();

        for (Reservation reservation : listReservation) {
            heuresFinUniques.add(reservation.getHeureFin());
        }

        // Convertir en liste et trier les heures de fin
        List<String> heuresFinListe = new ArrayList<>(heuresFinUniques);
        Collections.sort(heuresFinListe);

        // Ajouter l'option "Tous" en tête de liste
        heuresFinListe.addFirst("Tous");

        // Mettre à jour le filtre d'heure de fin avec la liste d'heures
        filtreHeureF.setItems(FXCollections.observableArrayList(heuresFinListe));
    }

    private void mettreAJourFiltreDateDebut() {
        // Extraire les dates de début uniques des réservations
        Set<String> datesDebutUniques = new HashSet<>();

        for (Reservation reservation : listReservation) {
            datesDebutUniques.add(reservation.getDateR());
        }

        // Convertir en liste et trier les dates de début
        List<String> datesDebutListe = new ArrayList<>(datesDebutUniques);
        Collections.sort(datesDebutListe);

        // Ajouter l'option "Tous" en tête de liste
        datesDebutListe.addFirst("Tous");

        // Mettre à jour le filtre de date de début avec la liste de dates
        filtreDateDebut.setItems(FXCollections.observableArrayList(datesDebutListe));
    }

    private void mettreAJourFiltreDateFin() {
        // Extraire les dates de fin uniques des réservations
        Set<String> datesFinUniques = new HashSet<>();

        for (Reservation reservation : listReservation) {
            datesFinUniques.add(reservation.getDateR());  // Si tu as une méthode pour récupérer la date de fin, adapte cette ligne
        }

        // Convertir en liste et trier les dates de fin
        List<String> datesFinListe = new ArrayList<>(datesFinUniques);
        Collections.sort(datesFinListe);

        // Ajouter l'option "Tous" en tête de liste
        datesFinListe.addFirst("Tous");

        // Mettre à jour le filtre de date de fin avec la liste de dates
        filtreDateFin.setItems(FXCollections.observableArrayList(datesFinListe));
    }

    private void appliquerFiltres() {
        // Récupérer les filtres sélectionnés
        String employeFiltre = filtreEmploye.getValue();
        String activiteFiltre = filtreActivite.getValue();
        String salleFiltre = filtreSalle.getValue();
        String dateFiltreDebut = filtreDateDebut.getValue();
        String dateFiltreFin = filtreDateFin.getValue();
        String heureDebutFiltre = filtreHeureD.getValue();
        String heureFinFiltre = filtreHeureF.getValue();

        // Appliquer les filtres sur les réservations
        ObservableList<Reservation> reservationsFiltrees = FXCollections.observableArrayList();

        for (Reservation reservation : listReservation) {

            // Appliquer les filtres de base
            boolean matchesFiltre =
                    (employeFiltre == null || employeFiltre.equals("Tous") || reservation.getEmployeR().equalsIgnoreCase(employeFiltre)) &&
                            (activiteFiltre == null || activiteFiltre.equals("Tous") || reservation.getActiviteR().equalsIgnoreCase(activiteFiltre)) &&
                            (salleFiltre == null || salleFiltre.equals("Tous") || reservation.getSalleR().equalsIgnoreCase(salleFiltre));

            // Appliquer les filtres de date
            boolean matchesDateDebut = true;
            boolean matchesDateFin = true;

            if (dateFiltreDebut != null && !dateFiltreDebut.equals("Tous")) {
                LocalDate dateDebut = parseDate(dateFiltreDebut);
                if (dateDebut != null) {
                    LocalDate dateReservation = parseDate(reservation.getDateR());
                    if (dateReservation != null) {
                        matchesDateDebut = !dateReservation.isBefore(dateDebut);
                    }
                }
            }

            if (dateFiltreFin != null && !dateFiltreFin.equals("Tous")) {
                LocalDate dateFin = parseDate(dateFiltreFin);
                if (dateFin != null) {
                    LocalDate dateReservation = parseDate(reservation.getDateR());
                    if (dateReservation != null) {
                        matchesDateFin = !dateReservation.isAfter(dateFin);
                    }
                }
            }

            // Appliquer les filtres d'heure
            boolean matchesHeureDebut = true;
            boolean matchesHeureFin = true;

            if (heureDebutFiltre != null && !heureDebutFiltre.equals("Tous")) {
                LocalTime heureDebut = parseHeure(heureDebutFiltre);
                if (heureDebut != null) {
                    LocalTime heureDebutReservation = parseHeure(reservation.getHeureDebut());
                    if (heureDebutReservation != null) {
                        matchesHeureDebut = !heureDebutReservation.isBefore(heureDebut);
                    }
                }
            }

            if (heureFinFiltre != null && !heureFinFiltre.equals("Tous")) {
                LocalTime heureFin = parseHeure(heureFinFiltre);
                if (heureFin != null) {
                    LocalTime heureFinReservation = parseHeure(reservation.getHeureFin());
                    if (heureFinReservation != null) {
                        matchesHeureFin = !heureFinReservation.isAfter(heureFin);
                    }
                }
            }

            // Ajouter la réservation si tous les filtres sont validés
            if (matchesFiltre && matchesDateDebut && matchesDateFin && matchesHeureDebut && matchesHeureFin) {
                reservationsFiltrees.add(reservation);
            }
        }

        // Mettre à jour la table avec les résultats filtrés
        tabReservation.setItems(reservationsFiltrees);
    }

    // Méthode utilitaire pour la conversion des heures en LocalTime
    private LocalTime parseHeure(String heure) {
        try {
            // Remplacer 'h' par ':' pour correspondre au format attendu par LocalTime
            heure = heure.replace('h', ':');
            return LocalTime.parse(heure);
        } catch (DateTimeParseException e) {
            System.out.println("Erreur de format d'heure: " + heure);
            return null; // Si l'heure est invalide, retourner null
        }
    }

    private LocalDate parseDate(String date) {
        try {
            // Créer un DateTimeFormatter avec le format "dd/MM/yyyy"
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            // Utiliser le formatter pour convertir la chaîne en LocalDate
            return LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Erreur de format de date: " + date);
            return null; // Si la date est invalide, retourner null
        }
    }

    @FXML
    private void handleReinitialiserFiltre() {
        reinitialiserFiltre();
    }
}