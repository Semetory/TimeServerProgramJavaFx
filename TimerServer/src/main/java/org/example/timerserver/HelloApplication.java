package org.example.timerserver;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        //Настройка масштабирования под разрешение экрана
        stage.setTitle("Pulse Server - Управление микроклиматом");
        stage.setScene(scene);

        //Автоматическое масштабирование окна с сохранением пропорций
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        //stage.setMaximized(true); //Открывается на весь экран

        stage.show();
    }
}