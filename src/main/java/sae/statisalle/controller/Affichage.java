package sae.statisalle.controller;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Callback;
import sae.statisalle.Activite;
import sae.statisalle.Employe;
import sae.statisalle.Reservation;
import sae.statisalle.Salle;

public class Affichage {

    @FXML
    private Button btnAfficherTableaux;

    @FXML
    private Text titre;

    @FXML
    private TabPane grandTableau;

    @FXML
    private AnchorPane toutTableau;

    // Table de réservations
    @FXML
    private TableView<Reservation> tabReservation;
    @FXML
    private TableColumn<Reservation, ?> idReservation;
    @FXML
    private TableColumn<Reservation, ?> salleR;
    @FXML
    private TableColumn<Reservation, ?> employeR;
    @FXML
    private TableColumn<Reservation, ?> activiteR;
    @FXML
    private TableColumn<Reservation, ?> dateR;
    @FXML
    private TableColumn<Reservation, ?> heureDebutR;
    @FXML
    private TableColumn<Reservation, ?> heureFinR;
    @FXML
    private TableColumn<Reservation, ?> descriptionR;
    @FXML
    private TableColumn<Reservation, ?> nomR;
    @FXML
    private TableColumn<Reservation, ?> prenomR;
    @FXML
    private TableColumn<Reservation, ?> numTelR;
    @FXML
    private TableColumn<Reservation, ?> usageR;

    // Table des salles
    @FXML
    private TableView<Salle> tabSalle;
    @FXML
    private TableColumn<Salle, ?> idSalle;
    @FXML
    private TableColumn<Salle, ?> nomS;
    @FXML
    private TableColumn<Salle, ?> capaciteS;
    @FXML
    private TableColumn<Salle, ?> videoProjS;
    @FXML
    private TableColumn<Salle, ?> ecranXXLS;
    @FXML
    private TableColumn<Salle, ?> nbrOrdiS;
    @FXML
    private TableColumn<Salle, ?> typeS;
    @FXML
    private TableColumn<Salle, ?> logicielS;
    @FXML
    private TableColumn<Salle, ?> imprimanteS;

    // Table des activités
    @FXML
    private TableView<Activite> tabActivite;
    @FXML
    private TableColumn<Activite, ?> idActivite;
    @FXML
    private TableColumn<Activite, ?> activiteA;

    // Table des employés
    @FXML
    private Tab ongletEmploye;
    @FXML
    private TableView<Employe> tabEmploye;
    @FXML
    private TableColumn<Employe, String> idEmploye;
    @FXML
    private TableColumn<Employe, String> nomE;
    @FXML
    private TableColumn<Employe, String> prenomE;
    @FXML
    private TableColumn<Employe, String> numTelE;

    ObservableList<Employe> listEmploye = FXCollections.observableArrayList(

            // TODO Création d'objet correspondat à la liste (ici employe)
            new Employe ("Id1","Nom1","Prenom1","numTel1"),
            new Employe ("Id2","Nom2","Prenom2","numTel2")

    );

    // Méthodes d'action pour les boutons
    @FXML
    private void actionAide() {
        // TODO Logique pour l'action du bouton Aide

    }

    @FXML
    void actionRetour() {
        grandTableau.setVisible(false);
        btnAfficherTableaux.setVisible(true);
        MainControleur.activerAccueil();
    }

    public void afficherEmploye() {

        idEmploye.setCellValueFactory(new PropertyValueFactory<>("idE"));
        nomE.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomE.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        numTelE.setCellValueFactory(new PropertyValueFactory<>("numTel"));

        tabEmploye.setItems(listEmploye);
    }

    @FXML
    private void afficherTableaux() {
        // Cache le bouton
        btnAfficherTableaux.setVisible(false);

        // Affiche les tableaux
        grandTableau.setVisible(true);
    }
}

//        Pour la modification du nom du titre
//        if (ongletEmploye.isSelected()) {
//            titre.setText("Affichage de Employe");
//        }

