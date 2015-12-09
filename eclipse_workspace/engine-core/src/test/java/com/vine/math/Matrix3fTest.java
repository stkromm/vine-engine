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
        Matrix3f mat = matrix;
        mat.makeIdentity();
        assertTrue(mat.equalTo(transposed));
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

    @Test
    public void testIdentity() {
        Matrix3f matrix = new Matrix3f(0.f);
        matrix.setRow(0, 2.f, 0.f, 0.f);
        matrix.setRow(1, 0.f, 2.f, 0.f);
        matrix.setRow(2, 0.f, 0.f, 2.f);
        assertTrue(matrix.equalTo(matrix));
        assertTrue(matrix.equalTo(matrix));
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
        Matrix3f mat = matrix;
        assertTrue(mat.determinant() == 1.f);
        matrix = new Matrix3f(0);
        mat = matrix;
        assertTrue(mat.determinant() == 0.f);
        Matrix3f notInvertable = new Matrix3f(matrix);
        mat.inverse();
        assertTrue(matrix.equalTo(notInvertable));
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
        Matrix3f mat = new Matrix3f(invertedMatrix);
        mat.inverse();
        mat.multiply(matrix);
        matrix.makeIdentity();
        assertTrue(matrix.equalTo(mat));
    }

    @Test
    public void testAddition() {
        Matrix3f matrix = new Matrix3f(0.f);
        matrix.setRow(0, 1.f, 1.f, 1.f);
        matrix.setRow(1, 6.f, 5.f, 4.f);
        matrix.setRow(2, 1.f, 1.f, 0.f);
        matrix.add(matrix);
        Matrix3f resultMatrix = new Matrix3f(0.f);
        resultMatrix.setRow(0, 2.f, 2.f, 2.f);
        resultMatrix.setRow(1, 12.f, 10.f, 8.f);
        resultMatrix.setRow(2, 2.f, 2.f, 0.f);
        assertTrue(matrix.equalTo(resultMatrix));
        matrix.add(null);
        assertTrue(matrix.equalTo(resultMatrix));
    }

    @Test
    public void testMultiply() {
        Matrix3f matrix = new Matrix3f(0.f);
        matrix.setRow(0, 1.f, 1.f, 1.f);
        matrix.setRow(1, 6.f, 5.f, 4.f);
        matrix.setRow(2, 1.f, 1.f, 0.f);
        matrix.multiply(matrix);

        Matrix3f resultMatrix = new Matrix3f(0.f);
        resultMatrix.setRow(0, 8.f, 7.f, 5.f);
        resultMatrix.setRow(1, 40.f, 35.f, 26.f);
        resultMatrix.setRow(2, 7.f, 6.f, 5.f);
        Matrix3f mat = matrix;
        assertTrue(mat.equalTo(resultMatrix));
        matrix = new Matrix3f(0.f);
        matrix.setRow(0, 1.f, 1.f, 1.f);
        matrix.setRow(1, 6.f, 5.f, 4.f);
        matrix.setRow(2, 1.f, 1.f, 0.f);
        resultMatrix = new Matrix3f(matrix);
        matrix.multiply(resultMatrix);
        resultMatrix = new Matrix3f(0.f);
        resultMatrix.setRow(0, 8.f, 7.f, 5.f);
        resultMatrix.setRow(1, 40.f, 35.f, 26.f);
        resultMatrix.setRow(2, 7.f, 6.f, 5.f);
        mat = matrix;
        assertTrue(mat.equalTo(resultMatrix));
        resultMatrix.multiply(null);
        assertTrue(matrix.equalTo(resultMatrix));
    }

    @Test
    public void testElementAccess() {

        Matrix3f matrix = new Matrix3f(0.f);
        matrix.setRow(0, 1.f, 1.f, 1.f);
        matrix.setRow(1, 6.f, 5.f, 4.f);
        matrix.setRow(2, 1.f, 1.f, 0.f);
        assertTrue(!matrix.equalTo(null));
        assertTrue(5 == matrix.getElement(1, 1));
        assertTrue(0 == matrix.getElement(1, 5));
        assertTrue(0 == matrix.getElement(1, -1));
        assertTrue(0 == matrix.getElement(-1, 1));
        assertTrue(0 == matrix.getElement(-1, 5));
        assertTrue(0 == matrix.getElement(-1, -1));
        assertTrue(0 == matrix.getElement(5, -1));
        assertTrue(0 == matrix.getElement(5, 5));
        assertTrue(0 == matrix.getElement(5, 1));

        Matrix3f compareMatrix = new Matrix3f(matrix);
        matrix.setRow(-1, 6.f, 5.f, 4.f);
        matrix.setRow(5, 6.f, 5.f, 4.f);
        assertTrue(matrix.equalTo(compareMatrix));
        matrix.setRow(1, 6.f, 4.f, 4.f);
        assertTrue(!matrix.equalTo(compareMatrix));
        System.out.println(matrix.toString());
        assertTrue(matrix.toString().equals("1.0,1.0,1.0,\n6.0,4.0,4.0,\n1.0,1.0,0.0,\n"));
        assertTrue(matrix.getSize() == Matrix3f.SIZE);
    }
}