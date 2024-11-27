/*
 * Connexion.java              15/11/2024
 * Pas de droits d'auteur ni de copyright
 */
package sae.statisalle.modele;

import java.net.InetAddress;

/**
 * Interface représentant une connexion réseau.
 * Fournit les méthodes essentielles pour l'envoi, la réception de données,
 * la fermeture de la connexion et l'obtention de l'adresse IP de la connexion.
 * @author valentin.munier-genie
 */
public interface Connexion {

    /**
     * Envoie des données sur la connexion.
     * Cette méthode doit être implémentée pour transmettre
     * une chaîne de caractères
     * au destinataire connecté.
     *
     * @param donnees Les données à envoyer, sous forme de chaîne
     *                de caractères.
     */
    void envoyer(String donnees);

    /**
     * Reçoit des données provenant de la connexion.
     * Cette méthode doit être implémentée pour lire les données envoyées
     * par le correspondant.
     *
     * @return Les données reçues sous forme de chaîne de caractères, ou null
     *         en cas d'erreur ou si aucune donnée n'est disponible.
     */
    String recevoir();

    /**
     * Ferme la connexion.
     * Cette méthode doit être implémentée pour s'assurer que tous les flux et
     * sockets associés à la connexion sont correctement fermés.
     */
    void fermer();

    /**
     * Récupère l'adresse IP associée à la connexion.
     * Cette méthode doit être implémentée pour retourner l'adresse IP
     * du client ou du serveur selon le contexte.
     *
     * @return L'adresse IP sous forme d'un objet InetAddress.
     */
    InetAddress renvoyerIP();
}
