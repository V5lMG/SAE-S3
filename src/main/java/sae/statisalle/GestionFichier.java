/**
 * GestionFichier.java          21/10/2024
 * IUT DE RODEZ            Pas de copyright
 */
package sae.statisalle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * La classe GestionFichier gère les opérations de lecture sur des fichiers CSV. Elle permet de lire le contenu
 * d'un fichier ligne par ligne afin de récupérer les données
 * <br>
 * Les fichiers pris en charge doivent avoir une extension .csv ou .CSV.
 * La classe gère également les erreurs courantes liées à l'ouverture,
 * la lecture et la fermeture des fichiers.
 * @author CAMBON Mathias
 * @author THIERRY Erwan
 */
public class GestionFichier {

    private File fichierExploite;
    private FileReader lecteurFichier;
    private BufferedReader tamponFichier;

    /** impossible de fermer le fichier renseigné */
    private static final String ERREUR_FERMETURE_FICHIER =
            "erreur : Impossible de fermer le fichier.";

    /** impossible d'acceder au contenu du fichier */
    private static final String ERREUR_CONTENU_FICHIER =
            "erreur : Impossible d'accéder au contenu du fichier.";

    /** le format du fichier n'est pas .csv ou .CSV */
    private static final String ERREUR_EXTENSION_FICHIER =
            "erreur : Le format du fichier doit être .csv ou .CSV";

    /** suffixe des fichiers pris en charge **/
    private static final String SUFFIXE_FICHIER = ".csv";

    public GestionFichier(String cheminFichier) {
        if (!cheminFichier.isEmpty()) {
            try {
                fichierExploite = new File(cheminFichier);

                if (!fichierExploite.getName()
                        .toLowerCase()
                        .endsWith(SUFFIXE_FICHIER)){

                    // TODO Changer ça en une constante
                    System.out.print("Erreur sur l'extension du fichier");
                }

                lecteurFichier = new FileReader(cheminFichier);
                tamponFichier = new BufferedReader(lecteurFichier);

            } catch (IOException pbOuverture) {
                // TODO Renvoyer un message
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Elle permet de récupérer 1 seul fichier CSV en réseau local à partir de l'interface graphique qui lui
     * récupère le chemin d'accès au dossier
     * @return
     * @throws
     */
    public void importerFichierCsv(String chemin)  {


    }

    // Méthode pour exporter un fichier CSV
    public void exporterFichierCsv() {
        // TODO: Implémenter le code pour exporter un fichier CSV
    }

    // Méthode pour exporter des fichiers réseaux
    public void exporterFichierReseaux() {
        // TODO: Implémenter le code pour exporter des fichiers réseaux
    }

    // Méthode pour importer des fichiers réseaux
    public void importerFichierReseaux() {
        // TODO: Implémenter le code pour importer des fichiers réseaux
    }

    // Méthode pour générer un fichier PDF
    public File generationPDF(String chemin) {
        // TODO: Implémenter le code pour générer un fichier PDF
        return new File(chemin); // BOUCHON
    }

    // Méthode pour écrire dans un fichier
    public File ecritureFichier(String chemin) {
        // TODO: Implémenter le code pour écrire dans un fichier
        return new File(chemin); // BOUCHON
    }

    // Méthode pour lire un fichier
    public File lectureFichier(String chemin) {
        // TODO: Implémenter le code pour lire un fichier
        return new File(chemin); // BOUCHON
    }

    // Méthode pour sauvegarder un fichier
    public File sauvegardeFichier(String chemin) {
        // TODO: Implémenter le code pour sauvegarder un fichier
        return new File(chemin); // BOUCHON
    }

    // Méthode pour charger un fichier
    public File chargerFichier(String chemin) {
        // TODO: Implémenter le code pour charger un fichier
        return new File(chemin); // BOUCHON
    }
}