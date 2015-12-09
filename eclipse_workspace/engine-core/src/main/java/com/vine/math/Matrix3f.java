package com.vine.math;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a 3x3 Matrix, with floating point values as elements.
 * 
 * @author Steffen
 *
 */
public final class Matrix3f implements MatrixOperations<Matrix3f> {
    /**
     * Maximum difference two floating point values can differ and still count
     * as equal.
     */
    protected static final float EPSILON = 0.000000000000000000000001f;

    private float a11;
    private float a12;
    private float a13;
    private float a21;
    private float a22;
    private float a23;
    private float a31;
    private float a32;
    private float a33;
    /**
     * Quadratic size of Matrix3f. 3...
     */
    protected static final int SIZE = 3;

    /**
     * Creates a new matrix identical to the given matrix.
     * 
     * @param matrix
     *            The matrix to copy.
     */
    public Matrix3f(final Matrix3f matrix) {

    }

    /**
     * Creates a matrix filled with the given value.
     */
    public Matrix3f(final float value) {

    }

    public float getA11() {
        return a11;
    }

    public float getA12() {
        return a12;
    }

    public float getA13() {
        return a13;
    }

    public float getA21() {
        return a21;
    }

    public float getA22() {
        return a22;
    }

    public float getA23() {
        return a23;
    }

    public float getA31() {
        return a31;
    }

    public float getA32() {
        return a32;
    }

    public float getA33() {
        return a33;
    }

    public void setA11(float a11) {
        this.a11 = a11;
    }

    public void setA12(float a12) {
        this.a12 = a12;
    }

    public void setA13(float a13) {
        this.a13 = a13;
    }

    public void setA21(float a21) {
        this.a21 = a21;
    }

    public void setA22(float a22) {
        this.a22 = a22;
    }

    public void setA23(float a23) {
        this.a23 = a23;
    }

    public void setA31(float a31) {
        this.a31 = a31;
    }

    public void setA32(float a32) {
        this.a32 = a32;
    }

    public void setA33(float a33) {
        this.a33 = a33;
    }

    /**
     * Makes this matrix the unit matrix.
     */
    public static final Matrix3f getIdentity() {
        final Matrix3f identity = new Matrix3f(0.f);
        identity.setA11(1);
        identity.setA22(1);
        identity.setA33(1);
        return identity;
    }

    /**
     * Sets the elements of the row that responds to the given index.
     * 
     * @param rowIndex
     *            Valid values are in [0,2].
     */
    public final void setRow(final int rowIndex, final float e1, final float e2, final float e3) {
        if (rowIndex == 0) {
            a11 = e1;
            a12 = e2;
            a13 = e3;
        } else if (rowIndex == 1) {
            a21 = e1;
            a22 = e2;
            a23 = e3;
        } else if (rowIndex == 2) {
            a31 = e1;
            a32 = e2;
            a33 = e3;
        } else {
            Logger.getGlobal().log(Level.WARNING, "Tried to set row of Matrix3f with invalid index:" + rowIndex);
        }
    }

    /**
     * Calculates the determinant of this matrix and returns it.
     */
    @Override
    public final strictfp float determinant() {
        return a11 * a22 * a33 + a12 * a23 * a31 + a13 * a21 * a32 - a12 * a21 * a33 - a13 * a22 * a13
                - a11 * a23 * a32;
    }

    /**
     * Transposes this matrix.
     */
    @Override
    public final void transpose() {
        float temp = a21;
        a21 = a12;
        a12 = temp;
        temp = a31;
        a31 = a13;
        a13 = temp;
        temp = a32;
        a32 = a23;
        a23 = temp;
    }

    /**
     * Transforms this matrix into its inversed matrix. Does nothin, if the
     * matrix is not invertable.
     */
    @Override
    public final strictfp void inverse() {
        final float[][] tempMatrix = new float[3][3];
        final float inversedDet = determinant();
        if (Math.abs(inversedDet) <= EPSILON) {
            return;
        }
        // Calculate elements of the inverse 3x3 matrix with the inversed
        // determinant defactored.

        tempMatrix[0][0] = a22 * a33 - a32 * a23;
        tempMatrix[0][1] = a32 * a13 - a33 * a12;
        tempMatrix[0][2] = a12 * a23 - a13 * a22;
        tempMatrix[1][0] = a23 * a31 - a21 * a33;
        tempMatrix[1][1] = a11 * a33 - a13 * a31;
        tempMatrix[1][2] = a13 * a21 - a23 * a11;
        tempMatrix[2][0] = a21 * a32 - a31 * a22;
        tempMatrix[2][1] = a12 * a31 - a11 * a32;
        tempMatrix[2][2] = a11 * a22 - a12 * a21;
        // assign calculated matrix elements to the corresponding elements of
        // this matrix.

        a11 = tempMatrix[0][0];
        a12 = tempMatrix[0][1];
        a13 = tempMatrix[0][2];
        a21 = tempMatrix[1][0];
        a22 = tempMatrix[1][1];
        a23 = tempMatrix[1][2];
        a31 = tempMatrix[2][0];
        a32 = tempMatrix[2][1];
        a33 = tempMatrix[2][2];
        // Multiply with inversed determinant of this matrix
        scale(1 / inversedDet);
    }

    /**
     * Multiplies the given matrix from the right with this Matrix3f.
     */
    @Override
    public final void rightMultiply(Matrix3f matrix) {
        if (matrix == null) {
            Logger.getGlobal().log(Level.WARNING, "Tried to multiply matrix with null.");
            return;
        }
        final float temp1 = a11 * matrix.getA11() + a12 * matrix.getA21() + a13 * matrix.getA31();
        final float temp2 = a11 * matrix.getA12() + a12 * matrix.getA22() + a13 * matrix.getA32();
        final float temp3 = a11 * matrix.getA13() + a12 * matrix.getA23() + a13 * matrix.getA33();
        final float temp4 = a21 * matrix.getA11() + a22 * matrix.getA21() + a23 * matrix.getA31();
        final float temp5 = a21 * matrix.getA12() + a22 * matrix.getA22() + a23 * matrix.getA32();
        final float temp6 = a21 * matrix.getA13() + a22 * matrix.getA23() + a23 * matrix.getA33();
        final float temp7 = a31 * matrix.getA11() + a32 * matrix.getA21() + a33 * matrix.getA31();
        final float temp8 = a31 * matrix.getA12() + a32 * matrix.getA22() + a33 * matrix.getA32();
        final float temp9 = a31 * matrix.getA13() + a32 * matrix.getA23() + a33 * matrix.getA33();
        // assign calculated matrix elements to the corresponding elements of
        // this matrix.
        a11 = temp1;
        a12 = temp2;
        a13 = temp3;
        a21 = temp4;
        a22 = temp5;
        a23 = temp6;
        a31 = temp7;
        a32 = temp8;
        a33 = temp9;
    }

    /**
     * Multiplies every element of the matrix with the given scale value.
     */
    @Override
    public final void scale(float scale) {
        a11 *= scale;
        a12 *= scale;
        a13 *= scale;
        //
        a21 *= scale;
        a22 *= scale;
        a23 *= scale;
        //
        a31 *= scale;
        a32 *= scale;
        a33 *= scale;
    }

    /**
     * Adds the entries of the given matrix to the elements of this matrix.
     */
    @Override
    public final void add(Matrix3f matrix) {
        if (matrix == null) {
            return;
        }
        a11 += matrix.getA11();
        a12 += matrix.getA12();
        a13 += matrix.getA13();
        //
        a21 += matrix.getA21();
        a22 += matrix.getA22();
        a23 += matrix.getA23();
        //
        a31 += matrix.getA31();
        a32 += matrix.getA32();
        a33 += matrix.getA33();
    }

    /**
     * Returns true, if every element of this matrix is equal to the equivalent
     * element of the given matrix.
     */
    @Override
    public final boolean isEqualTo(Matrix3f matrix) {
        if (matrix == null) {
            Logger.getGlobal().log(Level.WARNING, "Tried to compare matrix with null.");
            return false;
        }
        final boolean isFirstRowCorrect = Math
                .abs(a11 - matrix.getA11() + a12 - matrix.getA12() + a13 - matrix.getA13()) <= 3 * EPSILON;
        final boolean isSecondRowCorrect = Math
                .abs(a21 - matrix.getA21() + a22 - matrix.getA22() + a23 - matrix.getA23()) <= 3 * EPSILON;
        final boolean isThirdRowCorrect = Math
                .abs(a31 - matrix.getA31() + a32 - matrix.getA32() + a33 - matrix.getA33()) <= 3 * EPSILON;
        return isFirstRowCorrect && isSecondRowCorrect && isThirdRowCorrect;
    }
}
