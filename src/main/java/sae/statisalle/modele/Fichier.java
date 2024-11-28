/*
 * Fichier.java               21/10/2024
 * IUT DE RODEZ               Pas de copyrights
 */
package sae.statisalle.modele;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.err;

/**
 * La classe Fichier gère les opérations de lecture et d'écriture sur des fichiers
 * au format CSV (Comma Separated Values). Elle permet de lire et de manipuler
 * des fichiers CSV, d'en extraire les données et de les réécrire.
 * <p>
 * Les fichiers pris en charge doivent avoir une extension .csv ou .CSV.
 * Cette classe gère également les erreurs liées à l'ouverture, la lecture,
 * et l'écriture des fichiers. Elle peut également identifier le type de données
 * contenues dans un fichier CSV en analysant sa première ligne.
 * </p>
 *
 * @author erwan.thierry
 * @author mathias.cambon
 */
public class Fichier {

    /* Fichier courant de l'instance */
    private File fichierExploite;

    /* Impossible d'ouvrir le fichier renseigné */
    private static final String ERREUR_OUVERTURE_FICHIER =
            "erreur : Impossible d'ouvrir le fichier au lien : ";

    /* Impossible d'acceder au contenu du fichier **/
    private static final String ERREUR_CONTENU_FICHIER =
            "erreur : Impossible d'accéder au contenu du fichier.";

    /* Impossible de créer le fichier, droits insuffisants **/
    private static final String ERREUR_CREATION_FICHIER =
            "erreur : Impossible de créer le fichier, droits insuffisants. ";

    /* Le format du fichier n'est pas .txt ou .TXT **/
    private static final String ERREUR_EXTENSION_FICHIER =
            "erreur : Le format du fichier doit être .csv";

    /* Le format du paramètre est invalide. */
    private static final String ERREUR_FORMAT_PARAMETRE =
            "erreur : Le format du paramètre renseigné invalide.";

    /* L'écriture dans le fichier est impossible */
    private static final String ERREUR_ECRITURE_FICHIER =
            "erreur : L'écriture dans le fichier est impossible";

    /* Suffixe des fichiers pris en charge */
    private static final String SUFFIXE_FICHIER = ".csv";

    /**
     * Constructeur qui initialise un objet Fichier avec le chemin du fichier à exploiter.
     * Vérifie si le chemin est valide, si le fichier a l'extension correcte et s'il existe.
     *
     * @param cheminFichier Le chemin complet du fichier à ouvrir.
     */
    public Fichier(String cheminFichier) {
        if (cheminFichier == null || cheminFichier.isEmpty()) {
            err.println(ERREUR_FORMAT_PARAMETRE);
            return;
        }

        this.fichierExploite = new File(cheminFichier);

        if (!extensionValide()) {
            err.println(ERREUR_EXTENSION_FICHIER);
        }

        if (!fichierExploite.exists()) {
            err.println(ERREUR_OUVERTURE_FICHIER + cheminFichier
                        + " : Le fichier n'existe pas.");
        } else if (!fichierExploite.canWrite()) {
            err.println(ERREUR_CREATION_FICHIER + cheminFichier);
        }

    }


    /**
     * Vérifie si l'extension du fichier est valide (doit être .csv).
     *
     * @return true si l'extension est valide, false sinon.
     */
    public boolean extensionValide(){
        return this.fichierExploite.getName().endsWith(SUFFIXE_FICHIER);
    }

    /**
     * Lit le contenu du fichier CSV et renvoie une liste de chaînes de caractères,
     * chaque élément de la liste correspondant à une ligne du fichier.
     *
     * @return Une liste de chaînes de caractères représentant les lignes du fichier CSV.
     */
    public List<String> contenuFichier() {
        List<String> contenu = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(
                new FileReader(this.fichierExploite))) {

            String line;
            while ((line = br.readLine()) != null) {
                contenu.add(line);
            }

        } catch (IOException e) {
            err.println(ERREUR_CONTENU_FICHIER);
        }
        return contenu;
    }

    /**
     * Extrait les données d'un fichier CSV en ignorant la première ligne (entête)
     * et retourne une liste de listes où chaque sous-liste représente une ligne
     * du fichier CSV, avec chaque cellule séparée par un point-virgule.
     *
     * @return Une liste de listes de chaînes de caractères représentant
     *         les données du fichier CSV.
     */
    public List<List<String>> recupererDonnees() {
        List<List<String>> tableau3D = new ArrayList<>();
        List<String> contenu = contenuFichier();

        if (contenu.isEmpty()) {
            return tableau3D;
        }

        // Ajout de -1 pour gérer les colonnes vides à la fin
        String[] entetes = contenu.get(0).split(";", -1);

        for (int i = 1; i < contenu.size(); i++) {
            // Ajout de -1 ici également
            String[] ligne = contenu.get(i).split(";", -1);
            List<String> ligneTraitee = new ArrayList<>();

            for (String cellule : ligne) {
                // Remplace les cellules vides au milieu de la ligne
                // par un espace.
                if (cellule.isEmpty()) {
                    ligneTraitee.add(" ");
                } else {
                    ligneTraitee.add(cellule);
                }
            }

            // Complète la ligne avec des espaces
            // afin de faire la même taille que l'en-tête.
            while (ligneTraitee.size() < entetes.length) {
                ligneTraitee.add(" ");
            }
            tableau3D.add(ligneTraitee);
        }
        return tableau3D;
    }

    /**
     * Réécrit le contenu du fichier avec une nouvelle liste de chaînes de caractères,
     * chaque élément de la liste étant écrit dans une nouvelle ligne du fichier.
     * Le fichier d'origine est écrasé.
     *
     * @param ContenuFichier La liste de chaînes de caractères à écrire dans le fichier.
     */
    public void reecritureFichier(List<String> ContenuFichier) {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(this.fichierExploite))) {

            for (String ligne : ContenuFichier) {
                writer.write(ligne);
                writer.newLine();
            }

        } catch (IOException e) {
            err.println(ERREUR_ECRITURE_FICHIER);
        }
    }

    /**
     * Écrit une liste de chaînes de caractères dans un nouveau fichier spécifié par
     * le chemin donné. Chaque chaîne est écrite comme une ligne dans le fichier.
     *
     * @param contenuFichier La liste de chaînes de caractères à écrire dans le fichier.
     * @param cheminFichier Le chemin du fichier où les données doivent être écrites.
     */
    public static void ecritureFichier(List<String> contenuFichier,
                                       String cheminFichier) {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(cheminFichier))) {

            for (String ligne : contenuFichier) {
                writer.write(ligne);
                if (contenuFichier.size() >= 2) {
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            err.println(ERREUR_ECRITURE_FICHIER);
        }
    }

    /**
     * Vérifie si un fichier existe et qu'il n'est pas un répertoire.
     *
     * @param fichier Le chemin du fichier à vérifier.
     * @return true si le fichier existe et n'est pas un répertoire, false sinon.
     */
    public static boolean fichierExiste(String fichier) {
        Path path = Paths.get(fichier);
        return Files.exists(path) && !Files.isDirectory(path);
    }

    /**
     * Retourne l'objet File représentant le fichier actuellement exploité.
     *
     * @return L'objet File du fichier exploité.
     */
    public File getFichierExploite() {
        return this.fichierExploite;
    }

    /**
     * Détermine le type de fichier en fonction de sa première ligne de contenu.
     * Les types possibles sont "Salle", "Employe", "Activite", et "Reservation".
     *
     * @return Le type du fichier sous forme de chaîne de caractères ou null si aucun type ne correspond.
     * @throws IllegalStateException si le fichier est vide ou non valide.
     */
    public String getTypeFichier() {
        List<String> contenu = contenuFichier();
        if (contenu == null || contenu.isEmpty()) {
            throw new IllegalStateException("Le contenu du fichier est vide "
                                            + "ou non valide.");
        }
        return getTypeDepuisContenu(contenu);
    }

    /**
     * Analyse la première ligne d'une liste de chaînes pour déterminer un type prédéfini,
     * comme "Salle", "Employe", "Activite", ou "Reservation".
     *
     * @param contenu La liste des lignes du fichier à analyser.
     * @return Le type du contenu ou null si aucun type ne correspond.
     */
    public static String getTypeDepuisContenu(List<String> contenu) {
        if (contenu == null || contenu.isEmpty()) {
            return null;
        }

        String premiereLigne = contenu.get(0);
        return switch (premiereLigne) {
            case String line when line.contains("Ident;Nom;Capacite;videoproj;ecranXXL;ordinateur;type;logiciels;imprimante") ->
                    "Salle";
            case String line when line.contains("Ident;Nom;Prenom;Telephone") ->
                    "Employe";
            case String line when line.contains("Ident;Activité") ->
                    "Activite";
            case String line when line.contains("Ident;salle;employe;activite;date;heuredebut;heurefin") ->
                    "Reservation";
            default -> null;
        };
    }
}