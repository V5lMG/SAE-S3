package sae.statisalle.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sae.statisalle.Reseau;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestReseauClient {

    private Reseau serveur;
    private Reseau client;
    private static final int PORT = 55555;
    private static final String HOST = "127.0.0.1";

    public static void main(String[] args) {
        Reseau client = new Reseau();
        int port = 55555; // FIXME
        String host = "127.0.0.1";

        // préparer le client, envoyer une requete et attendre une réponse du serveur
        client.preparerClient(host, port);
        client.envoyer("envoie toi stp batard");
        String reponseServeur = client.recevoirReponse();

        if (reponseServeur != null) {
            client.utiliserReponse(reponseServeur);
        }

        client.fermerClient();
    }

    @BeforeEach
    public void setUp() {
        serveur = new Reseau();
        client = new Reseau();
    }

    @Test
    public void testPreparerServeur() {
        serveur.preparerServeur(PORT);
        assertNotNull(serveur, "Le serveur doit être préparé");
        System.out.print("vdbshjgdqsjkfjkgsfdhghfdghjghfdshgfdshgjkfdsghghjgshgkfd");
    }
}