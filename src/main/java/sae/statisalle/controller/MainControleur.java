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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sae.statisalle.Fichier;
import sae.statisalle.Reseau;

import java.io.File;
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
    private static Scene Affichage;

    /* Déclaration du stage */
    private static Stage fenetrePrincipale;

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
     * Change la scène pour afficher l'écran d'affichage des données.
     */
    public static void activerAffichage() {
        fenetrePrincipale.setScene(Affichage);
    }

    /**
     * Renvoie la fenêtre principale de l'application.
     * @return l'objet Stage de la fenêtre principale.
     */
    public static Stage getFenetrePrincipale() {
        return fenetrePrincipale;
    }

    /**
     * Méthode principale pour liée les interfaces.
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

            FXMLLoader chargeurFXMLAffichage = new FXMLLoader();
            chargeurFXMLAffichage.setLocation(getClass()
                    .getResource("/sae/statisalle/vue/affichage.fxml"));
            conteneur = chargeurFXMLAffichage.load();
            Affichage = new Scene(conteneur);

            primaryStage.setScene(Accueil);
            fenetrePrincipale = primaryStage;
            primaryStage.show();

        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des vues "
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

                            // thread javaFx pour dossier et nom
                            Platform.runLater(() ->
                                    afficherPopupSauvegarde(clientReseau,
                                                            contenuRequete));

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
     * Affiche un popup pour choisir le dossier de sauvegarde
     * et le nom du fichier CSV. Enregistre le fichier, ouvre
     * le dossier sélectionné, et ferme le popup.
     *
     * @param clientReseau L'objet réseau du client pour envoyer la réponse.
     * @param contenuRequete Le contenu de la requête prêt à être sauvegardé.
     */
    public static void afficherPopupSauvegarde(Reseau clientReseau,
                                               String contenuRequete) {

        // popup pour le dossier et le nom du fichier
        // FIXME remplacer par une vue
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Enregistrement des données");
        alert.setHeaderText("Réception de données");
        alert.setContentText("Sélectionnez un dossier et un nom de fichier "
                             + "pour enregistrer les données reçues.");

        // attendre la confirmation
        if (alert.showAndWait().filter(result -> result == ButtonType.OK).isPresent()) {

            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Choisir le dossier de téléchargement");
            File dossier = directoryChooser.showDialog(new Stage());

            if (dossier != null) {
                FileChooser fileChooser = new FileChooser();

                fileChooser.setInitialDirectory(dossier);
                fileChooser.setInitialFileName("resultat_requete.csv");

                fileChooser.getExtensionFilters().add(
                        new FileChooser.ExtensionFilter("CSV Files",
                                                        "*.csv"));

                File fichier = fileChooser.showSaveDialog(new Stage());

                if (fichier != null) {
                    Fichier.ecrireFichier(clientReseau,
                                          contenuRequete,
                                          fichier);
                }
            }
        }
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