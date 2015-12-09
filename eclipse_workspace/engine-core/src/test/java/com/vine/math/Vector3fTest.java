package com.vine.math;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class Vector3fTest {
    @Test
    public void testAdd() {
        Vector3f vector = new Vector3f(1.f, 2.f, 3.f);
        vector.add(new Vector3f(2, 2.f, 2.f));
        assertTrue(vector.equalTo(new Vector3f(3, 4, 5)));
        vector.add(-5.f, 1.f, -5.f);
        assertTrue(vector.equalTo(new Vector3f(-2, 5, 0)));
        vector.add(null);
        assertTrue(vector.equalTo(new Vector3f(-2, 5, 0)));
        assertTrue(!vector.equalTo(null));
        assertTrue(!vector.equalTo(new Vector3f(-1, 5, 0)));
        assertTrue(!vector.equalTo(new Vector3f(-1, 6, 0)));
        assertTrue(!vector.equalTo(new Vector3f(-2, 6, 0)));
        assertTrue(!vector.equalTo(new Vector3f(-2, 6, -2)));
        assertTrue(!vector.equalTo(new Vector3f(-2, 6, 11)));
        assertTrue(!vector.equalTo(new Vector3f(-2, 5, 5)));
    }

    @Test
    public void testDotProduct() {
        Vector3f vector = new Vector3f(1, 0, 0);
        assertTrue(vector.dot(vector) == 1.f);
        assertTrue(vector.dot(null) == 0);
    }

    @Test
    public void testCrossProduct() {
        Vector3f vector = new Vector3f(1, 0, 0);
        Vector3f vector2 = new Vector3f(0, 1, 0);
        assertTrue(vector.cross(vector2).equalTo(new Vector3f(0, 0, 1)));

        assertTrue(vector.cross(null).equalTo(new Vector3f(0, 0, 0)));
    }

    @Test
    public void testGetElement() {
        Vector3f vector = new Vector3f(1, 2.f, 5);
        assertTrue(vector.getY() == 2.f);
        assertTrue(vector.getZ() == 5.f);
        assertTrue(vector.getX() == 1.f);
    }

    @Test
    public void testScaling() {
        Vector3f vector = new Vector3f(1, 2, 1);
        vector.scale(0);
        assertTrue(vector.equalTo(new Vector3f(0, 0, 0)));
    }

    @Test
    public void testAngleBetween() {
        Vector3f vector = new Vector3f(3.f, 4.f, 0);
        Vector3f vector2 = new Vector3f(-8.f, 6.f, 0);
        assertTrue(Math.toDegrees(Math.acos(vector.getAngle(vector2))) == 90.f);
        assertTrue(vector.getAngle(null) == 13);
    }

    @Test
    public void testLength() {
        Vector3f vector = new Vector3f(0.f, 3.f, 0.f);
        assertTrue(vector.length() == 3.f);
    }
}
