/*
 * Classement.java               21/11/2024
 * IUT DE RODEZ               Pas de copyrights
 */

package sae.statisalle.controller;

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
import sae.statisalle.modele.objet.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.IntStream;

/**
 * Controleur des statistiques - Classement
 * @author erwan.thierry
 * @author rodrigo.xaviertaborda
 */
public class Classement {

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

                //Associer la réservation spécifique
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

                // Associer la réservation spécifique
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

                //Associer la réservation spécifique
                activiteDetaillee.getReservations().add(reservation);

                activitesDetaillees.add(activiteDetaillee);
            }
        }

        return activitesDetaillees;
    }

    public void chargerDonnees() {
        btnAfficherTableaux.setVisible(false);

        grandTableau.setVisible(true);

        tabEmploye.getItems().clear();
        tabSalle.getItems().clear();
        tabActivite.getItems().clear();

        // Appel de la méthode centralisée pour charger les fichiers
        LireFichier.chargerDonneesCSV("src/main/resources/csv", listEmploye, listSalle, listActivite, listReservation);

//        List<Reservation> listeSimple = new ArrayList<>(listReservation);
//        Reservation reservation;
//        reservation = listeSimple.getFirst();
//        System.out.print(reservation);

        // Concatène le nom et prénom de chaque employé
        for (Employe employe : listEmploye) {
            employe.setNom(employe.getNom() + " " + employe.getPrenom());
        }

        // Table employe
        idEmploye.setCellValueFactory(new PropertyValueFactory<>("idE"));
        nomPrenomE.setCellValueFactory(new PropertyValueFactory<>("nom"));
        salleE.setCellValueFactory(new PropertyValueFactory<>("sallesReservees"));
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

    // TODO
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
        // création d'une liste contenant tous les filtres sauf celui sur les salles
        List<Node> filtres = Arrays.asList(
                filtreEmploye, filtreActivite, filtreDateDebut, filtreDateFin, filtreHeureD, filtreHeureF,
                textfiltreEmploye, textfiltreActivite, textfiltreDateDebut, textfiltreDateFin, textfiltreHeureD,
                textfiltreHeureF, reinitialiserFiltre
        );

        // Détermine la visibilité en fonction de la feuille sélectionnée
        boolean visible = feuilleSalle.isSelected();

        // Applique la visibilité à chaque composant si celui-ci n'est pas null
        filtres.forEach(composantFiltre -> {
            //Vérification
            if (composantFiltre != null) {
                composantFiltre.setVisible(visible);
            }
        });
    }

    @FXML
    private void afficherFiltreActivite() {
        // création d'une liste contenant tous les filtres sauf celui sur les activités
        List<Node> filtres = Arrays.asList(
                filtreEmploye, filtreSalle, filtreDateDebut, filtreDateFin, filtreHeureD, filtreHeureF,
                textfiltreEmploye, textfiltreSalle, textfiltreDateDebut, textfiltreDateFin, textfiltreHeureD,
                textfiltreHeureF, reinitialiserFiltre
        );

        // Détermine la visibilité en fonction de la feuille sélectionnée
        boolean visible = feuilleActivite.isSelected();

        // Applique la visibilité à chaque composant si celui-ci n'est pas null
        filtres.forEach(composantFiltre -> {
            //Vérification
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
        // création d'une liste contenant tous les filtres sauf celui sur les employés
        List<Node> filtres = Arrays.asList(
                filtreSalle, filtreActivite, filtreDateDebut, filtreDateFin, filtreHeureD, filtreHeureF,
                textfiltreSalle, textfiltreActivite, textfiltreDateDebut, textfiltreDateFin, textfiltreHeureD,
                textfiltreHeureF, reinitialiserFiltre
        );

        // Détermine la visibilité en fonction de la feuille sélectionnée
        boolean visible = feuilleEmploye.isSelected();

        // Applique la visibilité à chaque composant si celui-ci n'est pas null
        filtres.forEach(composantFiltre -> {
            //Vérification
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
        // Vérifier l'initialisation des composants
        System.out.println(filtreEmploye); // Devrait retourner l'objet associé

      //  // Appel de méthodes
      //  masquerFiltres();

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

//    private void masquerFiltres() {
//        filtreEmploye.setVisible(false);
//        textfiltreEmploye.setVisible(false);
//        filtreSalle.setVisible(false);
//        textfiltreSalle.setVisible(false);
//        filtreActivite.setVisible(false);
//        textfiltreActivite.setVisible(false);
//        filtreDateDebut.setVisible(false);
//        filtreDateFin.setVisible(false);
//        textfiltreDateDebut.setVisible(false);
//        textfiltreDateFin.setVisible(false);
//        filtreHeureD.setVisible(false);
//        textfiltreHeureD.setVisible(false);
//        filtreHeureF.setVisible(false);
//        textfiltreHeureF.setVisible(false);
//        reinitialiserFiltre.setVisible(false);
//    }

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
                    (activiteFiltre == null || activiteFiltre.equals("Tous") || reservation.getActiviteR().equalsIgnoreCase(activiteFiltre)) /* &&
                     (salleFiltre == null || salleFiltre.equals("Tous") || reservation.getSalleR().equalsIgnoreCase(salleFiltre))*/;

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