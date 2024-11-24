package sae.statisalle.controleur;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import sae.statisalle.modele.LireFichier;
import sae.statisalle.modele.objet.*;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Controleur des statistiques - Pourcentage
 * @author erwan.thierry
 * @author rodrigo.xaviertaborda
 */
public class Pourcentage {

    @FXML
    private Button btnAide;
    @FXML
    private Button btnRetour;
    @FXML
    private Button btnAfficherTableau;
    @FXML
    private Button reinitialiserFiltre;

    @FXML
    private ComboBox<String> filtreEmploye;
    @FXML
    private ComboBox<String> filtreActivite;
    @FXML
    private ComboBox<String> filtreDateDebut;
    @FXML
    private ComboBox<String> filtreDateFin;
    @FXML
    private ComboBox<String> filtreHeureD;
    @FXML
    private ComboBox<String> filtreHeureF;

    @FXML
    private TableView<Salle> tabSalle;
    @FXML
    private TableColumn<Salle, String> nomS;
    @FXML
    private TableColumn<Salle, String> pourcentOccupation;

    @FXML
    private Text textfiltreActivite;
    @FXML
    private Text textfiltreDateDebut;
    @FXML
    private Text textfiltreDateFin;
    @FXML
    private Text textfiltreEmploye;
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
    void actionAide(ActionEvent event) {
        MainControleur.activerAidePourcentage();
    }

    @FXML
    void actionRetour(ActionEvent event) {
        MainControleur.activerActionAnalyse();
    }

    @FXML
    void handleReinitialiserFiltre(ActionEvent event) {
        reinitialiserFiltre();
    }

    /**
     * Calcule la durée d'occupation totale par salle
     * @return dureeParSalle la duree d'occupation de chacune des salles
     */
    private Map<String, Integer> calculerDureeOccupationParSalle() {
        Map<String, Integer> dureeParSalle = new HashMap<>();

        for (Reservation reservation : listReservation) {
            String salleNom = reservation.getSalleR();
            String heureDebutL = reservation.getHeureDebut();
            String heureFinL = reservation.getHeureFin();

            LocalTime heureDebut = parseHeure(heureDebutL);
            LocalTime heureFin = parseHeure(heureFinL);
            int dureeReservation = (int) Duration.between(heureDebut, heureFin).toMinutes();

            dureeParSalle.put(salleNom, dureeParSalle.getOrDefault(salleNom, 0) + dureeReservation);
        }
        return dureeParSalle;
    }

    /**
     * Calcule la durée totale de toutes les réservations
     * @return dureeTotale la duree totale des réservations
     */
    private int calculerDureeTotaleReservations() {
        int dureeTotale = 0;
        for (Reservation reservation : listReservation) {
            String heureDebutL = reservation.getHeureDebut();
            String heureFinL = reservation.getHeureFin();

            LocalTime heureDebut = parseHeure(heureDebutL);
            LocalTime heureFin = parseHeure(heureFinL);
            dureeTotale += Duration.between(heureDebut, heureFin).toMinutes();
        }
        return dureeTotale;
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

    /**
     * Calcule le pourcentage d'occupation d'une salle
     * @param salle objet salle afin de récupérer le pourcentage d'occupation
     *              de chacune d'entre elles
     * @return le pourcentage avec deux chiffres après la virgule
     */
    private String calculerPourcentageOccupation(Salle salle) {
        Map<String, Integer> dureeOccupation = calculerDureeOccupationParSalle();
        int dureeOccupee = dureeOccupation.getOrDefault(salle.getNom(), 0);

        int dureeTotaleReservations = calculerDureeTotaleReservations();

        if (dureeTotaleReservations == 0) {
            return "0%";
        }

        double pourcentage = (double) dureeOccupee / dureeTotaleReservations * 100;

        return String.format("%.2f%%", pourcentage);
    }

    /**
     * Chargement des données dans le tableau, en appelant la méthode
     * chargerDonneesCSV() de LireFichier qui récupère le contenu des fichiers
     * dans des listes
     */
    public void chargerDonnees() {
        btnAfficherTableau.setVisible(false);

        tabSalle.setVisible(true);

        tabSalle.getItems().clear();

        // Appel de la méthode centralisée pour charger les fichiers
        LireFichier.chargerDonneesCSV("src/main/resources/csv", listEmploye, listSalle, listActivite, listReservation);

        for (Employe employe : listEmploye) {
            employe.setNom(employe.getNom() + " " + employe.getPrenom());
        }

        nomS.setCellValueFactory(new PropertyValueFactory<>("nom"));
        pourcentOccupation.setCellValueFactory(cellData -> {
            Salle salle = cellData.getValue();
            return new SimpleStringProperty(calculerPourcentageOccupation(salle));
        });
        tabSalle.setItems(listSalle);

//        remplirComboBoxSalles();
        remplirComboBoxEmployes();
        remplirComboBoxDates();
        remplirComboBoxActivites();
        remplirComboBoxHeuresD();
        remplirComboBoxHeuresF();

//        mettreAJourFiltreHeureFin();
//        mettreAJourFiltreHeureDebut();

//        mettreAJourFiltreDateDebut();
//        mettreAJourFiltreDateFin();

        afficherFiltreEmploye();

        reinitialiserFiltre.setVisible(true);
    }


    @FXML
    private void reinitialiserFiltre() {

        filtreEmploye.getSelectionModel().select("Tous");
        filtreDateDebut.getSelectionModel().select("Tous");
        filtreDateFin.getSelectionModel().select("Tous");
        filtreActivite.getSelectionModel().select("Tous");
        filtreHeureD.getSelectionModel().select("Tous");
        filtreHeureF.getSelectionModel().select("Tous");

        if (tabSalle != null) tabSalle.setItems(listSalle);

        System.out.println("Filtres réinitialisés avec succès.");
    }

    @FXML
    private void afficherFiltreEmploye() {
        List<Node> filtres = Arrays.asList(
                /*filtreSalle,*/ filtreActivite, filtreEmploye, filtreDateDebut, filtreDateFin, filtreHeureD, filtreHeureF,
                /*textfiltreSalle,*/ textfiltreActivite, textfiltreEmploye, textfiltreDateDebut, textfiltreDateFin, textfiltreHeureD,
                textfiltreHeureF, reinitialiserFiltre
        );

        // Applique la visibilité à chaque composant si celui-ci n'est pas null
        filtres.forEach(composantFiltre -> {
            if (composantFiltre != null) {
                composantFiltre.setVisible(true);
            }
        });
    }

    private void remplirComboBox(ComboBox<String> comboBox, Set<String> valeurs) {
        ObservableList<String> items = FXCollections.observableArrayList();
        items.addFirst("Tous");

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

//    private void remplirComboBoxSalles() {
//        Set<String> nomsUniques = new HashSet<>();
//        if (!listSalle.isEmpty()) {
//            for (Salle salle : listSalle) {
//                nomsUniques.add(salle.getNom());
//            }
//        } else {
//            for (Reservation salle : listReservation) {
//                nomsUniques.add(salle.getSalleR());
//            }
//        }
//        remplirComboBox(filtreSalle, nomsUniques);
//    }

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

    private void appliquerFiltres() {
        // Liste filtrée des salles ou des réservations
        ObservableList<Salle> sallesFiltrees = FXCollections.observableArrayList(listSalle);
        ObservableList<Reservation> reservationsFiltrees = FXCollections.observableArrayList(listReservation);

        // Appliquer chaque filtre, s'il est actif
        String filtreEmployeValue = filtreEmploye.getValue();
        if (filtreEmployeValue != null && !filtreEmployeValue.equals("Tous")) {
            reservationsFiltrees.removeIf(reservation -> !reservation.getEmployeR().equals(filtreEmployeValue));
        }

        String filtreActiviteValue = filtreActivite.getValue();
        if (filtreActiviteValue != null && !filtreActiviteValue.equals("Tous")) {
            reservationsFiltrees.removeIf(reservation -> !reservation.getActiviteR().equals(filtreActiviteValue));
        }

        String filtreDateDebutValue = filtreDateDebut.getValue();
        if (filtreDateDebutValue != null && !filtreDateDebutValue.equals("Tous")) {
            reservationsFiltrees.removeIf(reservation -> reservation.getDateR().compareTo(filtreDateDebutValue) < 0);
        }

        String filtreDateFinValue = filtreDateFin.getValue();
        if (filtreDateFinValue != null && !filtreDateFinValue.equals("Tous")) {
            reservationsFiltrees.removeIf(reservation -> reservation.getDateR().compareTo(filtreDateFinValue) > 0);
        }

        String filtreHeureDValue = filtreHeureD.getValue();
        if (filtreHeureDValue != null && !filtreHeureDValue.equals("Tous")) {
            reservationsFiltrees.removeIf(reservation -> {
                LocalTime heureDebut = parseHeure(reservation.getHeureDebut());
                LocalTime filtreHeureDebut = parseHeure(filtreHeureDValue);
                return heureDebut.isBefore(filtreHeureDebut);
            });
        }

        String filtreHeureFValue = filtreHeureF.getValue();
        if (filtreHeureFValue != null && !filtreHeureFValue.equals("Tous")) {
            reservationsFiltrees.removeIf(reservation -> {
                LocalTime heureFin = parseHeure(reservation.getHeureFin());
                LocalTime filtreHeureFin = parseHeure(filtreHeureFValue);
                return heureFin.isAfter(filtreHeureFin);
            });
        }

        // Si des réservations sont filtrées, mettre à jour l'affichage avec elles
        if (!filtreEmployeValue.equals("Tous") || !filtreActiviteValue.equals("Tous")
                || !filtreDateDebutValue.equals("Tous") || !filtreDateFinValue.equals("Tous")
                || !filtreHeureDValue.equals("Tous") || !filtreHeureFValue.equals("Tous")) {

            // Mettre à jour le tableau pour afficher les réservations filtrées
            afficherReservationsFiltrees(reservationsFiltrees);
        } else {
            // Sinon, afficher le tableau global (mode salles)
            afficherTableauGlobal(sallesFiltrees);
        }
    }

    private void afficherTableauGlobal(ObservableList<Salle> salles) {
        tabSalle.getColumns().clear(); // Réinitialiser les colonnes du tableau
        tabSalle.getColumns().addAll(nomS, pourcentOccupation);

        nomS.setCellValueFactory(new PropertyValueFactory<>("nom"));
        pourcentOccupation.setCellValueFactory(cellData -> {
            Salle salle = cellData.getValue();
            return new SimpleStringProperty(calculerPourcentageOccupation(salle));
        });

        tabSalle.setItems(salles);
    }

    private void afficherReservationsFiltrees(ObservableList<Reservation> reservations) {
        //tabSalle.getColumns().clear(); // Réinitialiser les colonnes du tableau

        TableColumn<Salle, String> employeCol = new TableColumn<>("Employé");
        TableColumn<Salle, String> activiteCol = new TableColumn<>("Activité");

        nomS.setPrefWidth(425);
        employeCol.setPrefWidth(425);
        activiteCol.setPrefWidth(425);
        pourcentOccupation.setPrefWidth(425);

        employeCol.setCellValueFactory(new PropertyValueFactory<>("employeR"));
        activiteCol.setCellValueFactory(new PropertyValueFactory<>("activiteR"));









        // je l'ai mis en commentaire pour pouvoir commit comme c'est une erreur
        //tabSalle.setItems(reservations);
    }


    @FXML
    private void initialize(){
        // Liaisons des ComboBox pour les filtres sur les salles
        filtreEmploye.valueProperty().addListener((observable, oldValue, newValue) -> appliquerFiltres());
        filtreActivite.valueProperty().addListener((observable, oldValue, newValue) -> appliquerFiltres());
        // filtreSalle.valueProperty().addListener((observable, oldValue, newValue) -> appliquerFiltres());
        filtreDateDebut.valueProperty().addListener((observable, oldValue, newValue) -> appliquerFiltres());
        filtreDateFin.valueProperty().addListener((observable, oldValue, newValue) -> appliquerFiltres());
        filtreHeureD.valueProperty().addListener((observable, oldValue, newValue) -> appliquerFiltres());
        filtreHeureF.valueProperty().addListener((observable, oldValue, newValue) -> appliquerFiltres());
    }
}