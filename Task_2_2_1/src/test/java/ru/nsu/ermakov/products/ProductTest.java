package ru.nsu.ermakov.products;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тестовый класс для проверки иерархии продуктов.
 * Проверяет базовый интерфейс Product и его реализацию.
 */
class ProductTest {

    /**
     * Проверка интерфейса Product через реализацию Pizza.
     */
    @Test
    @DisplayName("Проверка интерфейса Product через Pizza")
    void testProductInterfaceThroughPizza() {
        Pizza pizza = new Pizza(1);
        
        assertEquals(1, pizza.getId());
        assertEquals(2, pizza.getSize());
        assertEquals(0, pizza.getOrderId());
        
        pizza.setOrderId(100);
        assertEquals(100, pizza.getOrderId());
        
        Product cloned = pizza.clone();
        assertNotNull(cloned);
        assertInstanceOf(Pizza.class, cloned);
        assertEquals(pizza.getId(), cloned.getId());
        assertEquals(pizza.getSize(), cloned.getSize());
    }

    /**
     * Проверка интерфейса Product через реализацию Burger.
     */
    @Test
    @DisplayName("Проверка интерфейса Product через Burger")
    void testProductInterfaceThroughBurger() {
        Burger burger = new Burger(2);
        
        assertEquals(2, burger.getId());
        assertEquals(1, burger.getSize());
        assertEquals(0, burger.getOrderId());
        
        burger.setOrderId(200);
        assertEquals(200, burger.getOrderId());
        
        Product cloned = burger.clone();
        assertNotNull(cloned);
        assertInstanceOf(Burger.class, cloned);
        assertEquals(burger.getId(), cloned.getId());
        assertEquals(burger.getSize(), cloned.getSize());
    }

    /**
     * Проверка интерфейса Product через реализацию CocaCola.
     */
    @Test
    @DisplayName("Проверка интерфейса Product через CocaCola")
    void testProductInterfaceThroughCocaCola() {
        CocaCola cola = new CocaCola(3);
        
        assertEquals(3, cola.getId());
        assertEquals(1, cola.getSize());
        assertEquals(0, cola.getOrderId());
        
        cola.setOrderId(300);
        assertEquals(300, cola.getOrderId());
        
        Product cloned = cola.clone();
        assertNotNull(cloned);
        assertInstanceOf(CocaCola.class, cloned);
        assertEquals(cola.getId(), cloned.getId());
        assertEquals(cola.getSize(), cloned.getSize());
    }

    /**
     * Проверка различных продуктов на равенство по ID.
     */
    @Test
    @DisplayName("Проверка различных продуктов на равенство по ID")
    void testDifferentProductsWithSameId() {
        Pizza pizza = new Pizza(1);
        Burger burger = new Burger(1);
        CocaCola cola = new CocaCola(1);
        
        assertEquals(pizza.getId(), burger.getId());
        assertEquals(burger.getId(), cola.getId());
        
        assertNotEquals(pizza.getSize(), burger.getSize());
        assertEquals(burger.getSize(), cola.getSize());
    }

    /**
     * Проверка клонирования продуктов.
     */
    @Test
    @DisplayName("Проверка клонирования продуктов")
    void testProductCloning() {
        Pizza pizza = new Pizza(10);
        pizza.setOrderId(1000);
        
        Product clonedPizza = pizza.clone();
        assertEquals(pizza.getId(), clonedPizza.getId());
        assertEquals(pizza.getSize(), clonedPizza.getSize());
        assertNotSame(pizza, clonedPizza);
        
        Burger burger = new Burger(20);
        burger.setOrderId(2000);
        
        Product clonedBurger = burger.clone();
        assertEquals(burger.getId(), clonedBurger.getId());
        assertEquals(burger.getSize(), clonedBurger.getSize());
        assertNotSame(burger, clonedBurger);
        
        CocaCola cola = new CocaCola(30);
        cola.setOrderId(3000);
        
        Product clonedCola = cola.clone();
        assertEquals(cola.getId(), clonedCola.getId());
        assertEquals(cola.getSize(), clonedCola.getSize());
        assertNotSame(cola, clonedCola);
    }

    /**
     * Проверка полиморфного поведения продуктов.
     */
    @Test
    @DisplayName("Проверка полиморфного поведения продуктов")
    void testPolymorphicBehavior() {
        Product pizza = new Pizza(1);
        Product burger = new Burger(2);
        Product cola = new CocaCola(3);
        
        assertTrue(pizza instanceof Food);
        assertTrue(burger instanceof Food);
        assertTrue(cola instanceof Drink);
        
        assertInstanceOf(Food.class, pizza);
        assertInstanceOf(Food.class, burger);
        assertInstanceOf(Drink.class, cola);
    }

    /**
     * Проверка установки orderId для разных продуктов.
     */
    @Test
    @DisplayName("Проверка установки orderId для разных продуктов")
    void testOrderIdSetting() {
        Product[] products = {
            new Pizza(1),
            new Burger(2),
            new CocaCola(3)
        };
        
        int[] orderIds = {100, 200, 300};
        
        for (int i = 0; i < products.length; i++) {
            products[i].setOrderId(orderIds[i]);
            assertEquals(orderIds[i], products[i].getOrderId());
        }
    }

    /**
     * Проверка неизменности ID после клонирования.
     */
    @Test
    @DisplayName("Проверка неизменности ID после клонирования")
    void testIdImmutabilityAfterClone() {
        Pizza originalPizza = new Pizza(42);
        Product clonedPizza = originalPizza.clone();
        
        assertEquals(42, originalPizza.getId());
        assertEquals(42, clonedPizza.getId());
        
        clonedPizza.setOrderId(999);
        assertEquals(42, originalPizza.getId());
        assertEquals(42, clonedPizza.getId());
        assertEquals(0, originalPizza.getOrderId());
        assertEquals(999, clonedPizza.getOrderId());
    }
}
