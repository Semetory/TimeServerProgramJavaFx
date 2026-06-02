package org.example.timerserver.model;

import java.util.ArrayList;
import java.util.List;

public class PulseServer {

    private final List<Observer> observers = new ArrayList<>();

    private int hour = 0;
    private int minute = 0;

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void tick() {

        minute++;

        if (minute >= 60) {
            minute = 0;
            hour++;
        }

        if (hour >= 24) {
            hour = 0;
        }

        notifyObservers();
    }

    private void notifyObservers() {
        observers.forEach(o -> o.update(hour, minute));
    }
}
