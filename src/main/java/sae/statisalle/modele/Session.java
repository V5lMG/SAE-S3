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
    private static String contenu;
    private static Reseau reseau;

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
     * Définit l'objet {@code Reseau} de la session.
     * @param reseau l'objet {@code Reseau} à associer à la session.
     */
    public static void setReseau(Reseau reseau) {
        Session.reseau = reseau;
    }
}