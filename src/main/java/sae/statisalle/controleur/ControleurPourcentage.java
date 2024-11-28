package sae.statisalle.controleur;

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
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controleur des statistiques - ControleurPourcentage
 * @author erwan.thierry
 * @author rodrigo.xaviertaborda
 * @author valentin.munier-genie
 */
public class ControleurPourcentage {

    @FXML
    private Button btnAide, btnRetour, btnAfficherTableau, reinitialiserFiltre;

    @FXML
    private ComboBox<String> filtreEmploye, filtreActivite, filtreDateDebut, filtreSalle, filtreDateFin, filtreHeureD, filtreHeureF;

    @FXML
    private TableView<Salle> tabSalle;

    @FXML
    private TableColumn<Salle, String> nomS, pourcentOccupation;

    @FXML
    private Text textfiltreActivite, textfiltreDateDebut, textfiltreDateFin, textfiltreEmploye, textfiltreHeureD, textfiltreHeureF, textfiltreSalle;

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
        MainControleur.activerAidePourcentage();
    }

    @FXML
    void actionRetour(ActionEvent event) {
        MainControleur.activerActionAnalyse();
    }

    @FXML
    void handleReinitialiserFiltre(ActionEvent event) {
        filtreSalle.getSelectionModel().select("Tous");
        filtreEmploye.getSelectionModel().select("Tous");
        filtreDateDebut.getSelectionModel().select("Tous");
        filtreDateFin.getSelectionModel().select("Tous");
        filtreActivite.getSelectionModel().select("Tous");
        filtreHeureD.getSelectionModel().select("Tous");
        filtreHeureF.getSelectionModel().select("Tous");

        if (tabSalle != null) {
            tabSalle.setItems(listSalle);
        }

        calculerPourcentageGlobal();

        System.out.println("Filtres réinitialisés avec succès.");
    }

    /**
     * Méthode pour filtrer les réservations selon les critères sélectionnés.
     */
    @FXML
    private void appliquerFiltre() {
        String salle = filtreSalle.getSelectionModel().getSelectedItem();
        String employe = filtreEmploye.getSelectionModel().getSelectedItem();
        String activite = filtreActivite.getSelectionModel().getSelectedItem();
        String dateDebut = filtreDateDebut.getSelectionModel().getSelectedItem();
        String dateFin = filtreDateFin.getSelectionModel().getSelectedItem();
        String heureDebut = filtreHeureD.getSelectionModel().getSelectedItem();
        String heureFin = filtreHeureF.getSelectionModel().getSelectedItem();

        reservationsFiltrees.clear();

        // Filtrage des réservations en fonction des critères choisis
        for (Reservation reservation : listReservation) {
            boolean matchesFiltre =
                    ("Tous".equals(salle) || reservation.getSalleR().equals(salle)) &&
                            ("Tous".equals(employe) || reservation.getEmployeR().equals(employe)) &&
                            ("Tous".equals(activite) || reservation.getActiviteR().equals(activite));

            boolean matchesDateDebut = true;
            boolean matchesDateFin = true;

            if (dateDebut != null && !"Tous".equals(dateDebut)) {
                LocalDate dateDebutFiltre = parseDate(dateDebut);
                if (dateDebutFiltre != null) {
                    LocalDate dateReservation = parseDate(reservation.getDateR());
                    if (dateReservation != null) {
                        matchesDateDebut = !dateReservation.isBefore(dateDebutFiltre);
                    }
                }
            }

            if (dateFin != null && !"Tous".equals(dateFin)) {
                LocalDate dateFinFiltre = parseDate(dateFin);
                if (dateFinFiltre != null) {
                    LocalDate dateReservation = parseDate(reservation.getDateR());
                    if (dateReservation != null) {
                        matchesDateFin = !dateReservation.isAfter(dateFinFiltre);
                    }
                }
            }

            boolean matchesHeureDebut = true;
            boolean matchesHeureFin = true;

            if (heureDebut != null && !"Tous".equals(heureDebut)) {
                LocalTime heureDebutFiltre = parseHeure(heureDebut);
                if (heureDebutFiltre != null) {
                    LocalTime heureDebutReservation = parseHeure(reservation.getHeureDebut());
                    if (heureDebutReservation != null) {
                        matchesHeureDebut = !heureDebutReservation.isBefore(heureDebutFiltre);
                    }
                }
            }

            if (heureFin != null && !"Tous".equals(heureFin)) {
                LocalTime heureFinFiltre = parseHeure(heureFin);
                if (heureFinFiltre != null) {
                    LocalTime heureFinReservation = parseHeure(reservation.getHeureFin());
                    if (heureFinReservation != null) {
                        matchesHeureFin = !heureFinReservation.isAfter(heureFinFiltre);
                    }
                }
            }

            // Si la réservation correspond à tous les filtres, on l'ajoute à la liste filtrée
            if (matchesFiltre && matchesDateDebut && matchesDateFin && matchesHeureDebut && matchesHeureFin) {
                reservationsFiltrees.add(reservation);
            }
        }

        // Si le filtre est "Salle", on ne calcule que pour la salle sélectionnée
        if (!"Tous".equals(salle)) {
            calculerPourcentageGlobal();
        } else {
            // Si aucun filtre sur la salle, on applique les filtres et on recalcul les pourcentages globaux
            calculerPourcentageFiltreEmploye(); // Par défaut, on calcule en fonction de l'employé, ou globalement.
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

    // Méthode utilitaire pour la conversion des dates en LocalTime
    private LocalDate parseDate(String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Erreur de format de date: " + date);
            return null;
        }
    }

    /**
     * Chargement des données dans la liste des réservations.
     */
    public void chargerDonnees() {
        btnAfficherTableau.setVisible(false);
        tabSalle.setVisible(true);
        tabSalle.getItems().clear();

        LireFichier.chargerDonneesCSV("src/main/resources/csv", listEmploye, listSalle, listActivite, listReservation);

        remplirComboBox(filtreSalle, listReservation.stream().map(Reservation::getSalleR).collect(Collectors.toSet()));
        remplirComboBox(filtreEmploye, listReservation.stream().map(Reservation::getEmployeR).collect(Collectors.toSet()));
        remplirComboBox(filtreActivite, listReservation.stream().map(Reservation::getActiviteR).collect(Collectors.toSet()));
        remplirComboBox(filtreDateDebut, listReservation.stream().map(Reservation::getDateR).collect(Collectors.toSet()));
        remplirComboBox(filtreDateFin, listReservation.stream().map(Reservation::getDateR).collect(Collectors.toSet()));
        remplirComboBox(filtreHeureD, listReservation.stream().map(Reservation::getHeureDebut).collect(Collectors.toSet()));
        remplirComboBox(filtreHeureF, listReservation.stream().map(Reservation::getHeureFin).collect(Collectors.toSet()));

        mettreAJourFiltreHeureFin();
        mettreAJourFiltreHeureDebut();

        mettreAJourFiltreDateDebut();
        mettreAJourFiltreDateFin();

        System.out.println("Données chargées avec succès.");

        nomS.setCellValueFactory(new PropertyValueFactory<>("nom"));
        pourcentOccupation.setCellValueFactory(new PropertyValueFactory<>("pourcentageOccupation"));

        calculerPourcentageGlobal();

        // Concaténer le nom et le prénom des employers
        for (Employe employe : listEmploye) {
            employe.setNom(employe.getNom() + " " + employe.getPrenom());
        }

        afficherFiltre();
        reinitialiserFiltre.setVisible(true);
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
     * Méthode utilitaire pour remplir les ComboBox.
     */
    private void remplirComboBox(ComboBox<String> comboBox, Set<String> valeurs) {
        ObservableList<String> items = FXCollections.observableArrayList("Tous");
        items.addAll(valeurs.stream().sorted().toList());
        comboBox.setItems(items);
        comboBox.getSelectionModel().selectFirst();
    }

    @FXML
    private void afficherFiltre() {
        List<Node> filtres = Arrays.asList(
                filtreSalle, filtreActivite, filtreEmploye, filtreDateDebut, filtreDateFin, filtreHeureD, filtreHeureF,
                textfiltreSalle, textfiltreActivite, textfiltreEmploye, textfiltreDateDebut, textfiltreDateFin, textfiltreHeureD,
                textfiltreHeureF, reinitialiserFiltre
        );

        filtres.forEach(composantFiltre -> {
            if (composantFiltre != null) {
                composantFiltre.setVisible(true);
            }
        });
    }

    @FXML
    private void initialize() {
        // Ajout de listeners pour appliquer automatiquement les filtres
        ajouterListenerFiltre(filtreSalle);
        ajouterListenerFiltre(filtreEmploye);
        ajouterListenerFiltre(filtreActivite);
        ajouterListenerFiltre(filtreDateDebut);
        ajouterListenerFiltre(filtreDateFin);
        ajouterListenerFiltre(filtreHeureD);
        ajouterListenerFiltre(filtreHeureF);

        System.out.println("Listeners pour filtres ajoutés.");
    }

    /**
     * Ajoute un listener sur une ComboBox pour appliquer le filtre automatiquement.
     */
    private void ajouterListenerFiltre(ComboBox<String> comboBox) {
        comboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            appliquerFiltre();
        });
    }

    private void calculerPourcentageGlobal() {
        // Calculer la durée totale de toutes les réservations
        Duration dureeTotaleReservations = Duration.ZERO;
        for (Reservation reservation : listReservation) {
            LocalTime heureDebut = parseHeure(reservation.getHeureDebut());
            LocalTime heureFin = parseHeure(reservation.getHeureFin());
            if (heureDebut != null && heureFin != null) {
                dureeTotaleReservations = dureeTotaleReservations.plus(Duration.between(heureDebut, heureFin));
            }
        }

        // Calculer la durée de réservation pour chaque salle
        Map<String, Duration> dureeParSalle = new HashMap<>();
        for (Reservation reservation : listReservation) {
            LocalTime heureDebut = parseHeure(reservation.getHeureDebut());
            LocalTime heureFin = parseHeure(reservation.getHeureFin());
            if (heureDebut != null && heureFin != null) {
                Duration dureeReservation = Duration.between(heureDebut, heureFin);
                dureeParSalle.put(reservation.getSalleR(),
                        dureeParSalle.getOrDefault(reservation.getSalleR(), Duration.ZERO).plus(dureeReservation));
            }
        }

        // Mettre à jour les salles avec le pourcentage d'occupation global
        for (Salle salle : listSalle) {
            Duration dureeSalle = dureeParSalle.getOrDefault(salle.getNom(), Duration.ZERO);
            double pourcentageOccupation = 0;
            if (!dureeTotaleReservations.isZero()) {
                pourcentageOccupation = (dureeSalle.toMinutes() * 100.0) / dureeTotaleReservations.toMinutes();
            }
            salle.setPourcentageOccupation(String.format("%.2f %%", pourcentageOccupation));
        }

        // Mettre à jour la TableView avec les données des salles globales
        tabSalle.setItems(FXCollections.observableArrayList(listSalle));
    }


    // -------------Calcul pour les Employes------------------

    /**
     * Calcul du pourcentage d'occupation pour chaque salle réservée par l'employé sélectionné dans la ComboBox.
     */
    public void calculerPourcentageFiltreEmploye() {
        // Récupérer l'employé sélectionné dans le ComboBox
        String employeSelectionne = filtreEmploye.getValue();
        if (employeSelectionne == null || employeSelectionne.isEmpty() || employeSelectionne.equals("Tous")) {
            System.out.println("Aucun employé sélectionné, on affiche les pourcentages globaux.");
            calculerPourcentageGlobal(); // Si aucun employé n'est sélectionné, afficher les pourcentages globaux
            return;
        }

        // Filtrer les réservations de l'employé sélectionné
        List<Reservation> reservationsEmploye = reservationsFiltrees.stream()
                .filter(reservation -> employeSelectionne.equals(reservation.getEmployeR()))
                .collect(Collectors.toList());

        // Si l'employé n'a aucune réservation
        if (reservationsEmploye.isEmpty()) {
            System.err.println("L'employé sélectionné n'a aucune réservation !");
            return;
        }

        // Calcul du temps total des réservations de l'employé
        int tempsTotalEmployeMinutes = 0;
        for (Reservation reservation : reservationsEmploye) {
            LocalTime heureDebut = parseHeure(reservation.getHeureDebut());
            LocalTime heureFin = parseHeure(reservation.getHeureFin());

            if (heureDebut != null && heureFin != null) {
                // Calculer la durée en minutes pour chaque réservation
                Duration dureeReservation = Duration.between(heureDebut, heureFin);
                tempsTotalEmployeMinutes += dureeReservation.toMinutes();
            }
        }

        // Vérifier que le total de l'employé n'est pas zéro avant de calculer les pourcentages
        if (tempsTotalEmployeMinutes == 0) {
            System.err.println("L'employé n'a pas de temps total de réservation !");
            return;
        }

        // Calcul du pourcentage d'occupation par salle
        Map<String, Long> dureeParSalle = new HashMap<>();
        for (Reservation reservation : reservationsEmploye) {
            String salleNom = reservation.getSalleR();
            LocalTime heureDebut = parseHeure(reservation.getHeureDebut());
            LocalTime heureFin = parseHeure(reservation.getHeureFin());

            if (heureDebut != null && heureFin != null) {
                // Calculer la durée de la réservation et l'ajouter à la salle
                Duration dureeReservation = Duration.between(heureDebut, heureFin);
                dureeParSalle.put(salleNom, dureeParSalle.getOrDefault(salleNom, 0L) + dureeReservation.toMinutes());
            }
        }

        // Mettre à jour les pourcentages d'occupation des salles
        for (Salle salle : listSalle) {
            Long dureeSalle = dureeParSalle.getOrDefault(salle.getNom(), 0L);
            double pourcentageOccupation = 0;
            if (tempsTotalEmployeMinutes != 0) {
                pourcentageOccupation = (dureeSalle * 100.0) / tempsTotalEmployeMinutes;
            }
            salle.setPourcentageOccupation(String.format("%.2f %%", pourcentageOccupation));
        }

        // Mettre à jour la TableView avec les données des salles filtrées
        tabSalle.setItems(FXCollections.observableArrayList(listSalle));
        tabSalle.refresh();
    }
}