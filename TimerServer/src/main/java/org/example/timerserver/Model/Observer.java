package org.example.timerserver.Model;

/**
 * Интерфейс Наблюдателя (Observer) для паттерна "Наблюдатель".
 *
 * <p>Все классы, которые должны получать уведомления от PulseServer,
 * должны реализовать этот интерфейс и переопределить метод update().</p>
 *
 * <p>Пример реализации:</p>
 * <pre>
 * public class TemperatureSensor implements Observer {
 *     {@literal @}Override
 *     public void update(String message) {
 *         if ("TICK".equals(message)) {
 *             measure(); // Выполнить измерение
 *         }
 *     }
 * }
 * </pre>
 *
 * @author Дмитрий
 * @version 1.0
 * @since 2026-04-24
 * @see PulseServer
 */

public interface Observer {

    /**
     * Вызывается Субъектом (PulseServer) при наступлении события.
     * Реализация этого метода определяет, как наблюдатель реагирует на уведомление.
     *
     * @param message сообщение от Субъекта (обычно "TICK")
     */

    void update(String message);
}
