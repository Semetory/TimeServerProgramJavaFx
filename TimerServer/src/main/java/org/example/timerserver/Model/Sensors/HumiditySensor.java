package org.example.timerserver.Model.Sensors;

import javafx.application.Platform;
import org.example.timerserver.Model.Observer;
import javafx.scene.control.TextArea;

public class HumiditySensor implements Observer {
    private boolean active = false;
    private boolean broken = false;
    private boolean justBroken = false;
    private double lastValue = 0;
    private TextArea console;

    public HumiditySensor(TextArea console) {
        this.console = console;
    }

    @Override
    public void update(String message) {
        if (active && !broken && "TICK".equals(message)) {
            measure();
        }
    }

    private void measure() {
        lastValue = receiveValue();
        addToConsole(String.format("Измерение влажности: %.1f%%", lastValue));
    }

    private double receiveValue() {
        // Влажность от 20% до 90%
        return 20 + Math.random() * 70;
    }

    public void activate() { active = true; }
    public void deactivate() { active = false; }
    public void breakSensor() { broken = true; justBroken = true; active = false; }
    public void repair() { broken = false; justBroken = false; active = true; }
    public boolean isBroken() { return broken; }
    public boolean isJustBroken() { return justBroken; }
    public void resetJustBroken() { justBroken = false; }
    public double getLastValue() { return lastValue; }

    private void addToConsole(String msg) {
        Platform.runLater(() -> console.appendText(msg + "\n"));
    }
}