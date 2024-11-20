/*
 * Reseau.java                21/10/2024
 * Pas de droits d'auteur ni de copyright
 */
package sae.statisalle.modele;

import java.io.*;
import java.net.*;
import java.io.IOException;

/**
 * Classe de base pour la gestion des communications réseau.
 * Contient les fonctionnalités partagées entre client et serveur,
 * telles que l'envoi, la réception et la fermeture des connexions.
 * </p>
 * @author valentin.munier-genie
 * @author rodrigo.xavier-taborda
 */
public abstract class Reseau implements Connexion {
    /** Socket pour la communication. */
    protected Socket socket;

    /** Flux de sortie pour envoyer des données. */
    protected PrintWriter fluxSortie;

    /** Flux d'entrée pour recevoir des données. */
    protected BufferedReader fluxEntree;

    /**
     * Initialise les flux d'entrée et de sortie associés au socket.
     * Cette méthode est appelée par les classes héritant de Reseau après avoir
     * établi une connexion (client) ou accepté une connexion (serveur).
     *
     * @param socket Le socket à associer.
     * @throws IOException Si une erreur survient lors de l'initialisation des flux.
     */
    protected void initialiserFlux(Socket socket) throws IOException {
        this.socket = socket;
        this.fluxSortie = new PrintWriter(socket.getOutputStream(), true);
        this.fluxEntree = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println("[RESEAU] Flux d'entrée et de sortie initialisés.");
    }

    /**
     * Renvoie l'adresse IP de la machine locale utilisée pour se connecter
     * à un serveur externe, ainsi que son nom d'hôte.
     *
     * @return L'adresse IP locale ou null en cas d'erreur.
     */
    public static InetAddress renvoyerIP() {
        try (Socket testSocket = new Socket("8.8.8.8", 53)) {
            InetAddress ipLocale = testSocket.getLocalAddress();
            System.out.println("[RESEAU] IP locale : " + ipLocale.getHostAddress());
            return ipLocale;
        } catch (IOException e) {
            System.err.println("[RESEAU] Erreur lors de la récupération de l'IP : " + e.getMessage());
            return null;
        }
    }
}