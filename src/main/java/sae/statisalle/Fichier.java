/*
 * Fichier.java               21/10/2024
 * IUT DE RODEZ               Pas de copyrights
 */
package sae.statisalle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static java.lang.System.err;

/**
 * La classe Fichier gère les opérations de lecture
 * sur des fichiers Comma Separated Values (csv). Elle permet de lire le contenu
 * d'un fichier ligne par ligne
 * <br>
 * Les fichiers pris en charge doivent avoir une extension .csv ou .CSV.
 * La classe gère également les erreurs courantes liées à l'ouverture,
 * la lecture et la fermeture des fichiers.
 * @author erwan.thierry
 */
public class Fichier {

    /** fichier courant de l'instance */
    private File fichierExploite;

    /** lecteur du fichier courant de l'instance */
    private FileReader lecteurFichier;

    /** tampon du fichier courant de l'instance */
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
                if (!this.fichierExploite.exists() && this.fichierExploite.createNewFile()) {
                    System.out.println("Fichier créé " + this.fichierExploite.getName());
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

        if (this.fichierExploite.getName()
                .toLowerCase()
                .endsWith(SUFFIXE_FICHIER)) {
            return true;
        }
        return false;
    }

    /**
     * Lit le contenu d'un fichier texte ligne par ligne et retourne un tableau
     * de chaînes de caractères contenant chaque ligne du fichier.
     * <br>
     * La méthode commence par compter le nombre de lignes dans le fichier
     * afin de créer un tableau de la taille appropriée.
     * Ensuite, elle relit le fichier pour remplir ce tableau avec les lignes lues.
     * <br>
     * Si une erreur survient pendant la lecture du fichier,
     * un message d'erreur est affiché et la méthode retourne null.
     *
     * @return Un tableau de chaînes de caractères contenant les lignes du fichier,
     *         ou null si une erreur survient.
     */
    public String[] contenuFichier() {
        int nbLignes = 0;

        String ligneAct;

        String[] contenu = null;

        try {
            while ((ligneAct = this.tamponFichier.readLine()) != null) {
                nbLignes++;
            }

            try {
                this.tamponFichier.close();
            } catch (IOException pbFermeture) {
                err.println(ERREUR_FERMETURE_FICHIER);
            }

            /* Réinitialiser le BufferedReader pour relire le fichier */
            this.lecteurFichier = new FileReader(this.fichierExploite);
            this.tamponFichier  = new BufferedReader(this.lecteurFichier);

            contenu = new String[nbLignes];
            int index = 0;

            while ((ligneAct = this.tamponFichier.readLine()) != null) {
                contenu[index] = ligneAct;
                index++;
            }

        } catch (IOException pbContenu) {
            err.println(ERREUR_CONTENU_FICHIER);
            contenu = null;
        }
        return contenu;
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
        String[] contenu;

        typeFichier = null;
        contenu = this.fichierExploite.contenuFichier();

        if (contenu[0].contains("Nom") && contenu[0].contains("Capacite")){
            typeFichier = "Salle";
        }
        if (contenu[0].contains("Nom") && contenu[0].contains("Prenom") && contenu[0].contains("Telephone")){
            typeFichier = "Employe";
        }
        if (contenu[0].contains("Activité")){
            typeFichier = "Activite";
        }
        if (contenu[0].contains("salle") && contenu[0].contains("employe") && contenu[0].contains("activite") && contenu[0].contains("date")) {
            typeFichier = "Reservation";
        } // else

        return typeFichier;
    }
}