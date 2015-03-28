package com.monolith.api.math;

/**
 * Represents 4x4 matrix of floats.
 */
public class Matrix4 {

    private final float[] values = new float[16];

    public Matrix4() {
        setIdentity();
    }

    public void set(Matrix4 source) {
        System.arraycopy(source.values, 0, values, 0, 16);
    }

    /**
     * Return value of matrix on given position. Row and column indices are 0 based.
     *
     * @param row    Row index of the value.
     * @param column Column index of the value.
     * @return Value of matrix on given position.
     */
    public float get(int row, int column) {
        return values[row * 4 + column];
    }

    /**
     * set value of matrix on given position. Row and column indices are 0 based.
     *
     * @param row    Row index of the value.
     * @param column Column index of the value.
     * @param value  Value to set.
     */
    public void set(int row, int column, float value) {
        values[row * 4 + column] = value;
    }

    /**
     * Returns the array backing this matrix.
     *
     * Array contains rows of the matrix.
     * @return The array backing this matrix.
     */
    public float[] getArray() {
        return values;
    }

    public void setIdentity() {
        for (int i = 0; i < values.length; ++i) {
            values[i] = (i == 0 || i == 5 || i == 10 || i == 15) ? 1 : 0;
        }
    }

    /**
     * Multiply the left and right matrices and store the result in the result matrix.
     * <p/>
     * Result cannot be one of the left or right matrices.
     *
     * @param left   Matrix to be on the left side of multiplication.
     * @param right  Matrix to be on the right side of multiplication.
     * @param result Matrix into which the result will be stored.
     */
    public static void multiply(Matrix4 left, Matrix4 right, Matrix4 result) {
        if (result == left || result == right) {
            throw new IllegalStateException("Result matrix cannot be one of the operands.");
        }
        float[] lV = left.values;
        float[] rV = right.values;
        float[] resV = result.values;
        
        resV[0] = lV[0] * rV[0] + lV[1] * rV[4] + lV[2] * rV[8] + lV[3] * rV[12];
        resV[1] = lV[0] * rV[1] + lV[1] * rV[5] + lV[2] * rV[9] + lV[3] * rV[13];
        resV[2] = lV[0] * rV[2] + lV[1] * rV[6] + lV[2] * rV[10] + lV[3] * rV[14];
        resV[3] = lV[0] * rV[3] + lV[1] * rV[7] + lV[2] * rV[11] + lV[3] * rV[15];

        resV[4] = lV[4] * rV[0] + lV[5] * rV[4] + lV[6] * rV[8] + lV[7] * rV[12];
        resV[5] = lV[4] * rV[1] + lV[5] * rV[5] + lV[6] * rV[9] + lV[7] * rV[13];
        resV[6] = lV[4] * rV[2] + lV[5] * rV[6] + lV[6] * rV[10] + lV[7] * rV[14];
        resV[7] = lV[4] * rV[3] + lV[5] * rV[7] + lV[6] * rV[11] + lV[7] * rV[15];

        resV[8] = lV[8] * rV[0] + lV[9] * rV[4] + lV[10] * rV[8] + lV[11] * rV[12];
        resV[9] = lV[8] * rV[1] + lV[9] * rV[5] + lV[10] * rV[9] + lV[11] * rV[13];
        resV[10] = lV[8] * rV[2] + lV[9] * rV[6] + lV[10] * rV[10] + lV[11] * rV[14];
        resV[11] = lV[8] * rV[3] + lV[9] * rV[7] + lV[10] * rV[11] + lV[11] * rV[15];

        resV[12] = lV[12] * rV[0] + lV[13] * rV[4] + lV[14] * rV[8] + lV[15] * rV[12];
        resV[13] = lV[12] * rV[1] + lV[13] * rV[5] + lV[14] * rV[9] + lV[15] * rV[13];
        resV[14] = lV[12] * rV[2] + lV[13] * rV[6] + lV[14] * rV[10] + lV[15] * rV[14];
        resV[15] = lV[12] * rV[3] + lV[13] * rV[7] + lV[14] * rV[11] + lV[15] * rV[15];
    }
}
