public class TestDiffieHellman {
    // Méthode pour calculer l'exponentiation modulaire
    public static int expoModulaire(int base, int exposant, int modulo) {
        int resultat = 1;
        base = base % modulo;
        while (exposant > 0) {
            if ((exposant & 1) == 1) {
                resultat = (resultat * base) % modulo;
            }
            exposant = exposant >> 1;
            base = (base * base) % modulo;
        }
        return resultat;
    }

    public static void main(String[] args) {
        // Paramètres de Diffie-Hellman
        int p = 23; // Nombre premier
        int g = 5;  // Générateur

        // Clés privées (Alice et Bob)
        int a = 6;  // Clé privée d'Alice
        int b = 15; // Clé privée de Bob

        // Étape 1: Alice calcule g^a mod p
        int gaModP = expoModulaire(g, a, p);
        System.out.println("g^a mod p (Alice) : " + gaModP);

        // Étape 2: Bob calcule g^b mod p
        int gbModP = expoModulaire(g, b, p);
        System.out.println("g^b mod p (Bob) : " + gbModP);

        // Alice envoie gaModP à Bob, et Bob envoie gbModP à Alice
        // Étape 3: Alice calcule (g^b)^a mod p
        int clePartageeAlice = expoModulaire(gbModP, a, p);
        System.out.println("Clé partagée calculée par Alice : " + clePartageeAlice);

        // Étape 4: Bob calcule (g^a)^b mod p
        int clePartageeBob = expoModulaire(gaModP, b, p);
        System.out.println("Clé partagée calculée par Bob : " + clePartageeBob);

        // Vérification que les deux clés partagées sont identiques
        if (clePartageeAlice == clePartageeBob) {
            System.out.println("Les clés partagées sont identiques : " + clePartageeAlice);
        } else {
            System.out.println("Les clés partagées ne correspondent pas.");
        }
    }
}
