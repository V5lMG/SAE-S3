module sae.statisalle {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens sae.statisalle to javafx.fxml;
    exports sae.statisalle;

    opens sae.statisalle.controller to javafx.fxml;
    exports sae.statisalle.controller;
}