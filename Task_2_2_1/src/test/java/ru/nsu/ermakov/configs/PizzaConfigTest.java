package ru.nsu.ermakov.configs;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

/**
 * Тестовый класс для проверки конфигурации пиццерии.
 * Проверяет структуру данных конфигурации.
 */
class PizzaConfigTest {

    /**
     * Проверка создания PizzaConfig.
     */
    @Test
    @DisplayName("Создание PizzaConfig")
    void testPizzaConfigCreation() {
        PizzaConfig config = new PizzaConfig();
        
        assertNotNull(config);
        assertNull(config.bakers);
        assertNull(config.baristas);
        assertNull(config.couriers);
    }

    /**
     * Проверка вложенного класса BakerData.
     */
    @Test
    @DisplayName("Вложенный класс BakerData")
    void testBakerData() {
        PizzaConfig.BakerData baker = new PizzaConfig.BakerData();
        
        assertNotNull(baker);
        assertNull(baker.name);
        
        baker.name = "Тестовый пекарь";
        assertEquals("Тестовый пекарь", baker.name);
    }

    /**
     * Проверка вложенного класса BaristaData.
     */
    @Test
    @DisplayName("Вложенный класс BaristaData")
    void testBaristaData() {
        PizzaConfig.BaristaData barista = new PizzaConfig.BaristaData();
        
        assertNotNull(barista);
        assertNull(barista.name);
        
        barista.name = "Тестовый бариста";
        assertEquals("Тестовый бариста", barista.name);
    }

    /**
     * Проверка вложенного класса CourierData.
     */
    @Test
    @DisplayName("Вложенный класс CourierData")
    void testCourierData() {
        PizzaConfig.CourierData courier = new PizzaConfig.CourierData();
        
        assertNotNull(courier);
        assertEquals(0, courier.boxSize);
        
        courier.boxSize = 10;
        assertEquals(10, courier.boxSize);
    }

    /**
     * Проверка заполненной конфигурации.
     */
    @Test
    @DisplayName("Заполненная конфигурация")
    void testFilledConfig() {
        PizzaConfig config = new PizzaConfig();
        
        PizzaConfig.BakerData baker1 = new PizzaConfig.BakerData();
        baker1.name = "Пекарь 1";
        
        PizzaConfig.BakerData baker2 = new PizzaConfig.BakerData();
        baker2.name = "Пекарь 2";
        
        PizzaConfig.BaristaData barista1 = new PizzaConfig.BaristaData();
        barista1.name = "Бариста 1";
        
        PizzaConfig.CourierData courier1 = new PizzaConfig.CourierData();
        courier1.boxSize = 5;
        
        PizzaConfig.CourierData courier2 = new PizzaConfig.CourierData();
        courier2.boxSize = 3;
        
        config.bakers = List.of(baker1, baker2);
        config.baristas = List.of(barista1);
        config.couriers = List.of(courier1, courier2);
        
        assertEquals(2, config.bakers.size());
        assertEquals(1, config.baristas.size());
        assertEquals(2, config.couriers.size());
        
        assertEquals("Пекарь 1", config.bakers.get(0).name);
        assertEquals("Пекарь 2", config.bakers.get(1).name);
        assertEquals("Бариста 1", config.baristas.get(0).name);
        assertEquals(5, config.couriers.get(0).boxSize);
        assertEquals(3, config.couriers.get(1).boxSize);
    }

    /**
     * Проверка пустых списков в конфигурации.
     */
    @Test
    @DisplayName("Пустые списки в конфигурации")
    void testEmptyListsConfig() {
        PizzaConfig config = new PizzaConfig();
        
        config.bakers = List.of();
        config.baristas = List.of();
        config.couriers = List.of();
        
        assertTrue(config.bakers.isEmpty());
        assertTrue(config.baristas.isEmpty());
        assertTrue(config.couriers.isEmpty());
    }

    /**
     * Проверка различных значений boxSize.
     */
    @Test
    @DisplayName("Различные значения boxSize")
    void testDifferentBoxSizes() {
        PizzaConfig.CourierData smallCourier = new PizzaConfig.CourierData();
        smallCourier.boxSize = 1;
        
        PizzaConfig.CourierData mediumCourier = new PizzaConfig.CourierData();
        mediumCourier.boxSize = 5;
        
        PizzaConfig.CourierData largeCourier = new PizzaConfig.CourierData();
        largeCourier.boxSize = 20;
        
        assertEquals(1, smallCourier.boxSize);
        assertEquals(5, mediumCourier.boxSize);
        assertEquals(20, largeCourier.boxSize);
    }

    /**
     * Проверка имен персонала.
     */
    @Test
    @DisplayName("Имена персонала")
    void testStaffNames() {
        PizzaConfig.BakerData baker = new PizzaConfig.BakerData();
        PizzaConfig.BaristaData barista = new PizzaConfig.BaristaData();
        
        baker.name = "Иван Пекарь";
        barista.name = "Петр Бариста";
        
        assertEquals("Иван Пекарь", baker.name);
        assertEquals("Петр Бариста", barista.name);
        
        baker.name = "";
        barista.name = null;
        
        assertEquals("", baker.name);
        assertNull(barista.name);
    }
}
