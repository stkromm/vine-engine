package com.vine.math;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class VectorTest {
    @Test
    public void testAdd() {
        Vector2f vector = new Vector2f(1, 2);
        vector.add(new Vector2f(2, 2));
        assertTrue(vector.equalTo(new Vector2f(3, 4)));
        vector.add(-5.f, 1.f);
        assertTrue(vector.equalTo(new Vector2f(-2, 5)));
    }

    @Test
    public void testDotProduct() {

    }

    @Test
    public void testCrossProduct() {

    }

    @Test
    public void testGetElement() {

    }

    @Test
    public void testScaling() {

    }

    @Test
    public void testAngleBetween() {

    }

    @Test
    public void testLength() {

    }
}