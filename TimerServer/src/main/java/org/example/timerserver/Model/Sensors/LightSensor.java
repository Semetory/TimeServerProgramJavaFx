package org.example.timerserver.Model.Sensors;

import org.example.timerserver.Model.Observer;

import java.util.Random;
import static java.lang.Math.random;

public class LightSensor implements Observer {
    private int currentLight;

    @Override
    public void update(String message) {
        if ("TICK".equals(message)) {
            controlLighting();
        }
    }

    private void controlLighting() {
        double currentLight = reciviningValueLiting();
        System.out.println("Текущая освещенность: " + currentLight + "lm");
    }

    private double reciviningValueLiting(){

        double x1 = 0.1;
        double x2 = 10000.999991;

        double rVL = 1;
        double f = 1;

        f = Math.random()/Math.nextDown(0.1);
        rVL = x1*(x1-f)+x2*f;

        return rVL;

    }
}
