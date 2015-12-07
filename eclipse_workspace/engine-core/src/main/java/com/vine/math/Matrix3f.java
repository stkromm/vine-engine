package com.vine.math;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a 3x3 Matrix, with floating point values as elements.
 * 
 * @author Steffen
 *
 */
public final class Matrix3f extends Matrixf implements MatrixOperations<Matrix3f> {
    /**
     * Quadratic size of Matrix3f. 3...
     */
    protected static final int SIZE = 3;
    /**
     * 2 dimensional Array that represents the element of the 3x3 matrix.
     */
    private final float[][] elements = new float[getSize()][getSize()];

    /**
     * Creates a new matrix identical to the given matrix.
     * 
     * @param matrix
     *            The matrix to copy.
     */
    public Matrix3f(final Matrix3f matrix) {
        iterateMatrix((row, column) -> elements[row][column] = matrix.getElement(row, column));
    }

    /**
     * Creates a matrix filled with the given value.
     */
    public Matrix3f(final float value) {
        iterateMatrix((row, column) -> elements[row][column] = value);
    }

    @Override
    public int getSize() {
        return SIZE;
    }

    /**
     * Sets the elements of the row that responds to the given index.
     * 
     * @param rowIndex
     *            Valid values are in [0,2].
     */
    public final void setRow(final int rowIndex, final float e1, final float e2, final float e3) {
        if (rowIndex >= 0 && rowIndex < SIZE) {
            elements[rowIndex][0] = e1;
            elements[rowIndex][1] = e2;
            elements[rowIndex][2] = e3;
        } else {
            Logger.getGlobal().log(Level.WARNING, "Tried to set row of Matrix3f with invalid index:" + rowIndex);
        }
    }

    /**
     * Returns the elements at the given position.
     */
    @Override
    public final float getElement(int row, int column) {
        if (column >= 0 && column < SIZE && row >= 0 && row < SIZE) {
            return elements[row][column];
        } else {
            Logger.getGlobal().log(Level.SEVERE, "Accessed not existing element!");
            return 0;
        }
    }

    /**
     * Calculates the determinant of this matrix and returns it.
     */
    public final float determinant() {
        return elements[0][0] * elements[1][1] * elements[2][2] + elements[0][1] * elements[1][2] * elements[2][0]
                + elements[0][2] * elements[1][0] * elements[2][1] - elements[0][1] * elements[1][0] * elements[2][2]
                - elements[0][2] * elements[1][1] * elements[0][2] - elements[0][0] * elements[1][2] * elements[2][1];
    }

    /**
     * Transposes this matrix.
     */
    public final void transpose() {
        float temp = elements[1][0];
        elements[1][0] = elements[0][1];
        elements[0][1] = temp;
        temp = elements[2][0];
        elements[2][0] = elements[0][2];
        elements[0][2] = temp;
        temp = elements[2][1];
        elements[2][1] = elements[1][2];
        elements[1][2] = temp;
    }

    /**
     * Transforms this matrix into its inversed matrix. Does nothin, if the
     * matrix is not invertable.
     */
    public final void inverse() {
        final float[][] tempMatrix = new float[3][3];
        final float inversedDet = determinant();
        if (Math.abs(inversedDet) <= EPSILON) {
            return;
        }
        // Calculate elements of the inverse 3x3 matrix with the inversed
        // determinant defactored.
        tempMatrix[0][0] = elements[1][1] * elements[2][2] - elements[2][1] * elements[1][2];
        tempMatrix[0][1] = elements[2][1] * elements[0][2] - elements[2][2] * elements[0][1];
        tempMatrix[0][2] = elements[0][1] * elements[1][2] - elements[0][2] * elements[1][1];
        //
        tempMatrix[1][0] = elements[1][2] * elements[2][0] - elements[1][0] * elements[2][2];
        tempMatrix[1][1] = elements[0][0] * elements[2][2] - elements[0][2] * elements[2][0];
        tempMatrix[1][2] = elements[0][2] * elements[1][0] - elements[1][2] * elements[0][0];
        //
        tempMatrix[2][0] = elements[1][0] * elements[2][1] - elements[2][0] * elements[1][1];
        tempMatrix[2][1] = elements[0][1] * elements[2][0] - elements[0][0] * elements[2][1];
        tempMatrix[2][2] = elements[0][0] * elements[1][1] - elements[0][1] * elements[1][0];
        // assign calculated matrix elements to the corresponding elements of
        // this matrix.
        iterateMatrix((row, column) -> elements[row][column] = tempMatrix[row][column]);
        // Multiply with inversed determinant of this matrix
        uniformScale(1 / inversedDet);
    }

    /**
     * Multiplies the given matrix from the right with this Matrix3f.
     */
    public void multiply(Matrix3f matrix) {
        if (isValidMatrix(matrix)) {
            Logger.getGlobal().log(Level.WARNING, "Tried to multiply matrix with null.");
            return;
        }
        final float[][] tempMatrix = new float[getSize()][getSize()];
        iterateMatrix((row, column) -> {
            for (int j = 0; j < getSize(); j++) {
                tempMatrix[row][column] += getElement(row, j) * matrix.getElement(j, column);
            }
        });
        iterateMatrix((row, column) -> elements[row][column] = tempMatrix[row][column]);
    }

    /**
     * Adds the entries of the given matrix to the elements of this matrix.
     */
    public void add(Matrix3f matrix) {
        if (isValidMatrix(matrix)) {
            return;
        }
        iterateMatrix((row, column) -> elements[row][column] = elements[row][column] + matrix.getElement(row, column));
    }

    /**
     * Returns true, if every element of this matrix is equal to the equivalent
     * element of the given matrix.
     */
    public boolean equalTo(Matrix3f matrix) {
        if (isValidMatrix(matrix)) {
            Logger.getGlobal().log(Level.WARNING, "Tried to compare matrix with null.");
            return false;
        }
        for (int i = 0; i < getSize(); i++) {
            for (int j = 0; j < getSize(); j++) {
                if (Math.abs(matrix.getElement(i, j) - getElement(i, j)) > EPSILON) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Makes this matrix the unit matrix.
     */
    @Override
    public void makeIdentity() {
        iterateMatrix((row, column) -> elements[row][column] = row == column ? 1 : 0);
    }

    /**
     * Multiplies every element of the matrix with the given scale value.
     */
    @Override
    public void uniformScale(float scale) {
        iterateMatrix((row, column) -> elements[row][column] = elements[row][column] * scale);
    }

    /**
     * Returns a string representation of this matrix.
     */
    @Override
    public final String toString() {
        String output = "";
        for (int i = 0; i < getSize(); i++) {
            for (int j = 0; j < getSize(); j++) {
                output += elements[i][j] + ",";
            }
            output += "\n";
        }
        return output;
    }
}
