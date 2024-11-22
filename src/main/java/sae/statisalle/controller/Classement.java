package sae.statisalle.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import sae.statisalle.modele.objet.*;

import java.util.ArrayList;
import java.util.List;

public class Classement {

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
    private Button btnAide;

    @FXML
    private Button btnRetour;

    @FXML
    private Button btnAfficherTableaux;

    @FXML
    private TabPane grandTableau;

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

        // Contène le nom et prénom de chaque employé
        for (Employe employe : listEmploye) {
            employe.setNom(employe.getNom() + " " + employe.getPrenom());
        }

        // Configurez les tables après avoir chargé les données
        idEmploye.setCellValueFactory(new PropertyValueFactory<>("idE"));
        nomPrenomE.setCellValueFactory(new PropertyValueFactory<>("nom"));
        activiteE.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        salleE.setCellValueFactory(new PropertyValueFactory<>("numTel"));
        totalE.setCellValueFactory(new PropertyValueFactory<>("numTel"));
        tabEmploye.setItems(listEmploye);

        // Configurez les tables après avoir chargé les données
//        idSalle.setCellValueFactory(new PropertyValueFactory<>("idE"));
//        nomS.setCellValueFactory(new PropertyValueFactory<>("nom"));
//        activiteS.setCellValueFactory(new PropertyValueFactory<>("prenom"));
//        employeS.setCellValueFactory(new PropertyValueFactory<>("numTel"));
//        totalS.setCellValueFactory(new PropertyValueFactory<>("numTel"));
//        tabSalle.setItems(listSalle);
//
//        // Configurez les tables après avoir chargé les données
//        idActivite.setCellValueFactory(new PropertyValueFactory<>("idE"));
//        activiteA.setCellValueFactory(new PropertyValueFactory<>("nom"));
//        salleA.setCellValueFactory(new PropertyValueFactory<>("prenom"));
//        employeA.setCellValueFactory(new PropertyValueFactory<>("numTel"));
//        totalA.setCellValueFactory(new PropertyValueFactory<>("numTel"));
//        tabActivite.setItems(listActivite);

        // Faites de même pour les autres tables si nécessaire...
    }

    public void setListEmploye(ObservableList<Employe> listEmploye) {
        this.listEmploye = listEmploye;
    }

    public void setListSalle(ObservableList<Salle> listSalle) {
        this.listSalle = listSalle;
    }

    public void setListActivite(ObservableList<Activite> listActivite) {
        this.listActivite = listActivite;
    }
}