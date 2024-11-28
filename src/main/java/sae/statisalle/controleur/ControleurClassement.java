/*
 * ControleurClassement.java               21/11/2024
 * IUT DE RODEZ               Pas de copyrights
 */

package sae.statisalle.controleur;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import sae.statisalle.modele.GenererPdf;
import sae.statisalle.modele.LireFichier;
import sae.statisalle.modele.objet.*;

import java.io.File;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Controleur des statistiques - ControleurClassement
 * @author erwan.thierry
 * @author rodrigo.xaviertaborda
 * @author cambon.mathias
 * @author montes.robin
 */
public class ControleurClassement {

    @FXML
    private Button reinitialiserFiltre;
    @FXML
    private Button btnAide;
    @FXML
    private Button btnRetour;
    @FXML
    private Button btnAfficherTableaux;
    @FXML
    private Button btnGenererPdf;

    @FXML
    private TabPane grandTableau;

    @FXML
    private Tab feuilleSalle;

    // Table de salle
    @FXML
    private TableView<ReservationDuree> tabSalle;
    @FXML
    private TableColumn<ReservationDuree, String> idSalle;
    @FXML
    private TableColumn<ReservationDuree, String> nomS;
    @FXML
    private TableColumn<ReservationDuree, String> employeS;
    @FXML
    private TableColumn<ReservationDuree, String> activiteS;
    @FXML
    private TableColumn<ReservationDuree, String> dateR;
    @FXML
    private TableColumn<ReservationDuree, String> heureDebutR;
    @FXML
    private TableColumn<ReservationDuree, String> heureFinR;
    @FXML
    private TableColumn<ReservationDuree, String> totalS;

    //Filtre
    @FXML
    private ComboBox<String> filtreEmploye;
    @FXML
    private ComboBox<String> filtreDateDebut;
    @FXML
    private ComboBox<String> filtreDateFin;
    @FXML
    private ComboBox<String> filtreHeureD;
    @FXML
    private ComboBox<String> filtreHeureF;
    @FXML
    private ComboBox<String> filtreSalle;
    @FXML
    private ComboBox<String> filtreActivite;

    @FXML
    private Text textfiltreEmploye;
    @FXML
    private Text textfiltreSalle;
    @FXML
    private Text textfiltreActivite;
    @FXML
    private Text textfiltreDateDebut;
    @FXML
    private Text textfiltreDateFin;
    @FXML
    private Text textfiltreHeureD;
    @FXML
    private Text textfiltreHeureF;

    @FXML
    ObservableList<Employe> listEmploye = FXCollections.observableArrayList();
    @FXML
    ObservableList<Activite> listActivite = FXCollections.observableArrayList();
    @FXML
    ObservableList<Salle> listSalle = FXCollections.observableArrayList();
    @FXML
    ObservableList<Reservation> listReservation = FXCollections.observableArrayList();
    @FXML
    ObservableList<ReservationDuree> reservationsFiltrees = FXCollections.observableArrayList();
    @FXML
    ObservableList<ReservationDuree> listReservationDuree = FXCollections.observableArrayList();

    private boolean filtreSet = false;

    @FXML
    void actionAide(ActionEvent event) {
        MainControleur.activerAideClassement();
    }

    @FXML
    void actionRetour(ActionEvent event) {
        MainControleur.activerActionAnalyse();
    }

    /**
     * Méthode qui sert à récupérer les attributs de l'objet Salle,en les liants
     * à une réservation spécifique.
     * @return sallesDetailles liste contenant les informations sur les salles
     *         liées à une réservation.
     */
    public ObservableList<ReservationDuree> getReservationDuree() {

        // Parcours de toutes les salles
        for (Salle salle : listSalle) {
            // Parcours des réservations de chaque salle
            for (Reservation reservation : salle.getReservations()) {

                String idReservation = reservation.getIdReservation();
                String salleNom = salle.getNom();
                String employeNom = reservation.getEmployeR();
                String activite = reservation.getActiviteR();
                String date = reservation.getDateR();
                String heureDebut = reservation.getHeureDebut();
                String heureFin = reservation.getHeureFin();

                // Création de la réservation détaillée
                ReservationDuree reservationD = new ReservationDuree(
                    idReservation,
                        salleNom,
                        employeNom,
                        activite,
                        date,
                        heureDebut,
                        heureFin
                );

                // Ajout de la réservation détaillée à la liste
                listReservationDuree.add(reservationD);
            }
            listReservationDuree.sort((r1, r2) -> {
                // Convertit les durées en minutes pour comparer
                int dureeR1 = parseDuree(r1.getDuree());
                int dureeR2 = parseDuree(r2.getDuree());
                return Integer.compare(dureeR2, dureeR1);  // Tri décroissant
            });
        }
        return listReservationDuree;
    }

    /**
     * Chargement des données dans les tableaux, en appelant la méthode
     * chargerDonneesCSV() de LireFichier qui récupère le contenu des fichiers
     * dans des listes
     */
    public void chargerDonnees() {
        btnAfficherTableaux.setVisible(false);

        grandTableau.setVisible(true);
        btnGenererPdf.setVisible(true);

        tabSalle.getItems().clear();

        // Appel de la méthode centralisée pour charger les fichiers
        LireFichier.chargerDonneesCSV("src/main/resources/csv", listEmploye, listSalle, listActivite, listReservation);

        // Table salle
        idSalle.setCellValueFactory(new PropertyValueFactory<>("idReservation"));
        nomS.setCellValueFactory(new PropertyValueFactory<>("salle"));
        employeS.setCellValueFactory(new PropertyValueFactory<>("employe"));
        activiteS.setCellValueFactory(new PropertyValueFactory<>("activite"));
        dateR.setCellValueFactory(new PropertyValueFactory<>("date"));
        heureDebutR.setCellValueFactory(new PropertyValueFactory<>("heureDebut"));
        heureFinR.setCellValueFactory(new PropertyValueFactory<>("heureFin"));
        totalS.setCellValueFactory(new PropertyValueFactory<>("duree"));
        tabSalle.setItems(getReservationDuree());

        afficherFiltreSalle();

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

        reinitialiserFiltre();
    }

    @FXML
    private void handleReinitialiserFiltre() {
        reinitialiserFiltre();
    }

    @FXML
    private void afficherFiltreSalle() {
        List<Node> filtres = Arrays.asList(
                filtreEmploye, filtreActivite, filtreSalle, filtreDateDebut, filtreDateFin, filtreHeureD, filtreHeureF,
                textfiltreEmploye, textfiltreActivite, textfiltreDateDebut, textfiltreDateFin, textfiltreHeureD,
                textfiltreHeureF, textfiltreSalle
        );

        boolean visible = feuilleSalle.isSelected();

        // Applique la visibilité à chaque composant si celui-ci n'est pas null
        filtres.forEach(composantFiltre -> {
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

        masquerFiltres();

        // Configurations additionnelles
        grandTableau.getSelectionModel().select(feuilleSalle);
        mettreAJourFiltreHeureFin();

        // Liaisons des ComboBox pour les filtres sur les salles
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
        filtreSalle.getSelectionModel().select("Tous");
        filtreEmploye.getSelectionModel().select("Tous");
        filtreActivite.getSelectionModel().select("Tous");
        filtreDateDebut.getSelectionModel().select("Tous");
        filtreDateFin.getSelectionModel().select("Tous");
        filtreHeureD.getSelectionModel().select("Tous");
        filtreHeureF.getSelectionModel().select("Tous");
        tabSalle.setItems(listReservationDuree);
        filtreSet = false;
        System.out.println("Filtres réinitialisés avec succès.");
    }

    private void mettreAJourFiltreHeureDebut() {
        Set<String> heuresDebutUniques = new HashSet<>();

        for (Reservation reservation : listReservation) {
            heuresDebutUniques.add(reservation.getHeureDebut());
        }

        List<String> heuresDebutListe = new ArrayList<>(heuresDebutUniques);
        Collections.sort(heuresDebutListe);

        heuresDebutListe.addFirst("Tous");

        filtreHeureD.setItems(FXCollections.observableArrayList(heuresDebutListe));
    }

    private void mettreAJourFiltreHeureFin() {
        Set<String> heuresFinUniques = new HashSet<>();

        for (Reservation reservation : listReservation) {
            heuresFinUniques.add(reservation.getHeureFin());
        }

        List<String> heuresFinListe = new ArrayList<>(heuresFinUniques);
        Collections.sort(heuresFinListe);

        heuresFinListe.addFirst("Tous");

        filtreHeureF.setItems(FXCollections.observableArrayList(heuresFinListe));
    }

    private void mettreAJourFiltreDateDebut() {
        Set<String> datesDebutUniques = new HashSet<>();

        for (Reservation reservation : listReservation) {
            datesDebutUniques.add(reservation.getDateR());
        }

        List<String> datesDebutListe = new ArrayList<>(datesDebutUniques);
        Collections.sort(datesDebutListe);

        datesDebutListe.addFirst("Tous");

        filtreDateDebut.setItems(FXCollections.observableArrayList(datesDebutListe));
    }

    private void mettreAJourFiltreDateFin() {
        Set<String> datesFinUniques = new HashSet<>();

        for (Reservation reservation : listReservation) {
            datesFinUniques.add(reservation.getDateR());
        }

        List<String> datesFinListe = new ArrayList<>(datesFinUniques);
        Collections.sort(datesFinListe);

        datesFinListe.addFirst("Tous");

        filtreDateFin.setItems(FXCollections.observableArrayList(datesFinListe));
    }

    /**
     * Applique les filtres sélectionnés pour filtrer les réservations en fonction
     * de plusieurs critères : employé, activité, salle, date et heures de début/fin.
     * <p>
     * Critères de filtre :
     * <ul>
     *   <li>Employé : Le nom de l'employé responsable de la réservation.</li>
     *   <li>Activité : L'activité associée à la réservation.</li>
     *   <li>Salle : La salle associée à la réservation.</li>
     *   <li>Date de début et de fin : Les dates de réservation doivent être comprises dans cette période.</li>
     *   <li>Heure de début et de fin : Les heures de réservation doivent correspondre à ces heures.</li>
     * <p>
     *
     * La méthode parcourt la liste des réservations et vérifie si chaque réservation
     * correspond aux filtres définis. Si une réservation satisfait tous les critères,
     * elle est ajoutée à la liste des réservations filtrées.
     */
    private void appliquerFiltres() {
        // Récupérer les valeurs des filtres
        String employeFiltre = filtreEmploye.getValue();
        String activiteFiltre = filtreActivite.getValue();
        String salleFiltre = filtreSalle.getValue();
        String dateFiltreDebut = filtreDateDebut.getValue();
        String dateFiltreFin = filtreDateFin.getValue();
        String heureDebutFiltre = filtreHeureD.getValue();
        String heureFinFiltre = filtreHeureF.getValue();

        reservationsFiltrees.clear();

        // Vérifier si tous les filtres sont sur "Tous"
        boolean aucunFiltreApplique =
                (employeFiltre == null || employeFiltre.equals("Tous")) &&
                        (activiteFiltre == null || activiteFiltre.equals("Tous")) &&
                        (salleFiltre == null || salleFiltre.equals("Tous")) &&
                        (dateFiltreDebut == null || dateFiltreDebut.equals("Tous")) &&
                        (dateFiltreFin == null || dateFiltreFin.equals("Tous")) &&
                        (heureDebutFiltre == null || heureDebutFiltre.equals("Tous")) &&
                        (heureFinFiltre == null || heureFinFiltre.equals("Tous"));

        if (aucunFiltreApplique) {
            // Si aucun filtre n'est appliqué, réinitialiser les items de la table avec toutes les salles disponibles
            tabSalle.setItems(listReservationDuree); // Revenir aux données d'origine sans filtrage
        } else {
            for (ReservationDuree reservation : listReservationDuree) {

                boolean matchesFiltre =
                        (employeFiltre == null || employeFiltre.equals("Tous") || reservation.getEmploye().equalsIgnoreCase(employeFiltre)) &&
                                (activiteFiltre == null || activiteFiltre.equals("Tous") || reservation.getActivite().equalsIgnoreCase(activiteFiltre)) &&
                                (salleFiltre == null || salleFiltre.equals("Tous") || reservation.getSalle().equalsIgnoreCase(salleFiltre));

                boolean matchesDateDebut = true;
                boolean matchesDateFin = true;

                if (dateFiltreDebut != null && !dateFiltreDebut.equals("Tous")) {
                    LocalDate dateDebut = parseDate(dateFiltreDebut);
                    if (dateDebut != null) {
                        LocalDate dateReservation = parseDate(reservation.getDate());
                        if (dateReservation != null) {
                            matchesDateDebut = !dateReservation.isBefore(dateDebut);
                        }
                    }
                }

                if (dateFiltreFin != null && !dateFiltreFin.equals("Tous")) {
                    LocalDate dateFin = parseDate(dateFiltreFin);
                    if (dateFin != null) {
                        LocalDate dateReservation = parseDate(reservation.getDate());
                        if (dateReservation != null) {
                            matchesDateFin = !dateReservation.isAfter(dateFin);
                        }
                    }
                }

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

                if (matchesFiltre && matchesDateDebut && matchesDateFin && matchesHeureDebut && matchesHeureFin) {
                    reservationsFiltrees.add(reservation);
                }
            }
            filtreSet = true;
            // Mettre à jour la table avec les salles filtrées
            tabSalle.setItems(reservationsFiltrees);
        }
    }

    // Méthode utilitaire pour la conversion des heures en LocalTime
    private LocalTime parseHeure(String heure) {
        try {
            heure = heure.replace('h', ':');
            return LocalTime.parse(heure);
        } catch (DateTimeParseException e) {
            System.out.println("Erreur de format d'heure: " + heure);
            return null;
        }
    }

    private LocalDate parseDate(String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Erreur de format de date: " + date);
            return null;
        }
    }

    private int parseDuree(String duree) {
        String[] parts = duree.split("h");
        int heures = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return heures * 60 + minutes;  // Conversion en minutes
    }

    @FXML
    private void handleGenererPdf(){
        FileChooser choixFichier = new FileChooser();
        choixFichier.setTitle("Enregistrer le fichier PDF");

        choixFichier.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));

        File fichier = choixFichier.showSaveDialog(btnGenererPdf.getScene().getWindow());
        if (fichier != null) {
            if (!filtreSet) {
                GenererPdf.genererPdfClassement(listReservationDuree, fichier);
            } else {
                GenererPdf.genererPdfClassement(reservationsFiltrees, fichier);
            }
        }
    }
}