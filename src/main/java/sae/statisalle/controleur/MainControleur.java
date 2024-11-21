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
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import sae.statisalle.modele.Fichier;
import sae.statisalle.modele.Serveur;
import sae.statisalle.modele.Session;

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
        fenetrePrincipale.setX(250);
        fenetrePrincipale.setY(150);
        fenetrePrincipale.setScene(Visualiser);
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
                    .getResource("/sae/statisalle/vue/aide/aideAccueil.fxml"));
            conteneur = chargeurFXMLAideAccueil.load();
            AideAccueil = new Scene(conteneur);

            FXMLLoader chargeurFXMLAideConnexion = new FXMLLoader();
            chargeurFXMLAideConnexion.setLocation(getClass()
                    .getResource("/sae/statisalle/vue/aide/aideConnexion.fxml"));
            conteneur = chargeurFXMLAideConnexion.load();
            AideConnexion = new Scene(conteneur);

            FXMLLoader chargeurFXMLAideEnvoyer = new FXMLLoader();
            chargeurFXMLAideEnvoyer.setLocation(getClass()
                    .getResource("/sae/statisalle/vue/aide/aideEnvoyer.fxml"));
            conteneur = chargeurFXMLAideEnvoyer.load();
            AideEnvoyer = new Scene(conteneur);

            FXMLLoader chargeurFXMLAideImporter = new FXMLLoader();
            chargeurFXMLAideImporter.setLocation(getClass()
                    .getResource("/sae/statisalle/vue/aide/aideImporter.fxml"));
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
            Visualiser = new Scene(conteneur);

            FXMLLoader chargeurFXMLAideAffichage = new FXMLLoader();
            chargeurFXMLAideAffichage.setLocation(getClass()
                    .getResource("/sae/statisalle/vue/aide/aideAffichage.fxml"));
            conteneur = chargeurFXMLAideAffichage.load();
            AideAffichage = new Scene(conteneur);

            primaryStage.setScene(Accueil);
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
                // Initialiser le serveur
                serveur = new Serveur();
                Session.setServeur(serveur);
                String ip = Session.getIpServeur();
                String port = Session.getPortServeur();
                serveur.demarrer(Integer.parseInt(port), ip);
                System.out.println("[MAIN] Serveur démarré sur le port 54321, en attente de connexions...");

                // Lancer un thread pour accepter les connexions client en continu
                Thread acceptClientThread = new Thread(serveur::accepterClients);
                acceptClientThread.start();

                // Le serveur continue à accepter des connexions jusqu'à ce qu'il soit explicitement arrêté
                acceptClientThread.join();

                afficherPopupFichierRecu(serveur.renvoyerDonnee());
            } catch (IOException | InterruptedException e) {
                System.err.println("[MAIN] Erreur lors de l'initialisation du serveur : " + e.getMessage());
            } finally {
                // Fermer le serveur une fois tout terminé
                if (serveur != null) {
                    serveur.fermerServeur();
                    System.out.println("[MAIN] Serveur arrêté.");
                }
            }
        });

        serveurThread.start();
        System.out.println("[MAIN] Serveur thread lancé : " + serveurThread.isAlive());
    }

    /**
     * TODO
     */
    public static void stopThreadServeur() {
        serveurThread.interrupt();
        System.out.println("[MAIN] Thread serveur arrêté.");
    }

    /**
     * Affiche une popup avec des boutons d'action pour gérer un fichier reçu.
     */
    public static void afficherPopupFichierRecu(String donneeRecu) {
        Alert popup = new Alert(Alert.AlertType.CONFIRMATION);
        popup.setTitle("Fichier Reçu");
        popup.setHeaderText("Vous avez reçu un (ou plusieurs) fichier(s).");
        popup.setContentText("Que souhaitez-vous faire ?");

        ButtonType boutonCharger = new ButtonType("Charger dans l'application");
        ButtonType boutonVisualiser = new ButtonType("Visualiser");
        ButtonType boutonAnnuler = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);

        popup.getButtonTypes().setAll(boutonCharger, boutonVisualiser, boutonAnnuler);

        Optional<ButtonType> resultat = popup.showAndWait();

        String reponseAvecRetourLigne = donneeRecu.replace("/N", "\n")
                                                  .replace("/R", "\r");

        String[] fichiers = reponseAvecRetourLigne.split("/EOF");

        if (resultat.isPresent()) {
            if (resultat.get() == boutonVisualiser) {
                afficherPopupVisualiser(reponseAvecRetourLigne);
            } else if (resultat.get() == boutonCharger) {
                String dateDuJour = new SimpleDateFormat("ddMMyyyy").format(new Date());

                try {
                    // Vérifier et créer le répertoire si nécessaire
                    File dossier = new File("src/main/resources/csv/");
                    if (!dossier.exists() && !dossier.mkdirs()) {
                        throw new IOException("Erreur lors de la création du répertoire.");
                    }

                    // parcourir chaque fichier extrait et les sauvegarder
                    for (String fichier : fichiers) {
                        List<String> donneesEnListe = Arrays.asList(fichier.split("\n"));

                        String nomFichier = Fichier.getTypeDepuisContenu(
                                                        donneesEnListe
                                            ) + "_" + dateDuJour + ".csv";

                        // créer ou réécrire le fichier avec les données reçues
                        Fichier.ecritureFichier(donneesEnListe, "src/main/resources/csv/" + nomFichier);
                        System.out.println("Les données ont été sauvegardées dans : " + nomFichier);
                    }

                    // renvoie l'utilisateur vers l'affichage des données
                    MainControleur.activerAffichage();
                } catch (IOException e) {
                    showAlert("Erreur d'importation", "Impossible de charger les données dans l'application : " + e.getMessage());
                }
            } else {
                System.out.println("Action annulée.");
                popup.close();
            }
        }
    }

    private static void afficherPopupVisualiser(String donnees) {
        Dialog<ButtonType> popupVisualiser = new Dialog<>();
        popupVisualiser.setTitle("Visualisation des données");
        popupVisualiser.setHeaderText("Données reçues");

        // création du contenu de la popup
        String contenu = donnees.replace("/EOF", "\n\n");
        TextArea zoneTexte = new TextArea(contenu);
        zoneTexte.setEditable(false);
        zoneTexte.setWrapText(true);

        ButtonType boutonRetour = new ButtonType("Retour", ButtonBar.ButtonData.OK_DONE);
        popupVisualiser.getDialogPane().getButtonTypes().add(boutonRetour);

        popupVisualiser.getDialogPane().setContent(zoneTexte);

        // gestion du retour
        popupVisualiser.showAndWait().ifPresent(result -> {
            if (result == boutonRetour) {
                afficherPopupFichierRecu(donnees);
            }
        });
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