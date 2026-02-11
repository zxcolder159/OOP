package ru.nsu.ermakov.configs;

import java.util.List;

/**
 * Конфиг для помощи чтения из json.
 */
public class PizzaConfig {
    public List<BakerData> bakers;
    public List<CourierData> couriers;

    /**
     * Вложенный класс, где только скорость готовки.
     */
    public static class BakerData {
        public String name;
    }

    /**
     * Вложенный класс, где только размер короба.
     */
    public static class CourierData {
        public int boxSize;
    }

}
