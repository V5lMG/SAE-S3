package sae.statisalle.test;

import sae.statisalle.Reseau;

public class TestReseauServeur {

    public static void main(String[] args) {
        Reseau serveur = new Reseau();
        int port = 55555; // FIXME

        serveur.preparerServeur(port);
        serveur.attendreConnexionClient();

        // recevoir un message, le traité et le renvoyer au client
        String requete = serveur.recevoirDonnees();
        if (requete != null) {
            String reponse = serveur.traiterRequete(requete);
            serveur.envoyerReponse(reponse);
        }

        serveur.fermerServeur();
    }
}
