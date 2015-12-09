package com.vine.math;

public class Vector3f implements VectorOperations<Vector3f> {
    /**
     * Maximum difference two floating point values can differ and still count
     * as equal.
     */
    protected static final float EPSILON = 0.000000000000000000000001f;
    private float x;
    private float y;
    private float z;

    /**
     * Creates a new Vector3f with the given x,y and z entries.
     */
    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    @Override
    public boolean equalTo(final Vector3f vector) {
        if (vector == null) {
            return false;
        }
        return Math.abs(vector.getX() - x + vector.getY() - y + vector.getZ() - z) <= 3 * EPSILON;
    }

    /**
     * Adds the given values to the corresponding elements of this Vector3f.
     */
    public void add(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
    }

    @Override
    public void add(Vector3f vector) {
        if (vector == null) {
            return;
        }
        x += vector.getX();
        y += vector.getY();
        z += vector.getZ();
    }

    @Override
    public strictfp double dot(Vector3f vector) {
        if (vector == null) {
            return 0;
        }
        return Math.sqrt(vector.getX() * x + vector.getY() * y + z * vector.getZ());
    }

    @Override
    public void scale(float factor) {
        x *= factor;
        y *= factor;
        z *= factor;

    }

    @Override
    public double length() {
        return dot(this);
    }

    @Override
    public double getAngle(Vector3f vector) {
        return this.dot(vector) / (this.length() * vector.length());
    }

    /**
     * Calculates the cross product of this and the given vector.
     */
    public Vector3f cross(Vector3f vector) {
        if (vector == null) {
            return new Vector3f(0, 0, 0);
        }
        return new Vector3f(y * vector.getZ() - z * vector.getY(), z * vector.getX() - x * vector.getZ(),
                x * vector.getY() - y * vector.getX());
    }

    @Override
    public void normalize() {
        scale((float) length());
    }

}
