package com.vine.math;

public abstract class Matrixf {
    /**
     * Maximum difference two floating point values can differ and still count
     * as equal.
     */
    protected static final float EPSILON = 0.000000000000000000000001f;

    @FunctionalInterface
    protected static interface ElementOperation {
        /**
         * Operate on a element of a matrix with the given indices.
         */
        public void operate(int row, int column);
    }

    protected Matrixf() {

    }

    /**
     * Iterates the matrix and executes the operator method for each element one
     * time.
     */
    protected final void iterateMatrix(ElementOperation operator) {
        for (int i = 0; i < getSize(); i++) {
            for (int j = 0; j < getSize(); j++) {
                operator.operate(i, j);
            }
        }
    }

    protected abstract float getElement(int row, int column);

    protected abstract int getSize();

    protected static boolean isValidMatrix(Matrixf matrix) {
        return matrix == null;
    }

}
