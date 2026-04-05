package ru.nsu.ermakov.pizzeria;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.nsu.ermakov.configs.PizzaConfig;
import ru.nsu.ermakov.products.Pizza;
import ru.nsu.ermakov.products.CocaCola;
import ru.nsu.ermakov.products.Product;
import ru.nsu.ermakov.warehouse.Warehouse;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Тестовый класс для проверки функциональности пиццерии.
 * Проверяет делегирование заказов и управление персоналом.
 */
class PizzeriaTest {

    @Mock
    private Warehouse mockWarehouse;
    
    @Mock
    private CountDownLatch mockLatch;
    
    private PizzaConfig pizzaConfig;
    private Pizzeria pizzeria;

    /**
     * Инициализация тестовых данных перед каждым тестом.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        pizzaConfig = new PizzaConfig();
        pizzaConfig.bakers = List.of(
            createBakerData("Пекарь 1"),
            createBakerData("Пекарь 2")
        );
        pizzaConfig.baristas = List.of(
            createBaristaData("Бариста 1"),
            createBaristaData("Бариста 2")
        );
        pizzaConfig.couriers = List.of(
            createCourierData(5),
            createCourierData(3)
        );
        
        pizzeria = new Pizzeria(pizzaConfig, mockWarehouse, mockLatch);
    }

    private PizzaConfig.BakerData createBakerData(String name) {
        PizzaConfig.BakerData baker = new PizzaConfig.BakerData();
        baker.name = name;
        return baker;
    }

    private PizzaConfig.BaristaData createBaristaData(String name) {
        PizzaConfig.BaristaData barista = new PizzaConfig.BaristaData();
        barista.name = name;
        return barista;
    }

    private PizzaConfig.CourierData createCourierData(int boxSize) {
        PizzaConfig.CourierData courier = new PizzaConfig.CourierData();
        courier.boxSize = boxSize;
        return courier;
    }

    /**
     * Проверка создания пиццерии.
     */
    @Test
    @DisplayName("Создание пиццерии")
    void testPizzeriaCreation() {
        assertNotNull(pizzeria);
        assertNotNull(mockWarehouse);
        assertNotNull(mockLatch);
    }

    /**
     * Проверка делегирования заказа еды.
     */
    @Test
    @DisplayName("Делегирование заказа еды")
    void testDelegateFoodOrder() {
        Pizza pizza = new Pizza(1);
        pizza.setOrderId(100);
        
        assertDoesNotThrow(() -> pizzeria.delegateOrder(pizza));
    }

    /**
     * Проверка делегирования заказа напитка.
     */
    @Test
    @DisplayName("Делегирование заказа напитка")
    void testDelegateDrinkOrder() {
        CocaCola cola = new CocaCola(1);
        cola.setOrderId(200);
        
        assertDoesNotThrow(() -> pizzeria.delegateOrder(cola));
    }

    /**
     * Проверка делегирования нескольких заказов.
     */
    @Test
    @DisplayName("Делегирование нескольких заказов")
    void testDelegateMultipleOrders() {
        Pizza pizza1 = new Pizza(1);
        Pizza pizza2 = new Pizza(2);
        CocaCola cola1 = new CocaCola(1);
        CocaCola cola2 = new CocaCola(2);
        
        pizza1.setOrderId(101);
        pizza2.setOrderId(102);
        cola1.setOrderId(201);
        cola2.setOrderId(202);
        
        assertDoesNotThrow(() -> {
            pizzeria.delegateOrder(pizza1);
            pizzeria.delegateOrder(pizza2);
            pizzeria.delegateOrder(cola1);
            pizzeria.delegateOrder(cola2);
        });
    }

    /**
     * Проверка делегирования заказа неизвестного типа.
     */
    @Test
    @DisplayName("Делегирование заказа неизвестного типа")
    void testDelegateUnknownOrder() {
        Product unknownProduct = new Product() {
            @Override
            public int getSize() { return 1; }
            
            @Override
            public int getId() { return 999; }
            
            @Override
            public int getOrderId() { return 0; }
            
            @Override
            public void setOrderId(int orderId) {}
            
            @Override
            public Product clone() { return this; }
        };
        
        assertDoesNotThrow(() -> pizzeria.delegateOrder(unknownProduct));
    }

    /**
     * Проверка остановки пиццерии.
     */
    @Test
    @DisplayName("Остановка пиццерии")
    void testStopPizzeria() {
        assertDoesNotThrow(() -> pizzeria.stopPizzeria());
    }

    /**
     * Проверка работы пиццерии без поваров.
     */
    @Test
    @DisplayName("Работа без поваров")
    void testPizzeriaWithoutBakers() {
        PizzaConfig configWithoutBakers = new PizzaConfig();
        configWithoutBakers.bakers = List.of();
        configWithoutBakers.baristas = List.of(createBaristaData("Бариста"));
        configWithoutBakers.couriers = List.of(createCourierData(5));
        
        Pizzeria pizzeriaWithoutBakers = new Pizzeria(configWithoutBakers, mockWarehouse, mockLatch);
        
        Pizza pizza = new Pizza(1);
        pizza.setOrderId(300);
        
        assertDoesNotThrow(() -> pizzeriaWithoutBakers.delegateOrder(pizza));
    }

    /**
     * Проверка работы пиццерии без бариста.
     */
    @Test
    @DisplayName("Работа без бариста")
    void testPizzeriaWithoutBaristas() {
        PizzaConfig configWithoutBaristas = new PizzaConfig();
        configWithoutBaristas.bakers = List.of(createBakerData("Пекарь"));
        configWithoutBaristas.baristas = List.of();
        configWithoutBaristas.couriers = List.of(createCourierData(5));
        
        Pizzeria pizzeriaWithoutBaristas = new Pizzeria(configWithoutBaristas, mockWarehouse, mockLatch);
        
        CocaCola cola = new CocaCola(1);
        cola.setOrderId(400);
        
        assertDoesNotThrow(() -> pizzeriaWithoutBaristas.delegateOrder(cola));
    }

    /**
     * Проверка работы пиццерии с пустыми списками персонала.
     */
    @Test
    @DisplayName("Работа с пустыми списками персонала")
    void testPizzeriaWithEmptyStaff() {
        PizzaConfig emptyConfig = new PizzaConfig();
        emptyConfig.bakers = List.of();
        emptyConfig.baristas = List.of();
        emptyConfig.couriers = List.of();
        
        assertDoesNotThrow(() -> {
            Pizzeria emptyPizzeria = new Pizzeria(emptyConfig, mockWarehouse, mockLatch);
            
            Pizza pizza = new Pizza(1);
            CocaCola cola = new CocaCola(1);
            
            emptyPizzeria.delegateOrder(pizza);
            emptyPizzeria.delegateOrder(cola);
            emptyPizzeria.stopPizzeria();
        });
    }

    /**
     * Проверка обработки прерываний при делегировании.
     */
    @Test
    @DisplayName("Обработка прерываний при делегировании")
    void testInterruptionDuringDelegation() {
        Pizza pizza = new Pizza(1);
        pizza.setOrderId(500);
        
        Thread.currentThread().interrupt();
        
        assertDoesNotThrow(() -> pizzeria.delegateOrder(pizza));
        
        assertTrue(Thread.currentThread().isInterrupted());
        Thread.interrupted();
    }

    /**
     * Проверка распределения нагрузки между пекарями.
     */
    @Test
    @DisplayName("Распределение нагрузки между пекарями")
    void testLoadBalancingBetweenBakers() {
        Pizza pizza1 = new Pizza(1);
        Pizza pizza2 = new Pizza(2);
        Pizza pizza3 = new Pizza(3);
        
        pizza1.setOrderId(601);
        pizza2.setOrderId(602);
        pizza3.setOrderId(603);
        
        assertDoesNotThrow(() -> {
            pizzeria.delegateOrder(pizza1);
            pizzeria.delegateOrder(pizza2);
            pizzeria.delegateOrder(pizza3);
        });
    }

    /**
     * Проверка распределения нагрузки между бариста.
     */
    @Test
    @DisplayName("Распределение нагрузки между бариста")
    void testLoadBalancingBetweenBaristas() {
        CocaCola cola1 = new CocaCola(1);
        CocaCola cola2 = new CocaCola(2);
        CocaCola cola3 = new CocaCola(3);
        
        cola1.setOrderId(701);
        cola2.setOrderId(702);
        cola3.setOrderId(703);
        
        assertDoesNotThrow(() -> {
            pizzeria.delegateOrder(cola1);
            pizzeria.delegateOrder(cola2);
            pizzeria.delegateOrder(cola3);
        });
    }

    /**
     * Очистка после тестов.
     */
    @AfterEach
    void tearDown() {
        if (pizzeria != null) {
            pizzeria.stopPizzeria();
        }
        reset(mockWarehouse, mockLatch);
    }
}
