package org.example.timerserver;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.controlsfx.control.ToggleSwitch;
import org.example.timerserver.Model.PulseServer;
import org.example.timerserver.Model.Sensors.*;

/**
 * Контроллер главного окна приложения Pulse Server.
 * Управляет взаимодействием между пользовательским интерфейсом и моделью данных.
 * Обрабатывает события от кнопок, переключателей и датчиков.
 *
 * <p>Основные функции:</p>
 * <ul>
 *   <li>Управление датчиками (температуры, влажности, освещенности)</li>
 *   <li>Управление системой вентиляции с различными режимами работы</li>
 *   <li>Управление системой освещения с режимами "Дискотека" и "Турбо"</li>
 *   <li>Отображение данных в реальном времени на графиках</li>
 *   <li>Логирование событий в консоль</li>
 * </ul>
 *
 * @author Дмитрий
 * @version 1.0
 * @since 2026-04-24
 */


public class HelloController {

    // ==================== UI КОМПОНЕНТЫ ====================

    //Датчики

    /** Переключатель активации датчика температуры на вкладке "Настройка компонентов" */
    @FXML private ToggleSwitch temperatureSwitch;
    /** Переключатель активации датчика влажности на вкладке "Настройка компонентов" */
    @FXML private ToggleSwitch humiditySwitch;
    /** Переключатель активации датчика освещенности на вкладке "Настройка компонентов" */
    @FXML private ToggleSwitch illuminationSwitch;

    // Система вентиляции
    /** Переключатель активации системы вентиляции на вкладке "Настройка компонентов" */
    @FXML private ToggleSwitch systemVentilationSwitch;
    /** Переключатель выбора варианта работы системы вентиляции на вкладке "Настройка компонентов" */
    @FXML private ToggleSwitch autoManualVentilationControlSwitch;
    /** Переключатель выбора скорости системы вентиляции на вкладке "Настройка компонентов" */
    @FXML private Slider ventilationSlider;
    /** Слайдер выбора скорости системы вентиляции на вкладке "Настройка компонентов" */
    @FXML private ToggleSwitch quietModeSwitch;
    /** Переключатель выбора тихого режима вентиляции на вкладке "Настройка компонентов" */
    @FXML private ToggleSwitch fullPowerModeSwitch;
    /** Переключатель выбора полоной мощности вентиляции на вкладке "Настройка компонентов" */
    @FXML private ToggleSwitch forcedVentilationSwitch;
    /** Переключатель выбора принудительного вентилирования вентиляции на вкладке "Настройка компонентов" */

    // Система освещения
    @FXML private ToggleSwitch systemLightingControlSwitch;
    /** Переключатель выбора включения освещения на вкладке "Настройка компонентов" */
    @FXML private ToggleSwitch systemLightDisco;
    /** Переключатель на режим дискотеки освещения на вкладке "Настройка компонентов" */
    @FXML private ToggleSwitch systemLightILight;
    /** Переключатель на режим турбо света освещения на вкладке "Настройка компонентов" */

    /** Прочие ui компоненты*/

    // UI элементы
    @FXML private TextArea textArea;
    @FXML private LineChart<Number, Number> lineChart;
    @FXML private Rectangle tempRect;
    @FXML private Rectangle humidityRect;
    @FXML private Rectangle lightRect;
    @FXML private Rectangle ventilationRect;
    @FXML private Rectangle lightingRect;

    // Кнопки
    @FXML private Button startSystemBtn;
    @FXML private Button stopSystemBtn;
    @FXML private Button repairTempBtn;
    @FXML private Button repairHumidityBtn;
    @FXML private Button repairLightBtn;
    @FXML private Button restartVentilationBtn;
    @FXML private Button repairLightingBtn;

    private PulseServer pulseServer = new PulseServer();
    private TemperatureSensor tempSensor;
    private HumiditySensor humiditySensor;
    private LightSensor lightSensor;

    private boolean systemRunning = false;
    private int localTime = 0;
    private Timeline pulseTimeline;
    private Timeline failureTimeline;

    // Графики
    private XYChart.Series<Number, Number> tempSeries;
    private XYChart.Series<Number, Number> humiditySeries;
    private XYChart.Series<Number, Number> lightSeries;
    private XYChart.Series<Number, Number> ventilationSeries;

    // Состояния систем
    private boolean discoBlinking = false;
    private Timeline discoTimeline;
    private int discoFailCounter = 0;
    private int turboFailCounter = 0;
    private boolean ventilationBroken = false;
    private boolean ventilationJustBroken = false;
    private boolean lightingBroken = false;
    private boolean lightingJustBroken = false;
    private boolean lightingIsOn = false;

    /**
     * Инициализирует контроллер после загрузки FXML.
     * Настраивает датчики, графики, таймеры и слушатели событий.
     * Автоматически вызывается JavaFX после загрузки FXML файла.
     */

    @FXML
    private void initialize() {
        initSensors();
        initUIComponents();
        initCharts();
        startPulseTimer();
        startFailureCheckTimer();

        // Изначально все элементы управления заблокированы
        setAllControlsDisabled(true);
    }

    /**
     * Инициализирует все датчики и подписывает их на PulseServer.
     * Создает экземпляры TemperatureSensor, HumiditySensor и LightSensor.
     */

    private void initSensors() {
        // Реализация метода...

        /**
         * Блокирует или разблокирует все элементы управления на вкладке "Настройка компонентов".
         * Используется при запуске/остановке системы.
         *
         * @param disabled true - блокировать все элементы, false - разблокировать
         */

        tempSensor = new TemperatureSensor(textArea);
        humiditySensor = new HumiditySensor(textArea);
        lightSensor = new LightSensor(textArea);

        pulseServer.attach(tempSensor);
        pulseServer.attach(humiditySensor);
        pulseServer.attach(lightSensor);
    }

    /**
     * Блокирует или разблокирует все элементы управления на вкладке "Настройка компонентов".
     * Используется при запуске/остановке системы.
     *
     * @param disabled true - блокировать все элементы, false - разблокировать
     */

    private void setAllControlsDisabled(boolean disabled) {

        // Реализация метода...

        // Датчики
        temperatureSwitch.setDisable(disabled);
        humiditySwitch.setDisable(disabled);
        illuminationSwitch.setDisable(disabled);

        // Система вентиляции
        systemVentilationSwitch.setDisable(disabled);
        autoManualVentilationControlSwitch.setDisable(disabled);
        ventilationSlider.setDisable(disabled);
        quietModeSwitch.setDisable(disabled);
        fullPowerModeSwitch.setDisable(disabled);
        forcedVentilationSwitch.setDisable(disabled);

        // Система освещения
        systemLightingControlSwitch.setDisable(disabled);
        systemLightDisco.setDisable(disabled);
        systemLightILight.setDisable(disabled);
    }

    private void initUIComponents() {
        // Датчики
        temperatureSwitch.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (!systemRunning) {
                if (newVal) {
                    addLog("Ошибка: Система не запущена! Сначала запустите систему.");
                    temperatureSwitch.setSelected(false);
                }
                return;
            }

            if (newVal) {
                if (!tempSensor.isBroken()) {
                    tempSensor.activate();
                    addLog("Датчик температуры активирован");
                } else {
                    addLog("Ошибка: Датчик температуры сломан! Требуется ремонт.");
                    temperatureSwitch.setSelected(false);
                }
            } else {
                tempSensor.deactivate();
                addLog("Датчик температуры деактивирован");
            }
            updateSensorStatus();
        });

        humiditySwitch.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (!systemRunning) {
                if (newVal) {
                    addLog("Ошибка: Система не запущена! Сначала запустите систему.");
                    humiditySwitch.setSelected(false);
                }
                return;
            }

            if (newVal) {
                if (!humiditySensor.isBroken()) {
                    humiditySensor.activate();
                    addLog("Датчик влажности активирован");
                } else {
                    addLog("Ошибка: Датчик влажности сломан! Требуется ремонт.");
                    humiditySwitch.setSelected(false);
                }
            } else {
                humiditySensor.deactivate();
                addLog("Датчик влажности деактивирован");
            }
            updateSensorStatus();
        });

        illuminationSwitch.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (!systemRunning) {
                if (newVal) {
                    addLog("Ошибка: Система не запущена! Сначала запустите систему.");
                    illuminationSwitch.setSelected(false);
                }
                return;
            }

            if (newVal) {
                if (!lightSensor.isBroken()) {
                    lightSensor.activate();
                    addLog("Датчик освещенности активирован");
                } else {
                    addLog("Ошибка: Датчик освещенности сломан! Требуется ремонт.");
                    illuminationSwitch.setSelected(false);
                }
            } else {
                lightSensor.deactivate();
                addLog("Датчик освещенности деактивирован");
            }
            updateSensorStatus();
        });

        // Система вентиляции
        systemVentilationSwitch.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (!systemRunning) {
                if (newVal) {
                    addLog("Ошибка: Система не запущена! Сначала запустите систему.");
                    systemVentilationSwitch.setSelected(false);
                }
                return;
            }

            if (newVal) {
                if (!ventilationBroken) {
                    addLog("Система вентиляции включена");
                    ventilationSeries.getData().clear();
                    updateVentilationPower(40);
                    // При включении вентиляции разблокируем элементы управления
                    setVentilationControlsEnabled(true);
                } else {
                    addLog("Ошибка: Система вентиляции сломана! Требуется ремонт.");
                    systemVentilationSwitch.setSelected(false);
                }
            } else {
                addLog("Система вентиляции выключена");
                ventilationSeries.getData().clear();
                // Блокируем все элементы управления вентиляцией
                setVentilationControlsEnabled(false);
            }
            updateVentilationStatus();
        });

        autoManualVentilationControlSwitch.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (!systemRunning || !systemVentilationSwitch.isSelected() || ventilationBroken) {
                if (newVal && (!systemRunning || !systemVentilationSwitch.isSelected())) {
                    addLog("Ошибка: Сначала включите систему вентиляции!");
                    autoManualVentilationControlSwitch.setSelected(false);
                } else if (newVal && ventilationBroken) {
                    addLog("Ошибка: Система вентиляции сломана! Невозможно переключить режим.");
                    autoManualVentilationControlSwitch.setSelected(false);
                }
                return;
            }

            ventilationSlider.setVisible(newVal);
            ventilationSlider.setDisable(!newVal);

            if (newVal) {
                addLog("Управление вентиляцией переключено на ручной режим");
            } else {
                addLog("Управление вентиляцией переключено на автоматический режим");
                applyVentilationMode();
            }
        });

        ventilationSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (systemRunning && systemVentilationSwitch.isSelected() &&
                    autoManualVentilationControlSwitch.isSelected() && !ventilationBroken) {
                addLog(String.format("Мощность вентиляции изменена на %.0f%%", newVal.doubleValue()));
                updateVentilationPower(newVal.doubleValue());
            }
        });

        quietModeSwitch.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (!systemRunning || !systemVentilationSwitch.isSelected() || ventilationBroken) {
                if (newVal) {
                    addLog("Ошибка: Невозможно активировать режим. Система вентиляции выключена или сломана!");
                    quietModeSwitch.setSelected(false);
                }
                return;
            }

            if (newVal) {
                fullPowerModeSwitch.setSelected(false);
                forcedVentilationSwitch.setSelected(false);
                autoManualVentilationControlSwitch.setSelected(false);
                updateVentilationPower(15);
                addLog("Тихий режим активирован (мощность 15%)");
            }
        });

        fullPowerModeSwitch.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (!systemRunning || !systemVentilationSwitch.isSelected() || ventilationBroken) {
                if (newVal) {
                    addLog("Ошибка: Невозможно активировать режим. Система вентиляции выключена или сломана!");
                    fullPowerModeSwitch.setSelected(false);
                }
                return;
            }

            if (newVal) {
                quietModeSwitch.setSelected(false);
                forcedVentilationSwitch.setSelected(false);
                autoManualVentilationControlSwitch.setSelected(false);
                updateVentilationPower(80);
                addLog("Режим полной мощности активирован (мощность 80%)");
            }
        });

        forcedVentilationSwitch.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (!systemRunning || !systemVentilationSwitch.isSelected() || ventilationBroken) {
                if (newVal) {
                    addLog("Ошибка: Невозможно активировать режим. Система вентиляции выключена или сломана!");
                    forcedVentilationSwitch.setSelected(false);
                }
                return;
            }

            if (newVal) {
                quietModeSwitch.setSelected(false);
                fullPowerModeSwitch.setSelected(false);
                autoManualVentilationControlSwitch.setSelected(false);
                updateVentilationPower(100);
                addLog("Принудительное вентилирование активировано (мощность 100%) - повышенный риск поломки!");
            }
        });

        // Система освещения
        systemLightingControlSwitch.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (!systemRunning) {
                if (newVal) {
                    addLog("Ошибка: Система не запущена! Сначала запустите систему.");
                    systemLightingControlSwitch.setSelected(false);
                }
                return;
            }

            if (newVal) {
                if (!lightingBroken) {
                    addLog("Система освещения включена");
                    lightingIsOn = true;
                    updateLightingStatus();
                    setLightingControlsEnabled(true);
                } else {
                    addLog("Ошибка: Система освещения сломана! Требуется ремонт.");
                    systemLightingControlSwitch.setSelected(false);
                }
            } else {
                addLog("Система освещения выключена");
                lightingIsOn = false;
                updateLightingStatus();
                stopDiscoMode();
                setLightingControlsEnabled(false);
            }
        });

        systemLightDisco.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (!systemRunning || !systemLightingControlSwitch.isSelected() || lightingBroken) {
                if (newVal) {
                    addLog("Ошибка: Невозможно активировать режим. Система освещения выключена или сломана!");
                    systemLightDisco.setSelected(false);
                }
                return;
            }

            if (newVal) {
                systemLightILight.setSelected(false);
                startDiscoMode(1.2);
                addLog("Режим 'Дискотека' активирован (мигание раз в 1.2 сек)");
            } else if (!newVal && !systemLightILight.isSelected() && systemLightingControlSwitch.isSelected()) {
                stopDiscoMode();
                lightingIsOn = true;
                updateLightingStatus();
            }
        });

        systemLightILight.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (!systemRunning || !systemLightingControlSwitch.isSelected() || lightingBroken) {
                if (newVal) {
                    addLog("Ошибка: Невозможно активировать режим. Система освещения выключена или сломана!");
                    systemLightILight.setSelected(false);
                }
                return;
            }

            if (newVal) {
                systemLightDisco.setSelected(false);
                startDiscoMode(0.25);
                addLog("Режим 'Турбо' активирован (мигание раз в 0.25 сек) - ВЫСОКИЙ РИСК ПОЛОМКИ!");
            } else if (!newVal && !systemLightDisco.isSelected() && systemLightingControlSwitch.isSelected()) {
                stopDiscoMode();
                lightingIsOn = true;
                updateLightingStatus();
            }
        });

        // Кнопки управления системой
        if (startSystemBtn != null) {
            startSystemBtn.setOnAction(e -> startSystem());
        }
        if (stopSystemBtn != null) {
            stopSystemBtn.setOnAction(e -> stopSystem());
        }

        // Кнопки ремонта
        if (repairTempBtn != null) {
            repairTempBtn.setOnAction(e -> {
                if (tempSensor.isBroken()) {
                    tempSensor.repair();
                    addLog("Датчик температуры отремонтирован");
                    updateSensorStatus();
                    // Если датчик был включен до поломки, автоматически включаем его
                    if (temperatureSwitch.isSelected()) {
                        tempSensor.activate();
                        addLog("Датчик температуры автоматически активирован");
                    }
                } else {
                    addLog("Датчик температуры исправен");
                }
            });
        }

        if (repairHumidityBtn != null) {
            repairHumidityBtn.setOnAction(e -> {
                if (humiditySensor.isBroken()) {
                    humiditySensor.repair();
                    addLog("Датчик влажности отремонтирован");
                    updateSensorStatus();
                    if (humiditySwitch.isSelected()) {
                        humiditySensor.activate();
                        addLog("Датчик влажности автоматически активирован");
                    }
                } else {
                    addLog("Датчик влажности исправен");
                }
            });
        }

        if (repairLightBtn != null) {
            repairLightBtn.setOnAction(e -> {
                if (lightSensor.isBroken()) {
                    lightSensor.repair();
                    addLog("Датчик освещенности отремонтирован");
                    updateSensorStatus();
                    if (illuminationSwitch.isSelected()) {
                        lightSensor.activate();
                        addLog("Датчик освещенности автоматически активирован");
                    }
                } else {
                    addLog("Датчик освещенности исправен");
                }
            });
        }

        if (restartVentilationBtn != null) {
            restartVentilationBtn.setOnAction(e -> {
                if (ventilationBroken) {
                    repairVentilation();
                } else {
                    addLog("Система вентиляции исправна");
                }
            });
        }

        if (repairLightingBtn != null) {
            repairLightingBtn.setOnAction(e -> {
                if (lightingBroken) {
                    repairLighting();
                } else {
                    addLog("Система освещения исправна");
                }
            });
        }
    }

    /**
     * Блокирует или разблокирует элементы управления системой вентиляции.
     *
     * @param enabled true - разблокировать управление, false - заблокировать
     */

    private void setVentilationControlsEnabled(boolean enabled) {
        // Реализация метода...
        autoManualVentilationControlSwitch.setDisable(!enabled);
        ventilationSlider.setDisable(!enabled || !autoManualVentilationControlSwitch.isSelected());
        quietModeSwitch.setDisable(!enabled);
        fullPowerModeSwitch.setDisable(!enabled);
        forcedVentilationSwitch.setDisable(!enabled);
    }

    private void setLightingControlsEnabled(boolean enabled) {
        systemLightDisco.setDisable(!enabled);
        systemLightILight.setDisable(!enabled);
    }

    // ==================== УПРАВЛЕНИЕ СИСТЕМОЙ ====================

    /**
     * Запускает систему управления микроклиматом.
     * Разблокирует все элементы управления и через 5 секунд автоматически активирует все датчики.
     *
     * <p>Последовательность действий:</p>
     * <ol>
     *   <li>Устанавливает флаг systemRunning = true</li>
     *   <li>Разблокирует все переключатели</li>
     *   <li>Записывает событие в консоль</li>
     *   <li>Через 5 секунд включает все датчики и системы</li>
     * </ol>
     */


    private void startSystem() {

        // Реализация метода...

        if (!systemRunning) {
            systemRunning = true;
            localTime = 0;

            // Разблокируем все элементы управления
            setAllControlsDisabled(false);

            addLog("=== СИСТЕМА ЗАПУЩЕНА ===");
            addLog("Автоматическая активация всех датчиков через 5 секунд...");

            // Устанавливаем прямоугольники в серый цвет (неактивны)
            updateSensorStatus();
            updateVentilationStatus();
            updateLightingStatus();

            Timeline delayTimeline = new Timeline(new KeyFrame(Duration.seconds(5), ev -> {
                if (systemRunning) {
                    temperatureSwitch.setSelected(true);
                    humiditySwitch.setSelected(true);
                    illuminationSwitch.setSelected(true);
                    systemVentilationSwitch.setSelected(true);
                    systemLightingControlSwitch.setSelected(true);
                    autoManualVentilationControlSwitch.setSelected(true);
                    ventilationSlider.setValue(50);
                    addLog("Все системы активированы!");
                }
            }));
            delayTimeline.play();
        }
    }

    /**
     * Останавливает систему управления микроклиматом.
     * Блокирует все элементы управления и деактивирует все датчики.
     *
     * <p>Действия при остановке:</p>
     * <ul>
     *   <li>Выключает все переключатели</li>
     *   <li>Деактивирует все датчики</li>
     *   <li>Останавливает режимы "Дискотека" и "Турбо"</li>
     *   <li>Блокирует все элементы управления</li>
     *   <li>Устанавливает красный цвет для всех индикаторов</li>
     * </ul>
     */

    private void stopSystem() {

        // Реализация метода...

        if (systemRunning) {
            systemRunning = false;

            // Выключаем все переключатели
            temperatureSwitch.setSelected(false);
            humiditySwitch.setSelected(false);
            illuminationSwitch.setSelected(false);
            systemVentilationSwitch.setSelected(false);
            systemLightingControlSwitch.setSelected(false);

            // Деактивируем датчики
            tempSensor.deactivate();
            humiditySensor.deactivate();
            lightSensor.deactivate();

            // Останавливаем режимы
            stopDiscoMode();
            lightingIsOn = false;

            // Блокируем все элементы управления
            setAllControlsDisabled(true);

            // Устанавливаем красный цвет для всех прямоугольников
            updateAllRectanglesToRed();

            addLog("=== СИСТЕМА ОСТАНОВЛЕНА ===");
            clearCharts();
        }
    }

    private void updateAllRectanglesToRed() {
        Platform.runLater(() -> {
            if (tempRect != null) tempRect.setFill(Color.RED);
            if (humidityRect != null) humidityRect.setFill(Color.RED);
            if (lightRect != null) lightRect.setFill(Color.RED);
            if (ventilationRect != null) ventilationRect.setFill(Color.RED);
            if (lightingRect != null) lightingRect.setFill(Color.RED);
        });
    }

    // ==================== УПРАВЛЕНИЕ ТАЙМЕРАМИ ====================

    /**
     * Запускает основной таймер Pulse Server.
     * Генерирует сигнал "TICK" каждую секунду для всех подписанных датчиков.
     *
     * @see PulseServer#pulse()
     */

    private void startPulseTimer() {

        // Реализация метода...

        pulseTimeline = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
            if (systemRunning) {
                localTime++;
                pulseServer.pulse();
                updateCharts();
            }
        }));
        pulseTimeline.setCycleCount(Timeline.INDEFINITE);
        pulseTimeline.play();
    }

    /**
     * Запускает таймер проверки случайных поломок.
     * Каждые 10 секунд с вероятностью 80% может сломать любой активный датчик.
     * Также отслеживает повышенный риск поломки при использовании специальных режимов.
     */

    private void startFailureCheckTimer() {

        // Реализация метода...

        failureTimeline = new Timeline(new KeyFrame(Duration.seconds(10), ev -> {
            if (systemRunning) {
                checkRandomFailures();
            }
        }));
        failureTimeline.setCycleCount(Timeline.INDEFINITE);
        failureTimeline.play();
    }

    private void checkRandomFailures() {
        // Случайные поломки датчиков
        if (Math.random() < 0.8) {
            if (temperatureSwitch.isSelected() && !tempSensor.isBroken() && Math.random() < 0.3) {
                tempSensor.breakSensor();
                addLog("⚠ ДАТЧИК ТЕМПЕРАТУРЫ СЛОМАЛСЯ!");
                updateSensorStatus();
            }
            if (humiditySwitch.isSelected() && !humiditySensor.isBroken() && Math.random() < 0.3) {
                humiditySensor.breakSensor();
                addLog("⚠ ДАТЧИК ВЛАЖНОСТИ СЛОМАЛСЯ!");
                updateSensorStatus();
            }
            if (illuminationSwitch.isSelected() && !lightSensor.isBroken() && Math.random() < 0.3) {
                lightSensor.breakSensor();
                addLog("⚠ ДАТЧИК ОСВЕЩЕННОСТИ СЛОМАЛСЯ!");
                updateSensorStatus();
            }
        }

        // Проверка поломки вентиляции
        if (systemVentilationSwitch.isSelected() && !ventilationBroken && localTime > 80) {
            if (Math.random() < 0.85) {
                breakVentilation();
                addLog("⚠⚠⚠ СИСТЕМА ВЕНТИЛЯЦИИ СЛОМАЛАСЬ! ⚠⚠⚠");
            }
        }

        // Поломка системы освещения
        if (systemLightingControlSwitch.isSelected() && !lightingBroken) {
            if (systemLightDisco.isSelected()) {
                discoFailCounter++;
                if (discoFailCounter >= 19) {
                    double chance = 0.20 + (discoFailCounter / 19) * 0.1;
                    if (Math.random() < chance) {
                        breakLighting();
                        addLog("⚠⚠⚠ СИСТЕМА ОСВЕЩЕНИЯ СЛОМАЛАСЬ в режиме 'Дискотека'! ⚠⚠⚠");
                    }
                }
            } else if (systemLightILight.isSelected()) {
                turboFailCounter++;
                if (turboFailCounter >= 26) {
                    double chance = 0.60 + (turboFailCounter / 26) * 0.1;
                    if (Math.random() < chance) {
                        breakLighting();
                        addLog("⚠⚠⚠ СИСТЕМА ОСВЕЩЕНИЯ СЛОМАЛАСЬ в режиме 'Турбо'! ⚠⚠⚠");
                    }
                }
            }
        }
    }

    // ==================== ГРАФИКИ ====================

    /**
     * Инициализирует все графики для отображения показаний датчиков.
     * Создает серии данных для температуры, влажности, освещенности и мощности вентиляции.
     */

    private void initCharts() {

        // Реализация метода...

        tempSeries = new XYChart.Series<>();
        tempSeries.setName("Температура");
        humiditySeries = new XYChart.Series<>();
        humiditySeries.setName("Влажность");
        lightSeries = new XYChart.Series<>();
        lightSeries.setName("Освещенность");
        ventilationSeries = new XYChart.Series<>();
        ventilationSeries.setName("Мощность вентиляции");

        lineChart.getData().addAll(tempSeries, humiditySeries, lightSeries, ventilationSeries);
    }

    /**
     * Обновляет графики новыми показаниями датчиков.
     * Обновление происходит каждые 5 секунд для оптимизации производительности.
     *
     * <p>Добавляет новые точки на график и удаляет старые, сохраняя только последние 20 значений.</p>
     */

    private void updateCharts() {

        // Реализация метода...

        if (localTime % 5 == 0) {
            if (temperatureSwitch.isSelected() && !tempSensor.isBroken()) {
                tempSeries.getData().add(new XYChart.Data<>(localTime, tempSensor.getLastValue()));
                if (tempSeries.getData().size() > 20) tempSeries.getData().remove(0);
            }
            if (humiditySwitch.isSelected() && !humiditySensor.isBroken()) {
                humiditySeries.getData().add(new XYChart.Data<>(localTime, humiditySensor.getLastValue()));
                if (humiditySeries.getData().size() > 20) humiditySeries.getData().remove(0);
            }
            if (illuminationSwitch.isSelected() && !lightSensor.isBroken()) {
                lightSeries.getData().add(new XYChart.Data<>(localTime, lightSensor.getLastValue()));
                if (lightSeries.getData().size() > 20) lightSeries.getData().remove(0);
            }
        }
    }

    // ==================== СИСТЕМА ВЕНТИЛЯЦИИ ====================

    /**
     * Обновляет значение мощности вентиляции на графике.
     *
     * @param power новое значение мощности в процентах (от 0 до 100)
     */

    private void updateVentilationPower(double power) {
        // Реализация метода...
        if (!ventilationBroken) {
            ventilationSeries.getData().add(new XYChart.Data<>(localTime, power));
            if (ventilationSeries.getData().size() > 20) ventilationSeries.getData().remove(0);
        }
    }

    /**
     * Применяет выбранный режим вентиляции (тихий, полная мощность, принудительный).
     * Автоматически переключает управление в автоматический режим.
     */

    private void applyVentilationMode() {
        // Реализация метода...
        if (quietModeSwitch.isSelected()) updateVentilationPower(15);
        else if (fullPowerModeSwitch.isSelected()) updateVentilationPower(80);
        else if (forcedVentilationSwitch.isSelected()) updateVentilationPower(100);
        else updateVentilationPower(40);
    }

    // ==================== СИСТЕМА ОСВЕЩЕНИЯ ====================

    /**
     * Запускает режим мигания для системы освещения.
     *
     * @param interval интервал мигания в секундах
     */

    private void startDiscoMode(double interval) {
        stopDiscoMode();
        discoBlinking = true;
        discoTimeline = new Timeline(new KeyFrame(Duration.seconds(interval), ev -> {
            if (discoBlinking && systemLightingControlSwitch.isSelected() && !lightingBroken && systemRunning) {
                lightingIsOn = !lightingIsOn;
                updateLightingStatus();
            }
        }));
        discoTimeline.setCycleCount(Timeline.INDEFINITE);
        discoTimeline.play();
    }

    /**
     * Останавливает режим мигания системы освещения.
     */

    private void stopDiscoMode() {
        // Реализация метода...
        if (discoTimeline != null) {
            discoTimeline.stop();
            discoBlinking = false;
        }
    }

    // ==================== ОБНОВЛЕНИЕ ИНДИКАТОРОВ ====================

    /**
     * Обновляет цветовые индикаторы датчиков на вкладке "Статус компонентов".
     *
     * <p>Цветовая схема:</p>
     * <ul>
     *   <li><b>Зеленый</b> - датчик активен и исправен</li>
     *   <li><b>Красный</b> - датчик сломан или система остановлена</li>
     *   <li><b>Желтый</b> - датчик только что сломался (первые 3 секунды)</li>
     *   <li><b>Серый</b> - датчик выключен пользователем</li>
     * </ul>
     */

    private void updateSensorStatus() {
        // Реализация метода...
        Platform.runLater(() -> {
            if (tempRect != null) {
                if (!systemRunning) tempRect.setFill(Color.RED);
                else if (!temperatureSwitch.isSelected()) tempRect.setFill(Color.GRAY);
                else if (tempSensor.isBroken()) tempRect.setFill(Color.RED);
                else if (tempSensor.isJustBroken()) tempRect.setFill(Color.YELLOW);
                else tempRect.setFill(Color.GREEN);
            }

            if (humidityRect != null) {
                if (!systemRunning) humidityRect.setFill(Color.RED);
                else if (!humiditySwitch.isSelected()) humidityRect.setFill(Color.GRAY);
                else if (humiditySensor.isBroken()) humidityRect.setFill(Color.RED);
                else if (humiditySensor.isJustBroken()) humidityRect.setFill(Color.YELLOW);
                else humidityRect.setFill(Color.GREEN);
            }

            if (lightRect != null) {
                if (!systemRunning) lightRect.setFill(Color.RED);
                else if (!illuminationSwitch.isSelected()) lightRect.setFill(Color.GRAY);
                else if (lightSensor.isBroken()) lightRect.setFill(Color.RED);
                else if (lightSensor.isJustBroken()) lightRect.setFill(Color.YELLOW);
                else lightRect.setFill(Color.GREEN);
            }
        });

        // Сброс желтого цвета через 3 секунды
        Timeline resetTimeline = new Timeline(new KeyFrame(Duration.seconds(3), ev -> {
            if (tempSensor != null) tempSensor.resetJustBroken();
            if (humiditySensor != null) humiditySensor.resetJustBroken();
            if (lightSensor != null) lightSensor.resetJustBroken();
            updateSensorStatus();
        }));
        resetTimeline.setCycleCount(1);
        resetTimeline.play();
    }

    /**
     * Обновляет цветовой индикатор системы вентиляции.
     * Использует ту же цветовую схему, что и датчики.
     */

    private void updateVentilationStatus() {
        // Реализация метода...
        Platform.runLater(() -> {
            if (ventilationRect != null) {
                if (!systemRunning) ventilationRect.setFill(Color.RED);
                else if (!systemVentilationSwitch.isSelected()) ventilationRect.setFill(Color.GRAY);
                else if (ventilationBroken) ventilationRect.setFill(Color.RED);
                else if (ventilationJustBroken) ventilationRect.setFill(Color.YELLOW);
                else ventilationRect.setFill(Color.GREEN);
            }
        });

        if (ventilationJustBroken) {
            Timeline resetTimeline = new Timeline(new KeyFrame(Duration.seconds(3), ev -> {
                ventilationJustBroken = false;
                updateVentilationStatus();
            }));
            resetTimeline.setCycleCount(1);
            resetTimeline.play();
        }
    }

    /**
     * Обновляет цветовой индикатор системы освещения.
     * При включенном освещении индикатор горит желтым цветом.
     */

    private void updateLightingStatus() {
        // Реализация метода...
        Platform.runLater(() -> {
            if (lightingRect != null) {
                if (!systemRunning) lightingRect.setFill(Color.RED);
                else if (!systemLightingControlSwitch.isSelected()) lightingRect.setFill(Color.GRAY);
                else if (lightingBroken) lightingRect.setFill(Color.RED);
                else if (lightingJustBroken) lightingRect.setFill(Color.YELLOW);
                else if (lightingIsOn) lightingRect.setFill(Color.YELLOW);
                else lightingRect.setFill(Color.GRAY);
            }
        });

        if (lightingJustBroken) {
            Timeline resetTimeline = new Timeline(new KeyFrame(Duration.seconds(3), ev -> {
                lightingJustBroken = false;
                updateLightingStatus();
            }));
            resetTimeline.setCycleCount(1);
            resetTimeline.play();
        }
    }

    // ==================== ОБРАБОТКА ПОЛОМОК И РЕМОНТА ====================

    /**
     * Обрабатывает поломку системы вентиляции.
     * Блокирует все связанные элементы управления и очищает график.
     */

    private void breakVentilation() {
        // Реализация метода...
        ventilationBroken = true;
        ventilationJustBroken = true;
        systemVentilationSwitch.setSelected(false);
        setVentilationControlsEnabled(false);
        autoManualVentilationControlSwitch.setSelected(false);
        quietModeSwitch.setSelected(false);
        fullPowerModeSwitch.setSelected(false);
        forcedVentilationSwitch.setSelected(false);
        ventilationSlider.setVisible(false);
        ventilationSeries.getData().clear();
        updateVentilationStatus();
    }

    /**
     * Восстанавливает систему вентиляции после поломки.
     * Разблокирует управление, если система активна.
     */

    private void repairVentilation() {
        // Реализация метода...
        ventilationBroken = false;
        ventilationJustBroken = false;
        addLog("Система вентиляции отремонтирована!");
        if (systemRunning) {
            systemVentilationSwitch.setSelected(true);
        }
        updateVentilationStatus();
    }

    /**
     * Обрабатывает поломку системы освещения.
     * Останавливает режимы мигания и блокирует управление.
     */

    private void breakLighting() {
        // Реализация метода...
        lightingBroken = true;
        lightingJustBroken = true;
        lightingIsOn = false;
        systemLightingControlSwitch.setSelected(false);
        setLightingControlsEnabled(false);
        systemLightDisco.setSelected(false);
        systemLightILight.setSelected(false);
        stopDiscoMode();
        updateLightingStatus();
        addLog("Система освещения сломана! Требуется ремонт.");
    }

    /**
     * Восстанавливает систему освещения после поломки.
     */

    private void repairLighting() {
        // Реализация метода...
        lightingBroken = false;
        lightingJustBroken = false;
        addLog("Система освещения отремонтирована!");
        if (systemRunning) {
            systemLightingControlSwitch.setSelected(true);
        }
        updateLightingStatus();
    }

    private void clearCharts() {
        tempSeries.getData().clear();
        humiditySeries.getData().clear();
        lightSeries.getData().clear();
        ventilationSeries.getData().clear();
    }

    /**
     * Добавляет сообщение в консоль вывода данных.
     * Сообщения автоматически снабжаются временной меткой (секунды с момента запуска).
     *
     * @param message текст сообщения для вывода
     */

    private void addLog(String message) {
        // Реализация метода...
        Platform.runLater(() -> {
            textArea.appendText("[" + localTime + "с] " + message + "\n");
            textArea.setScrollTop(Double.MAX_VALUE);
        });
    }
}