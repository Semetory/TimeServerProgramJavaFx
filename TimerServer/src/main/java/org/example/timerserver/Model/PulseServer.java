package org.example.timerserver.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервер тактовых импульсов (Pulse Server).
 * Реализует паттерн "Наблюдатель" (Observer Pattern) как Субъект (Subject).
 *
 * <p>Генерирует периодические сигналы "TICK" и уведомляет всех подписанных
 * наблюдателей (датчики, системы управления) о наступлении события.</p>
 *
 * <p>Пример использования:</p>
 * <pre>
 * PulseServer server = new PulseServer();
 * TemperatureSensor sensor = new TemperatureSensor();
 * server.attach(sensor);
 *
 * // Каждую секунду
 * server.pulse(); // Все датчики получат уведомление
 * </pre>
 *
 * @author Дмитрий
 * @version 1.0
 * @since 2026-04-24
 * @see Observer
 */


public class PulseServer {

    /** Список наблюдателей, подписанных на получение уведомлений */

    private List<Observer> observers = new ArrayList<>();

    /**
     * Добавляет нового наблюдателя в список подписчиков.
     * После добавления наблюдатель будет получать все последующие уведомления.
     *
     * @param observer объект, реализующий интерфейс Observer
     */

    public void attach(Observer observer) {
        observers.add(observer);
    }

    /**
     * Отправляет уведомление всем подписанным наблюдателям.
     * Каждый наблюдатель получает сообщение и реагирует на него своим способом.
     *
     * @param message текст уведомления (обычно "TICK")
     */

    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    /**
     * Генерирует тактовый импульс.
     * Вызывает notifyObservers("TICK") для всех подписчиков.
     * Обычно вызывается периодически через таймер (например, каждую секунду).
     */

    public void pulse() {
        notifyObservers("TICK");
    }
}
