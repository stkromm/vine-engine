package com.vine.math;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MatrixTest {

    /**
     * A correctly transposed , orthogonal Matrix multiplied with the not
     * transposed Matrix creates the identity matrix.
     */
    @Test
    public void testOrthogonalInversed() {
        Matrix3f matrix = new Matrix3f(0.f);
        matrix.setRow(0, 1.f, 0.f, 0.f);
        matrix.setRow(1, 0.f, 0.f, 1.f);
        matrix.setRow(2, 0.f, 1.f, 0.f);
        Matrix3f transposed = new Matrix3f(matrix);
        transposed.transpose();
        transposed.multiply(matrix);
        matrix.makeIdentity();
        assertTrue(matrix.equalTo(transposed));
    }

    /**
     * Tests transpose of Matrix3f.
     */
    @Test
    public void testTranspose() {
        Matrix3f matrix = new Matrix3f(0.f);
        matrix.setRow(0, 1.f, 1.f, 1.f);
        matrix.setRow(1, 6.f, 5.f, 4.f);
        matrix.setRow(2, 1.f, 1.f, 0.f);
        Matrix3f transposedMatrix = new Matrix3f(0.f);
        transposedMatrix.setRow(0, 1.f, 6.f, 1.f);
        transposedMatrix.setRow(1, 1.f, 5.f, 1.f);
        transposedMatrix.setRow(2, 1.f, 4.f, 0.f);
        matrix.transpose();
        assertTrue(matrix.equalTo(transposedMatrix));
    }

    /**
     * Tests, that the calculation of the determinant of given matrices is
     * correct.
     */
    @Test
    public void testCalculateDeterminant() {
        Matrix3f matrix = new Matrix3f(0.f);
        matrix.setRow(0, 1.f, 1.f, 1.f);
        matrix.setRow(1, 6.f, 5.f, 4.f);
        matrix.setRow(2, 1.f, 1.f, 0.f);
        assertTrue(matrix.determinant() == 1.f);
    }

    /**
     * Tests, that Matrix3f invert is correct.
     */
    @Test
    public void testInvert() {
        Matrix3f matrix = new Matrix3f(0.f);
        matrix.setRow(0, 1.f, 1.f, 1.f);
        matrix.setRow(1, 6.f, 5.f, 4.f);
        matrix.setRow(2, 1.f, 1.f, 0.f);
        Matrix3f invertedMatrix = new Matrix3f(0.f);
        invertedMatrix.setRow(0, 1.f, 1.f, 1.f);
        invertedMatrix.setRow(1, 6.f, 5.f, 4.f);
        invertedMatrix.setRow(2, 1.f, 1.f, 0.f);
        invertedMatrix.inverse();
        invertedMatrix.multiply(matrix);
        matrix.makeIdentity();
        assertTrue(matrix.equalTo(invertedMatrix));
    }
}