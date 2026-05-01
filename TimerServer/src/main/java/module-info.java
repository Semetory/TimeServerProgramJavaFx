module org.example.timerserver {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.controlsfx.controls;
    requires javafx.media;


    opens org.example.timerserver to javafx.fxml;
    exports org.example.timerserver;
    exports org.example.timerserver.Model;
    exports org.example.timerserver.Model.Sensors;
}