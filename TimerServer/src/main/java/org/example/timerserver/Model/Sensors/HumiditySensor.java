package org.example.timerserver.Model.Sensors;

import org.example.timerserver.Model.Observer;

public class HumiditySensor  implements Observer {
    private int currentHumidity;

    @Override
    public void update(String message) {
        if ("TICK".equals(message)) {
            measure();
        }
    }

    private void measure() {
        //Логика измерения влажности
        double currentHumidity = reciviningValueMeasure();
        System.out.println("Измерение влажности: " + currentHumidity + "%");
    }

    private double reciviningValueMeasure(){

        double x1 = 1.1;
        double x2 = 500.0;

        double rVM = 1;
        double f = 1;

        f = Math.random()/Math.nextDown(1.1);
        rVM = x1*(x1-f)+x2*f;

        return rVM;

    }
}
