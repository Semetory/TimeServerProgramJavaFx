package org.example.timerserver.model.sensors;

import org.example.timerserver.model.Observer;
import java.util.Random;

public class TemperatureHumiditySensor implements Observer {

    private double temperature;
    private double humidity;

    private final Random random = new Random();

    @Override
    public void update(int hour, int minute) {

        if (hour >= 6 && hour < 12) {
            temperature = 18 + random.nextDouble() * 6;
        }

        else if (hour >= 12 && hour < 18) {
            temperature = 24 + random.nextDouble() * 8;
        }

        else if (hour >= 18 && hour < 23) {
            temperature = 18 + random.nextDouble() * 8;
        }

        else {
            temperature = 14 + random.nextDouble() * 6;
        }

        humidity = 90 - temperature * 1.5;

        humidity += random.nextDouble() * 5 - 2.5;

        humidity = Math.max(30, humidity);
        humidity = Math.min(90, humidity);
    }

    public double getTemperature() {
        return temperature;
    }

    public double getHumidity() {
        return humidity;
    }
}