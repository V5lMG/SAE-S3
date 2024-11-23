/*
 * Envoyer.java                 30/10/2024
 * IUT DE RODEZ                 Pas de copyrights
 */
package sae.statisalle.controleur;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sae.statisalle.modele.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Contrôleur pour gérer l'envoi de fichiers dans l'application StatiSalle.
 * Permet de sélectionner des fichiers à envoyer et de gérer l'interface.
 *
 * @author valentin.munier-genie
 * @author erwan.thierry
 */
public class ControleurEnvoyer {

    /**
     * Instance de la classe Client pour la connexion réseau.
     */
    private Client client;

    /**
     * Liste contenant les chemins des fichiers sélectionnés.
     */
    private List<String> cheminsDesFichiers;

    @FXML
    private Text cheminFx, nomFx, cheminFichier, nomFichier, adresseIP, ipFx;

    @FXML
    private Button btnEnvoyer;

    /**
     * Gère l'action de retour à l'écran d'accueil de l'application.
     */
    @FXML
    void actionRetour() {
        MainControleur.activerAccueil();
    }

    /**
     * Gère l'action d'affichage de l'aide relative à l'envoi de fichiers.
     */
    @FXML
    void actionAide() {
        MainControleur.activerAideEnvoyer();
    }

    /**
     * Initialise l'état de l'interface, désactivant le bouton d'envoi.
     */
    @FXML
    void initialize() {
        btnEnvoyer.setDisable(true);
    }

    /**
     * Ouvre un sélecteur de fichiers pour
     * choisir des fichiers à envoyer.
     * Met à jour l'interface avec les chemins
     * et noms des fichiers sélectionnés.
     */
    @FXML
    private void actionChoixFichier() {
        Serveur serveur = Session.getServeur();
        ipFx.setText(serveur.renvoyerIP().getHostAddress());

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir des fichiers");

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Fichiers CSV", "*.csv")
        );

        Stage stage = MainControleur.getFenetrePrincipale();
        List<File> fichiers = fileChooser.showOpenMultipleDialog(stage);

        if (fichiers != null && fichiers.size() <= 4) {
            StringBuilder chemins = new StringBuilder();
            StringBuilder noms = new StringBuilder();
            cheminsDesFichiers = new ArrayList<>();

            for (File fichier : fichiers) {
                cheminsDesFichiers.add(fichier.getPath());
                chemins.append(fichier.getAbsolutePath()).append("\n");
                noms.append(fichier.getName()).append("\n");
            }

            cheminFx.setText(chemins.toString());
            nomFx.setText(noms.toString());

            cheminFx.setStyle("-fx-fill: #000000;");
            nomFx.setStyle("-fx-fill: #000000;");
            cheminFichier.setStyle("-fx-fill: #000000;");
            nomFichier.setStyle("-fx-fill: #000000;");
            ipFx.setStyle("-fx-fill: #000000;");
            adresseIP.setStyle("-fx-fill: #000000;");
            btnEnvoyer.setStyle("-fx-background-color: #4CAF50;");
            btnEnvoyer.setDisable(false);

            System.out.println("Fichiers sélectionnés : \n" + chemins);
        } else if (fichiers != null) {
            System.out.println("Vous devez sélectionner "
                               + "au maximum 4 fichiers.");
            MainControleur.showAlert("Trop de fichier",
                    "Vous devez sélectionner au maximum 4 fichiers.");
        } else {
            System.out.println("Aucun fichier sélectionné.");
            MainControleur.showAlert("Pas de fichier",
                    "Aucun fichier sélectionné.");
        }
    }

    /**
     * Gère l'envoi des fichiers sélectionnés au serveur.
     */
    @FXML
    void actionEnvoyer() {
        try {
            // Initialisation du client et connexion au serveur
            client = Session.getClient();
            String ip = Session.getIpServeur();
            String port = Session.getPortServeur();
            client.connecter(ip, Integer.parseInt(port));

            // préparer les données à envoyer
            StringBuilder contenuTotal = new StringBuilder();
            for (String cheminFichier : cheminsDesFichiers) {
                File fichier = new File(cheminFichier);

                if (!fichier.exists()) {
                    throw new IllegalArgumentException("Le fichier n'existe pas : " + cheminFichier);
                }

                try (BufferedReader reader = new BufferedReader(new FileReader(fichier))) {
                    String ligne;
                    while ((ligne = reader.readLine()) != null) {
                        contenuTotal.append(ligne).append("\n");
                    }
                } catch (IOException e) {
                    throw new IOException("Erreur lors de la lecture du fichier : " + fichier.getName(), e);
                }

                contenuTotal.append("/EOF");
            }

            String contenuFormate = contenuTotal.toString().replace("\n", "/N").replace("\r", "/R");

            // Initialisation Diffie-Hellman
            int p = DiffieHellman.genererEntierPremier(1,9999); // TODO passer en BigInteger
            int g = DiffieHellman.genererGenerateur(p);
            int a = DiffieHellman.genererEntierPremier(1,9999);

            int clePubliqueClient = DiffieHellman.expoModulaire(g, a, p);
            client.envoyerClePublic(clePubliqueClient + " ; " + p + " ; " + g);

            String clePartageeServeur = client.recevoirClePublic();
            String[] parties = clePartageeServeur.split(" ; ");
            if (parties.length != 3) {
                throw new IllegalArgumentException("Format de clé publique invalide.");
            }

            int clePubliqueServeur = Integer.parseInt(parties[0]);
            BigInteger cleSecreteCalculee = BigInteger.valueOf(DiffieHellman.expoModulaire(clePubliqueServeur, a, p));
            System.out.println("[CLIENT] Clé secrète calculé : " + cleSecreteCalculee);

            // chiffrement et envoi des données
            String donneesChiffrees = Vigenere.chiffrementDonnees(contenuFormate, cleSecreteCalculee);
            client.envoyer(donneesChiffrees);

            String reponse = client.recevoir();
            System.out.println("[CLIENT] Réponse du serveur : " + reponse);

            afficherConfirmationEtRetour();
        } catch (Exception e) {
            MainControleur.showAlert("Erreur d'envoi", "Une erreur est survenue.");
            System.out.println("[CLIENT] Une erreur est survenue :" + e.getMessage());
        } finally {
            if (client != null) {
                client.fermer();
            }
        }
    }

    /**
     * Affiche une alerte de confirmation et
     * redirige vers l'écran de connexion si l'IP n'est pas localhost.
     */
    private void afficherConfirmationEtRetour() {
        // récup l'ip et enlever le port
        String ipSource = Session.getIpServeur();

        // Si l'IP est localhost, ne pas quitter la page d'envoi
        if (ipSource.equals("localhost") || ipSource.equals("127.0.0.1 ")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Envoi réussi (local)");
            alert.setHeaderText(null);
            alert.setContentText("Les fichiers ont été envoyés avec succès.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Envoi réussi");
            alert.setHeaderText(null);
            alert.setContentText("Les fichiers ont été envoyés avec succès.");

            alert.setOnHidden(event -> MainControleur.activerConnexion());
            alert.showAndWait();
        }
    }
}