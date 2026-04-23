package org.example.timerserver.Model.Sensors;

import javafx.application.Platform;
import org.example.timerserver.Model.Observer;
import javafx.scene.control.TextArea;

public class TemperatureSensor implements Observer {
    private boolean active = false;
    private boolean broken = false;
    private boolean justBroken = false;
    private int lastValue = 0;
    private TextArea console;

    public TemperatureSensor(TextArea console) {
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
        addToConsole("Измерение температуры: " + lastValue + "°C");
    }

    private int receiveValue() {
        // Реалистичные значения температуры от -10 до +40
        return -10 + (int)(Math.random() * 50);
    }

    public void activate() { active = true; }
    public void deactivate() { active = false; }
    public void breakSensor() {
        broken = true;
        justBroken = true;
        active = false;
    }
    public void repair() {
        broken = false;
        justBroken = false;
        active = true;
    }
    public boolean isBroken() { return broken; }
    public boolean isJustBroken() { return justBroken; }
    public void resetJustBroken() { justBroken = false; }
    public int getLastValue() { return lastValue; }

    private void addToConsole(String msg) {
        Platform.runLater(() -> console.appendText(msg + "\n"));
    }
}