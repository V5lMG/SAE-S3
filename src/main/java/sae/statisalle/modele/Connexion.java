package sae.statisalle.modele;

public interface Connexion {
    void envoyer(String donnees);
    String recevoir();
    void fermer();
}
