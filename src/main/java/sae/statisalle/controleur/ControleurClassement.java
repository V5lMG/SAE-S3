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
import sae.statisalle.modele.LireFichier;
import sae.statisalle.modele.objet.*;

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
    private TabPane grandTableau;

    @FXML
    private Tab feuilleSalle;

    // Table de salle
    @FXML
    private TableView<Reservation> tabSalle;
    @FXML
    private TableColumn<Reservation, String> idSalle;
    @FXML
    private TableColumn<Reservation, String> nomS;
    @FXML
    private TableColumn<Reservation, String> employeS;
    @FXML
    private TableColumn<Reservation, String> activiteS;
    @FXML
    private TableColumn<Reservation, String> dateR;
    @FXML
    private TableColumn<Reservation, String> heureDebutR;
    @FXML
    private TableColumn<Reservation, String> heureFinR;
    @FXML
    private TableColumn<Reservation, String> totalS;

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
    ObservableList<Reservation> reservationsFiltrees = FXCollections.observableArrayList();

    @FXML
    void actionAide(ActionEvent event) {
        MainControleur.activerAideClassement();
    }

    @FXML
    void actionRetour(ActionEvent event) {
        MainControleur.activerActionAnalyse();
    }

    /**
     * Chargement des données dans les tableaux, en appelant la méthode
     * chargerDonneesCSV() de LireFichier qui récupère le contenu des fichiers
     * dans des listes
     */
    public void chargerDonnees() {
        btnAfficherTableaux.setVisible(false);

        grandTableau.setVisible(true);

        tabSalle.getItems().clear();

        // Appel de la méthode centralisée pour charger les fichiers
        LireFichier.chargerDonneesCSV("src/main/resources/csv", listEmploye, listSalle, listActivite, listReservation);

        // Table salle
        idSalle.setCellValueFactory(new PropertyValueFactory<>("idReservation"));
        nomS.setCellValueFactory(new PropertyValueFactory<>("salleR"));
        employeS.setCellValueFactory(new PropertyValueFactory<>("employeR"));
        activiteS.setCellValueFactory(new PropertyValueFactory<>("activiteR"));
        dateR.setCellValueFactory(new PropertyValueFactory<>("dateR"));
        heureDebutR.setCellValueFactory(new PropertyValueFactory<>("heureDebut"));
        heureFinR.setCellValueFactory(new PropertyValueFactory<>("heureFin"));
        totalS.setCellValueFactory(cellData -> {
            Reservation ligne = cellData.getValue();
            return new SimpleStringProperty(calculerDureeSalle(ligne));
        });
        listReservation.sort(Comparator.comparingInt((Reservation r) -> {
                    LocalTime debut = parseHeure(r.getHeureDebut());
                    LocalTime fin = parseHeure(r.getHeureFin());
                    return (debut != null && fin != null) ? (int) Duration.between(debut, fin).toMinutes() : 0;
                }).reversed());  // Trie les durées par ordre décroissant
        tabSalle.setItems(listReservation);

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

    public String calculerDureeSalle(Reservation salle) {
        String heureDebutL = salle.getHeureDebut();
        String heureFinL = salle.getHeureFin();

        LocalTime heureDebut = parseHeure(heureDebutL);
        LocalTime heureFin = parseHeure(heureFinL);

        if (heureDebut != null && heureFin != null) {
            int duree = (int) Duration.between(heureDebut, heureFin).toMinutes();
            int heure = duree / 60;
            int minutes = duree % 60;

            return (heure < 10 ? "0" + heure : heure) + "h" + (minutes < 10 ? "0" + minutes : minutes);
        } else {
            return "Non Valide";
        }
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

        tabSalle.setItems(listReservation);

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
            tabSalle.setItems(listReservation); // Revenir aux données d'origine sans filtrage
        } else {
            // Appliquer le filtrage des salles
            ObservableList<Reservation> reservationsFiltrees = FXCollections.observableArrayList();

            for (Reservation reservation : listReservation) {

                boolean matchesFiltre =
                        (employeFiltre == null || employeFiltre.equals("Tous") || reservation.getEmployeR().equalsIgnoreCase(employeFiltre)) &&
                                (activiteFiltre == null || activiteFiltre.equals("Tous") || reservation.getActiviteR().equalsIgnoreCase(activiteFiltre)) &&
                                (salleFiltre == null || salleFiltre.equals("Tous") || reservation.getSalleR().equalsIgnoreCase(salleFiltre));

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
}