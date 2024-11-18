module sae.statisalle {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    // Ouverture des packages à JavaFX
    opens sae.statisalle.controller to javafx.fxml;
    opens sae.statisalle.controller.aides to javafx.fxml;
    opens sae.statisalle.exception to javafx.fxml;
    opens sae.statisalle.modele to javafx.fxml;
    opens sae.statisalle.modele.objet to javafx.fxml;

    // Exportation des packages nécessaires
    exports sae.statisalle.controller;
    exports sae.statisalle.controller.aides;
    exports sae.statisalle.exception;
    exports sae.statisalle.modele;
    exports sae.statisalle.modele.objet;
}
