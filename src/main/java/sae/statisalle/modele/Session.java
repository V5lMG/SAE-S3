/*
 * Session.java               17/11/2024
 * IUT DE RODEZ,pas de copyrights
 */
package sae.statisalle.modele;

/**
 * La classe Session gère les informations relatives à une session réseau
 * telles que l'adresse IP, le contenu de la session, les objets réseau et la clé associée.
 * Elle permet de centraliser et d'accéder aux informations de la session à travers des méthodes statiques.
 * @author Cambon Mathias
 * @author valentin.munier-genie
 * @author Xavier-Taborda Rodrigo
 */
public class Session {
    private static String adresseIp;
    private static Reseau reseau;
    private static String ipServeur;
    private static String portServeur;

    /**
     * Récupère l'adresse IP de la session.
     * @return l'adresse IP de la session.
     */
    public static String getAdresseIp() {
        return adresseIp;
    }

    /**
     * Définit l'adresse IP de la session.
     * @param ip l'adresse IP à définir pour la session.
     */
    public static void setAdresseIp(String ip) {
        adresseIp = ip;
    }

    /**
     * Récupère l'instance de la session.
     * @return l'instance de la classe Reseau associé à la session.
     */
    public static Reseau getReseau() {
        return reseau;
    }

    /**
     * Définit l'objet Reseau de la session.
     * @param reseau l'objet Reseau à associer à la session.
     */
    public static void setReseau(Reseau reseau) {
        Session.reseau = reseau;
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
}