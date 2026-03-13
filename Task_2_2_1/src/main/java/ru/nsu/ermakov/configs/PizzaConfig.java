package ru.nsu.ermakov.configs;

import java.util.List;

/**
 * Конфиг для помощи чтения из json.
 */
public class PizzaConfig {
    public List<BakerData> bakers;
    public List<CourierData> couriers;
    public List<BaristaData> baristas;

    /**
     * Вложенный класс, где имя.
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

    /**
     * Вложенный класс, где имя.
     */
    public static class BaristaData {
        public String name;
    }

}
