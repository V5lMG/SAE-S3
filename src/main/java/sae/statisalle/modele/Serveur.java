package sae.statisalle.modele;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Serveur implements Connexion {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader fluxEntree;
    private PrintWriter fluxSortie;
    private volatile boolean isClosed = false; // Indicateur de fermeture du serveur
    private String dernierRequeteRecu = "";

    public void demarrer(int port, String ip) throws IOException {
        // Initialiser le serveur socket
        serverSocket = (ip != null)
                ? new ServerSocket(port, 50, InetAddress.getByName(ip))
                : new ServerSocket(port);
        System.out.println("[SERVEUR] Démarré sur " + ip + ":" + port);
    }

    public void accepterClients() {
        while (!isClosed) { // Tant que le serveur n'est pas fermé, accepter des connexions
            try {
                clientSocket = serverSocket.accept(); // Attente d'une connexion client
                fluxSortie = new PrintWriter(clientSocket.getOutputStream(), true);
                fluxEntree = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                System.out.println("[SERVEUR] Client connecté : " + clientSocket.getInetAddress());

                // Créer un thread pour gérer cette connexion client
                Thread clientThread = new Thread(() -> {
                    try {
                        // Traiter la communication avec le client
                        String requete = recevoir();
                        System.out.println("[SERVEUR] Requête reçue : " + requete);
                        String reponse = traiterRequete(requete);
                        envoyer(reponse);
                        System.out.println("[SERVEUR] Réponse envoyée : " + reponse);
                    } catch (Exception e) {
                        System.err.println("[SERVEUR] Erreur lors de la gestion du client : " + e.getMessage());
                    } finally {
                        // Fermer la connexion avec le client
                        fermer();
                        System.out.println("[SERVEUR] Connexion client fermée.");
                    }
                });

                clientThread.start(); // Lancer le thread de gestion du client
            } catch (IOException e) {
                if (isClosed) {
                    System.out.println("[SERVEUR] Le serveur est arrêté, plus de clients à accepter.");
                } else {
                    System.err.println("[SERVEUR] Erreur lors de l'attente d'un client : " + e.getMessage());
                }
            }
        }
    }

    @Override
    public void envoyer(String donnees) {
        fluxSortie.println(donnees);
    }

    @Override
    public String recevoir() {
        try {
            dernierRequeteRecu = fluxEntree.readLine();
            return dernierRequeteRecu;
        } catch (IOException e) {
            System.err.println("[SERVEUR] Erreur lors de la réception : " + e.getMessage());
            return null;
        }
    }

    @Override
    public void fermer() {
        try {
            if (fluxEntree != null) fluxEntree.close();
            if (fluxSortie != null) fluxSortie.close();
            if (clientSocket != null) clientSocket.close();
        } catch (IOException e) {
            System.err.println("[SERVEUR] Erreur lors de la fermeture de la connexion : " + e.getMessage());
        }
    }

    public String renvoyerDonnee() {
        return dernierRequeteRecu;
    }

    public void fermerServeur() {
        isClosed = true; // Marque le serveur comme fermé
        try {
            if (serverSocket != null) {
                serverSocket.close(); // Ferme le serveur pour arrêter d'accepter de nouvelles connexions
            }
        } catch (IOException e) {
            System.err.println("[SERVEUR] Erreur lors de la fermeture du serveur : " + e.getMessage());
        }
    }

    /**
     * Simule le traitement d'une requête reçue.
     * Cette méthode peut être enrichie pour traiter différents types de requêtes.
     * @param requete La requête envoyée par le client.
     * @return La réponse à retourner au client.
     */
    private static String traiterRequete(String requete) {
        if (requete == null || requete.isEmpty()) {
            return "Requête invalide.";
        }
        return "Données bien reçues et traitées : " + requete;
    }
}