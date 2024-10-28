/*
 * Reseau.java                21/10/2024
 * Pas de droit d'auteur ni de copyright
 */
package sae.statisalle;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;

/**
 * Classe responsable de la gestion des communications réseau.
 * Elle permet l'envoi et la réception de données entre les
 * différentes utilisa  tions de l'application via des sockets.
 *
 * <p>
 * La classe fournit des méthodes pour initialiser un serveur,
 * accepter des connexions de clients, envoyer et recevoir des données,
 * ainsi que pour gérer la fermeture des connexions.
 * </p>
 *
 * @author valentin.munier-genie
 * @author rodrigo.xavier-taborda
 */
public class Reseau {

    /** Socket serveur pour écouter les connexions clients. */
    private ServerSocket serverSocket;

    /** Socket pour la connexion avec un client. */
    private Socket clientSocket;

    /** Flux de sortie pour envoyer des données au client. */
    protected PrintWriter fluxSortie;

    /** Flux d'entrée pour recevoir des données du client. */
    protected BufferedReader fluxEntree;

    // --------- PARTIE SERVEUR -----------

    /**
     * Initialise le serveur en créant un ServerSocket sur le port spécifié.
     * @param port Le numéro de port sur lequel le serveur doit écouter.
     */
    public void preparerServeur(int port) throws IOException {
        if (port < 0 || port > 65535) {
            throw new IllegalArgumentException("Le port doit être compris entre 0 et 65535.");
        }
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Serveur démarré sur le port : " + port);
        } catch (IOException e) {
            throw new IOException("Erreur lors de la création du serveur : " + e.getMessage());
        }
    }

    /**
     * Attend la connexion d'un client et initialise les flux de communication.
     * Cette méthode bloque l'exécution jusqu'à ce qu'un client se connecte.
     * Une fois connecté, elle initialise les flux d'entrée et de sortie pour
     * la communication avec le client.
     */
    public void attendreConnexionClient() {
        try {
            clientSocket = serverSocket.accept();
            fluxSortie = new PrintWriter(clientSocket.getOutputStream(), true);
            fluxEntree = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            System.out.println("Client connecté.");
        } catch (IOException e) {
            System.out.println("Erreur lors de l'attente d'un client : " + e.getMessage());
        }
    }

    /**
     * Reçoit les données envoyées par le client.
     *
     * @return La ligne de texte reçue du client, ou {@code null} en cas d'erreur de lecture.
     */
    public String recevoirDonnees() {
        try {
            return fluxEntree.readLine();
        } catch (IOException e) {
            System.out.println("Erreur lors de la réception des données : " + e.getMessage());
        }
        return null;
    }

    /**
     * Traite la requête reçue du client et retourne une réponse.
     * Cette méthode affiche la requête reçue dans la console et construit une réponse
     * basée sur le contenu de la requête.
     *
     * @param requete La requête reçue du client sous forme de chaîne de caractères.
     * @return Une chaîne de caractères confirmant la réception du message.
     */
    public String traiterRequete(String requete) {
        System.out.println("Message reçu du client : " + requete);
        return "Message reçu : " + requete;
    }

    /**
     * Envoie une réponse au client.
     * Cette méthode envoie la réponse fournie au client via le flux de sortie.
     * @param reponse La réponse à envoyer au client sous forme de chaîne de caractères.
     */
    public void envoyerReponse(String reponse) {
        fluxSortie.println(reponse);
    }

    /**
     * Ferme toutes les connexions et libère les ressources du serveur.
     * Cette méthode ferme les flux de communication et les sockets du client et du serveur,
     * s'ils sont ouverts, pour garantir une libération propre des ressources.
     * En cas d'erreur, un message est affiché dans la console.
     */
    public void fermerServeur() {
        try {
            if (fluxEntree != null) fluxEntree.close();
            if (fluxSortie != null) fluxSortie.close();
            if (clientSocket != null) clientSocket.close();
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            System.out.println("Erreur lors de la fermeture du serveur : " + e.getMessage());
        }
    }

    // --------- PARTIE CLIENT -----------

    /**
     * Préparation du client en établissant la connection au serveur
     * @param adresse adresse du serveur
     * @param port port du serveur
     */
    public void preparerClient(String adresse, int port) {
        try {
            clientSocket = new Socket(adresse, port);
            fluxSortie = new PrintWriter(clientSocket.getOutputStream(), true);
            fluxEntree = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            System.out.println("Connexion au serveur " + adresse + " sur le port " + port + " réussie.");
        } catch (IOException e) {
            System.out.println("Erreur lors de la connexion au serveur : " + e.getMessage());
        }
    }

    /**
     * Envoie le contenu d'un fichier au serveur.
     * @param cheminFichier Le chemin du fichier à envoyer.
     * @throws IllegalArgumentException si le fichier n'existe pas ou n'est pas un fichier valide.
     */
    public void envoyer(String cheminFichier) {

        File fichier = new File(cheminFichier);
        if (!fichier.exists() || !fichier.isFile()) {
            System.out.println("Le fichier spécifié n'existe pas.");
            throw new IllegalArgumentException("Le fichier spécifié n'existe pas.");
        }

        try {
            String contenu = Files.readString(fichier.toPath());
            fluxSortie.println(contenu);

            System.out.println("Fichier envoyé avec succès.");
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture ou de l'envoi du fichier : " + e.getMessage());
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
            System.out.println("Erreur lors de la réception de la réponse : " + e.getMessage());
        }
        return null;
    }

    /**
     * Utilisation de la réponse du serveur.
     * Ici affichage de celle-ci dans la console
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
            System.out.println("Erreur lors de la fermeture du client : " + e.getMessage());
        }
    }

    /**
     * Affiche l'adresse IP de la machine locale.
     * @return L'adresse IP de la machine sous forme de chaîne.
     * @throws UnknownHostException si l'adresse IP ne peut pas être déterminée.
     */
    public static InetAddress afficherIP() throws UnknownHostException {
        InetAddress ip = InetAddress.getLocalHost();
        System.out.println("IP de la machine : " + ip);
        return ip;
    }
}
