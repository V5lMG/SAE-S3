/*
 * Client.java                 24/10/2024
 * IUT DE RODEZ                Pas de copyrights
 */
package sae.statisalle.modele.objet;

import sae.statisalle.modele.Connexion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * La classe Client implémente l'interface Connexion pour établir
 * une connexion réseau avec un serveur distant. Elle permet d'envoyer et de
 * recevoir des données en utilisant des flux d'entrée et de sortie sur une
 * socket réseau.
 * <p>
 * @author valentin.munier-genie
 * @author rodrigo xavier-taborda
 */
public class Client implements Connexion {

    /**
     * Le socket utilisé pour la connexion réseau du client.
     */
    private Socket clientSocket;

    /**
     * Flux d'entrée pour recevoir des données du serveur.
     */
    private BufferedReader fluxEntree;

    /**
     * Flux de sortie pour envoyer des données au serveur.
     */
    private PrintWriter fluxSortie;

    /**
     * Se connecte à un serveur spécifié par une adresse et un port.
     * Le client établit une connexion en utilisant un socket et
     * crée les flux d'entrée et de sortie pour communiquer avec le serveur.
     *
     * @param adresse L'adresse IP du serveur auquel se connecter.
     * @param port Le port du serveur auquel se connecter.
     * @throws IOException Si une erreur de communication survient lors de la
     *                     connexion ou de la création des flux.
     */
    public void connecter(String adresse, int port) throws IOException {
        clientSocket = new Socket();
        clientSocket.connect(new InetSocketAddress(adresse, port),
                      5000); // timeout de 5 secondes
        fluxSortie = new PrintWriter(clientSocket.getOutputStream(),
                            true);
        fluxEntree = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));

        System.out.println("[CLIENT] Connecté au serveur "
                           + adresse + ":" + port);
    }

    /**
     * Reçoit la clé publique du serveur.
     * Cette méthode utilise le flux d'entrée pour lire la clé
     * publique envoyée par le serveur et la retourne sous forme
     * de chaîne de caractères.
     *
     * @return La clé publique reçue du serveur.
     */
    public String recevoirClePublic() {
        String clePubliqueServeur = recevoir();
        System.out.println("[CLIENT] Clé publique du serveur reçue : "
                           + clePubliqueServeur);
        return clePubliqueServeur;
    }

    /**
     * Envoie la clé publique au serveur.
     * Cette méthode utilise le flux de sortie pour envoyer la clé publique
     * au serveur.
     *
     * @param clePublique La clé publique à envoyer au serveur.
     */
    public void envoyerClePublic(String clePublique) {
        envoyer(clePublique);
        System.out.println("[CLIENT] Clé publique envoyée : " + clePublique);
    }

    /**
     * Envoie des données au serveur. Cette méthode envoie
     * les données spécifiées en utilisant le flux de sortie
     * du client.
     *
     * @param donnees Les données à envoyer au serveur.
     */
    @Override
    public void envoyer(String donnees) {
        fluxSortie.println(donnees);
    }

    /**
     * Reçoit des données du serveur.
     * Cette méthode lit une ligne de texte du flux d'entrée et la retourne.
     * Si une erreur de lecture se produit, elle affiche un message d'erreur.
     *
     * @return La chaîne de caractères lue du serveur, ou null si une
     *         erreur de lecture survient.
     */
    @Override
    public String recevoir() {
        try {
            return fluxEntree.readLine();
        } catch (IOException e) {
            System.err.println("[CLIENT] Erreur lors de la réception : "
                               + e.getMessage());
            return null;
        }
    }

    /**
     * Ferme la connexion au serveur.
     * Cette méthode ferme les flux d'entrée et de sortie ainsi que le socket
     * du client, mettant fin à la communication avec le serveur.
     */
    @Override
    public void fermer() {
        try {
            if (fluxEntree != null) fluxEntree.close();
            if (fluxSortie != null) fluxSortie.close();
            if (clientSocket != null) clientSocket.close();
        } catch (IOException e) {
            System.err.println("[CLIENT] Erreur lors de la fermeture : "
                               + e.getMessage());
        }
    }

    /**
     * Renvoie l'adresse IP de la machine locale utilisée pour se connecter
     * à un serveur externe et son nom de machine.
     * @return l'adresse IP de la machine sous forme d'objet InetAddress,
     *         ou null en cas d'erreur.
     */
    @Override
    public InetAddress renvoyerIP() {
        // 8.8.8.8 correspond au DNS de google
        try (Socket socket = new Socket("8.8.8.8", 53)) {
            return socket.getLocalAddress();
        } catch (IOException e) {
            System.err.println("[CLIENT] Erreur lors de la récupération de "
                               + "l'IP : " + e.getMessage());
            return null; // retourne null pour éviter les erreurs
        }
    }
}