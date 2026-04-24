package ru.nsu.ermakov.products;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductTest {

    @Test
    void testPizza() {
        Pizza pizza = new Pizza(1);
        
        assertEquals(1, pizza.getId());
        assertEquals(2, pizza.getSize());
        assertEquals(2000, pizza.getCookingTime());
        assertEquals(0, pizza.getOrderId());
        
        pizza.setOrderId(10);
        assertEquals(10, pizza.getOrderId());
        
        Product clonedPizza = pizza.clone();
        assertNotSame(pizza, clonedPizza);
        assertEquals(pizza.getId(), clonedPizza.getId());
        assertEquals(pizza.getSize(), clonedPizza.getSize());
        assertEquals(pizza.getCookingTime(), ((Pizza) clonedPizza).getCookingTime());
        assertEquals(0, clonedPizza.getOrderId());
    }

    @Test
    void testPizzaWithJsonConstructor() {
        Pizza pizza = new Pizza(5);
        
        assertEquals(5, pizza.getId());
        assertEquals(2, pizza.getSize());
        assertEquals(2000, pizza.getCookingTime());
    }

    @Test
    void testBurger() {
        Burger burger = new Burger(2);
        
        assertEquals(2, burger.getId());
        assertEquals(1, burger.getSize());
        assertEquals(1000, burger.getCookingTime());
        assertEquals(0, burger.getOrderId());
        
        burger.setOrderId(20);
        assertEquals(20, burger.getOrderId());
        
        Product clonedBurger = burger.clone();
        assertNotSame(burger, clonedBurger);
        assertEquals(burger.getId(), clonedBurger.getId());
        assertEquals(burger.getSize(), clonedBurger.getSize());
        assertEquals(burger.getCookingTime(), ((Burger) clonedBurger).getCookingTime());
        assertEquals(0, clonedBurger.getOrderId());
    }

    @Test
    void testBurgerWithJsonConstructor() {
        Burger burger = new Burger(10);
        
        assertEquals(10, burger.getId());
        assertEquals(1, burger.getSize());
        assertEquals(1000, burger.getCookingTime());
    }

    @Test
    void testCocaCola() {
        CocaCola cola = new CocaCola(3);
        
        assertEquals(3, cola.getId());
        assertEquals(1, cola.getSize());
        assertEquals(0, cola.getProcessingTime());
        assertEquals(0, cola.getOrderId());
        
        cola.setOrderId(30);
        assertEquals(30, cola.getOrderId());
        
        Product clonedCola = cola.clone();
        assertNotSame(cola, clonedCola);
        assertEquals(cola.getId(), clonedCola.getId());
        assertEquals(cola.getSize(), clonedCola.getSize());
        assertEquals(cola.getProcessingTime(), ((CocaCola) clonedCola).getProcessingTime());
        assertEquals(0, clonedCola.getOrderId());
    }

    @Test
    void testCocaColaWithJsonConstructor() {
        CocaCola cola = new CocaCola(15);
        
        assertEquals(15, cola.getId());
        assertEquals(1, cola.getSize());
        assertEquals(0, cola.getProcessingTime());
    }

    @Test
    void testProductInterfaces() {
        Pizza pizza = new Pizza(1);
        assertTrue(pizza instanceof Food);
        assertTrue(pizza instanceof Product);
        Burger burger = new Burger(2);
        assertTrue(burger instanceof Food);
        assertTrue(burger instanceof Product);
        CocaCola cola = new CocaCola(3);
        assertTrue(cola instanceof Drink);
        assertTrue(cola instanceof Product);
    }

    @Test
    void testProductEquality() {
        Pizza pizza1 = new Pizza(1);
        Pizza pizza2 = new Pizza(1);
        Pizza pizza3 = new Pizza(2);
        
        assertEquals(pizza1.getId(), pizza2.getId());
        assertNotEquals(pizza1.getId(), pizza3.getId());
        
        pizza1.setOrderId(10);
        pizza2.setOrderId(10);
        
        assertEquals(pizza1.getOrderId(), pizza2.getOrderId());
        assertEquals(pizza1.getSize(), pizza2.getSize());
        assertEquals(pizza1.getCookingTime(), pizza2.getCookingTime());
    }

    @Test
    void testProductCloneIndependence() {
        Pizza originalPizza = new Pizza(1);
        originalPizza.setOrderId(100);
        
        Product clonedPizza = originalPizza.clone();
        
        assertNotSame(originalPizza, clonedPizza);
        assertEquals(originalPizza.getId(), clonedPizza.getId());
        
        clonedPizza.setOrderId(200);
        assertEquals(100, originalPizza.getOrderId());
        assertEquals(200, clonedPizza.getOrderId());
    }

    @Test
    void testProductSizeDifferences() {
        Pizza pizza = new Pizza(1);
        Burger burger = new Burger(2);
        CocaCola cola = new CocaCola(3);
        
        assertEquals(2, pizza.getSize());
        assertEquals(1, burger.getSize());
        assertEquals(1, cola.getSize());
    }

    @Test
    void testProductTimeDifferences() {
        Pizza pizza = new Pizza(1);
        Burger burger = new Burger(2);
        CocaCola cola = new CocaCola(3);
        
        assertEquals(2000, pizza.getCookingTime());
        assertEquals(1000, burger.getCookingTime());
        assertEquals(0, cola.getProcessingTime());
    }

    @Test
    void testProductOrderIdInitialState() {
        Pizza pizza = new Pizza(1);
        Burger burger = new Burger(2);
        CocaCola cola = new CocaCola(3);
        
        assertEquals(0, pizza.getOrderId());
        assertEquals(0, burger.getOrderId());
        assertEquals(0, cola.getOrderId());
    }

    @Test
    void testProductOrderIdUpdate() {
        Pizza pizza = new Pizza(1);
        Burger burger = new Burger(2);
        CocaCola cola = new CocaCola(3);
        
        pizza.setOrderId(50);
        burger.setOrderId(60);
        cola.setOrderId(70);
        
        assertEquals(50, pizza.getOrderId());
        assertEquals(60, burger.getOrderId());
        assertEquals(70, cola.getOrderId());
    }

    @Test
    void testProductMultipleClones() {
        Pizza original = new Pizza(5);
        original.setOrderId(25);
        
        Product clone1 = original.clone();
        Product clone2 = original.clone();
        
        assertNotSame(original, clone1);
        assertNotSame(original, clone2);
        assertNotSame(clone1, clone2);
        
        assertEquals(original.getId(), clone1.getId());
        assertEquals(original.getId(), clone2.getId());
        
        clone1.setOrderId(100);
        clone2.setOrderId(200);
        
        assertEquals(25, original.getOrderId());
        assertEquals(100, clone1.getOrderId());
        assertEquals(200, clone2.getOrderId());
    }

    @Test
    void testProductConsistentClone() {
        Pizza pizza = new Pizza(10);
        Burger burger = new Burger(20);
        CocaCola cola = new CocaCola(30);
        
        Product pizzaClone = pizza.clone();
        Product burgerClone = burger.clone();
        Product colaClone = cola.clone();
        
        assertTrue(pizzaClone instanceof Pizza);
        assertTrue(burgerClone instanceof Burger);
        assertTrue(colaClone instanceof CocaCola);
        
        assertEquals(pizza.getSize(), pizzaClone.getSize());
        assertEquals(burger.getSize(), burgerClone.getSize());
        assertEquals(cola.getSize(), colaClone.getSize());
    }
}
