package org.example.timerserver.Model.Sensors;

import org.example.timerserver.Model.Observer;

public class TemperatureSensor implements Observer {
    private int currentTemperature;

    @Override
    public void update(String message) {
        if ("TICK".equals(message)) {
            temperature();
        }
    }

    private void temperature() {
        // Логика измерения температуры и влажности
        currentTemperature = reciviningValueTemperature();
        System.out.println("Измерение температуры: " + currentTemperature + "°C, ");
    }

    private int reciviningValueTemperature(){

        int x1 = 0;
        int x2 = 100;

        int rVL = 1;
        int f = 1;
        int a = 1;

        f = (int) (Math.random()/Math.negateExact(a));
        rVL = x1*(x1-f)+x2*f;

        return rVL;

    }
}