module org.example.timerserver {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.controlsfx.controls;


    opens org.example.timerserver to javafx.fxml;
    exports org.example.timerserver.application;
    opens org.example.timerserver.application to javafx.fxml;
    exports org.example.timerserver.model;
    exports org.example.timerserver.model.sensors;
    exports org.example.timerserver.model.systems;
}