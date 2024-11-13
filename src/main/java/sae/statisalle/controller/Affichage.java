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
import sae.statisalle.Fichier;

import java.io.File;
import java.net.URL;
import java.util.List;

public class Affichage {

    @FXML
    private Button btnAfficherTableaux;

    @FXML
    private TabPane grandTableau;

    @FXML
    private Text titre;

    @FXML
    private AnchorPane toutTableau;

    // Table de réservations
    @FXML
    private TableView<Reservation> tabReservation;
    @FXML
    private TableColumn<Reservation, String> idReservation;
    @FXML
    private TableColumn<Reservation, String> salleR;
    @FXML
    private TableColumn<Reservation, String> employeR;
    @FXML
    private TableColumn<Reservation, String> activiteR;
    @FXML
    private TableColumn<Reservation, String> dateR;
    @FXML
    private TableColumn<Reservation, String> heureDebutR;
    @FXML
    private TableColumn<Reservation, String> heureFinR;
    @FXML
    private TableColumn<Reservation, String> descriptionR;
    @FXML
    private TableColumn<Reservation, String> nomR;
    @FXML
    private TableColumn<Reservation, String> prenomR;
    @FXML
    private TableColumn<Reservation, String> numTelR;
    @FXML
    private TableColumn<Reservation, String> usageR;

    // Table des salles
    @FXML
    private TableView<Salle> tabSalle;
    @FXML
    private TableColumn<Salle, String> idSalle;
    @FXML
    private TableColumn<Salle, String> nomS;
    @FXML
    private TableColumn<Salle, String> capaciteS;
    @FXML
    private TableColumn<Salle, String> videoProjS;
    @FXML
    private TableColumn<Salle, String> ecranXXLS;
    @FXML
    private TableColumn<Salle, String> nbrOrdiS;
    @FXML
    private TableColumn<Salle, String> typeS;
    @FXML
    private TableColumn<Salle, String> logicielS;
    @FXML
    private TableColumn<Salle, String> imprimanteS;

    // Table des activités
    @FXML
    private TableView<Activite> tabActivite;
    @FXML
    private TableColumn<Activite, String> idActivite;
    @FXML
    private TableColumn<Activite, String> activiteA;

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

    private ObservableList<Reservation> listReservation;

    private ObservableList<Salle> listSalle;

    private ObservableList<Activite> listActivite;

    private ObservableList<Employe> listEmploye;

    private static final String REPERTOIRE_CSV = "SAE-S3/"; // TODO Modifier le chemin du répertoire

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

    @FXML
    private void chargerDonnees() {
        // Cache le bouton
        btnAfficherTableaux.setVisible(false);

        // Affiche les tableaux
        grandTableau.setVisible(true);

        // Récupère le chemin du répertoire "resources"
        ClassLoader classLoader = getClass().getClassLoader();
        URL ressourceURL = classLoader.getResource("csv"); // URL = la racine de classLoader => src/main/ressources
        if (ressourceURL != null) {
            File dossier = new File(ressourceURL.getPath());
            // Filtre pour obtenir uniquement les fichiers CSV dans le répertoire
            File[] fichiers = dossier.listFiles((dir, name) -> name.endsWith(".csv"));

            for (File fichier : fichiers) {
                Fichier fichierExploite = new Fichier(fichier.getPath());

                List<List<String>> contenu = fichierExploite.recupererDonnees();

                System.out.println(contenu);
                switch (fichierExploite.getTypeFichier()) {
                    case "Employe":
                        listEmploye = FXCollections.observableArrayList();
                        for (List<String> ligne : contenu) {
                            if (ligne.size() >= 4) {
                                listEmploye.add(new Employe(ligne.get(0), ligne.get(1), ligne.get(2), ligne.get(3)));
                            }
                        }
                        //TODO afficher le numéro de téléphone décoder sur 10 caractere et non 4 actuellement
                        idEmploye.setCellValueFactory(new PropertyValueFactory<>("idE"));
                        nomE.setCellValueFactory(new PropertyValueFactory<>("nom"));
                        prenomE.setCellValueFactory(new PropertyValueFactory<>("prenom"));
                        numTelE.setCellValueFactory(new PropertyValueFactory<>("numTel"));

                        tabEmploye.setItems(listEmploye);
                        break;
                    case "Salle":
                        listSalle = FXCollections.observableArrayList();
                        for (List<String> ligne : contenu) {
                            if (ligne.size() >= 9) {
                                listSalle.add(new Salle(ligne.get(0), ligne.get(1), ligne.get(2), ligne.get(3),
                                        ligne.get(4), ligne.get(5), ligne.get(6), ligne.get(7), ligne.get(8)));
                            }
                        }
                        idSalle.setCellValueFactory(new PropertyValueFactory<>("identifiant"));
                        nomS.setCellValueFactory(new PropertyValueFactory<>("nom"));
                        capaciteS.setCellValueFactory(new PropertyValueFactory<>("capacite"));
                        ecranXXLS.setCellValueFactory(new PropertyValueFactory<>("ecranXXL"));
                        typeS.setCellValueFactory(new PropertyValueFactory<>("typeMachine"));
                        videoProjS.setCellValueFactory(new PropertyValueFactory<>("videoProj"));
                        nbrOrdiS.setCellValueFactory(new PropertyValueFactory<>("nbMachine"));
                        logicielS.setCellValueFactory(new PropertyValueFactory<>("logiciel"));
                        imprimanteS.setCellValueFactory(new PropertyValueFactory<>("imprimante"));

                        tabSalle.setItems(listSalle);
                        break;
                    case "Activite":
                        listActivite = FXCollections.observableArrayList();
                        for (List<String> ligne : contenu) {
                            if (ligne.size() >= 2) {
                                listActivite.add(new Activite(ligne.get(0), ligne.get(1)));
                            }
                        }
                        idActivite.setCellValueFactory(new PropertyValueFactory<>("idActivite"));
                        activiteA.setCellValueFactory(new PropertyValueFactory<>("type"));

                        tabActivite.setItems(listActivite);
                        break;
                    case "Reservation":
                        listReservation = FXCollections.observableArrayList();
                        for (List<String> ligne : contenu) {
                            if (ligne.size() >= 12) {
                                listReservation.add(new Reservation(ligne.get(0), ligne.get(1), ligne.get(2), ligne.get(3),
                                        ligne.get(4), ligne.get(5), ligne.get(6), ligne.get(7), ligne.get(8), ligne.get(9),
                                        ligne.get(10),ligne.get(11)));
                            }
                        }
                        idReservation.setCellValueFactory(new PropertyValueFactory<>("idReservation"));
                        salleR.setCellValueFactory(new PropertyValueFactory<>("salleR"));
                        employeR.setCellValueFactory(new PropertyValueFactory<>("employeR"));
                        activiteR.setCellValueFactory(new PropertyValueFactory<>("activiteR"));
                        dateR.setCellValueFactory(new PropertyValueFactory<>("dateR"));
                        heureDebutR.setCellValueFactory(new PropertyValueFactory<>("heureDebut"));
                        heureFinR.setCellValueFactory(new PropertyValueFactory<>("heureFin"));
                        descriptionR.setCellValueFactory(new PropertyValueFactory<>("description"));
                        nomR.setCellValueFactory(new PropertyValueFactory<>("nomIntervenant"));
                        prenomR.setCellValueFactory(new PropertyValueFactory<>("prenomIntervenant"));
                        numTelR.setCellValueFactory(new PropertyValueFactory<>("numTelIntervenant"));
                        usageR.setCellValueFactory(new PropertyValueFactory<>("usage"));

                        tabReservation.setItems(listReservation);
                        break;
                    default:
                        // TODO Faire un message d'erreur plus clair
                        System.out.println("Fichier inconnu : " + fichier.getName());
                }
            }
        }
    }
}