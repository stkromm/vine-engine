package com.vine.math;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Matrix3f extends Matrix implements MatrixOperations<Matrix3f> {
    private static final float EPSILON = 0.00000000001f;
    private float[][] elements = new float[3][3];

    /**
     * Creates a new matrix identical to the given matrix.
     * 
     * @param matrix
     *            The matrix to copy.
     */
    public Matrix3f(Matrix3f matrix) {
        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                elements[i][j] = matrix.getElement(i, j);
            }
        }
    }

    /**
     * Creates a matrix filled with the given value.
     */
    public Matrix3f(float value) {
        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                elements[i][j] = value;
            }
        }
    }

    /**
     * Makes this matrix the unit matrix.
     */
    public void makeIdentity() {
        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                if (i == j) {
                    elements[i][j] = 1;
                } else {
                    elements[i][j] = 0;
                }
            }
        }
    }

    /**
     * Returns the elements at the given position.
     */
    public float getElement(int row, int column) {
        if (column >= 0 && column <= 2 && row >= 0 && row <= 2) {
            return elements[row][column];
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    /**
     * Multiplies every element of the matrix with the given scale value.
     */
    public void uniformScale(float scale) {
        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                elements[i][j] *= scale;
            }
        }
    }

    /**
     * Calculates the determinant of this matrix and returns it.
     */
    public float determinant() {
        return elements[0][0] * elements[1][1] * elements[2][2] + elements[0][1] * elements[1][2] * elements[2][0]
                + elements[0][2] * elements[1][0] * elements[2][1] - elements[0][1] * elements[1][0] * elements[2][2]
                - elements[0][2] * elements[1][1] * elements[0][2] - elements[0][0] * elements[1][2] * elements[2][1];
    }

    /**
     * Transposes this matrix.
     */
    public void transpose() {
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
     * Multiplies the given matrix from the right with this Matrix3f.
     */
    public void multiply(Matrix3f matrix) {
        float[][] tempMatrix = new float[3][3];
        for (int i = 0; i <= 2; i++) {
            for (int k = 0; k <= 2; k++) {
                tempMatrix[i][k] = elements[i][k];
            }
        }
        for (int i = 0; i <= 2; i++) {
            for (int k = 0; k <= 2; k++) {
                elements[i][k] = 0;
                for (int j = 0; j <= 2; j++) {
                    elements[i][k] += tempMatrix[i][j] * matrix.getElement(j, k);
                }
            }
        }
    }

    /**
     * Transforms this matrix into its inversed matrix. Does nothin, if the
     * matrix is not inversable.
     */
    public void inverse() {
        float[][] tempMatrix = new float[3][3];
        float inversedDet = 1 / determinant();
        if (Math.abs(inversedDet) <= EPSILON) {
            return;
        }
        for (int i = 0; i <= 2; i++) {
            for (int k = 0; k <= 2; k++) {
                tempMatrix[i][k] = elements[i][k];
            }
        }
        // Calculate elements as determinant of 2x2 matrices
        for (int i = 0; i <= 2; i++) {
            for (int k = 0; k <= 2; k++) {
            }
        }
        // Multiply with inversed determinant of this matrix
        uniformScale(inversedDet);
    }

    /**
     * Adds the entries of the given matrix to the elements of this matrix.
     */
    public void add(Matrix3f matrix) {
        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                elements[i][j] += matrix.getElement(i, j);
            }
        }
    }

    /**
     * Sets the elements of the row that responds to the given index.
     * 
     * @param rowIndex
     *            Valid values are in [0,2].
     */
    public void setRow(int rowIndex, float e1, float e2, float e3) {
        if (rowIndex >= 0 && rowIndex <= 2) {
            elements[rowIndex][0] = e1;
            elements[rowIndex][1] = e2;
            elements[rowIndex][2] = e3;
        } else {
            Logger.getGlobal().log(Level.WARNING, "Tried to set row of Matrix3f with invalid index:" + rowIndex);
        }
    }

    /**
     * Returns true, if every element of this matrix is equal to the equivalent
     * element of the given matrix.
     */
    public boolean equalTo(Matrix3f matrix) {
        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                if (Math.abs(matrix.getElement(i, j) - elements[i][j]) > EPSILON) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Returns a string representation of this matrix.
     */
    public String toString() {
        String output = "";
        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                output += elements[i][j] + ",";
            }
            output += "\n";
        }
        return output;
    }
}