package ru.nsu.ermakov.configs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Тестовый класс для проверки функциональности чтения конфигураций.
 * Проверяет загрузку JSON конфигурационных файлов.
 */
class ConfigReaderTest {

    private ConfigReader configReader;
    private @TempDir Path tempDir;

    /**
     * Инициализация читателя конфигураций перед каждым тестом.
     */
    @BeforeEach
    void setUp() {
        configReader = new ConfigReader();
    }

    /**
     * Проверка создания читателя конфигураций.
     */
    @Test
    @DisplayName("Создание читателя конфигураций")
    void testConfigReaderCreation() {
        assertNotNull(configReader);
    }

    /**
     * Проверка чтения валидного PizzaConfig.
     */
    @Test
    @DisplayName("Чтение валидного PizzaConfig")
    void testReadValidPizzaConfig() throws IOException {
        String jsonContent = """
            {
                "bakers": [
                    {"name": "Пекарь 1"},
                    {"name": "Пекарь 2"}
                ],
                "baristas": [
                    {"name": "Бариста 1"}
                ],
                "couriers": [
                    {"boxSize": 5},
                    {"boxSize": 3}
                ]
            }
            """;
        
        File configFile = tempDir.resolve("pizza.json").toFile();
        java.nio.file.Files.write(configFile.toPath(), jsonContent.getBytes());
        
        PizzaConfig config = configReader.readPizzaConfig(configFile.getAbsolutePath());
        
        assertNotNull(config);
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
     * Проверка чтения пустого PizzaConfig.
     */
    @Test
    @DisplayName("Чтение пустого PizzaConfig")
    void testReadEmptyPizzaConfig() throws IOException {
        String jsonContent = """
            {
                "bakers": [],
                "baristas": [],
                "couriers": []
            }
            """;
        
        File configFile = tempDir.resolve("empty_pizza.json").toFile();
        java.nio.file.Files.write(configFile.toPath(), jsonContent.getBytes());
        
        PizzaConfig config = configReader.readPizzaConfig(configFile.getAbsolutePath());
        
        assertNotNull(config);
        assertTrue(config.bakers.isEmpty());
        assertTrue(config.baristas.isEmpty());
        assertTrue(config.couriers.isEmpty());
    }

    /**
     * Проверка чтения валидного OrderConfig.
     */
    @Test
    @DisplayName("Чтение валидного OrderConfig")
    void testReadValidOrderConfig() throws IOException {
        String jsonContent = """
            {
                "orderSpawnRate": 1000,
                "totalOrders": 50,
                "productsList": [
                    {"type": "pizza", "id": 1},
                    {"type": "cola", "id": 2}
                ]
            }
            """;
        
        File configFile = tempDir.resolve("order.json").toFile();
        java.nio.file.Files.write(configFile.toPath(), jsonContent.getBytes());
        
        OrderConfig config = configReader.readOrderConfig(configFile.getAbsolutePath());
        
        assertNotNull(config);
        assertEquals(1000, config.orderSpawnRate);
        assertEquals(50, config.totalOrders);
        assertNotNull(config.productsList);
        assertEquals(2, config.productsList.size());
    }

    /**
     * Проверка чтения валидного WarehouseConfig.
     */
    @Test
    @DisplayName("Чтение валидного WarehouseConfig")
    void testReadValidWarehouseConfig() throws IOException {
        String jsonContent = """
            {
                "storageSize": 100
            }
            """;
        
        File configFile = tempDir.resolve("warehouse.json").toFile();
        java.nio.file.Files.write(configFile.toPath(), jsonContent.getBytes());
        
        WarehouseConfig config = configReader.readWarehouseConfig(configFile.getAbsolutePath());
        
        assertNotNull(config);
        assertEquals(100, config.storageSize);
    }

    /**
     * Проверка чтения несуществующего файла.
     */
    @Test
    @DisplayName("Чтение несуществующего файла")
    void testReadNonExistentFile() {
        assertThrows(IOException.class, () -> {
            configReader.readPizzaConfig("non_existent_file.json");
        });
        
        assertThrows(IOException.class, () -> {
            configReader.readOrderConfig("non_existent_file.json");
        });
        
        assertThrows(IOException.class, () -> {
            configReader.readWarehouseConfig("non_existent_file.json");
        });
    }

    /**
     * Проверка чтения невалидного JSON.
     */
    @Test
    @DisplayName("Чтение невалидного JSON")
    void testReadInvalidJson() throws IOException {
        String invalidJson = """
            {
                "bakers": [
                    {"name": "Пекарь 1"
                ]
            }
            """;
        
        File configFile = tempDir.resolve("invalid.json").toFile();
        java.nio.file.Files.write(configFile.toPath(), invalidJson.getBytes());
        
        assertThrows(IOException.class, () -> {
            configReader.readPizzaConfig(configFile.getAbsolutePath());
        });
    }

    /**
     * Проверка чтения файла с неполными данными.
     */
    @Test
    @DisplayName("Чтение файла с неполными данными")
    void testReadIncompleteData() throws IOException {
        String incompleteJson = """
            {
                "bakers": [
                    {"name": "Пекарь 1"}
                ]
            }
            """;
        
        File configFile = tempDir.resolve("incomplete.json").toFile();
        java.nio.file.Files.write(configFile.toPath(), incompleteJson.getBytes());
        
        PizzaConfig config = configReader.readPizzaConfig(configFile.getAbsolutePath());
        
        assertNotNull(config);
        assertEquals(1, config.bakers.size());
        assertNull(config.baristas);
        assertNull(config.couriers);
    }

    /**
     * Проверка чтения пустого JSON файла.
     */
    @Test
    @DisplayName("Чтение пустого JSON файла")
    void testReadEmptyJsonFile() throws IOException {
        String emptyJson = "{}";
        
        File configFile = tempDir.resolve("empty.json").toFile();
        java.nio.file.Files.write(configFile.toPath(), emptyJson.getBytes());
        
        PizzaConfig config = configReader.readPizzaConfig(configFile.getAbsolutePath());
        
        assertNotNull(config);
        assertNull(config.bakers);
        assertNull(config.baristas);
        assertNull(config.couriers);
    }
}
