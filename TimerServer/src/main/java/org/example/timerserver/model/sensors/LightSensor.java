package org.example.timerserver.model.sensors;

import org.example.timerserver.model.Observer;

public class LightSensor implements Observer {

    private int lightLevel;

    @Override
    public void update(int hour, int minute) {

        if (hour >= 6 && hour <= 18) {
            lightLevel = 60 + (int)(Math.random() * 40);
        }
        else {
            lightLevel = 5 + (int)(Math.random() * 20);
        }
    }

    public int getLightLevel() {
        return lightLevel;
    }
}
