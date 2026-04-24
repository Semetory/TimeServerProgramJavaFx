package org.example.timerserver.Model.Sensors;

import javafx.application.Platform;
import org.example.timerserver.Model.Observer;
import javafx.scene.control.TextArea;

/**
 * Датчик температуры.
 * Реализует интерфейс Observer для получения уведомлений от PulseServer.
 *
 * <p>При получении сигнала "TICK" выполняет измерение температуры
 * и выводит результат в консоль.</p>
 *
 * <p>Датчик может находиться в следующих состояниях:</p>
 * <ul>
 *   <li><b>active</b> - датчик включен и реагирует на TICK</li>
 *   <li><b>broken</b> - датчик сломан и не выполняет измерения</li>
 *   <li><b>justBroken</b> - датчик только что сломался (для желтого индикатора)</li>
 * </ul>
 *
 * @author Дмитрий
 * @version 1.0
 * @since 2026-04-24
 * @see Observer
 * @see HumiditySensor
 * @see LightSensor
 */

public class TemperatureSensor implements Observer {

    /** Флаг активности датчика (включен/выключен пользователем) */
    private boolean active = false;

    /** Флаг поломки датчика */
    private boolean broken = false;

    /** Флаг "только что сломался" (для отображения желтого цвета 3 секунды) */
    private boolean justBroken = false;

    /** Последнее измеренное значение температуры */
    private int lastValue = 0;

    /** Ссылка на консоль для вывода сообщений */
    private TextArea console;

    /**
     * Конструктор датчика температуры.
     *
     * @param console текстовое поле для вывода сообщений измерений
     */

    public TemperatureSensor(TextArea console) {
        this.console = console;
    }

    /**
     * Обработка уведомления от PulseServer.
     * Выполняет измерение температуры только если датчик активен и не сломан.
     *
     * @param message сообщение от PulseServer (ожидается "TICK")
     */

    @Override
    public void update(String message) {
        if (active && !broken && "TICK".equals(message)) {
            measure();
        }
    }

    /**
     * Выполняет измерение температуры.
     * Генерирует случайное значение и выводит его в консоль.
     */

    private void measure() {
        lastValue = receiveValue();
        addToConsole("Измерение температуры: " + lastValue + "°C");
    }

    /**
     * Генерирует случайное значение температуры.
     * Диапазон: от -10 до +40 градусов Цельсия.
     *
     * @return случайное значение температуры
     */

    private int receiveValue() {
        // Реалистичные значения температуры от -10 до +40
        return -10 + (int)(Math.random() * 50);
    }

    /**
     * Активирует датчик.
     * После активации датчик начинает реагировать на сигналы TICK.
     */

    public void activate() { active = true; }

    /**
     * Деактивирует датчик.
     * Датчик перестает реагировать на сигналы TICK.
     */

    public void deactivate() { active = false; }

    /**
     * Имитирует поломку датчика.
     * Деактивирует датчик и устанавливает флаги поломки.
     */

    public void breakSensor() {
        broken = true;
        justBroken = true;
        active = false;
    }

    /**
     * Восстанавливает датчик после поломки.
     * Сбрасывает флаги поломки.
     */

    public void repair() {
        broken = false;
        justBroken = false;
        active = true;
    }

    /**
     * Возвращает статус поломки датчика.
     *
     * @return true - датчик сломан, false - исправен
     */

    public boolean isBroken() { return broken; }

    /**
     * Возвращает статус "только что сломался".
     *
     * @return true - датчик сломался в течение последних 3 секунд
     */

    public boolean isJustBroken() { return justBroken; }

    /**
     * Сбрасывает флаг "только что сломался".
     * Вызывается через 3 секунды после поломки.
     */

    public void resetJustBroken() { justBroken = false; }

    /**
     * Возвращает последнее измеренное значение температуры.
     *
     * @return последнее значение в градусах Цельсия
     */

    public int getLastValue() { return lastValue; }

    /**
     * Выводит сообщение в консоль через Platform.runLater().
     * Обеспечивает потокобезопасность при работе с JavaFX UI.
     *
     * @param msg текст сообщения
     */

    private void addToConsole(String msg) {
        Platform.runLater(() -> console.appendText(msg + "\n"));
    }
}