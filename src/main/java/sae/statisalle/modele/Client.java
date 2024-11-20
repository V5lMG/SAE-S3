package sae.statisalle.modele;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Connexion {
    private Socket clientSocket;
    private BufferedReader fluxEntree;
    private PrintWriter fluxSortie;

    public void connecter(String adresse, int port) throws IOException {
        clientSocket = new Socket(adresse, port);
        fluxSortie = new PrintWriter(clientSocket.getOutputStream(), true);
        fluxEntree = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        System.out.println("[CLIENT] Connecté au serveur " + adresse + ":" + port);
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
}