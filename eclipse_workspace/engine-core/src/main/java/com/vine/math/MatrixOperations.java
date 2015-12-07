package com.vine.math;

public interface MatrixOperations<M extends Matrixf> {
    void inverse();

    void add(M matrix);

    void makeIdentity();

    void uniformScale(float scale);

    float determinant();

    void transpose();

    void multiply(M matrix);
}