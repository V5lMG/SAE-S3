package sae.statisalle.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;

public class Affichage {

    @FXML
    private Button btnAide;

    @FXML
    private Button btnRetour;

    @FXML
    private Text titre;

    // Table de réservations
    @FXML
    private TableView<?> tabReservation;
    @FXML
    private TableColumn<?, ?> idReservation;
    @FXML
    private TableColumn<?, ?> salleR;
    @FXML
    private TableColumn<?, ?> employeR;
    @FXML
    private TableColumn<?, ?> activiteR;
    @FXML
    private TableColumn<?, ?> dateR;
    @FXML
    private TableColumn<?, ?> heureDebutR;
    @FXML
    private TableColumn<?, ?> heureFinR;
    @FXML
    private TableColumn<?, ?> descriptionR;
    @FXML
    private TableColumn<?, ?> nomR;
    @FXML
    private TableColumn<?, ?> prenomR;
    @FXML
    private TableColumn<?, ?> numTelR;
    @FXML
    private TableColumn<?, ?> usageR;

    // Table des salles
    @FXML
    private TableView<?> tabSalle;
    @FXML
    private TableColumn<?, ?> idSalle;
    @FXML
    private TableColumn<?, ?> nomS;
    @FXML
    private TableColumn<?, ?> capaciteS;
    @FXML
    private TableColumn<?, ?> videoProjS;
    @FXML
    private TableColumn<?, ?> ecranXXLS;
    @FXML
    private TableColumn<?, ?> nbrOrdiS;
    @FXML
    private TableColumn<?, ?> typeS;
    @FXML
    private TableColumn<?, ?> logicielS;
    @FXML
    private TableColumn<?, ?> imprimanteS;

    // Table des activités
    @FXML
    private TableView<?> tabActivite;
    @FXML
    private TableColumn<?, ?> idActivite;
    @FXML
    private TableColumn<?, ?> activiteA;

    // Table des employés
    @FXML
    private TableView<?> tabEmploye;
    @FXML
    private TableColumn<?, ?> idEmploye;
    @FXML
    private TableColumn<?, ?> nomE;
    @FXML
    private TableColumn<?, ?> prenomE;
    @FXML
    private TableColumn<?, ?> numTelE;

    // Méthodes d'action pour les boutons
    @FXML
    private void actionAide() {
        // Logique pour l'action du bouton Aide
    }

    @FXML
    void actionRetour() {
        MainControleur.activerAccueil();
    }
}

