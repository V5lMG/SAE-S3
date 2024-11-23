///*
// * Exporter.java                24/10/2024
// * IUT DE RODEZ                 Pas de copyrights
// */
//package sae.statisalle.controller;
//
//import javafx.fxml.FXML;
//import javafx.scene.control.Alert;
//import javafx.scene.control.Button;
//import javafx.scene.control.ButtonType;
//import javafx.scene.text.Text;
//import javafx.stage.FileChooser;
//import javafx.stage.Stage;
//
//import java.io.File;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Optional;
//
///**
// * Classe du controller Exporter, qui fait le lien entre
// * la vue Exporter et son modèle.
// * @author MathiasCambon
// * @author Montes Robin
// */
//public class Exporter {
//
//    /**
//     * Liste contenant les chemins des fichiers sélectionnés pour l'envoi.
//     * Chaque chemin représente un fichier que l'utilisateur souhaite
//     * envoyer au serveur.
//     */
//    private List<String> cheminsDesFichiers;
//
//    @FXML
//    private Text cheminFx;
//
//    @FXML
//    private Text nomFx;
//
//    @FXML
//    private Button btnExporter;
//
//    /**
//     * Initialise l'état de l'interface, désactivant le bouton d'exportation.
//     */
//    @FXML
//    void initialize() {
//        btnExporter.setDisable(true);
//    }
//
//    /**
//     * Retourne à l'écran d'accueil de l'application.
//     */
//    @FXML
//    void actionRetour() {
//        MainControleur.activerAccueil();
//    }
//
//    /**
//     * Affiche l'écran d'aide pour l'exportation.
//     */
//    @FXML
//    void actionAide() {
//        MainControleur.activerAideExporter();
//    }
//
//    /**
//     * Gère l'action d'exportation des fichiers.
//     * Affiche une alerte de confirmation avant d'exporter.
//     */
//    @FXML
//    void actionExporter() {
//        Alert envoyer = new Alert(Alert.AlertType.CONFIRMATION);
//        envoyer.setTitle("Exporter");
//        envoyer.setHeaderText(null);
//        envoyer.setContentText("Voulez-vous vraiment exporter ce/ces fichier(s) ?");
//        Optional<ButtonType> result = envoyer.showAndWait();
//
//        if (result.isPresent() && result.get() == ButtonType.OK) {
//            String dateDuJour = new SimpleDateFormat("ddMMyyyy").format(new Date());
//
//            /* Fichier.reecritureFichier(fichierImporter.contenuFichier(),
//               "/fichier/" + fichierImporter.getTypeFichier() + "_" + dateDuJour); */
//
//            System.out.println("Le fichier ou les fichiers ont bien été exporté(s)");
//        } else if (result.isPresent() && result.get() == ButtonType.CANCEL) {
//            envoyer.close();
//        } else {
//            System.out.print("Erreur : le ou les fichiers n'ont pas pu être exporté(s)");
//        }
//    }
//
//    /**
//     * Affiche une alerte pour informer
//     * l'utilisateur d'une situation spécifique.
//     *
//     * @param title   le titre de l'alerte.
//     * @param message le message de l'alerte.
//     */
//    private void showAlert(String title, String message) {
//        Alert alert = new Alert(Alert.AlertType.ERROR);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
//
//    /**
//     * Ouvre une boîte de dialogue pour choisir les données à exporter.
//     * Traite le fichier sélectionné pour l'exportation.
//     */
//    @FXML
//    void actionChoixDonnees() {
//        // Création d'une instance de FileChooser
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Choisir les données à exporter");
//
//        // Filtre pour les types de fichiers appropriés (par exemple, fichiers CSV)
//        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers CSV", "*.csv"));
//
//        // Obtenir le stage principal
//        Stage stage = MainControleur.getFenetrePrincipale();
//
//        // Afficher la boîte de dialogue pour la sélection multiple de fichiers
//        List<File> fichiers = fileChooser.showOpenMultipleDialog(stage);
//
//        // Vérifier si des fichiers ont été sélectionnés
//        if (fichiers != null && fichiers.size() <= 4) {
//            StringBuilder chemins = new StringBuilder();
//            StringBuilder noms = new StringBuilder();
//            cheminsDesFichiers = new ArrayList<>();
//
//            for (File fichier : fichiers) {
//                cheminsDesFichiers.add(fichier.getPath());
//                chemins.append(fichier.getAbsolutePath()).append("\n");
//                noms.append(fichier.getName()).append("\n");
//            }
//
//            cheminFx.setText(chemins.toString());
//            nomFx.setText(noms.toString());
//            System.out.println("Fichiers sélectionnés : \n" + chemins);
//
//            btnExporter.setStyle("-fx-background-color: #4CAF50;");
//            btnExporter.setDisable(false);
//        } else if (fichiers != null) {
//            System.out.println("Vous devez sélectionner au maximum 4 fichiers.");
//            showAlert("Trop de fichier", "Vous devez sélectionner au maximum 4 fichiers.");
//        } else {
//            System.out.println("Aucun fichier sélectionné.");
//            showAlert("Pas de fichier", "Aucun fichier sélectionné.");
//        }
//    }
//}