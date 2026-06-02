package org.example.timerserver.model;

public interface Observer {
    void update(int hour, int minute);
}