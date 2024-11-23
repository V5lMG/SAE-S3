/*
 * Session.java               17/11/2024
 * IUT DE RODEZ,pas de copyrights
 */
package sae.statisalle.modele;

import sae.statisalle.modele.objet.Client;
import sae.statisalle.modele.objet.Serveur;

/**
 * La classe Session gère les informations relatives à une session réseau
 * telles que l'adresse IP, le contenu de la session, les objets réseau et la clé associée.
 * Elle permet de centraliser et d'accéder aux informations de la session à travers des méthodes statiques.
 * @author Cambon Mathias
 * @author valentin.munier-genie
 * @author rodrigo.xavier-taborda
 */
public class Session {
    private static Client client;
    private static Serveur serveur;
    private static String ipServeur;
    private static String portServeur = "54321";


    /**
     * Récupère l'instance de la session.
     * @return l'instance de la classe Client associé à la session.
     */
    public static Client getClient() {
        return client;
    }

    /**
     * Définit l'objet Client de la session.
     * @param client l'objet Client à associer à la session.
     */
    public static void setClient(Client client) {
        Session.client = client;
    }

    /**
     * Obtient l'adresse IP du serveur.
     * @return l'adresse IP du serveur.
     */
    public static String getIpServeur() {
        return ipServeur;
    }

    /**
     * Définit l'adresse IP du serveur.
     * @param ipServeur l'adresse IP du serveur à définir.
     */
    public static void setIpServeur(String ipServeur) {
        Session.ipServeur = ipServeur;
    }

    /**
     * Obtient le port du serveur.
     * @return le port du serveur.
     */
    public static String getPortServeur() {
        return portServeur;
    }

    /**
     * Définit le port du serveur.
     *
     * @param portServeur le port du serveur à définir.
     */
    public static void setPortServeur(String portServeur) {
        Session.portServeur = portServeur;
    }

    /**
     * TODO
     * @return
     */
    public static Serveur getServeur() {
        return serveur;
    }

    /**
     * TODO
     * @param serveur
     */
    public static void setServeur(Serveur serveur) {
        Session.serveur = serveur;
    }
}