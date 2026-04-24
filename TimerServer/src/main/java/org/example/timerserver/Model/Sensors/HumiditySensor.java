package org.example.timerserver.Model.Sensors;

import javafx.application.Platform;
import org.example.timerserver.Model.Observer;
import javafx.scene.control.TextArea;

/**

* Датчик освещённости.
* Реализует интерфейс Observer для получения уведомлений от PulseServer.
* При получении сигнала "TICK
* и выводит результат в консоль.
* Датчик может находиться в следующих состояниях:
* active - датчик включен и реагирует на TICK
* broken - датчик сломан и не выполняет измерения
* justBroken - датчик только что сломался (для желтого индикатора)
* @author Дмитрий
* @version 1.0
* @since 2026-04-24
* @see Observer
* @see TemperatureSensor
* @see HumiditySensor
 */

public class HumiditySensor implements Observer {

    /** Флаг активности датчика (включен/выключен пользователем) */
    private boolean active = false; /** Флаг поломки датчика */
    private boolean broken = false; /** Флаг "только что сломался" (для отображения желтого цвета 3 секунды) */
    private boolean justBroken = false; /** Последнее измеренное значение освещённости в lux */
    private double lastValue = 0; /** Ссылка на консоль для вывода сообщений */
    private TextArea console;

    public HumiditySensor(TextArea console) {
        this.console = console;
    }

    @Override
    public void update(String message) {
        if (active && !broken && "TICK".equals(message)) {
            measure();
        }
    }

    private void measure() {
        lastValue = receiveValue();
        addToConsole(String.format("Измерение влажности: %.1f%%", lastValue));
    }

    private double receiveValue() {
        // Влажность от 20% до 90%
        return 20 + Math.random() * 70;
    }

    public void activate() { active = true; }
    public void deactivate() { active = false; }
    public void breakSensor() { broken = true; justBroken = true; active = false; }
    public void repair() { broken = false; justBroken = false; active = true; }
    public boolean isBroken() { return broken; }
    public boolean isJustBroken() { return justBroken; }
    public void resetJustBroken() { justBroken = false; }
    public double getLastValue() { return lastValue; }

    private void addToConsole(String msg) {
        Platform.runLater(() -> console.appendText(msg + "\n"));
    }
}