package ru.nsu.ermakov.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the Point record.
 * Point represents coordinates in the game field.
 */
class PointTest {

    /**
     * Tests that Point correctly stores x and y coordinates.
     */
    @Test
    @DisplayName("Point should store x and y coordinates correctly")
    void testPointCreation() {
        Point point = new Point(5, 10);
        Assertions.assertEquals(5, point.x(), "X coordinate should be 5");
        Assertions.assertEquals(10, point.y(), "Y coordinate should be 10");
    }

    /**
     * Tests Point with zero coordinates.
     */
    @Test
    @DisplayName("Point should handle zero coordinates")
    void testPointWithZeroCoordinates() {
        Point point = new Point(0, 0);
        Assertions.assertEquals(0, point.x(), "X coordinate should be 0");
        Assertions.assertEquals(0, point.y(), "Y coordinate should be 0");
    }

    /**
     * Tests Point with negative coordinates.
     */
    @Test
    @DisplayName("Point should handle negative coordinates")
    void testPointWithNegativeCoordinates() {
        Point point = new Point(-5, -10);
        Assertions.assertEquals(-5, point.x(), "X coordinate should be -5");
        Assertions.assertEquals(-10, point.y(), "Y coordinate should be -10");
    }

    /**
     * Tests Point equality - two points with same coordinates should be equal.
     */
    @Test
    @DisplayName("Points with same coordinates should be equal")
    void testPointEquality() {
        Point point1 = new Point(3, 4);
        Point point2 = new Point(3, 4);
        Assertions.assertEquals(point1, point2, "Points with same coordinates should be equal");
        Assertions.assertEquals(point1.hashCode(), point2.hashCode(), "Hash codes should match for equal points");
    }

    /**
     * Tests that Points with different coordinates are not equal.
     */
    @Test
    @DisplayName("Points with different coordinates should not be equal")
    void testPointInequality() {
        Point point1 = new Point(3, 4);
        Point point2 = new Point(4, 3);
        Assertions.assertNotEquals(point1, point2, "Points with different coordinates should not be equal");
    }

    /**
     * Tests Point toString method.
     */
    @Test
    @DisplayName("Point toString should contain coordinates")
    void testPointToString() {
        Point point = new Point(5, 10);
        String str = point.toString();
        Assertions.assertTrue(str.contains("5"), "String representation should contain x coordinate");
        Assertions.assertTrue(str.contains("10"), "String representation should contain y coordinate");
    }
}
