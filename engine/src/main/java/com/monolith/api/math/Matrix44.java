package com.monolith.api.math;

/**
 * Represents 4x4 matrix of floats.
 * <p/>
 * Matrix holds it's values in an array storing columns of the matrix.
 * This array is returned by {@link Matrix44#getValues()} method.
 * <p/>
 * Order of values in the backing array:
 * m[0] m[4] m[8]  m[12]
 * m[1] m[5] m[9]  m[13]
 * m[2] m[6] m[10] m[14]
 * m[3] m[7] m[11] m[15]
 * <p/>
 * For vector transformation Matrix uses the convention
 * of column vector on the right side of the multiplication.
 * <p/>
 * All operations performed on this matrix are equal to multiplying
 * this matrix from left with the matrix containing the appropriate operation.
 */
public class Matrix44 {

    private final float[] mValues;

    /**
     * Create a new matrix. It's values are initialized to 0.
     */
    public Matrix44() {
        mValues = new float[16];
    }

    /**
     * Returns the array backing this matrix.
     * @return The array backing this matrix.
     */
    public float[] getValues() {
        return mValues;
    }

    /**
     * Set values in this matrix from other matrix.
     * @param source Matrix from which values are set into this matrix.
     */
    public void set(Matrix44 source) {
        System.arraycopy(source.mValues, 0, this.mValues, 0, 16);
    }

    /**
     * Return the value at given row and column.
     *
     * @param row    Index of row. Indexed from 0.
     * @param column Index of column. Indexed from 0.
     * @return Value in given row and column.
     */
    public float get(int row, int column) {
        return mValues[row + column * 4];
    }

    /**
     * Set value on given row and column.
     *
     * @param row    Index of row. Indexed from 0.
     * @param column Index of column. Indexed from 0.
     * @param value  Value to set.
     */
    public void set(int row, int column, float value) {
        mValues[row + column * 4] = value;
    }

    public void setIdentity() {
        for (int i = 0; i < 16; ++i) {
            mValues[i] =
                    (i == 0 || i == 5 || i == 10 || i == 15)
                            ? 1 : 0;
        }
    }

    public void translate(Vector3 translation) {
        translate(
                translation.getX(),
                translation.getY(),
                translation.getZ());
    }

    public void translate(float x, float y, float z) {
        mValues[12] += x;
        mValues[13] += y;
        mValues[14] += z;
    }

    /**
     * @param angle Angle to rotate in degrees.
     */
    public void rotateX(float angle) {
        double angleRad = Math.toRadians(angle);
        float sin = (float) Math.sin(angleRad);
        float cos = (float) Math.cos(angleRad);

        int row1Index;
        int row2Index;
        float row1;
        float row2;

        for (int i = 0; i < 4; ++i) {
            row1Index = 1 + i * 4;
            row2Index = row1Index + 1;
            row1 = mValues[row1Index];
            row2 = mValues[row2Index];
            mValues[row1Index] = cos * row1 - sin * row2;
            mValues[row2Index] = sin * row1 + cos * row2;
        }
    }

    /**
     * @param angle Angle to rotate in degrees.
     */
    public void rotateY(float angle) {
        double angleRad = Math.toRadians(angle);
        float sin = (float) Math.sin(angleRad);
        float cos = (float) Math.cos(angleRad);

        int row1Index;
        int row2Index;
        float row1;
        float row2;

        for (int i = 0; i < 4; ++i) {
            row1Index = i * 4;
            row2Index = row1Index + 2;
            row1 = mValues[row1Index];
            row2 = mValues[row2Index];
            mValues[row1Index] = cos * row1 + sin * row2;
            mValues[row2Index] = -sin * row1 + cos * row2;
        }
    }

    /**
     * @param angle Angle to rotate in degrees.
     */
    public void rotateZ(float angle) {
        double angleRad = Math.toRadians(angle);
        float sin = (float) Math.sin(angleRad);
        float cos = (float) Math.cos(angleRad);

        int row1Index;
        int row2Index;
        float row1;
        float row2;

        for (int i = 0; i < 4; ++i) {
            row1Index = i * 4;
            row2Index = row1Index + 1;
            row1 = mValues[row1Index];
            row2 = mValues[row2Index];
            mValues[row1Index] = cos * row1 - sin * row2;
            mValues[row2Index] = sin * row1 + cos * row2;
        }
    }

    public void scale(Vector3 scale) {
        scale(scale.getX(), scale.getY(), scale.getZ());
    }

    public void scale(float x, float y, float z) {
        mValues[0] *= x;
        mValues[4] *= x;
        mValues[8] *= x;
        mValues[12] *= x;
        mValues[1] *= y;
        mValues[5] *= y;
        mValues[9] *= y;
        mValues[13] *= y;
        mValues[2] *= z;
        mValues[6] *= z;
        mValues[10] *= z;
        mValues[14] *= z;
    }

    public static void multiply(Matrix44 result, Matrix44 left, Matrix44 right) {
        float[] res = result.getValues();
        float[] l = left.getValues();
        float[] r = right.getValues();

        res[0] = l[0] * r[0] + l[4] * r[1] + l[8] * r[2] + l[12] * r[3];
        res[1] = l[1] * r[0] + l[5] * r[1] + l[9] * r[2] + l[13] * r[3];
        res[2] = l[2] * r[0] + l[6] * r[1] + l[10] * r[2] + l[14] * r[3];
        res[3] = l[3] * r[0] + l[7] * r[1] + l[11] * r[2] + l[15] * r[3];

        res[4] = l[0] * r[4] + l[4] * r[5] + l[8] * r[6] + l[12] * r[7];
        res[5] = l[1] * r[4] + l[5] * r[5] + l[9] * r[6] + l[13] * r[7];
        res[6] = l[2] * r[4] + l[6] * r[5] + l[10] * r[6] + l[14] * r[7];
        res[7] = l[3] * r[4] + l[7] * r[5] + l[11] * r[6] + l[15] * r[7];

        res[8] = l[0] * r[8] + l[4] * r[9] + l[8] * r[10] + l[12] * r[11];
        res[9] = l[1] * r[8] + l[5] * r[9] + l[9] * r[10] + l[13] * r[11];
        res[10] = l[2] * r[8] + l[6] * r[9] + l[10] * r[10] + l[14] * r[11];
        res[11] = l[3] * r[8] + l[7] * r[9] + l[11] * r[10] + l[15] * r[11];

        res[12] = l[0] * r[12] + l[4] * r[13] + l[8] * r[14] + l[12] * r[15];
        res[13] = l[1] * r[12] + l[5] * r[13] + l[9] * r[14] + l[13] * r[15];
        res[14] = l[2] * r[12] + l[6] * r[13] + l[10] * r[14] + l[14] * r[15];
        res[15] = l[3] * r[12] + l[7] * r[13] + l[11] * r[14] + l[15] * r[15];
    }

    public void transformVector(Vector3 vector) {
        transformVector(vector, vector);
    }

    public void transformVector(Vector3 result, Vector3 vector) {
        float x = vector.getX() * mValues[0] + vector.getY() * mValues[4] + vector.getZ() * mValues[8];
        float y = vector.getX() * mValues[1] + vector.getY() * mValues[5] + vector.getZ() * mValues[9];
        float z = vector.getX() * mValues[2] + vector.getY() * mValues[6] + vector.getZ() * mValues[10];

        result.set(x, y, z);
    }

    public void transformPoint(Vector3 point) {
        transformPoint(point, point);
    }

    public void transformPoint(Vector3 result, Vector3 point) {
        float x = point.getX() * mValues[0] + point.getY() * mValues[4] + point.getZ() * mValues[8] + mValues[12];
        float y = point.getX() * mValues[1] + point.getY() * mValues[5] + point.getZ() * mValues[9] + mValues[13];
        float z = point.getX() * mValues[2] + point.getY() * mValues[6] + point.getZ() * mValues[10] + mValues[14];

        result.set(x, y, z);
    }
}
