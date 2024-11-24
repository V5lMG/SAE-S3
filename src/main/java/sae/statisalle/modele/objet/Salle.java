    /*
     * Salle.java               23/10/2024
     * IUT DE RODEZ               Pas de copyrights
     */
    package sae.statisalle.modele.objet;

    import javafx.collections.FXCollections;
    import javafx.collections.ObservableList;

    /**
     * La classe Salle initialise les objets de type Salle.
     * L'objet Salle est constitué de :
     * <ul>
     *     <li>Un identifiant</li>
     *     <li>Une capacité</li>
     *     <li>Un indicateur de la présence d'un video projecteur</li>
     *     <li>Un indicateur de la présence d'un écran XXL</li>
     *     <li>Le nombre de machine</li>
     *     <li>Une description</li>
     *     <li>La liste des logiciels</li>
     *     <li>Un indicateur de la présence d'une imprimante</li>
     * </ul>
     * <br>
     * Elle gère également toutes les erreurs relatives à l'instantiation
     * des salles en fonction des contenus des fichiers
     * @author erwan.thierry
     * @author rodrigo.xaviertaborda
     */
    public class Salle {
        /* Identifiant de la salle */
        String identifiant;

        /* Nom de la salle*/
        String nom;

        /* Capacité de place de la salle */
        String capacite;

        /* Indique la présence de vidéo projecteur dans la salle */
        String videoProj;

        /* Indique la présence d'écran XXL dans la salle */
        String ecranXXL;

        /* Nombre de machine présente dans la salle */
        String nbMachine;

        /* Type des machines*/
        String typeMachine;

        /* Liste des logiciels utilisable dans la salle */
        String logiciel;

        /* Indique la présence d'imprimante dans la salle */
        String imprimante;

        private ObservableList<Reservation> listReservation;

        public Salle(String identifiant, String nom, String capacite, String videoProj, String ecranXXL,
                     String nbMachine, String typeMachine, String logiciel, String imprimante) {
            this.identifiant = identifiant;
            this.nom = nom;
            this.capacite = capacite;
            this.ecranXXL = ecranXXL;
            this.typeMachine = typeMachine;
            this.videoProj = videoProj;
            this.nbMachine = nbMachine;
            this.logiciel = logiciel;
            this.imprimante = imprimante;
            this.listReservation = FXCollections.observableArrayList();
        }

        public String getIdentifiant() { return identifiant; }

        public String getNom() {
            return nom;
        }

        public String getCapacite() {
            return capacite;
        }

        public String getVideoProj() {
            return videoProj;
        }

        public String getEcranXXL() {
            return ecranXXL;
        }

        public String getTypeMachine() {
            return typeMachine;
        }

        public String getNbMachine() {
            return nbMachine;
        }

        public String getLogiciel() {
            return logiciel;
        }

        public String getImprimante() {
            return imprimante;
        }

        public ObservableList<Reservation> getReservations() {
            return listReservation;
        }

        //Obtenir le nom de l'employé qui a réservé la salle
        public String getNomEmploye() {
            return listReservation.stream()
                    .map(reservation -> reservation.getEmployeR())
                    .distinct()
                    .reduce((a, b) -> a + ", " + b) // Combine les noms distincts
                    .orElse("Aucun employé");
        }

        //Obtenir les types d'activités associées à la salle
        public String getTypesActivite() {
            return listReservation.stream()
                    .map(reservation -> reservation.getActiviteR())
                    .distinct()
                    .reduce((a, b) -> a + ", " + b) // Combine les types distincts
                    .orElse("Aucune activité");
        }

        //Obtenir les types d'activités associées à la salle
        public String getDateR() {
            return listReservation.stream()
                    .map(reservation -> reservation.getDateR())
                    .distinct()
                    .reduce((a, b) -> a + ", " + b) // Combine les types distincts
                    .orElse("Aucune activité");
        }

        //Obtenir les types d'activités associées à la salle
        public String getHeureDebutR() {
            return listReservation.stream()
                    .map(reservation -> reservation.getHeureDebut())
                    .distinct()
                    .reduce((a, b) -> a + ", " + b) // Combine les types distincts
                    .orElse("Aucune activité");
        }

        //Obtenir les types d'activités associées à la salle
        public String getHeureFinR() {
            return listReservation.stream()
                    .map(reservation -> reservation.getHeureFin())
                    .distinct()
                    .reduce((a, b) -> a + ", " + b) // Combine les types distincts
                    .orElse("Aucune activité");
        }

        public void setIdentifiant(String identifiant) {
            this.identifiant = identifiant;
        }

        public void setNom(String nom) {
            this.nom = nom;
        }

        public void setCapacite(String capacite) {
            this.capacite = capacite;
        }

        public void setVideoProj(String videoProj) {
            this.videoProj = videoProj;
        }

        public void setEcranXXL(String ecranXXL) {
            this.ecranXXL = ecranXXL;
        }

        public void setNbMachine(String nbMachine) {
            this.nbMachine = nbMachine;
        }

        public void setTypeMachine(String typeMachine) {
            this.typeMachine = typeMachine;
        }

        public void setLogiciel(String logiciel) {
            this.logiciel = logiciel;
        }

        public void setImprimante(String imprimante) {
            this.imprimante = imprimante;
        }
    }