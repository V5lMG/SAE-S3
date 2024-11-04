/*
 * Reseau.java                21/10/2024
 * Pas de droits d'auteur ni de copyright
 */
package sae.statisalle;

import sae.statisalle.exception.MauvaiseConnexionServeur;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;

/**
 * Classe responsable de la gestion des communications réseau.
 * Elle permet l'envoi et la réception de données entre deux
 * différentes machines via des sockets.
 * <p>
 * La classe fournit des méthodes pour initialiser un serveur,
 * accepter des connexions de clients, envoyer et recevoir des données,
 * ainsi que pour gérer la fermeture des connexions.
 * </p>
 * @author valentin.munier-genie
 * @author rodrigo.xavier-taborda
 */
public class Reseau {

    /** Socket serveur pour écouter les connexions clients. */
    private ServerSocket serverSocket;

    /** Socket pour la connexion avec un client. */
    private Socket clientSocket;

    /** Flux de sortie pour envoyer des données. */
    protected PrintWriter fluxSortie;

    /** Flux d'entrée pour recevoir des données. */
    protected BufferedReader fluxEntree;

    // --------- PARTIE SERVEUR -----------

    /**
     * Initialise le serveur en créant un ServerSocket sur le port spécifié.
     * @param port Le numéro de port sur lequel le serveur doit écouter.
     * @throws IllegalArgumentException si le port spécifié n'est pas dans
     *         l'intervalle valide (0 - 65535).
     * @throws IOException si une erreur survient lors de la création du
     *         ServerSocket, par exemple, si le port est déjà utilisé.
     */
    public void preparerServeur(int port) throws IOException {
        if (port < 0 || port > 65535) {
            throw new IllegalArgumentException("Le port doit être compris"
                    + " entre 0 et 65535.");
        }
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Serveur démarré sur le port : " + port);
        } catch (IOException e) {
            throw new IOException("Erreur lors de la création du serveur : "
                    + e.getMessage());
        }
    }

    /**
     * Attendre une connexion d'un client et renvoyer un objet Reseau
     * configuré pour ce client.
     *
     * @return Reseau un objet représentant la connexion du client,
     *                ou null en cas d'erreur.
     */
    public Reseau attendreConnexionClient() {
        try {
            clientSocket = serverSocket.accept();
            Reseau clientReseau = new Reseau();
            clientReseau.clientSocket = clientSocket;

            // initialiser les flux pour le client
            clientReseau.fluxSortie =
                    new PrintWriter(clientSocket.getOutputStream(),
                                    true);
            clientReseau.fluxEntree =
                    new BufferedReader(
                            new InputStreamReader(
                                    clientSocket.getInputStream()
                            )
                    );

            String clientIP = clientSocket.getInetAddress().getHostAddress();
            String clientNomMachine = clientSocket.getInetAddress()
                                                  .getHostName();
            System.out.println("Client connecté : " + clientNomMachine
                               + " (" + clientIP + ")");

            return clientReseau;
        } catch (IOException e) {
            System.out.println("Erreur lors de l'attente d'un client : "
                               + e.getMessage());
            return null;
        }
    }

    /**
     * Reçoit les données envoyées par le client.
     * @return la ligne de texte reçue du client,
     *         ou "null" en cas d'erreur de lecture.
     */
    public String recevoirDonnees() {
        try {
            return fluxEntree.readLine();
        } catch (IOException e) {
            System.out.println("Erreur lors de la réception des données : "
                    + e.getMessage());
        }
        return null;
    }

    /**
     * Traite la requête reçue du client et retourne une réponse.
     * Cette méthode affiche la requête reçue dans la console et
     * construit une réponse basée sur le contenu de la requête.
     * @param requete La requête reçue du client sous forme de
     *                chaîne de caractères.
     * @return une chaîne de caractères confirmant la réception du message.
     */
    public String traiterRequete(String requete) {
        System.out.println("Message reçu du client : " + requete);
        return "Message reçu : " + requete;
    }

    /**
     * Envoie une réponse au client.
     * Cette méthode envoie la réponse fournie au client via le flux de sortie.
     * @param reponse la réponse à envoyer au client sous forme
     *                de chaîne de caractères.
     */
    public void envoyerReponse(String reponse) {
        fluxSortie.println(reponse);
    }

    /**
     * Ferme toutes les connexions et libère les ressources du serveur.
     * Cette méthode ferme les flux de communication et les sockets
     * du client et du serveur, s'ils sont ouverts,
     * pour garantir une libération propre des ressources.
     * En cas d'erreur, un message est affiché dans la console.
     */
    public void fermerServeur() {
        try {
            if (fluxEntree != null) fluxEntree.close();
            if (fluxSortie != null) fluxSortie.close();
            if (clientSocket != null) clientSocket.close();
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            System.out.println("Erreur lors de la fermeture du serveur : "
                    + e.getMessage());
        }
    }

    public void recevoirFichierCSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("fichier_reconstruit.csv", true))) {
            String ligne;

            // Boucle pour recevoir chaque ligne jusqu'à ce que le client ait terminé l'envoi
            while ((ligne = recevoirDonnees()) != null) {
                writer.println(ligne);  // Écrire la ligne dans le fichier CSV
                envoyerReponse("Ligne reçue : " + ligne);  // Confirmer la réception de la ligne au client
            }

            System.out.println("Fichier CSV reconstruit avec succès.");

        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture du fichier CSV : " + e.getMessage());
        }
    }

    // --------- PARTIE CLIENT -----------

    /**
     * Préparation du client en établissant la connexion au serveur.
     * Cette méthode tente de créer une socket pour se connecter à un serveur
     * spécifié par son adresse IP et son port.
     * Si la connexion échoue, une exception {@link MauvaiseConnexionServeur}
     * est levée avec un message d'erreur approprié.
     *
     * @param adresse L'adresse du serveur, qui peut être une adresse IP ou
     *                un nom d'hôte.
     * @param port Le numéro de port sur lequel le serveur écoute.
     * @throws MauvaiseConnexionServeur si la connexion au serveur échoue
     *         (par exemple, si le serveur n'est pas disponible).
     */
    public void preparerClient(String adresse, int port)
            throws MauvaiseConnexionServeur {

        try {
            clientSocket = new Socket(adresse, port);
            fluxSortie = new PrintWriter(clientSocket.getOutputStream(), true);
            fluxEntree = new BufferedReader(new InputStreamReader(
                    clientSocket.getInputStream()
            ));
            System.out.println("Connexion au serveur " + adresse
                               + " sur le port " + port + " réussie.");
        } catch (IOException e) {
            System.out.println("Erreur lors de la connexion au serveur : "
                               + e.getMessage());
            throw new MauvaiseConnexionServeur(
                    "Aucun serveur sur cette IP et/ou sur ce port");
        }
    }

    /**
     * Envoie le contenu d'un fichier au serveur ligne par ligne.
     * @param cheminFichier le chemin du fichier à envoyer.
     * @throws IllegalArgumentException si le fichier n'existe pas
     * ou n'est pas valide.
     */
    public void envoyer(String cheminFichier) {

        File fichier = new File(cheminFichier);
        if (!fichier.exists() || !fichier.isFile()) {
            System.out.println("Le fichier spécifié n'existe pas.");
            throw new IllegalArgumentException("Le fichier spécifié "
                    + "n'existe pas.");
        }

        try (BufferedReader lecteur = new BufferedReader(new FileReader(fichier))) {
            String ligne;
            while ((ligne = lecteur.readLine()) != null) {
                fluxSortie.println(ligne);  // envoi de chaque ligne
            }

            System.out.println("Fichier envoyé avec succès, ligne par ligne.");
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture ou de "
                    + "l'envoi du fichier : " + e.getMessage());
        }
    }

    /**
     * Réception de la réponse du serveur
     * @return null
     */
    public String recevoirReponse() {
        try {
            return fluxEntree.readLine();
        } catch (IOException e) {
            System.out.println("Erreur lors de la réception de la réponse : "
                    + e.getMessage());
        }
        return null;
    }

    /**
     * Utilisation de la réponse du serveur.
     * Affichage de celle-ci dans la console
     * @param reponse réponse du serveur
     */
    public void utiliserReponse(String reponse) {
        System.out.println("Réponse du serveur : " + reponse);
    }

    /**
     * Fermeture de l'ensemble des ressources du client
     */
    public void fermerClient() {
        try {
            if (fluxEntree != null) fluxEntree.close();
            if (fluxSortie != null) fluxSortie.close();
            if (clientSocket != null) clientSocket.close();
        } catch (IOException e) {
            System.out.println("Erreur lors de la fermeture du client : "
                    + e.getMessage());
        }
    }

    /**
     * Renvoie l'adresse IP de la machine locale et son nom.
     * @return l'adresse IP de la machine sous forme d'objet InetAddress.
     * @throws UnknownHostException si l'adresse IP ne peut pas être déterminée.
     */
    public static InetAddress renvoyerIP() throws UnknownHostException {
        InetAddress ip = InetAddress.getLocalHost();
        System.out.println("Nom de la machine : " + ip.getHostName());
        System.out.println("IP de la machine : " + ip.getHostAddress());
        return ip;
    }
}
