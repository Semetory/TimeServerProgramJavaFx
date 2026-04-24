package org.example.timerserver;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Главный класс приложения Pulse Server.
 * Загружает FXML-интерфейс и запускает JavaFX приложение.
 *
 * @author Дмитрий бИСТ-234
 * @version 1.0
 * @since 2026-04-24
 */


public class HelloApplication extends Application {

    /**
     * Точка входа в JavaFX приложение.
     * Загружает FXML файл с пользовательским интерфейсом и отображает главное окно.
     *
     * @param stage первичная сцена, предоставляемая JavaFX
     * @throws IOException если не удается загрузить FXML файл
     */

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