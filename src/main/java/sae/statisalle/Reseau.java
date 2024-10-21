/*
 * Reseau.java                21/10/2024
 * Pas de droit d'auteur ni de copyright
 */
package sae.statisalle;

import java.net.InetAddress;
import java.net.Socket;
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

    /**
     * Envoie des données concernant une salle, un employé, une réservation et une activité.
     *
     * @param salle La salle concernée par l'envoi.
     * @param employe L'employé concerné par l'envoi.
     * @param reservation La réservation à envoyer.
     * @param activite L'activité à envoyer.
     */
    public void envoyerDonnees(Salle salle, Employe employe,
                               Reservation reservation, Activite activite) {
        // TODO
    }

    /**
     * Reçoit des données depuis une source externe.
     */
    public void recevoirDonnees() {
        // TODO
    }

    /**
     * Prépare un serveur pour la communication.
     */
    public void preparerServeur() {
        // TODO
        Socket socket = new Socket();
        socket.bind();

    }

    public static void main(String[] args) throws UnknownHostException {
        preparerClient();
    }

    /**
     * Prépare un client pour la communication.
     */
    public static void preparerClient() throws UnknownHostException {

        /* On récupère l'InetAddress du serveur grâce à son IP */
        InetAddress address2 = InetAddress.getByName("8.8.8.8"); // TODO STUB
        System.out.println(address2.getHostName());
    }

    /**
     * Construit une requête réseau à envoyer.
     */
    public void construireRequete() {
        // TODO
    }

    /**
     * Envoie une requête à un client sur une adresse spécifiée.
     *
     * @param client Le socket du client à qui envoyer la requête.
     * @param requete La requête à envoyer.
     * @param adresseDestination L'adresse du destinataire.
     */
    public void envoyer(Socket client, String requete, String adresseDestination) {
        // TODO
    }

    /**
     * Reçoit une réponse depuis un client via un socket.
     *
     * @param client Le socket du client.
     * @return La réponse reçue sous forme de chaîne de caractères.
     */
    public String recevoirReponse(Socket client) {
        // TODO
        return null; // STUB
    }

    /**
     * Traite une réponse reçue sous forme de chaîne de caractères.
     *
     * @param messageRecu Le message reçu à traiter.
     * @return Le résultat du traitement.
     */
    public String traiterReponse(String messageRecu) {
        // TODO
        return null; // STUB
    }

    /**
     * Utilise la réponse traitée.
     *
     * @param reponse La réponse à utiliser.
     */
    public void utiliserReponse(String reponse) {
        // TODO
    }

    /**
     * Traite la réponse reçue par un client.
     *
     * @param client Le socket du client.
     */
    public void traiterReponse(Socket client) {
        // TODO
    }
}

