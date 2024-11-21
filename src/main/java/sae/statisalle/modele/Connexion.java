package sae.statisalle.modele;

import java.net.InetAddress;

public interface Connexion {

    void envoyer(String donnees);

    String recevoir();

    void fermer();

    InetAddress renvoyerIP();
}
