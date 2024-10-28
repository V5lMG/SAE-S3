package sae.statisalle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestReseauClient {

    private Reseau serveur;
    private Reseau client;
    private static final int PORT = 55555;
    private static final String HOST = "127.0.0.1";

    public static void main(String[] args) {
        Reseau serveur = new Reseau();
        Thread serveurThread = new Thread(() -> {
            serveur.preparerServeur(PORT);
            serveur.attendreConnexionClient();
            String requete = serveur.recevoirDonnees();
            if (requete != null) {
                String reponse = serveur.traiterRequete(requete);
                serveur.envoyerReponse(reponse);
            }
            serveur.fermerServeur();
        });
        serveurThread.start();

        // Attendre que le serveur démarre
        try {
            Thread.sleep(1000);  // Ajustez la durée selon les besoins
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Créer et démarrer le client
        Reseau client = new Reseau();
        client.preparerClient(HOST, PORT);
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