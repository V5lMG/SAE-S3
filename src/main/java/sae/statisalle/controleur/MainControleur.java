/*
 * MainControleur.java         30/10/2024
 * Pas de droits d'auteur ni de copyright
 */
package sae.statisalle.controleur;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import sae.statisalle.modele.objet.Serveur;
import sae.statisalle.modele.Session;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Classe principale du package controleur, qui va lier les vues entre-elles
 * @author rodrigo.xavier-taborda
 * @author mathias.cambon
 * @author valentin.munier-genie
 * @author erwan.thierry
 */
public class MainControleur extends Application {

    private static Thread serveurThread;

    /* déclaration de l'ensemble des scenes */
    private static Scene Accueil;
    private static Scene AideAccueil;
    private static Scene AideConnexion;
    private static Scene AideEnvoyer;
    private static Scene AideAffichage;
    private static Scene AideImporter;
    private static Scene Connexion;
    private static Scene Envoyer;
    private static Scene Importer;
    private static Scene Visualiser;
    private static Scene DonneesCalculees;
    private static Scene ActionAnalyse;
    private static Scene Classement;
    private static Scene Pourcentage;
    private static Scene AideActionAnalyse;
    private static Scene AideClassement;
    private static Scene AideDonneesCalculees;
    private static Scene AidePourcentage;

    /* déclaration du stage */
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
    public static void activerAideAffichage() {
        fenetrePrincipale.setScene(AideAffichage);
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
        fenetrePrincipale.setX(50);
        fenetrePrincipale.setY(50);
        fenetrePrincipale.setScene(Visualiser);
    }

    /**
     * Change la scène pour afficher l'écran de choix d'une action d'analyse
     */
    public static void activerActionAnalyse () {
        fenetrePrincipale.setScene(ActionAnalyse);
    }

    /**
     * Change la scène pour afficher l'écran des données calculés simples
     */
    public static void activerDonneesCalculees () {
        fenetrePrincipale.setScene(DonneesCalculees);
    }

    /**
     * Change la scène pour afficher l'écran des statistiques par classement
     */
    public static void activerClassement () {
        fenetrePrincipale.setScene(Classement);
    }

    /**
     * Change la scène pour afficher l'écran des statistiques par pourcentage
     */
    public static void activerPourcentage() {
        fenetrePrincipale.setScene(Pourcentage);
    }

    /**
     * Change la scène pour afficher l'aide sur le choix d'une action d'analyse
     */
    public static void activerAideActionAnalyse () {
        fenetrePrincipale.setScene(AideActionAnalyse);
    }

    /**
     * Change la scène pour afficher l'aide sur les statistiques de classement
     */
    public static void activerAideClassement() {
        fenetrePrincipale.setScene(AideClassement);
    }

    /**
     * Change la scène pour afficher l'aide sur les données calculées simples
     */
    public static void activerAideDonneesCalculees() {
        fenetrePrincipale.setScene(AideDonneesCalculees);
    }

    /**
     * Change la scène pour afficher l'aide sur les statistiques de pourcentages
     */
    public static void activerAidePourcentage() {
        fenetrePrincipale.setScene(AidePourcentage);
    }

    /**
     * Renvoie la fenêtre principale de l'application.
     * @return l'objet Stage de la fenêtre principale.
     */
    public static Stage getFenetrePrincipale() {
        return fenetrePrincipale;
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            // Tableau des noms et chemins des vues
            Map<String, String> vues = new HashMap<>();
            vues.put("Accueil", "/sae/statisalle/vue/accueil.fxml");
            vues.put("AideAccueil", "/sae/statisalle/vue/aide/aideAccueil.fxml");
            vues.put("AideConnexion", "/sae/statisalle/vue/aide/aideConnexion.fxml");
            vues.put("AideEnvoyer", "/sae/statisalle/vue/aide/aideEnvoyer.fxml");
            vues.put("AideImporter", "/sae/statisalle/vue/aide/aideImporter.fxml");
            vues.put("Connexion", "/sae/statisalle/vue/connexion.fxml");
            vues.put("Envoyer", "/sae/statisalle/vue/envoyer.fxml");
            vues.put("Importer", "/sae/statisalle/vue/importer.fxml");
            vues.put("Visualiser", "/sae/statisalle/vue/affichage.fxml");
            vues.put("AideAffichage", "/sae/statisalle/vue/aide/aideAffichage.fxml");
            vues.put("DonneesCalculees", "/sae/statisalle/vue/donneesCalculees.fxml");
            vues.put("Classement", "/sae/statisalle/vue/classement.fxml");
            vues.put("Pourcentage", "/sae/statisalle/vue/pourcentage.fxml");
            vues.put("AideActionAnalyse", "/sae/statisalle/vue/aide/aideActionAnalyse.fxml");
            vues.put("AideClassement", "/sae/statisalle/vue/aide/aideClassement.fxml");
            vues.put("AideDonneesCalculees", "/sae/statisalle/vue/aide/aideDonneesCalculees.fxml");
            vues.put("AidePourcentage", "/sae/statisalle/vue/aide/aidePourcentage.fxml");
            vues.put("ActionAnalyse", "/sae/statisalle/vue/actionAnalyse.fxml");

            // Chargement des scènes dans une boucle
            for (Map.Entry<String, String> entry : vues.entrySet()) {
                FXMLLoader chargeur = new FXMLLoader();
                chargeur.setLocation(getClass().getResource(entry.getValue()));
                Parent conteneur = chargeur.load();

                Field champScene = this.getClass().getDeclaredField(
                        entry.getKey()
                );
                champScene.setAccessible(true);
                champScene.set(this, new Scene(conteneur));
            }

            Image logo = new Image(Objects.requireNonNull(
                                  ControleurPopup.class.getResourceAsStream(
                            "/sae/statisalle/img/LogoStatisalle.jpg")));

            primaryStage.setScene(Accueil);
            primaryStage.getIcons().add(logo);
            fenetrePrincipale = primaryStage;
            primaryStage.setResizable(false);
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
    public static void initServeur() {
        serveurThread = new Thread(() -> {
            Serveur serveur = null;
            try {
                // initialiser le serveur
                serveur = new Serveur();
                Session.setServeur(serveur);
                String ip = Session.getIpServeur();
                String port = Session.getPortServeur();
                serveur.demarrer(Integer.parseInt(port), ip);
                System.out.println("[MAIN] Serveur démarré sur le "
                                  + "port 54321, en attente de connexions...");

                // lancer un thread pour accepter
                // les connexions client en continu
                Thread acceptClientThread =
                        new Thread(serveur::accepterClients);
                acceptClientThread.start();

                // Continuer à accepter des connexions
                // jusqu'à l'execution du .stop()
                acceptClientThread.join();
            } catch (IOException e) {
                System.err.println("[MAIN] Erreur lors de l'initialisation"
                                   + " du serveur : " + e.getMessage());
            } catch (InterruptedException e) {
                System.out.println("[MAIN] Serveur arrêté.");
            } finally {
                if (serveur != null) {
                    serveur.fermerServeur();
                    System.out.println("[MAIN] Serveur arrêté.");
                }
            }
        });

        serveurThread.start();
        System.out.println("[MAIN] Serveur thread lancé : "
                           + serveurThread.isAlive());
    }

    /**
     * Arrête le thread serveur en cours d'exécution.
     */
    public static void stopThreadServeur() {
        serveurThread.interrupt();
        System.out.println("[MAIN] Thread serveur arrêté.");
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