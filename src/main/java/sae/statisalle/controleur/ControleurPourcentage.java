/*
 * ControleurPourcentage.java                 17/10/2024
 * IUT DE RODEZ                Pas de copyrights
 */
package sae.statisalle.controleur;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import sae.statisalle.modele.GenererPdf;
import sae.statisalle.modele.LireFichier;
import sae.statisalle.modele.objet.*;

import java.io.File;
import java.time.Duration;
import java.time.LocalTime;
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
    private ComboBox<String> filtreEmploye, filtreActivite, filtreSalle;

    @FXML
    private TableView<Salle> tabSalle;

    @FXML
    private TableColumn<Salle, String> nomS, pourcentOccupation;

    @FXML
    private Button btnGenererPdf;

    @FXML
    private Text textfiltreActivite, textfiltreEmploye, textfiltreSalle;

    @FXML
    ObservableList<Employe> listEmploye = FXCollections.observableArrayList();
    @FXML
    ObservableList<Activite> listActivite = FXCollections.observableArrayList();
    @FXML
    ObservableList<Salle> listSalle = FXCollections.observableArrayList();
    @FXML
    ObservableList<Reservation> listReservation =
            FXCollections.observableArrayList();
    @FXML
    ObservableList<Reservation> reservationsFiltrees =
            FXCollections.observableArrayList();

    @FXML
    void actionAide(ActionEvent event) {
        MainControleur.activerAidePourcentage();
    }

    @FXML
    void actionRetour(ActionEvent event) {
        // Cacher les ComboBox de filtres
        filtreEmploye.setVisible(false);
        filtreSalle.setVisible(false);
        filtreActivite.setVisible(false);

        // Réinitialiser les ComboBox
        filtreEmploye.getSelectionModel().select("Tous");
        filtreSalle.getSelectionModel().select("Tous");
        filtreActivite.getSelectionModel().select("Tous");

        // Cacher les textes de description des filtres
        textfiltreEmploye.setVisible(false);
        textfiltreSalle.setVisible(false);
        textfiltreActivite.setVisible(false);

        // Cacher les boutons supplémentaires
        reinitialiserFiltre.setVisible(false);
        btnGenererPdf.setVisible(false);

        // Réinitialiser et cacher le tableau
        tabSalle.getItems().clear();
        tabSalle.setVisible(false);

        // Réinitialiser les listes filtrées
        reservationsFiltrees.clear();
        listReservation.clear();
        listSalle.clear();
        listEmploye.clear();
        listActivite.clear();

        // Rendre le bouton d'affichage du tableau visible
        btnAfficherTableau.setVisible(true);

        // Effectuer l'action globale liée au retour
        MainControleur.activerActionAnalyse();

        System.out.println("Retour effectué, état réinitialisé.");
    }

    @FXML
    void handleReinitialiserFiltre(ActionEvent event) {
        resetFiltre();
    }

    public void resetFiltre() {
        filtreSalle.getSelectionModel().select("Tous");
        filtreEmploye.getSelectionModel().select("Tous");
        filtreActivite.getSelectionModel().select("Tous");

        calculerPourcentage(listReservation);

        if (tabSalle != null) {
            tabSalle.setItems(FXCollections.observableArrayList(listSalle));
        }
        System.out.println("Filtres réinitialisés avec succès.");
    }

    /**
     * Filtrage du contenu du tableau "reservation" selon les
     * critères sélectionnés.
     */
    @FXML
    private void appliquerFiltre() {
        String salle = filtreSalle.getSelectionModel().getSelectedItem();
        String employe = filtreEmploye.getSelectionModel().getSelectedItem();
        String activite = filtreActivite.getSelectionModel().getSelectedItem();

        reservationsFiltrees.clear();

        // filtrage des réservations en fonction des critères choisis
        for (Reservation reservation : listReservation) {
            boolean matchesFiltre =
                    ("Tous".equals(salle) ||
                            reservation.getSalleR().equals(salle)) &&
                    ("Tous".equals(employe) ||
                            reservation.getEmployeR().equals(employe)) &&
                    ("Tous".equals(activite) ||
                            reservation.getActiviteR().equals(activite));

            // si la réservation correspond à tous les filtres,
            // on l'ajoute à la liste filtrée
            if (matchesFiltre) {
                reservationsFiltrees.add(reservation);
            }
        }

        calculerPourcentage(reservationsFiltrees);

        // Filtrage des salles basé sur les réservations filtrées
        Set<String> sallesFiltrees = reservationsFiltrees.stream()
                .map(Reservation::getSalleR)
                .collect(Collectors.toSet());

        List<Salle> salles = listSalle.stream()
                .filter(salleObj -> sallesFiltrees.contains(salleObj.getNom()))
                .collect(Collectors.toList());

        // Mettre à jour la TableView des salles filtrées
        tabSalle.setItems(FXCollections.observableArrayList(salles));
    }

    /**
     * Méthode utilitaire pour convertir une chaîne représentant une heure en
     * un objet LocalTime.
     * La chaîne d'entrée peut utiliser le caractère 'h' comme séparateur
     * des heures et des minutes.
     * Ce caractère sera remplacé par ':' avant la tentative de conversion.
     * @param heure la chaîne représentant une heure à convertir
     * @return un objet LocalTime représentant l'heure ou bien "null"
     *         si la chaîne est mal formatée
     */
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
     * Ouvre un dialogue pour enregistrer un fichier PDF contenant les
     * statistiques des salles filtrées.
     * <p>
     * Cette méthode permet à l'utilisateur de choisir un emplacement pour
     * sauvegarder un fichier PDF
     * avec les informations des salles actuellement visibles dans la table.
     * </p>
     */
    public void chargerDonnees() {
        btnAfficherTableau.setVisible(false);
        tabSalle.setVisible(true);
        tabSalle.getItems().clear();

        LireFichier.chargerDonneesCSV("src/main/resources/csv",
                listEmploye, listSalle, listActivite, listReservation);

        remplirComboBox(filtreSalle, listReservation.stream()
                .map(Reservation::getSalleR).collect(Collectors.toSet()));
        remplirComboBox(filtreEmploye, listReservation.stream()
                .map(Reservation::getEmployeR).collect(Collectors.toSet()));
        remplirComboBox(filtreActivite, listReservation.stream()
                .map(Reservation::getActiviteR).collect(Collectors.toSet()));

        System.out.println("Données chargées avec succès.");

        nomS.setCellValueFactory(new PropertyValueFactory<>("nom"));
        pourcentOccupation.setCellValueFactory(new PropertyValueFactory<>
                ("pourcentageOccupation"));

        calculerPourcentage(listReservation);

        // Concaténer le nom et le prénom des employers
        for (Employe employe : listEmploye) {
            employe.setNom(employe.getNom() + " " + employe.getPrenom());
        }

        afficherFiltre();
        reinitialiserFiltre.setVisible(true);
    }

    /**
     * Remplit une ComboBox avec des valeurs à partir d'un ensemble donné.
     * <p>
     * Cette méthode ajoute un élément "Tous" au début de la liste,
     * puis ajoute les éléments de l'ensemble `valeurs` triés par ordre alphabétique.
     * Ensuite, elle définit la première valeur de la ComboBox
     * comme étant l'élément sélectionné par défaut.
     * </p
     * @param comboBox la ComboBox à remplir avec les valeurs.
     * @param valeurs l'ensemble des valeurs à ajouter à la ComboBox.
     */
    private void remplirComboBox(ComboBox<String> comboBox, Set<String> valeurs) {
        ObservableList<String> items = FXCollections.observableArrayList("Tous");
        items.addAll(valeurs.stream().sorted().toList());
        comboBox.setItems(items);
        comboBox.getSelectionModel().selectFirst();
    }

    /**
     * Affiche les filtres dans l'interface utilisateur.
     * <p>
     * Cette méthode rend visibles tous les composants associés aux filtres
     * (ComboBox et textes),
     * ainsi que le bouton de réinitialisation des filtres.
     * Dans un second temps, elle s'assure que chaque composant de filtre
     * est affiché dans l'interface si celui-ci n'est pas "null".
     * </p>
     */
    @FXML
    private void afficherFiltre() {
        List<Node> filtres = Arrays.asList(
                filtreSalle, filtreActivite, filtreEmploye,
                textfiltreSalle, textfiltreActivite, textfiltreEmploye,
                reinitialiserFiltre
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

        System.out.println("Listeners pour filtres ajoutés.");
    }

    /**
     * Ajoute un écouteur de sélection sur une ComboBox pour appliquer
     * automatiquement un filtre.
     * <p>
     * Cette méthode attache un listener à la propriété de sélection de la
     * ComboBox donnée.
     * </p>
     * @param comboBox la ComboBox sur laquelle l'écouteur doit être ajouté.
     *                 Cette ComboBox est donc utilisée pour filtrer les données
     *                 affichées en fonction de la sélection
     *                 de l'utilisateur.
     */
    private void ajouterListenerFiltre(ComboBox<String> comboBox) {
        comboBox.getSelectionModel().selectedItemProperty()
                .addListener((observable,
                              oldValue,
                              newValue) -> appliquerFiltre());
    }

    /**
     * Calcule et met à jour le pourcentage d'occupation des salles en fonction
     * des réservations filtrées.
     * <p>
     * Cette méthode calcule la durée totale des réservations et la durée
     * d'occupation pour chaque salle,
     * puis met à jour l'affichage des pourcentages dans la table des salles.
     * </p>
     * @param listFiltree liste des réservations filtrées utilisées pour le calcul.
     */
    private void calculerPourcentage(ObservableList<Reservation> listFiltree) {
        // Calculer la durée totale de toutes les réservations
        Duration dureeTotaleReservations = Duration.ZERO;
        for (Reservation reservation : listFiltree) {
            LocalTime heureDebut = parseHeure(reservation.getHeureDebut());
            LocalTime heureFin = parseHeure(reservation.getHeureFin());
            if (heureDebut != null && heureFin != null) {
                dureeTotaleReservations = dureeTotaleReservations
                        .plus(Duration.between(heureDebut, heureFin));
            }
        }

        // Calculer la durée de réservation pour chaque salle
        Map<String, Duration> dureeParSalle = new HashMap<>();

        for (Reservation reservation : listFiltree) {
            LocalTime heureDebut = parseHeure(reservation.getHeureDebut());
            LocalTime heureFin = parseHeure(reservation.getHeureFin());
            if (heureDebut != null && heureFin != null) {
                Duration dureeReservation = Duration.between(heureDebut, heureFin);
                dureeParSalle.put(reservation.getSalleR(),
                        dureeParSalle.getOrDefault(reservation.getSalleR(),
                                Duration.ZERO).plus(dureeReservation));
            }
        }

        // Mettre à jour les salles avec le pourcentage d'occupation
        for (Salle salle : listSalle) {
            Duration dureeSalle = dureeParSalle
                    .getOrDefault(salle.getNom(), Duration.ZERO);
            double pourcentageOccupation = 0;
            if (!dureeTotaleReservations.isZero()) {
                pourcentageOccupation = (dureeSalle.toMinutes() * 100.0)
                        / dureeTotaleReservations.toMinutes();
            }
            salle.setPourcentageOccupation(String.format
                    ("%.2f %%", pourcentageOccupation));
        }

        // Mettre à jour la TableView avec les données des salles
        tabSalle.getItems().clear();
        tabSalle.setItems(FXCollections.observableArrayList(listSalle));
        tabSalle.refresh();
    }

    /**
     * Ouvre un dialogue pour enregistrer un fichier PDF contenant les
     * statistiques des salles filtrées.
     * <p>
     * Cette méthode permet à l'utilisateur de choisir un emplacement pour
     * sauvegarder un fichier PDF
     * avec les informations des salles actuellement visibles dans la table.
     * </p>
     */
    @FXML
    private void handleGenererPdf() {
        FileChooser choixFichier = new FileChooser();
        choixFichier.setTitle("Enregistrer le fichier PDF");

        choixFichier.getExtensionFilters().add(new FileChooser.ExtensionFilter
                ("Fichiers PDF", "*.pdf"));

        File fichier = choixFichier.showSaveDialog(btnGenererPdf.getScene()
                .getWindow());

        ObservableList<Salle> sallesFiltrees = tabSalle.getItems();

        // Générer le PDF avec la liste filtrée
        GenererPdf.genererPdfStatistique(sallesFiltrees, fichier);
    }
}