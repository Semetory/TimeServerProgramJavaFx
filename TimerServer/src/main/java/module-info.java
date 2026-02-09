module org.example.timerserver {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.controlsfx.controls;


    opens org.example.timerserver to javafx.fxml;
    exports org.example.timerserver;
}