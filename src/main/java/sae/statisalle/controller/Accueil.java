import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Contrôleur pour la vue d'accueil de l'application.
 * <p>
 * Cette classe gère les actions des boutons dans la vue d'accueil
 * et permet de naviguer vers d'autres vues de l'application.
 * </p>
 * @author MathiasCambon
 */
public class Accueil {

    /** Bouton pour quitter l'application. */
    @FXML
    private Button btnQuitter;

    /** Bouton pour ouvrir la page d'importation. */
    @FXML
    private Button btnImporter;

    /** Bouton pour ouvrir la page d'exportation. */
    @FXML
    private Button btnExporter;

    /** Bouton pour ouvrir la page d'envoi. */
    @FXML
    private Button btnEnvoyer;

    /** Bouton pour accéder à l'aide de la vue d'accueil. */
    @FXML
    private Button btnAide;

    /**
     * Méthode d'initialisation appelée automatiquement après le chargement de la vue.
     * Associe des actions aux boutons pour naviguer vers les différentes pages.
     */
    @FXML
    private void initialize() {
        // Associer les actions aux boutons
        btnImporter.setOnAction(event -> openPage("importer.fxml"));
        btnExporter.setOnAction(event -> openPage("exporter.fxml"));
        btnEnvoyer.setOnAction(event -> openPage("envoyer.fxml"));
        btnAide.setOnAction(event -> openPage("aideAccueil.fxml"));
        btnQuitter.setOnAction(event -> ((Stage) btnQuitter.getScene().getWindow()).close());
    }

    /**
     * Ouvre une nouvelle scène en chargeant la vue spécifiée.
     * @param fxmlFile Le nom du fichier FXML à charger (doit être dans le même répertoire).
     */
    private void openPage(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
