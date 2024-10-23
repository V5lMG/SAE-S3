module sae.statisalle {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.junit.jupiter.api;


    opens sae.statisalle to javafx.fxml;
    exports sae.statisalle;

    opens sae.statisalle.controller to javafx.fxml;
    exports sae.statisalle.controller;
}