package org.example.timerserver;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import org.controlsfx.control.ToggleSwitch;
import org.example.timerserver.Model.Sensors.LightSensor;
import org.example.timerserver.Model.PulseServer;
import org.example.timerserver.Model.Sensors.TemperatureSensor;

public class HelloController {

    @FXML
    private ToggleSwitch temperatureSwitch;
    @FXML
    private ToggleSwitch humiditySwitch;
    @FXML
    private ToggleSwitch illuminationSwitch;

    @FXML
    private ToggleSwitch systemVentilationSwitch;
    @FXML
    private ToggleSwitch autoManualVentilationControlSwitch;
    @FXML
    private Slider mySlider;
    @FXML
    private ToggleSwitch quietModeSwitch;
    @FXML
    private ToggleSwitch fullPowerMdeSwitch;
    @FXML
    private ToggleSwitch forcedVentilationSwitch;

    @FXML
    private ToggleSwitch systemLightingControlSwitch;
    @FXML
    private ToggleSwitch systemLightDisco;
    @FXML
    private ToggleSwitch systemLightILight;

    private PulseServer pulseServer = new PulseServer();

    @FXML
    private void initialize() {
        TemperatureSensor sensor = new TemperatureSensor();
        LightSensor lightSensor = new LightSensor();

        pulseServer.attach(sensor);
        pulseServer.attach(lightSensor);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
               // updateUI();
            }
        };
        timer.start();

        initStatusSensors();
        initStatusSystem();

    }

    private void initStatusSensors(){

        temperatureSwitch.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                System.out.println("Действие: Датчик температуры активирован!");
                toggleTemperatureSensor();
            } else { System.out.println("Действие: Датчик температуры деактивирован!"); }
        });

        humiditySwitch.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                System.out.println("Действие: Датчик влажности активирован!");
                toggleHumiditySensor();
            } else { System.out.println("Действие: Датчик влажности деактивирован!"); }
        });

        illuminationSwitch.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                System.out.println("Действие: Датчик света активирован!");
                toggleLightSensor();
            } else { System.out.println("Действие: Датчик света деактивирован!"); }
        });

    }

    private void initStatusSystem(){

        systemVentilationSwitch.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                System.out.println("Действие: Система вентиляции включена!");
                toggleVentilationSystem();
            } else {
                System.out.println("Действие: Система вентиляции выключена!");
            }
        });

        systemLightingControlSwitch.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                System.out.println("Действие: Система освещения включена!");
                toggleLightSystem();
            } else {
                System.out.println("Действие: Система освещения выключена!");
            }
        });

    }

    private void toggleVentilationSystem() {
        // Логика включения/выключения вентиляции
        //statusLabel.setText("Вентиляция переключена");
    }

    private void toggleLightSystem() {
        // Логика включения/выключения света
        //statusLabel.setText("Свет переключен");
    }

    private void toggleTemperatureSensor() {
        // Логика включения/выключения света
        //statusLabel.setText("Свет переключен");
    }

    private void toggleHumiditySensor() {
        // Логика включения/выключения света
        //statusLabel.setText("Свет переключен");
    }

    private void toggleLightSensor() {
        // Логика включения/выключения света
        //statusLabel.setText("Свет переключен");
    }

}
