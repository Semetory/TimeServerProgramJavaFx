package org.example.timerserver.model.systems;

import org.example.timerserver.model.Observer;
import org.example.timerserver.model.sensors.TemperatureHumiditySensor;

public class Heater implements Observer {

    private final TemperatureHumiditySensor sensor;

    private boolean active;

    public Heater(TemperatureHumiditySensor sensor) {
        this.sensor = sensor;
    }

    @Override
    public void update(int hour, int minute) {

        active = sensor.getTemperature() < 18;
    }

    public boolean isActive() {
        return active;
    }
}