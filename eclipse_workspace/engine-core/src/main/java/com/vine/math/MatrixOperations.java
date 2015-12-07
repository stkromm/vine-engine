package com.vine.math;

public interface MatrixOperations<MAT extends Matrix> {
    void inverse();

    void add(MAT matrix);

    void makeIdentity();

    void uniformScale(float scale);

    float determinant();

    void transpose();

    void multiply(MAT matrix);
}