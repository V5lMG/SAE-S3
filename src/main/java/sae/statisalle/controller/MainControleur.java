/*
 * MainControleur.java         30/10/2024
 * Pas de droits d'auteur ni de copyright
 */
package sae.statisalle.controller;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sae.statisalle.Reseau;
import sae.statisalle.Session;

import java.io.IOException;

/**
 * Classe principale du package controleur, qui va lier les vues entre-elles
 * @author rodrigo.xavier-taborda
 * @author MathiasCambon
 * @author valentin.munier-genie
 */
public class MainControleur extends Application {

    /* Déclaration de l'ensemble des scenes */
    private static Scene Accueil;
    private static Scene AideAccueil;
    private static Scene AideConnexion;
    private static Scene AideEnvoyer;
    private static Scene AideImporter;
    private static Scene Connexion;
    private static Scene Envoyer;
    private static Scene Importer;
    private static Scene Sauvegarder;

    /* Déclaration du stage */
    private static Stage fenetrePrincipale;

    /* Chargeur pour la page de sauvegarde */
    static FXMLLoader chargeurFXMLSauvegarder = new FXMLLoader();

    /**
     * Change la scène de l'application principale
     * pour afficher l'écran d'accueil.
     */
    public static void activerAccueil() {
        fenetrePrincipale.setScene(Accueil);
    }

    /**
     * Change la scène pour afficher l'aide de l'écran d'accueil.
     */
    public static void activerAideAccueil() {
        fenetrePrincipale.setScene(AideAccueil);
    }

    /**
     * Change la scène pour afficher l'aide de connexion.
     */
    public static void activerAideConnexion() {
        fenetrePrincipale.setScene(AideConnexion);
    }

    /**
     * Change la scène pour afficher l'aide pour l'envoi de données.
     */
    public static void activerAideEnvoyer() {
        fenetrePrincipale.setScene(AideEnvoyer);
    }

    /**
     * Change la scène pour afficher l'aide pour l'importation de données.
     */
    public static void activerAideImporter() {
        fenetrePrincipale.setScene(AideImporter);
    }

    /**
     * Change la scène pour afficher l'écran de connexion.
     */
    public static void activerConnexion() {
        fenetrePrincipale.setScene(Connexion);
    }

    /**
     * Change la scène pour afficher l'écran d'envoi de données.
     */
    public static void activerEnvoyer() {
        fenetrePrincipale.setScene(Envoyer);
    }

    /**
     * Change la scène pour afficher l'écran d'importation de données.
     */
    public static void activerImporter() {
        fenetrePrincipale.setScene(Importer);
    }

    /**
     * Change la scène pour afficher l'écran de choix de dossier
     * pour la réception de fichier csv.
     */
    public static void activerSauvegarder() {
        fenetrePrincipale.setScene(Sauvegarder);
    }

    /**
     * Renvoie la fenêtre principale de l'application.
     * @return l'objet Stage de la fenêtre principale.
     */
    public static Stage getFenetrePrincipale() {
        return fenetrePrincipale;
    }

    /**
     * Méthode principale pour lier les interfaces.
     *
     * @param primaryStage Le stage principal de l'application.
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader chargeurFXMLAccueil= new FXMLLoader();
            chargeurFXMLAccueil.setLocation(getClass()
                    .getResource("/sae/statisalle/vue/accueil.fxml"));
            Parent conteneur = chargeurFXMLAccueil.load();
            Accueil = new Scene(conteneur);

            FXMLLoader chargeurFXMLAideAccueil = new FXMLLoader();
            chargeurFXMLAideAccueil.setLocation(getClass()
                    .getResource("/sae/statisalle/vue/aideAccueil.fxml"));
            conteneur = chargeurFXMLAideAccueil.load();
            AideAccueil = new Scene(conteneur);

            FXMLLoader chargeurFXMLAideConnexion = new FXMLLoader();
            chargeurFXMLAideConnexion.setLocation(getClass()
                    .getResource("/sae/statisalle/vue/aideConnexion.fxml"));
            conteneur = chargeurFXMLAideConnexion.load();
            AideConnexion = new Scene(conteneur);

            FXMLLoader chargeurFXMLAideEnvoyer = new FXMLLoader();
            chargeurFXMLAideEnvoyer.setLocation(getClass()
                    .getResource("/sae/statisalle/vue/aideEnvoyer.fxml"));
            conteneur = chargeurFXMLAideEnvoyer.load();
            AideEnvoyer = new Scene(conteneur);

            FXMLLoader chargeurFXMLAideImporter = new FXMLLoader();
            chargeurFXMLAideImporter.setLocation(getClass()
                    .getResource("/sae/statisalle/vue/aideImporter.fxml"));
            conteneur = chargeurFXMLAideImporter.load();
            AideImporter = new Scene(conteneur);

            FXMLLoader chargeurFXMLConnexion = new FXMLLoader();
            chargeurFXMLConnexion.setLocation(getClass()
                    .getResource("/sae/statisalle/vue/connexion.fxml"));
            conteneur = chargeurFXMLConnexion.load();
            Connexion = new Scene(conteneur);

            FXMLLoader chargeurFXMLEnvoyer = new FXMLLoader();
            chargeurFXMLEnvoyer.setLocation(getClass()
                    .getResource("/sae/statisalle/vue/envoyer.fxml"));
            conteneur = chargeurFXMLEnvoyer.load();
            Envoyer = new Scene(conteneur);

            FXMLLoader chargeurFXMLImporter = new FXMLLoader();
            chargeurFXMLImporter.setLocation(getClass()
                    .getResource("/sae/statisalle/vue/importer.fxml"));
            conteneur = chargeurFXMLImporter.load();
            Importer = new Scene(conteneur);

            chargeurFXMLSauvegarder.setLocation(getClass()
                    .getResource( "/sae/statisalle/vue/" +
                            "sauvegarderFichier.fxml"));
            conteneur = chargeurFXMLSauvegarder.load();
            Sauvegarder = new Scene(conteneur);

            primaryStage.setScene(Accueil);
            fenetrePrincipale = primaryStage;
            primaryStage.show();

        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des vues : "
                               + e.getMessage());
        }
    }

    /**
     * Lance un serveur au démarrage de l'application.
     * Le serveur est exécuté dans un thread séparé pour ne pas
     * bloquer l'interface utilisateur et peut accepter plusieurs
     * connexions successives.
     */
    private static void initServeur() {
        Reseau serveur = new Reseau();

        Thread serveurThread = new Thread(() -> {
            try {
                serveur.preparerServeur(54321);
                System.out.println("Serveur démarré et en attente "
                                   + "de connexions...");

                // boucle pour accepter plusieurs connexions
                while (true) {
                    // attendre une nouvelle connexion client
                    Reseau clientReseau = serveur.attendreConnexionClient();

                    // chaque client est géré dans un thread séparé
                    Thread clientHandler = new Thread(() -> {
                        try {
                            String requete = clientReseau.recevoirDonnees();
                            String contenuRequete = clientReseau
                                                       .traiterRequete(requete);
                            Session.setContenu(contenuRequete);

                            SauvegarderFichier controller =
                                    chargeurFXMLSauvegarder.getController();
                            controller.initialiserPage();

                            // thread javaFx pour la page de sauvegarde
                            Platform.runLater(
                                    MainControleur::afficherPopupSauvegarde);

                        } finally {
                            clientReseau.fermerClient();
                        }
                    });

                    clientHandler.start();
                }
            } catch (IOException e) {
                System.err.println("Erreur lors de l'initialisation "
                                   + "du serveur : " + e.getMessage());
            }
        });

        serveurThread.start();
        System.out.println("Serveur en cours d'exécution : "
                           + serveurThread.isAlive());
    }

    /**
     * Affiche automatiquement la scène de sauvegarde
     * pour enregistrer les fichiers reçus.
     */
    public static void afficherPopupSauvegarde() {
        try {
            MainControleur.activerSauvegarder();
        } catch (Exception e) {
            System.err.println("Erreur lors de l'ouverture de la fenêtre "
                    + "de sauvegarde : " + e.getMessage());
            showAlert("Erreur d'ouverture",
                    "Impossible de charger la fenêtre de sauvegarde.");
        }
    }

    /**
     * Affiche une alerte d'erreur en cas de problème.
     */
    public static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Méthode principale pour lancer l'application.
     * @param args Les arguments de la ligne de commande.
     */
    public static void main(String[] args) {
        initServeur();
        launch(args);
    }
}