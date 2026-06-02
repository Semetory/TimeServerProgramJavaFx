package org.example.timerserver.model.systems;

import org.example.timerserver.model.Observer;
import org.example.timerserver.model.sensors.LightSensor;
public class LightingSystem implements Observer {

    private final LightSensor sensor;

    private boolean active;

    public LightingSystem(LightSensor sensor) {
        this.sensor = sensor;
    }

    @Override
    public void update(int hour, int minute) {

        active = sensor.getLightLevel() < 30;
    }

    public boolean isActive() {
        return active;
    }
}
