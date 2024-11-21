package sae.statisalle.modele;

import sae.statisalle.controleur.RevoirNomClasse;

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
    private volatile boolean isClosed = false;
    private String dernierRequeteRecu = "";

    public void demarrer(int port, String ip) throws IOException {
        if (ip != null && !ip.isEmpty()) {
            serverSocket = new ServerSocket(port, 50, InetAddress.getByName(ip));
        } else {
            serverSocket = new ServerSocket(port);
        }

        // TODO tester l'efficacité
        String ipEffective = serverSocket.getInetAddress().getHostAddress();
        if (ipEffective.equals("0.0.0.0")) {
            ipEffective = InetAddress.getLocalHost().getHostAddress();
        }
        System.out.println("[SERVEUR] Démarré sur " + ipEffective + ":" + port);
    }

    public void envoyerClePublic(String clePublique) {
        envoyer(clePublique);  // Envoie la clé publique au client
        System.out.println("[SERVEUR] Clé publique envoyée : " + clePublique);

    }

    public void accepterClients() {
        while (!isClosed) {
            try {
                clientSocket = serverSocket.accept();
                fluxSortie = new PrintWriter(clientSocket.getOutputStream(),
                                    true);
                fluxEntree = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream())
                );
                Client client = new Client();
                System.out.println("[SERVEUR] Client connecté : "
                                   + client.renvoyerIP().getHostAddress());

                // Créer un thread pour gérer cette connexion client
                Thread clientThread = new Thread(() -> {
                    try {
                        String initialisationDiffieHellman = recevoir();
                        System.out.println("[SERVEUR] Clé publique du client "
                                + "reçue : " + initialisationDiffieHellman);

                        String[] parties = initialisationDiffieHellman
                                                            .split(" ; ");

                        int clePublicClient = Integer.parseInt(parties[0]);
                        Session.setClePublicClient(clePublicClient);
                        int b = 15;//DiffieHellman.genererEntierPremier(0,999999999);

                        int p = Integer.parseInt(parties[1]);
                        int g = Integer.parseInt(parties[2]);

                        String clePartageeGB = DiffieHellman.expoModulaire(g, b, p) + " ; " + p + " ; " + g;
                        envoyerClePublic(clePartageeGB);

                        String requete = recevoir();
                        System.out.println("[SERVEUR] Requête reçue : " + requete);
                        //RevoirNomClasse.afficherPopupFichierRecu(requete);
                        String reponse = traiterRequete(requete);
                        envoyer(reponse);
                    } catch (Exception e) {
                        System.err.println("[SERVEUR] Erreur lors de la gestion du client : " + e.getMessage());
                    } finally {
                        fermer();
                    }
                });

                clientThread.start();
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
        return "Données bien envoyé :" +requete;
    }

    /**
     * Renvoie l'adresse IP de la machine locale utilisée pour se connecter
     * à un serveur externe et son nom de machine.
     * @return l'adresse IP de la machine sous forme d'objet InetAddress.
     */
    @Override
    public InetAddress renvoyerIP() {
        // 8.8.8.8 correspond au DNS de google
        try (Socket socket = new Socket("8.8.8.8", 53)) {
            return socket.getLocalAddress();
        } catch (IOException e) {
            System.err.println("[CLIENT] Erreur lors de la récupération de l'IP : " + e.getMessage());
            return null; // retourne null pour éviter les erreurs
        }
    }
}