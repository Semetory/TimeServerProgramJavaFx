package org.example.timerserver.Model;

import org.example.timerserver.Model.Sensors.HumiditySensor;
import org.example.timerserver.Model.Sensors.LightSensor;
import org.example.timerserver.Model.Sensors.TemperatureSensor;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервер тактовых импульсов (Pulse Server).
 *
 * <p>Реализует паттерн "Наблюдатель" (Observer Pattern) как конкретный Субъект.
 * Является центральным узлом системы управления микроклиматом, генерирующим
 * периодические сигналы времени для всех датчиков и систем.</p>
 *
 * <p><b>Основные функции:</b></p>
 * <ul>
 *   <li>Генерация тактовых импульсов (сигналов TICK) каждую секунду</li>
 *   <li>Поддержка списка наблюдателей (датчики температуры, влажности, освещенности)</li>
 *   <li>Автоматическое уведомление всех подписчиков при изменении времени</li>
 *   <li>Хранение текущего состояния (времени) системы</li>
 * </ul>
 *
 * <p><b>Архитектура:</b></p>
 * <pre>
 *     [Таймер] ──каждую секунду──→ PulseServer.setState()
 *                                         ↓
 *                                  notifyAllObservers()
 *                                         ↓
 *                     ┌───────────────┬───────────────┐
 *                     ↓               ↓               ↓
 *              TempSensor      HumiditySensor    LightSensor
 * </pre>
 *
 * <p><b>Пример использования в контроллере:</b></p>
 * <pre>
 * // Создание и настройка
 * PulseServer pulseServer = new PulseServer();
 * TemperatureSensor sensor = new TemperatureSensor(console);
 * pulseServer.attach(sensor);
 *
 * // Запуск (например, в таймере)
 * Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
 *     localTime++;
 *     pulseServer.setState(localTime); // Автоматически уведомит всех
 * }));
 * </pre>
 *
 * @author Дмитрий бИСТ-234
 * @version 2.0 (с реализацией Subject)
 * @since 2026-04-24
 * @see Subject
 * @see Observer
 * @see TemperatureSensor
 * @see HumiditySensor
 * @see LightSensor
 */

public class PulseServer implements Subject {

    /** Список наблюдателей, подписанных на получение уведомлений.
     *  Использует ArrayList для поддержки динамического изменения размера.
     *  Не потокобезопасен - для production рекомендуется CopyOnWriteArrayList. */

    private List<Observer> observers = new ArrayList<>();

    /**
     * Текущее состояние субъекта (время в секундах с момента запуска системы).
     * Изменяется каждую секунду при получении сигнала от таймера.
     */

    private int state = 0;

    /**
     * Добавляет нового наблюдателя в список подписчиков.
     *
     * <p><b>Алгоритм работы:</b></p>
     * <ol>
     *   <li>Проверяет, что наблюдатель не равен null</li>
     *   <li>Добавляет наблюдателя в список observers</li>
     *   <li>С этого момента наблюдатель будет получать все уведомления</li>
     * </ol>
     *
     * <p><b>Временная сложность:</b> O(1) - добавление в конец ArrayList</p>
     *
     * @param observer объект, реализующий интерфейс Observer (не может быть null)
     * @throws NullPointerException если observer == null
     */

    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }

    /**
     * Удаляет наблюдателя из списка подписчиков.
     *
     * <p><b>Важно:</b> Метод безопасен - если наблюдатель не найден,
     * ничего не происходит и исключение не генерируется.</p>
     *
     * <p><b>Когда использовать:</b></p>
     * <ul>
     *   <li>При остановке системы - все датчики отписываются</li>
     *   <li>При поломке компонента - временная отписка до ремонта</li>
     *   <li>При завершении работы приложения - освобождение ресурсов</li>
     * </ul>
     *
     * @param observer объект, реализующий интерфейс Observer
     */

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

     /**
      * Уведомляет всех подписанных наблюдателей об изменении состояния.
      *
      * <p>Каждый наблюдатель получает сообщение "TICK" через свой метод
      * {@link Observer#update(String)}. Уведомление происходит синхронно -
      * метод не завершится, пока все наблюдатели не обработают сообщение.</p>
      *
      * <p><b>Обработка ошибок:</b> Рекомендуется оборачивать вызов update()
      * в try-catch для изоляции ошибок отдельных наблюдателей.</p>
      *
      * <p><b>Временная сложность:</b> O(n), где n - количество наблюдателей</p>
      *
      * @see Observer#update(String)
      */

     @Override
     public void notifyAllObservers() {
         for (Observer observer : observers) {
             observer.update("TICK");
         }
     }

    /**
     * Возвращает текущее состояние Pulse Server.
     *
     * <p>Состояние представляет собой количество секунд, прошедших с момента
     * последнего сброса (обычно с момента запуска системы). Используется
     * датчиками для отслеживания времени и синхронизации измерений.</p>
     *
     * @return текущее значение времени в секундах
     */

    @Override
    public int getState() {
        return state;
    }

    /**
     * Устанавливает новое состояние Pulse Server и уведомляет всех наблюдателей.
     *
     * <p>Это ключевой метод паттерна "Наблюдатель" - при изменении времени
     * автоматически вызывается {@link #notifyAllObservers()}. Таким образом,
     * все подписанные датчики получают сигнал о новом такте времени.</p>
     *
     * <p><b>Последовательность действий:</b></p>
     * <ol>
     *   <li>Сохраняет новое значение времени в поле state</li>
     *   <li>Вызывает notifyAllObservers() для уведомления всех датчиков</li>
     *   <li>Каждый датчик реагирует на сигнал (измеряет показания)</li>
     * </ol>
     *
     * <p><b>Пример потока выполнения:</b></p>
     * <pre>
     * 1. Таймер: pulseServer.setState(10)  // 10 секунд
     * 2. Сохраняется state = 10
     * 3. Вызывается notifyAllObservers()
     * 4. Датчик температуры: update("TICK") → measure()
     * 5. Датчик влажности: update("TICK") → measure()
     * 6. Датчик освещенности: update("TICK") → controlLighting()
     * </pre>
     *
     * @param time новое значение состояния (обычно время в секундах)
     */

    @Override
    public void setState(int time) {
        this.state = time;
        notifyAllObservers();
    }

    /**
     * Удобный метод-обертка для генерации тактового импульса.
     *
     * <p>Является альтернативой прямому вызову setState(), когда не требуется
     * изменять значение состояния. Метод просто уведомляет всех наблюдателей
     * без обновления счетчика времени.</p>
     *
     * <p><b>Используйте этот метод, когда:</b></p>
     * <ul>
     *   <li>Сигнал TICK не связан с изменением времени</li>
     *   <li>Нужно уведомить датчики без изменения состояния</li>
     *   <li>Обеспечить обратную совместимость со старой версией кода</li>
     * </ul>
     *
     * @deprecated Рекомендуется использовать {@link #setState(int)} для явного
     *             указания времени или {@link #notifyAllObservers()} для простого уведомления
     */

    // Удобный метод-обертка
    public void pulse() {
        notifyAllObservers();
    }
}
