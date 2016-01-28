package vine.math;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import vine.math.Vector2f;

import java.util.Random;

/**
 * @author Steffen
 *
 */
public class Vector2fTest {
    /**
     * 
     */
    @Test
    public void testAdd() {
        Vector2f vector = new Vector2f(1, 2);
        vector.add(new Vector2f(2, 2));
        assertTrue(vector.equals(new Vector2f(3, 4)));
        vector.add(-5.f, 1.f);
        assertTrue(vector.equals(new Vector2f(-2, 5)));
        vector.add(null);
        assertTrue(vector.equals(new Vector2f(-2, 5)));
        assertTrue(!vector.equals(null));
        assertTrue(!vector.equals(new Vector2f(-1, 5)));
        assertTrue(!vector.equals(new Vector2f(-1, 6)));
        assertTrue(!vector.equals(new Vector2f(-2, 6)));
    }

    /**
     * 
     */
    @Test
    public void testNormalize() {
        Vector2f vector = new Vector2f(3, 0);
        vector.normalize();
        assertTrue(Math.abs(Math.abs(vector.getX()) - 1.f) <= Vector2f.EPSILON);
        assertTrue(Math.abs(vector.getY()) <= Vector2f.EPSILON);
        assertTrue(Math.abs(Math.abs(vector.length()) - 1) <= Vector2f.EPSILON);
        Vector2f nullVector = new Vector2f(0, 0);
        nullVector.normalize();
        assertTrue(nullVector.length() == 0);
        Random rn = new Random();
        for (int i = 0; i <= 100000; i++) {
            Vector2f vec = new Vector2f(rn.nextFloat() * (float) System.currentTimeMillis() - 12345,
                    (float) System.nanoTime());
            vec.normalize();
            assertTrue(Math.abs(vec.length() - 1) <= Vector2f.EPSILON);
        }
    }

    /**
     * 
     */
    @Test
    public void testPropertyAccess() {
        Vector2f vector = new Vector2f(0, 0);
        vector.setX(1);
        assertTrue(vector.getX() == 1);
        vector.setY(1);
        assertTrue(vector.getY() == 1);
    }

    /**
     * 
     */
    @Test
    public void testDotProduct() {
        Vector2f vector = new Vector2f(1, 0);
        assertTrue(vector.dot(vector) == 1.f);
        assertTrue(vector.dot(null) == 0);
    }

    /**
     * 
     */
    @Test
    public void testGetPerpendicular() {
        Vector2f vector = new Vector2f(1, 0);
        assertTrue(vector.getPerpendicular().equals(new Vector2f(0, 1)));
    }

    /**
     * 
     */
    @Test
    public void testGetElement() {
        Vector2f vector = new Vector2f(1, 2.f);
        assertTrue(vector.getY() == 2.f);
        assertTrue(vector.getX() == 1.f);
    }

    /**
     * 
     */
    @Test
    public void testScaling() {
        Vector2f vector = new Vector2f(1, 2);
        vector.scale(0);
        assertTrue(vector.equals(new Vector2f(0, 0)));
    }

    /**
     * 
     */
    @Test
    public void testAngleBetween() {
        Vector2f vector = new Vector2f(3.f, 4.f);
        Vector2f vector2 = new Vector2f(-8.f, 6.f);
        assertTrue(Math.toDegrees(Math.acos(vector.getAngle(vector2))) == 90.f);
        assertTrue(vector.getAngle(null) == 0);
        vector2 = new Vector2f(0, 0);
        assertTrue(vector.getAngle(vector2) == 0);
        vector = new Vector2f(0, 0);
        assertTrue(vector2.getAngle(vector) == 0);
    }

    /**
     * 
     */
    @Test
    public void testLength() {
        Vector2f vector = new Vector2f(0.f, 3.f);
        assertTrue(vector.length() == 3.f);
    }
}