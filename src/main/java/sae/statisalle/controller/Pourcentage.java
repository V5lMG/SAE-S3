package sae.statisalle.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.event.ActionEvent;
import sae.statisalle.modele.objet.Activite;
import sae.statisalle.modele.objet.Employe;
import sae.statisalle.modele.objet.Reservation;
import sae.statisalle.modele.objet.Salle;

public class Pourcentage {

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
    private TableColumn<Salle, String> totalS;

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
    private Button btnAide;

    @FXML
    private Button btnRetour;

    @FXML
    void handleReinitialiserFiltre(ActionEvent event) {

    }

    @FXML
    void actionAide(ActionEvent event) {
        MainControleur.activerAidePourcentage();
    }

    @FXML
    void actionRetour(ActionEvent event) {
        MainControleur.activerActionAnalyse();
    }

    // Méthode d'initialisation si nécessaire
    @FXML
    public void initialize() {

    }
}