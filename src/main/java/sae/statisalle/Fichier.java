/*
 * Fichier.java               21/10/2024
 * IUT DE RODEZ               Pas de copyrights
 */
package sae.statisalle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.err;

/**
 * La classe Fichier gère les opérations de lecture
 * sur des fichiers Comma Separated Values (csv). Elle permet de lire le contenu
 * d'un fichier ligne par ligne
 * <br>
 * Les fichiers pris en charge doivent avoir une extension .csv ou .CSV.
 * La classe gère également les erreurs courantes liées à l'ouverture,
 * la lecture et la fermeture des fichiers.
 * @author erwan.thierry, MathiasCambon
 * @author MathiasCambon
 */
public class Fichier {


    private static final String ERREUR_ECRITURE_FICHIER =
            "Erreur : impossible d'écrire dans le fichier";

    /* fichier courant de l'instance */
    private File fichierExploite;

    /* lecteur du fichier courant de l'instance */
    private FileReader lecteurFichier;

    /* */
    private BufferedReader tamponFichier;

    /** impossible d'ouvrir le fichier renseigné */
    private static final String ERREUR_OUVERTURE_FICHIER =
            "erreur : Impossible d'ouvrir le fichier au lien : ";

    /** impossible de fermer le fichier renseigné */
    private static final String ERREUR_FERMETURE_FICHIER =
            "erreur : Impossible de fermer le fichier.";

    /** impossible d'acceder au contenu du fichier **/
    private static final String ERREUR_CONTENU_FICHIER =
            "erreur : Impossible d'accéder au contenu du fichier.";

    /** impossible de créer le fichier, droits insuffisants **/
    private static final String ERREUR_CREATION_FICHIER =
            "erreur : Impossible de créer le fichier, droits insuffisants. ";

    /** le format du fichier n'est pas .txt ou .TXT **/
    private static final String ERREUR_EXTENSION_FICHIER =
            "erreur : Le format du fichier doit être .csv";

    /** le format du paramètre est invalide **/
    private static final String ERREUR_FORMAT_PARAMETRE =
            "erreur : Le format du paramètre renseigné invalide.";

    /** Suffixe des fichiers pris en charge **/
    private static final String SUFFIXE_FICHIER = ".csv";

    /**
     * Constructeur de la classe Fichier.
     * <br>
     * Initialise les objets nécessaires pour lire le fichier
     * spécifié par le chemin fourni.
     * <p>
     * Vérifie si le chemin du fichier est valide et si le
     * fichier a une extension correcte (.txt ou .bin).
     *
     * @param cheminFichier : Le chemin du fichier à lire.
     */
    public Fichier(String cheminFichier) {
        if (!cheminFichier.isEmpty()) {
            try {
                this.fichierExploite = new File(cheminFichier);

                if (!extensionValide()){
                    err.println(ERREUR_EXTENSION_FICHIER);
                }

                if (!this.fichierExploite.canWrite()){
                    err.println(ERREUR_CREATION_FICHIER +cheminFichier);
                }

                this.lecteurFichier = new FileReader(cheminFichier);
                this.tamponFichier = new BufferedReader(this.lecteurFichier);

            } catch (IOException pbOuverture) {
                err.println(ERREUR_OUVERTURE_FICHIER + cheminFichier);
            }
        } else {
            err.println(ERREUR_FORMAT_PARAMETRE);
        }
    }

    /**
     * Vérifie si le fichier a une extension valide.
     *
     * @return true si l'extension du fichier est valide,
     * 	       false si l'extension du fichier n'est pas valide.
     */
    public boolean extensionValide(){

        return this.fichierExploite.getName()
                   .toLowerCase()
                   .endsWith(SUFFIXE_FICHIER);
    }

    /**
     * Lit le contenu d'un fichier texte ligne par ligne et retourne une liste
     * de chaînes de caractères contenant chaque ligne du fichier.
     * <br>
     * Si une erreur survient pendant la lecture du fichier,
     * un message d'erreur est affiché et la méthode retourne une liste vide.
     *
     * @return Une liste de chaînes de caractères contenant les lignes du fichier,
     *         ou Une liste vide si une erreur survient.
     */
    public List<String> contenuFichier() {
        List<String> contenu = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(this.fichierExploite))) {
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
     * Réécrit le contenu du fichier avec une nouvelle liste de chaînes de caractères.
     * <br>
     * Chaque chaîne dans la liste représente une ligne qui sera écrite dans le fichier.
     * Si une erreur survient lors de l'écriture, un message d'erreur est affiché.
     * <br>
     * Le fichier d'origine est écrasé et remplacé par les nouvelles lignes.
     *
     * @param ContenuFichier : La liste de chaînes de caractères à écrire dans le fichier.
     */
    public void reecritureFichier(List<String> ContenuFichier) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.fichierExploite))) {
            for (String ligne : ContenuFichier) {
                writer.write(ligne);
                writer.newLine();
            }

            writer.close();
        } catch (IOException e) {
            err.println(ERREUR_ECRITURE_FICHIER);
        }
    }

    /**
     * Recupère le nom du fichier sans sont extention
     * @return nom du fichier
     */
    public String nomFichier(){
        String nomFichier = this.fichierExploite.getName();
        int pointIndex = nomFichier.lastIndexOf(".");

        if (pointIndex == -1) {
            return nomFichier;
        }
        return nomFichier.substring(0, pointIndex);
    }

    /**
     * Retourne l'objet File représentant le fichier exploité.
     *
     * @return fichier exploité.
     */
    public File getFichierExploite() {
        return this.fichierExploite;
    }

    /**
     * Prend la premiere ligne du contenu du fichier et renvoie le type en fonction de ce qu'il contient
     * @return typeFichier est soit Salle, Employe, Activite ou Reservation
     */
    public String getTypeFichier() {

        String typeFichier;
        List<String> contenu;

        typeFichier = null;
        contenu = contenuFichier();

        if (contenu.get(0).contains("Ident;Nom;Capacite;videoproj;ecranXXL;ordinateur;type;logiciels;imprimante")){
            typeFichier = "Salle";
        }
        if (contenu.get(0).contains("Ident;Nom;Prenom;Telephone")){
            typeFichier = "Employe";
        }
        if (contenu.get(0).contains("Ident;Activité")){
            typeFichier = "Activite";
        }
        if (contenu.get(0).contains("Ident;salle;employe;activite;date;heuredebut;heurefin")) {
            typeFichier = "Reservation";
        } // else

        return typeFichier;
    }
}