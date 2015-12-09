package com.vine.math;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class Vector2fTest {
    @Test
    public void testAdd() {
        Vector2f vector = new Vector2f(1, 2);
        vector.add(new Vector2f(2, 2));
        assertTrue(vector.equalTo(new Vector2f(3, 4)));
        vector.add(-5.f, 1.f);
        assertTrue(vector.equalTo(new Vector2f(-2, 5)));
        vector.add(null);
        assertTrue(vector.equalTo(new Vector2f(-2, 5)));
        assertTrue(!vector.equalTo(null));
        assertTrue(!vector.equalTo(new Vector2f(-1, 5)));
        assertTrue(!vector.equalTo(new Vector2f(-1, 6)));
        assertTrue(!vector.equalTo(new Vector2f(-2, 6)));
    }

    @Test
    public void testDotProduct() {
        Vector2f vector = new Vector2f(1, 0);
        assertTrue(vector.dot(vector) == 1.f);
        assertTrue(vector.dot(null) == 0);
    }

    @Test
    public void testGetPerpendicular() {
        Vector2f vector = new Vector2f(1, 0);
        assertTrue(vector.getPerpendicular().equalTo(new Vector2f(0, 1)));
    }

    @Test
    public void testGetElement() {
        Vector2f vector = new Vector2f(1, 2.f);
        assertTrue(vector.getY() == 2.f);
        assertTrue(vector.getX() == 1.f);
    }

    @Test
    public void testScaling() {
        Vector2f vector = new Vector2f(1, 2);
        vector.scale(0);
        assertTrue(vector.equalTo(new Vector2f(0, 0)));
    }

    @Test
    public void testAngleBetween() {
        Vector2f vector = new Vector2f(3.f, 4.f);
        Vector2f vector2 = new Vector2f(-8.f, 6.f);
        assertTrue(vector.getAngle(vector2) == 90.f);
        assertTrue(vector.getAngle(null) == 0);
    }

    @Test
    public void testLength() {
        Vector2f vector = new Vector2f(0.f, 3.f);
        assertTrue(vector.length() == 3.f);
    }
}
