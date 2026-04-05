package ru.nsu.ermakov.pizzeria;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.nsu.ermakov.configs.PizzaConfig;
import ru.nsu.ermakov.products.Burger;
import ru.nsu.ermakov.products.CocaCola;
import ru.nsu.ermakov.products.Food;
import ru.nsu.ermakov.products.Pizza;
import ru.nsu.ermakov.products.Product;
import ru.nsu.ermakov.warehouse.Warehouse;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for Pizzeria functionality.
 * Tests order delegation, staff management, and pizzeria shutdown.
 */
class PizzeriaTest {
    @Mock
    private Warehouse warehouse;
    
    private PizzaConfig config;
    private CountDownLatch latch;
    private Pizzeria pizzeria;

    /**
     * Sets up test environment before each test.
     * Creates mock configuration and initializes pizzeria.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        config = new PizzaConfig();
        config.bakers = new java.util.ArrayList<>();
        config.couriers = new java.util.ArrayList<>();
        config.baristas = new java.util.ArrayList<>();
        
        PizzaConfig.BakerData baker1 = new PizzaConfig.BakerData();
        baker1.name = "Baker1";
        config.bakers.add(baker1);
        
        PizzaConfig.BakerData baker2 = new PizzaConfig.BakerData();
        baker2.name = "Baker2";
        config.bakers.add(baker2);
        
        PizzaConfig.CourierData courier1 = new PizzaConfig.CourierData();
        courier1.boxSize = 5;
        config.couriers.add(courier1);
        
        PizzaConfig.CourierData courier2 = new PizzaConfig.CourierData();
        courier2.boxSize = 3;
        config.couriers.add(courier2);
        
        PizzaConfig.BaristaData barista1 = new PizzaConfig.BaristaData();
        barista1.name = "Barista1";
        config.baristas.add(barista1);
        
        PizzaConfig.BaristaData barista2 = new PizzaConfig.BaristaData();
        barista2.name = "Barista2";
        config.baristas.add(barista2);
        
        latch = new CountDownLatch(10);
        pizzeria = new Pizzeria(config, warehouse, latch);
    }

    /**
     * Tests pizzeria constructor with valid configuration.
     * Verifies that all staff members are created and threads are started.
     */
    @Test
    void testConstructor() {
        assertNotNull(pizzeria);
        assertEquals(2, pizzeria.bakers.size());
        assertEquals(2, pizzeria.couriers.size());
        assertEquals(2, pizzeria.baristas.size());
        assertEquals(6, pizzeria.threads.size());
        
        assertEquals("Baker1", pizzeria.bakers.get(0).getName());
        assertEquals("Baker2", pizzeria.bakers.get(1).getName());
        assertEquals(5, pizzeria.couriers.get(0).getBoxSize());
        assertEquals(3, pizzeria.couriers.get(1).getBoxSize());
        assertEquals("Barista1", pizzeria.baristas.get(0).getName());
        assertEquals("Barista2", pizzeria.baristas.get(1).getName());
    }

    /**
     * Tests order delegation when no bakers are available.
     * Verifies appropriate error handling and message output.
     */
    @Test
    void testDelegateOrderNoBakers() {
        PizzaConfig emptyConfig = new PizzaConfig();
        emptyConfig.bakers = new java.util.ArrayList<>();
        emptyConfig.couriers = new java.util.ArrayList<>();
        emptyConfig.baristas = new java.util.ArrayList<>();
        
        Pizzeria emptyPizzeria = new Pizzeria(emptyConfig, warehouse, latch);
        
        Pizza pizza = new Pizza(1);
        pizza.setOrderId(100);
        
        pizzeria.delegateOrder(pizza);
    }

    /**
     * Tests order delegation when no baristas are available.
     * Verifies appropriate error handling and message output.
     */
    @Test
    void testDelegateOrderNoBaristas() {
        PizzaConfig emptyConfig = new PizzaConfig();
        emptyConfig.bakers = new java.util.ArrayList<>();
        emptyConfig.couriers = new java.util.ArrayList<>();
        emptyConfig.baristas = new java.util.ArrayList<>();
        
        Pizzeria emptyPizzeria = new Pizzeria(emptyConfig, warehouse, latch);
        
        CocaCola cola = new CocaCola(1);
        cola.setOrderId(200);
        
        pizzeria.delegateOrder(cola);
    }

    /**
     * Tests order delegation with unknown product type.
     * Verifies appropriate error handling for unsupported product types.
     */
    @Test
    void testDelegateOrderUnknownProduct() {
        Product unknownProduct = new Product() {
            @Override
            public int getSize() { return 1; }
            
            @Override
            public int getId() { return 999; }
            
            @Override
            public int getOrderId() { return 999; }
            
            @Override
            public void setOrderId(int orderId) { }
            
            @Override
            public Product clone() { return null; }
        };
        
        pizzeria.delegateOrder(unknownProduct);
    }

    /**
     * Tests order delegation with interruption during order assignment.
     * Verifies that thread interruption is properly handled.
     */
    @Test
    void testDelegateOrderWithInterruption() {
        Thread.currentThread().interrupt();
        
        Pizza pizza = new Pizza(1);
        pizza.setOrderId(100);
        
        pizzeria.delegateOrder(pizza);
        
        assertTrue(Thread.currentThread().isInterrupted());
        Thread.currentThread().interrupt();
    }


    /**
     * Tests pizzeria with empty configuration.
     * Verifies that pizzeria can be created with no staff members.
     */
    @Test
    void testEmptyConfiguration() {
        PizzaConfig emptyConfig = new PizzaConfig();
        emptyConfig.bakers = new java.util.ArrayList<>();
        emptyConfig.couriers = new java.util.ArrayList<>();
        emptyConfig.baristas = new java.util.ArrayList<>();
        
        CountDownLatch emptyLatch = new CountDownLatch(1);
        Pizzeria emptyPizzeria = new Pizzeria(emptyConfig, warehouse, emptyLatch);
        
        assertTrue(emptyPizzeria.bakers.isEmpty());
        assertTrue(emptyPizzeria.couriers.isEmpty());
        assertTrue(emptyPizzeria.baristas.isEmpty());
        assertTrue(emptyPizzeria.threads.isEmpty());
    }

    /**
     * Tests pizzeria with single staff member of each type.
     * Verifies proper functionality with minimal staff configuration.
     */
    @Test
    void testSingleStaffConfiguration() {
        PizzaConfig singleConfig = new PizzaConfig();
        singleConfig.bakers = new java.util.ArrayList<>();
        singleConfig.couriers = new java.util.ArrayList<>();
        singleConfig.baristas = new java.util.ArrayList<>();
        
        PizzaConfig.BakerData baker = new PizzaConfig.BakerData();
        baker.name = "SingleBaker";
        singleConfig.bakers.add(baker);
        
        PizzaConfig.CourierData courier = new PizzaConfig.CourierData();
        courier.boxSize = 10;
        singleConfig.couriers.add(courier);
        
        PizzaConfig.BaristaData barista = new PizzaConfig.BaristaData();
        barista.name = "SingleBarista";
        singleConfig.baristas.add(barista);
        
        CountDownLatch singleLatch = new CountDownLatch(3);
        Pizzeria singlePizzeria = new Pizzeria(singleConfig, warehouse, singleLatch);
        
        assertEquals(1, singlePizzeria.bakers.size());
        assertEquals(1, singlePizzeria.couriers.size());
        assertEquals(1, singlePizzeria.baristas.size());
        assertEquals(3, singlePizzeria.threads.size());
        
        Pizza pizza = new Pizza(1);
        pizza.setOrderId(100);
        singlePizzeria.delegateOrder(pizza);
        
        assertEquals(1, singlePizzeria.bakers.get(0).getOrderSize());
        
        CocaCola cola = new CocaCola(1);
        cola.setOrderId(200);
        singlePizzeria.delegateOrder(cola);
        
        assertEquals(1, singlePizzeria.baristas.get(0).getOrderSize());
        
        singlePizzeria.stopPizzeria();
    }
}
