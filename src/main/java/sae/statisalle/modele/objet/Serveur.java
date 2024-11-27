/*
 * Serveur.java                 21/10/2024
 * IUT DE RODEZ                 Pas de copyrights
 */
package sae.statisalle.modele.objet;

import javafx.application.Platform;
import sae.statisalle.controleur.ControleurPopup;
import sae.statisalle.modele.Connexion;
import sae.statisalle.modele.DiffieHellman;
import sae.statisalle.modele.Vigenere;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * La classe Serveur implémente l'interface Connexion pour établir
 * un serveur réseau qui écoute les connexions entrantes d'un client.
 * Le serveur prend en charge l'échange de clés publiques avec le
 * client à l'aide du protocole Diffie-Hellman,
 * puis chiffre et déchiffre les données échangées à l'aide du
 * chiffrement Vigenère.
 * <p>
 * Le serveur peut gérer plusieurs clients simultanément
 * en démarrant un nouveau thread pour chaque connexion.
 * <p>
 * @author valentin.munier-genie
 */
public class Serveur implements Connexion {

    /**
     * Le socket d'écoute du serveur.
     */
    private ServerSocket serverSocket;

    /**
     * Le socket de connexion avec un client.
     */
    private Socket clientSocket;

    /**
     * Flux d'entrée pour recevoir des données du client.
     */
    private BufferedReader fluxEntree;

    /**
     * Flux de sortie pour envoyer des données au client.
     */
    private PrintWriter fluxSortie;

    /**
     * Indicateur pour savoir si le serveur est fermé.
     */
    private volatile boolean isClosed = false;

    /**
     * Démarre le serveur sur un port et une adresse spécifiés.
     * Si une adresse IP est fournie,
     * le serveur écoute uniquement sur cette adresse.
     * Si l'adresse est vide ou nulle,
     * le serveur écoute sur toutes les interfaces réseau.
     *
     * @param port Le port sur lequel le serveur écoutera les connexions
     *             entrantes.
     * @param ip L'adresse IP à laquelle le serveur doit être lié.
     * @throws IOException Si une erreur se produit lors de l'ouverture
     *                     du socket du serveur.
     */
    public void demarrer(int port, String ip) throws IOException {
        if (ip != null && !ip.isEmpty()) {
            serverSocket = new ServerSocket(port,
                                            50,
                                            InetAddress.getByName(ip));
        } else {
            serverSocket = new ServerSocket(port);
        }

        String ipEffective = InetAddress.getLocalHost().getHostAddress();
        System.out.println("[SERVEUR] Démarré sur " + ipEffective + ":" + port);
    }

    /**
     * Envoie la clé publique du serveur au client.
     * Cette méthode utilise le flux de sortie pour envoyer la clé
     * publique au client après la génération de cette dernière à
     * l'aide du protocole Diffie-Hellman.
     *
     * @param clePublique La clé publique du serveur à envoyer.
     */
    public void envoyerClePublic(String clePublique) {
        envoyer(clePublique);  // envoie la clé publique au client
        System.out.println("[SERVEUR] Clé publique envoyée : " + clePublique);

    }

    /**
     * Attend les connexions des clients et les accepte lorsqu'elles arrivent.
     * Une fois qu'une connexion est établie, le serveur crée un thread pour
     * gérer la communication avec le client, y compris l'échange de
     * clés publiques et le traitement des requêtes chiffrées.
     */
    public void accepterClients() {
        while (!isClosed) {
            try {
                clientSocket = serverSocket.accept();
                fluxSortie = new PrintWriter(clientSocket.getOutputStream(),
                        true);
                fluxEntree = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream())
                );
                String clientIP = clientSocket.getInetAddress()
                                              .getHostAddress();
                System.out.println("[SERVEUR] Client connecté : " + clientIP);

                // Créer un thread pour gérer cette connexion client
                Thread clientThread = new Thread(() -> {
                    try {
                        String initialisationDiffieHellman = recevoir();
                        System.out.println("[SERVEUR] Clé publique du client "
                                + "reçue : " + initialisationDiffieHellman);

                        String[] parties = initialisationDiffieHellman
                                           .split(" ; ");
                        if (parties.length != 3) {
                            throw new IllegalArgumentException("Format de clé "
                                    + "publique invalide : "
                                    + initialisationDiffieHellman);
                        }

                        int clePublicClient = Integer.parseInt(parties[0]);
                        int p = Integer.parseInt(parties[1]);
                        int g = Integer.parseInt(parties[2]);

                        // génération de la clé publique du serveur
                        int b = DiffieHellman.genererEntierPremier(1,9999);
                        int clePubliqueServeur = DiffieHellman.expoModulaire(g,
                                                                             b,
                                                                             p);
                        envoyerClePublic(clePubliqueServeur + " ; "
                                         + p + " ; " + g);

                        // calcul de la clé secrète partagée
                        BigInteger cleSecretePartagee =
                                BigInteger.valueOf(DiffieHellman.expoModulaire(
                                                clePublicClient, b, p
                                        )
                                );
                        System.out.println("[SERVEUR] Clé secrète partagée "
                                + "calculée : " + cleSecretePartagee);

                        String requeteChiffree  = recevoir();
                        System.out.println("[SERVEUR] Requête reçue : "
                                + requeteChiffree );

                        String requeteDechiffree =
                                Vigenere.dechiffrementDonnees(requeteChiffree,
                                        cleSecretePartagee);

                        System.out.println("[SERVEUR] Données déchiffrées : "
                                + requeteDechiffree);

                        Platform.runLater(() ->
                                ControleurPopup.afficherPopupFichierRecu(
                                        requeteDechiffree
                                ));

                        String reponse = traiterRequete(requeteDechiffree);
                        envoyer(reponse);
                    } catch (IllegalArgumentException e) {
                        System.err.println("[SERVEUR] Erreur attendue lors de "
                                + "la gestion du client : " + e.getMessage());
                    } catch (Exception e) {
                        System.err.println("[SERVEUR] Erreur inattendue lors "
                                           + "de la gestion du client : "
                                           + e.getMessage());
                    } finally {
                        fermer();
                    }
                });

                clientThread.start();
            } catch (SocketException e) {
                // cas où le client se déconnecte brutalement
                System.err.println("[SERVEUR] Le client s'est "
                        + "déconnecté brusquement : "
                        + e.getMessage());
            } catch (IOException e) {
                if (isClosed) {
                    System.out.println("[SERVEUR] Le serveur est arrêté, "
                            + "plus de clients à accepter.");
                } else {
                    System.err.println("[SERVEUR] Erreur lors de l'attente "
                            + "d'un client : " + e.getMessage());
                }
            }
        }
    }

    /**
     * Envoie des données au client.
     * Cette méthode utilise le flux de sortie
     * pour envoyer les données spécifiées
     * au client.
     *
     * @param donnees Les données à envoyer au client.
     */
    @Override
    public void envoyer(String donnees) {
        fluxSortie.println(donnees);
    }

    /**
     * Reçoit des données envoyées par le client.
     * Cette méthode lit une ligne de texte du flux
     * d'entrée et la retourne.
     *
     * @return La chaîne de caractères lue du client, ou null si une
     *         erreur de lecture se produit.
     */
    @Override
    public String recevoir() {
        try {
            return fluxEntree.readLine();
        } catch (IOException e) {
            System.err.println("[SERVEUR] Erreur lors de la réception : "
                               + e.getMessage());
            return null;
        }
    }

    /**
     * Ferme la connexion avec le client.
     * Cette méthode ferme les flux d'entrée
     * et de sortie ainsi que le socket
     * de connexion avec le client.
     */
    @Override
    public void fermer() {
        try {
            if (fluxEntree != null) {
                fluxEntree.close();
            }
            if (fluxSortie != null) {
                fluxSortie.close();
            }
            if (clientSocket != null) {
                clientSocket.close();
            }
        } catch (IOException e) {
            System.err.println("[SERVEUR] Erreur lors de la fermeture de "
                               + "la connexion : " + e.getMessage());
        }
    }

    /**
     * Ferme le serveur et empêche l'acceptation
     * de nouvelles connexions.
     * Cette méthode marque le serveur comme fermé et
     * ferme le socket du serveur.
     */
    public void fermerServeur() {
        isClosed = true;
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("[SERVEUR] Erreur lors de la fermeture du "
                               + "serveur : " + e.getMessage());
        }
    }

    /**
     * Traite la requête reçue du client.
     * Cette méthode effectue un traitement simple sur la requête et retourne
     * une réponse à envoyer au client.
     *
     * @param requete La requête reçue du client.
     * @return La réponse générée en fonction de la requête.
     */
    public static String traiterRequete(String requete) {
        if (requete == null || requete.isEmpty()) {
            return "Requête invalide.";
        }
        return "Données bien envoyées :" + requete;
    }

    /**
     * Renvoie l'adresse IP locale du serveur.
     * @return L'adresse IP locale du serveur, ou null en cas d'erreur.
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
