package org.example.timerserver.Model;

import java.util.ArrayList;
import java.util.List;

public class PulseServer {
    private List<Observer> observers = new ArrayList<>();

    public void attach(Observer observer) {
        observers.add(observer);
    }

    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    public void pulse() {
        notifyObservers("TICK");
    }
}
