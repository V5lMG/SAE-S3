package sae.statisalle.controleur;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;

public class ControleurActionAnalyse {

    @FXML
    private Button btnDonneesCalculees;

    @FXML
    private Button btnClassement;

    @FXML
    private Button btnPourcentage;

    @FXML
    private Button btnAide;

    @FXML
    private Button btnRetour;

    @FXML
    private Label lblInstruction;

    @FXML
    private Text txtTitre;

    @FXML
    void actionDonneesCalculees(ActionEvent event) {
        MainControleur.activerDonneesCalculees();
    }

    @FXML
    void actionClassement(ActionEvent event) {
        MainControleur.activerClassement();
    }

    @FXML
    void actionPourcentage(ActionEvent event) {
        MainControleur.activerPourcentage();
    }

    @FXML
    void actionAide(ActionEvent event) {
        MainControleur.activerAideActionAnalyse();
    }

    @FXML
    void actionRetour(ActionEvent event) {
        MainControleur.activerAffichage();
    }
}