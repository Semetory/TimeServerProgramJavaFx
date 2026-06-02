package org.example.timerserver.model.systems;

import org.example.timerserver.model.Observer;

public class VentilationSystem implements Observer {

    private boolean active;

    @Override
    public void update(int hour, int minute) {

        active = minute < 10 && minute % 30 == 0;
    }

    public boolean isActive() {
        return active;
    }
}