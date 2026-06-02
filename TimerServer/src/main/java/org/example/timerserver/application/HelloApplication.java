package org.example.timerserver.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

    public class HelloApplication extends Application {
        @Override
        public void start(Stage stage) throws Exception {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/org/example/timerserver/hello-view.fxml")
            );
            stage.setTitle("Управление микроклиматом — Observer Pattern");
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
        }
        public static void main(String[] args) { launch(args); }
    }