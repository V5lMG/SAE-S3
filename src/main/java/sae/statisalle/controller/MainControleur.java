/*
 * MainControleur.java           30/10/2024
 * Pas de droits d'auteur ni de copyright
 */
package sae.statisalle.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Classe principale du package controleur, qui va lier les vues entre-elles
 * @author rodrigo.xavier-taborda
 */
public class MainControleur extends Application {

    /* Déclaration de l'ensemble des scenes */
    private static Scene Accueil;
    private static Scene AideAccueil;
    private static Scene AideConnexion;
    private static Scene AideEnvoyer;
    private static Scene AideExporter;
    private static Scene AideImporter;
    private static Scene Connexion;
    private static Scene Envoyer;
    private static Scene Exporter;
    private static Scene Importer;

    /* Déclaration du stage */
    private static Stage fenetrePrincipale;

    /**
     * TODO commenter le rôle de cette méthode
     */
    public static void activerAccueil() {
        fenetrePrincipale.setScene(Accueil);
    }

    /**
     * TODO commenter le rôle de cette méthode
     */
    public static void activerAideAccueil() {
        fenetrePrincipale.setScene(AideAccueil);
    }

    /**
     * TODO commenter le rôle de cette méthode
     */
    public static void activerAideConnexion() {
        fenetrePrincipale.setScene(AideConnexion);
    }

    /**
     * TODO commenter le rôle de cette méthode
     */
    public static void activerAideEnvoyer() {
        fenetrePrincipale.setScene(AideEnvoyer);
    }

    /**
     * TODO commenter le rôle de cette méthode
     */
    public static void activerAideExporter() {
        fenetrePrincipale.setScene(AideExporter);
    }

    /**
     * TODO commenter le rôle de cette méthode
     */
    public static void activerAideImporter() {
        fenetrePrincipale.setScene(AideImporter);
    }

    /**
     * TODO commenter le rôle de cette méthode
     */
    public static void activerConnexion() {
        fenetrePrincipale.setScene(Connexion);
    }

    /**
     * TODO commenter le rôle de cette méthode
     */
    public static void activerEnvoyer() {
        fenetrePrincipale.setScene(Envoyer);
    }

    /**
     * TODO commenter le rôle de cette méthode
     */
    public static void activerExporter() {
        fenetrePrincipale.setScene(Exporter);
    }

    /**
     * TODO commenter le rôle de cette méthode
     */
    public static void activerImporter() {
        fenetrePrincipale.setScene(Importer);
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
            chargeurFXMLAccueil.setLocation(getClass().getResource("sae.statisalle/vue/accueil.fxml"));
            Parent conteneur = chargeurFXMLAccueil.load();
            Accueil = new Scene(conteneur);

            FXMLLoader chargeurFXMLAideAccueil = new FXMLLoader();
            chargeurFXMLAideAccueil.setLocation(getClass().getResource("sae.statisalle/vue/aideAccueil.fxml"));
            conteneur = chargeurFXMLAideAccueil.load();
            AideAccueil = new Scene(conteneur);

            FXMLLoader chargeurFXMLAideConnexion = new FXMLLoader();
            chargeurFXMLAideConnexion.setLocation(getClass().getResource("sae.statisalle/vue/aideConnexion.fxml"));
            conteneur = chargeurFXMLAideConnexion.load();
            AideConnexion = new Scene(conteneur);

            FXMLLoader chargeurFXMLAideEnvoyer = new FXMLLoader();
            chargeurFXMLAideEnvoyer.setLocation(getClass().getResource("sae.statisalle/vue/aideEnvoyer.fxml"));
            conteneur = chargeurFXMLAideEnvoyer.load();
            AideEnvoyer = new Scene(conteneur);

            FXMLLoader chargeurFXMLAideExporter = new FXMLLoader();
            chargeurFXMLAideExporter.setLocation(getClass().getResource("sae.statisalle/vue/aideExporter.fxml"));
            conteneur = chargeurFXMLAideExporter.load();
            AideExporter = new Scene(conteneur);

            FXMLLoader chargeurFXMLAideImporter = new FXMLLoader();
            chargeurFXMLAideImporter.setLocation(getClass().getResource("sae.statisalle/vue/aideImporter.fxml"));
            conteneur = chargeurFXMLAideImporter.load();
            AideImporter = new Scene(conteneur);

            FXMLLoader chargeurFXMLConnexion = new FXMLLoader();
            chargeurFXMLConnexion.setLocation(getClass().getResource("sae.statisalle/vue/connexion.fxml"));
            conteneur = chargeurFXMLConnexion.load();
            Connexion = new Scene(conteneur);

            FXMLLoader chargeurFXMLEnvoyer = new FXMLLoader();
            chargeurFXMLEnvoyer.setLocation(getClass().getResource("sae.statisalle/vue/envoyer.fxml"));
            conteneur = chargeurFXMLEnvoyer.load();
            Envoyer = new Scene(conteneur);

            FXMLLoader chargeurFXMLExporter = new FXMLLoader();
            chargeurFXMLExporter.setLocation(getClass().getResource("sae.statisalle/vue/exporter.fxml"));
            conteneur = chargeurFXMLExporter.load();
            Exporter = new Scene(conteneur);

            FXMLLoader chargeurFXMLImporter = new FXMLLoader();
            chargeurFXMLImporter.setLocation(getClass().getResource("sae.statisalle/vue/importer.fxml"));
            conteneur = chargeurFXMLImporter.load();
            Importer = new Scene(conteneur);


            // Définir le titre, la hauteur et la largeur de la fenêtre principale
            //primaryStage.setTitle("Jeu de dames");

            // Définir explicitement la taille de la fenêtre principale
            //primaryStage.setWidth(745);
            //primaryStage.setHeight(572);

            primaryStage.setScene(Accueil);
            fenetrePrincipale = primaryStage;
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Méthode principale pour lancer l'application.
     * @param args Les arguments de la ligne de commande.
     */
    public static void main(String[] args) {
        launch(args);
    }
}