package vine.math;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import vine.math.Vector3f;

import java.util.Random;

/**
 * @author Steffen
 *
 */
public class Vector3fTest
{
    /**
     * 
     */
    @Test
    public void testAdd()
    {
        Vector3f vector = new Vector3f(1.f, 2.f, 3.f);
        vector.add(new Vector3f(2, 2.f, 2.f));
        assertTrue(vector.equals(new Vector3f(3, 4, 5)));
        vector.add(-5.f, 1.f, -5.f);
        assertTrue(vector.equals(new Vector3f(-2, 5, 0)));
        vector.add(null);
        assertTrue(vector.equals(new Vector3f(-2, 5, 0)));
        assertTrue(!vector.equals(null));
        assertTrue(!vector.equals(new Vector3f(-1, 5, 0)));
        assertTrue(!vector.equals(new Vector3f(-1, 6, 0)));
        assertTrue(!vector.equals(new Vector3f(-2, 6, 0)));
        assertTrue(!vector.equals(new Vector3f(-2, 6, -2)));
        assertTrue(!vector.equals(new Vector3f(-2, 6, 11)));
        assertTrue(!vector.equals(new Vector3f(-2, 5, 5)));
    }

    /**
     * 
     */
    @Test
    public void testPropertyAccess()
    {
        Vector3f vector = new Vector3f(0, 0, 0);
        assertTrue(vector.getX() == 0);
        vector.setX(1);
        assertTrue(vector.getX() == 1);
        vector.setY(1);
        assertTrue(vector.getY() == 1);
        vector.setZ(5);
        assertTrue(vector.getZ() == 5);
    }

    /**
     * 
     */
    @Test
    public void testNormalize()
    {
        Vector3f vector = new Vector3f(2, 2, 1);
        vector.normalize();
        System.out.println(Math.abs(vector.getX()));
        assertTrue(Math.abs(Math.abs(vector.getX()) - 2.f / 3.f) <= Vector3f.EPSILON);
        assertTrue(Math.abs(Math.abs(vector.getY()) - 2.f / 3.f) <= Vector3f.EPSILON);
        assertTrue(Math.abs(Math.abs(vector.getZ()) - 1.f / 3.f) <= Vector3f.EPSILON);
        System.out.println(Math.abs(vector.length()));
        assertTrue(Math.abs(Math.abs(vector.length()) - 1) <= Vector3f.EPSILON);
        Vector3f nullVector = new Vector3f(0, 0, 0);
        nullVector.normalize();
        assertTrue(nullVector.length() == 0);
        Random rn = new Random();
        for (int i = 0; i <= 100000; i++)
        {
            Vector3f vec = new Vector3f(rn.nextFloat() * 10000, rn.nextFloat() * System.currentTimeMillis() - 12345,
                    System.nanoTime());
            vec.normalize();
            assertTrue(Math.abs(Math.abs(vec.length()) - 1) <= Vector3f.EPSILON);
        }
    }

    /**
     * 
     */
    @Test
    public void testDotProduct()
    {
        Vector3f vector = new Vector3f(1, 0, 0);
        assertTrue(vector.dot(vector) == 1.f);
        assertTrue(vector.dot(null) == 0);
    }

    /**
     * 
     */
    @Test
    public void testCrossProduct()
    {
        Vector3f vector = new Vector3f(1, 0, 0);
        Vector3f vector2 = new Vector3f(0, 1, 0);
        assertTrue(vector.cross(vector2).equals(new Vector3f(0, 0, 1)));

        assertTrue(vector.cross(null).equals(new Vector3f(0, 0, 0)));
    }

    /**
     * 
     */
    @Test
    public void testGetElement()
    {
        Vector3f vector = new Vector3f(1, 2.f, 5);
        assertTrue(vector.getY() == 2.f);
        assertTrue(vector.getZ() == 5.f);
        assertTrue(vector.getX() == 1.f);
    }

    /**
     * 
     */
    @Test
    public void testScaling()
    {
        Vector3f vector = new Vector3f(1, 2, 1);
        vector.scale(0);
        assertTrue(vector.equals(new Vector3f(0, 0, 0)));
    }

    /**
     * 
     */
    @Test
    public void testAngleBetween()
    {
        Vector3f vector = new Vector3f(3.f, 4.f, 0);
        Vector3f vector2 = new Vector3f(-8.f, 6.f, 0);
        assertTrue(Math.toDegrees(Math.acos(vector.getAngle(vector2))) == 90.f);
        assertTrue(vector.getAngle(null) == 0);
        vector2 = new Vector3f(0, 0, 0);
        assertTrue(vector.getAngle(vector2) == 0);
        vector = new Vector3f(0, 0, 0);
        assertTrue(vector2.getAngle(vector) == 0);
    }

    /**
     * 
     */
    @Test
    public void testLength()
    {
        Vector3f vector = new Vector3f(0.f, 3.f, 0.f);
        assertTrue(vector.length() == 3.f);
    }
}
