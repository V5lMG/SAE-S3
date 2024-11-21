package sae.statisalle.modele;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client implements Connexion {
    private Socket clientSocket;
    private BufferedReader fluxEntree;
    private PrintWriter fluxSortie;

    public void connecter(String adresse, int port) throws IOException {
        clientSocket = new Socket();
        clientSocket.connect(new InetSocketAddress(adresse, port), 5000); // Timeout de 5 secondes
        fluxSortie = new PrintWriter(clientSocket.getOutputStream(), true);
        fluxEntree = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        System.out.println("[CLIENT] Connecté au serveur " + adresse + ":" + port);
    }

    public String recevoirClePublic() {
        String clePubliqueServeur = recevoir();
        System.out.println("[CLIENT] Clé publique du serveur reçue : " + clePubliqueServeur);
        return clePubliqueServeur;
    }

    public void envoyerClePublic(String clePublique) {
        envoyer(clePublique);
        System.out.println("[CLIENT] Clé publique envoyée : " + clePublique);
    }

    @Override
    public void envoyer(String donnees) {
        fluxSortie.println(donnees);
    }

    @Override
    public String recevoir() {
        try {
            return fluxEntree.readLine();
        } catch (IOException e) {
            System.err.println("[CLIENT] Erreur lors de la réception : " + e.getMessage());
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
            System.err.println("[CLIENT] Erreur lors de la fermeture : " + e.getMessage());
        }
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