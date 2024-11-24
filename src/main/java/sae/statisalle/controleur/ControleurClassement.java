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
    @FXML
    private Tab feuilleActivite;
    @FXML
    private Tab feuilleEmploye;

    // Table de salle
    @FXML
    private TableView<Salle> tabSalle;
    @FXML
    private TableColumn<Salle, String> idSalle;
    @FXML
    private TableColumn<Salle, String> nomS;
    @FXML
    private TableColumn<Salle, String> employeS;
    @FXML
    private TableColumn<Salle, String> activiteS;

    @FXML
    private TableColumn<Salle, String> dateR;

    @FXML
    private TableColumn<Salle, String> heureDebutR;

    @FXML
    private TableColumn<Salle, String> heureFinR;

    @FXML
    private TableColumn<Salle, String> totalS;

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

    // Table d'activité
    @FXML
    private TableView<Activite> tabActivite;
    @FXML
    private TableColumn<Activite, String> idActivite;
    @FXML
    private TableColumn<Activite, String> activiteA;
    @FXML
    private TableColumn<Activite, String> salleA;
    @FXML
    private TableColumn<Activite, String> employeA;
    @FXML
    private TableColumn<Activite, String> totalA;

    // Table d'employe
    @FXML
    private TableView<Employe> tabEmploye;
    @FXML
    private TableColumn<Employe, String> idEmploye;
    @FXML
    private TableColumn<Employe, String> nomPrenomE;
    @FXML
    private TableColumn<Employe, String> salleE;
    @FXML
    private TableColumn<Employe, String> activiteE;
    @FXML
    private TableColumn<Employe, String> totalE;

    @FXML
    ObservableList<Employe> listEmploye = FXCollections.observableArrayList();
    @FXML
    ObservableList<Activite> listActivite = FXCollections.observableArrayList();
    @FXML
    ObservableList<Salle> listSalle = FXCollections.observableArrayList();
    @FXML
    ObservableList<Reservation> listReservation = FXCollections.observableArrayList();
    @FXML
    ObservableList<Employe> listEmployeClasser = FXCollections.observableArrayList();
    @FXML
    ObservableList<Activite> listActiviteClasser = FXCollections.observableArrayList();
    @FXML
    ObservableList<Salle> listSalleClasser = FXCollections.observableArrayList();

    @FXML
    void handleReinitialiserFiltre(ActionEvent event) {
        reinitialiserFiltre();
    }

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
    public ObservableList<Salle> getDetailsDesReservations() {
        ObservableList<Salle> sallesDetaillees = FXCollections.observableArrayList();

        for (Salle salle : listSalle) {
            for (Reservation reservation : salle.getReservations()) {
                Salle salleDetaillee = new Salle(
                        salle.getIdentifiant(),
                        salle.getNom(),
                        salle.getCapacite(),
                        salle.getVideoProj(),
                        salle.getEcranXXL(),
                        salle.getNbMachine(),
                        salle.getTypeMachine(),
                        salle.getLogiciel(),
                        salle.getImprimante()
                );

                salleDetaillee.getReservations().add(reservation);

                sallesDetaillees.add(salleDetaillee);
            }
        }

        return sallesDetaillees;
    }

    /**
     * Méthode qui sert à récupérer les attributs de l'objet Employe, en les liants
     * à une réservation spécifique.
     * @return employesDetaillees liste contenant les informations sur les employe
     *         liées à une réservation.
     */
    public ObservableList<Employe> getDetailsDesEmployes() {
        ObservableList<Employe> employesDetaillees = FXCollections.observableArrayList();

        for (Employe employe : listEmploye) {
            for (Reservation reservation : employe.getReservations()) {
                Employe employeDetaille = new Employe(
                        employe.getIdE(),
                        employe.getNom(),
                        employe.getPrenom(),
                        employe.getNumTel()
                );

                employeDetaille.getReservations().add(reservation);

                employesDetaillees.add(employeDetaille);
            }
        }

        return employesDetaillees;
    }

    /**
     * Méthode qui sert à récupérer les attributs de l'objet Activite, en les liants
     * à une réservation spécifique.
     * @return activitesDetaillees liste contenant les informations sur les activite
     *         liées à une réservation.
     */
    public ObservableList<Activite> getDetailsDesActivites() {
        ObservableList<Activite> activitesDetaillees = FXCollections.observableArrayList();

        for (Activite activite : listActivite) {
            for (Reservation reservation : activite.getReservations()) {
                Activite activiteDetaillee = new Activite(
                        activite.getType(),
                        activite.getIdActivite()
                );

                activiteDetaillee.getReservations().add(reservation);

                activitesDetaillees.add(activiteDetaillee);
            }
        }

        return activitesDetaillees;
    }

    /**
     * Chargement des données dans les tableaux, en appelant la méthode
     * chargerDonneesCSV() de LireFichier qui récupère le contenu des fichiers
     * dans des listes
     */
    public void chargerDonnees() {
        btnAfficherTableaux.setVisible(false);

        grandTableau.setVisible(true);

        tabEmploye.getItems().clear();
        tabSalle.getItems().clear();
        tabActivite.getItems().clear();

        // Appel de la méthode centralisée pour charger les fichiers
        LireFichier.chargerDonneesCSV("src/main/resources/csv", listEmploye, listSalle, listActivite, listReservation);

        for (Employe employe : listEmploye) {
            employe.setNom(employe.getNom() + " " + employe.getPrenom());
        }

        // Table employe
        idEmploye.setCellValueFactory(new PropertyValueFactory<>("idE"));
        nomPrenomE.setCellValueFactory(new PropertyValueFactory<>("nom"));
        salleE.setCellValueFactory(new PropertyValueFactory<>("s    allesAssociees"));
        activiteE.setCellValueFactory(new PropertyValueFactory<>("typesActivite"));
        totalE.setCellValueFactory(cellData -> {
            Employe ligne = cellData.getValue();
            return new SimpleStringProperty(calculerDureeEmploye(ligne));
        });
        tabEmploye.setItems(getDetailsDesEmployes());

        // Table salle
        idSalle.setCellValueFactory(new PropertyValueFactory<>("identifiant"));
        nomS.setCellValueFactory(new PropertyValueFactory<>("nom"));
        employeS.setCellValueFactory(new PropertyValueFactory<>("nomEmploye"));
        activiteS.setCellValueFactory(new PropertyValueFactory<>("typesActivite"));
        dateR.setCellValueFactory(new PropertyValueFactory<>("dateR"));
        heureDebutR.setCellValueFactory(new PropertyValueFactory<>("heureDebutR"));
        heureFinR.setCellValueFactory(new PropertyValueFactory<>("heureFinR"));
        totalS.setCellValueFactory(cellData -> {
            Salle ligne = cellData.getValue();
            return new SimpleStringProperty(calculerDureeSalle(ligne));
        });
        tabSalle.setItems(getDetailsDesReservations());

        // Table activite
        idActivite.setCellValueFactory(new PropertyValueFactory<>("type"));
        activiteA.setCellValueFactory(new PropertyValueFactory<>("idActivite"));
        salleA.setCellValueFactory(new PropertyValueFactory<>("sallesAssociees"));
        employeA.setCellValueFactory(new PropertyValueFactory<>("employeAssocies"));
        totalA.setCellValueFactory(cellData -> {
            Activite ligne = cellData.getValue();
            return new SimpleStringProperty(calculerDureeActivite(ligne));
        });
        tabActivite.setItems(getDetailsDesActivites());

        if (grandTableau.getSelectionModel().getSelectedItem() == feuilleActivite) {
            afficherFiltreActivite(); // Chaque tableau à son filtre
        }

        if (grandTableau.getSelectionModel().getSelectedItem() == feuilleSalle) {
            afficherFiltreSalle(); // Chaque tableau à son filtre
        }

        if (grandTableau.getSelectionModel().getSelectedItem() == feuilleEmploye) {
            afficherFiltreEmploye(); // Chaque tableau à son filtre
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

    // TODO Mettre les durée par odre décroissant et par ordre chronologique si durée égale
    /* ---------------------------------------------------------------- */
    public String calculerDureeSalle(Salle salle) {
        String heureDebutL = salle.getHeureDebutR();
        String heureFinL = salle.getHeureFinR();

        LocalTime heureDebut = parseHeure(heureDebutL);
        LocalTime heureFin = parseHeure(heureFinL);

        if (heureDebut != null && heureFin != null) {
            int duree = (int) Duration.between(heureDebut, heureFin).toMinutes();
            int heure = duree / 60;
            int minutes = duree % 60;

            return heure + "h" + (minutes < 10 ? "0" + minutes : minutes);
        } else {
            return "N/A";
        }
    }

    public String calculerDureeActivite(Activite Activite) {
        String heureDebutL = Activite.getHeureDebutR();
        String heureFinL = Activite.getHeureFinR();

        LocalTime heureDebut = parseHeure(heureDebutL);
        LocalTime heureFin = parseHeure(heureFinL);

        if (heureDebut != null && heureFin != null) {
            int duree = (int) Duration.between(heureDebut, heureFin).toMinutes();
            int heure = duree / 60;
            int minutes = duree % 60;

            return heure + "h" + (minutes < 10 ? "0" + minutes : minutes);
        } else {
            return "N/A";
        }
    }

    public String calculerDureeEmploye(Employe Employe) {
        String heureDebutL = Employe.getHeureDebutR();
        String heureFinL = Employe.getHeureFinR();

        LocalTime heureDebut = parseHeure(heureDebutL);
        LocalTime heureFin = parseHeure(heureFinL);

        if (heureDebut != null && heureFin != null) {
            int duree = (int) Duration.between(heureDebut, heureFin).toMinutes();
            int heure = duree / 60;
            int minutes = duree % 60;

            return heure + "h" + (minutes < 10 ? "0" + minutes : minutes);
        } else {
            return "N/A";
        }
    }
    /* ---------------------------------------------------- */
    /* Ligne rajouté par Erwan */

    public void setListEmploye(ObservableList<Employe> listEmploye) {
        this.listEmploye = listEmploye;
    }

    public void setListSalle(ObservableList<Salle> listSalle) {
        this.listSalle = listSalle;
    }

    public void setListActivite(ObservableList<Activite> listActivite) {
        this.listActivite = listActivite;
    }

    @FXML
    private void afficherFiltreSalle() {
        List<Node> filtres = Arrays.asList(
                filtreEmploye, filtreActivite, filtreDateDebut, filtreDateFin, filtreHeureD, filtreHeureF,
                textfiltreEmploye, textfiltreActivite, textfiltreDateDebut, textfiltreDateFin, textfiltreHeureD,
                textfiltreHeureF, reinitialiserFiltre
        );

        boolean visible = feuilleSalle.isSelected();

        // Applique la visibilité à chaque composant si celui-ci n'est pas null
        filtres.forEach(composantFiltre -> {
            if (composantFiltre != null) {
                composantFiltre.setVisible(visible);
            }
        });
    }

    @FXML
    private void afficherFiltreActivite() {
        List<Node> filtres = Arrays.asList(
                filtreEmploye, filtreSalle, filtreDateDebut, filtreDateFin, filtreHeureD, filtreHeureF,
                textfiltreEmploye, textfiltreSalle, textfiltreDateDebut, textfiltreDateFin, textfiltreHeureD,
                textfiltreHeureF, reinitialiserFiltre
        );

        boolean visible = feuilleActivite.isSelected();

        // Applique la visibilité à chaque composant si celui-ci n'est pas null
        filtres.forEach(composantFiltre -> {
            if (composantFiltre != null) {
                composantFiltre.setVisible(visible);
            }
        });

        // Récupérer la position X et Y
        double positionX = filtreActivite.getLayoutX();
        double positionY = filtreActivite.getLayoutY();

        filtreSalle.setLayoutX(positionX);
        filtreSalle.setLayoutY(positionY);

        positionX = textfiltreActivite.getLayoutX();
        positionY = textfiltreActivite.getLayoutY();

        textfiltreSalle.setLayoutX(positionX);
        textfiltreSalle.setLayoutY(positionY);
    }

    @FXML
    private void afficherFiltreEmploye() {
        List<Node> filtres = Arrays.asList(
                filtreSalle, filtreActivite, filtreDateDebut, filtreDateFin, filtreHeureD, filtreHeureF,
                textfiltreSalle, textfiltreActivite, textfiltreDateDebut, textfiltreDateFin, textfiltreHeureD,
                textfiltreHeureF, reinitialiserFiltre
        );

        boolean visible = feuilleEmploye.isSelected();

        // Applique la visibilité à chaque composant si celui-ci n'est pas null
        filtres.forEach(composantFiltre -> {
            if (composantFiltre != null) {
                composantFiltre.setVisible(visible);
            }
        });

        // Récupérer la position X et Y
        double positionX = filtreEmploye.getLayoutX();
        double positionY = filtreEmploye.getLayoutY();

        filtreSalle.setLayoutX(positionX);
        filtreSalle.setLayoutY(positionY);

        positionX = textfiltreEmploye.getLayoutX();
        positionY = textfiltreEmploye.getLayoutY();

        textfiltreSalle.setLayoutX(positionX);
        textfiltreSalle.setLayoutY(positionY);
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
                nomsUniques.add(employe.getNom());
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

        if (tabSalle != null) tabSalle.setItems(getDetailsDesReservations());
        if (tabEmploye != null) tabEmploye.setItems(getDetailsDesEmployes());
        if (tabActivite != null) tabActivite.setItems(getDetailsDesActivites());

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
            tabSalle.setItems(listSalle); // Revenir aux données d'origine sans filtrage
        } else {
            // Appliquer le filtrage des salles
            ObservableList<Salle> sallesFiltrees = FXCollections.observableArrayList();

            for (Salle salle : listSalle) {
                // Filtrage basé sur les valeurs sélectionnées
                boolean matchesFiltre =
                        (salleFiltre == null || salleFiltre.equals("Tous") || salle.getNom().equalsIgnoreCase(salleFiltre));

                // Filtrage des réservations
                ObservableList<Reservation> reservationsFiltrees = FXCollections.observableArrayList();
                for (Reservation reservation : salle.getReservations()) {
                    boolean matches = true;
                    // Appliquer les autres filtres (employé, activité, date, heure)
                    matches &= (employeFiltre == null || employeFiltre.equals("Tous") || reservation.getEmployeR().equalsIgnoreCase(employeFiltre));
                    matches &= (activiteFiltre == null || activiteFiltre.equals("Tous") || reservation.getActiviteR().equalsIgnoreCase(activiteFiltre));
                    matches &= (dateFiltreDebut == null || dateFiltreDebut.equals("Tous") || reservation.getDateR().equals(dateFiltreDebut));
                    matches &= (heureDebutFiltre == null || heureDebutFiltre.equals("Tous") || reservation.getHeureDebut().equals(heureDebutFiltre));

                    if (matches) {
                        reservationsFiltrees.add(reservation);
                    }
                }

                // Ajouter la salle filtrée si elle a des réservations valides
                if (!reservationsFiltrees.isEmpty()) {
                    Salle salleFiltree = new Salle(salle.getIdentifiant(), salle.getNom(), salle.getCapacite(),
                            salle.getVideoProj(), salle.getEcranXXL(), salle.getNbMachine(),
                            salle.getTypeMachine(), salle.getLogiciel(), salle.getImprimante());
                    salleFiltree.getReservations().setAll(reservationsFiltrees);
                    sallesFiltrees.add(salleFiltree);
                }
            }
            //TODO Cette ligne casse l'affichage des classements à l'initialisation, guette quand tu cliques sur le bouton "reset" pour avoir l'affichage qu'il faut
            // Mettre à jour la table avec les salles filtrées
            tabSalle.setItems(sallesFiltrees);
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