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

/**
 * Classe responsable de la gestion des communications réseau.
 * Elle permet l'envoi et la réception de données entre les
 * différentes utilisations de l'application via des sockets.
 *
 * @author valentin.munier-genie
 * @author rodrigo.xavier-taborda
 */
public class Reseau {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    // --------- PARTIE SERVEUR -----------

    /**
     * Initialise le serveur en créant un ServerSocket sur le port spécifié.
     * @param port Le numéro de port sur lequel le serveur doit écouter.
     */
    public void preparerServeur(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Serveur démarré sur le port : " + port);
        } catch (IOException e) {
            System.out.println("Erreur lors de la création du serveur : " + e.getMessage());
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
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
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
            return in.readLine();
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
        out.println(reponse);
    }

    /**
     * Ferme toutes les connexions et libère les ressources du serveur.
     * Cette méthode ferme les flux de communication et les sockets du client et du serveur,
     * s'ils sont ouverts, pour garantir une libération propre des ressources.
     * En cas d'erreur, un message est affiché dans la console.
     */
    public void fermerServeur() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
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
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            System.out.println("Connexion au serveur " + adresse + " sur le port " + port + " réussie.");
        } catch (IOException e) {
            System.out.println("Erreur lors de la connexion au serveur : " + e.getMessage());
        }
    }

    /**
     * Construction de la requête grâce à l'entrée de l'utilisateur
     * @param entreeUtilisateur chaîne qu'entre l'utilisateur
     * @return de la chaîne
     */
    public String construireRequete(String entreeUtilisateur) {
        return entreeUtilisateur;
    }

    /**
     * Envoie de la requête au serveur
     * @param requete requête à envoyer
     */
    public void envoyer(String requete) {
        out.println(requete);
    }

    /**
     * Réception de la réponse du serveur
     * @return
     */
    public String recevoirReponse() {
        try {
            return in.readLine(); // FIXME
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
            if (in != null) in.close();
            if (out != null) out.close();
            if (clientSocket != null) clientSocket.close();
        } catch (IOException e) {
            System.out.println("Erreur lors de la fermeture du client : " + e.getMessage());
        }
    }

    /**
     * Affichage de l'IP de l'appareil
     */
    public String afficherIP() throws UnknownHostException {
        InetAddress ip = InetAddress.getLocalHost();
        System.out.print("IP du serveur : " + ip);
        return ip.toString();
    }
}
