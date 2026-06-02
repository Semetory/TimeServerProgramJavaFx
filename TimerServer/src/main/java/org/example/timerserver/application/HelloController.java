package org.example.timerserver.application;


import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;
import org.example.timerserver.model.PulseServer;
import org.example.timerserver.model.sensors.LightSensor;
import org.example.timerserver.model.sensors.TemperatureHumiditySensor;
import org.example.timerserver.model.systems.Heater;
import org.example.timerserver.model.systems.Humidifier;
import org.example.timerserver.model.systems.LightingSystem;
import org.example.timerserver.model.systems.VentilationSystem;

public class HelloController {

    @FXML
    private Label timeLabel;

    @FXML
    private Label tempLabel;

    @FXML
    private Label humidityLabel;

    @FXML
    private Label lightLabel;

    @FXML
    private Label heaterLabel;

    @FXML
    private Label humidifierLabel;

    @FXML
    private Label ventilationLabel;

    @FXML
    private Label lightingSystemLabel;

    private PulseServer pulseServer;

    private TemperatureHumiditySensor sensor;
    private LightSensor lightSensor;

    private Heater heater;
    private Humidifier humidifier;
    private VentilationSystem ventilation;
    private LightingSystem lighting;

    @FXML
    public void initialize() {

        pulseServer = new PulseServer();

        sensor = new TemperatureHumiditySensor();
        lightSensor = new LightSensor();

        heater = new Heater(sensor);
        humidifier = new Humidifier(sensor);
        ventilation = new VentilationSystem();
        lighting = new LightingSystem(lightSensor);

        pulseServer.addObserver(sensor);
        pulseServer.addObserver(lightSensor);

        pulseServer.addObserver(heater);
        pulseServer.addObserver(humidifier);
        pulseServer.addObserver(ventilation);
        pulseServer.addObserver(lighting);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {

                    pulseServer.tick();

                    updateUI();
                })
        );

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void updateUI() {

        tempLabel.setText(
                String.format("Температура: %.1f °C",
                        sensor.getTemperature()));

        humidityLabel.setText(
                String.format("Влажность: %.1f %%", sensor.getHumidity()));

        lightLabel.setText(
                "Освещенность: " + lightSensor.getLightLevel());

        heaterLabel.setText(
                "Обогреватель: " +
                        (heater.isActive() ? "ВКЛ" : "ВЫКЛ"));

        humidifierLabel.setText(
                "Увлажнитель: " +
                        (humidifier.isActive() ? "ВКЛ" : "ВЫКЛ"));

        ventilationLabel.setText(
                "Вентиляция: " +
                        (ventilation.isActive() ? "ВКЛ" : "ВЫКЛ"));

        lightingSystemLabel.setText(
                "Освещение: " +
                        (lighting.isActive() ? "ВКЛ" : "ВЫКЛ"));
    }
}