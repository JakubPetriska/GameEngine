package com.monolith.api.math;

/**
 * Represents three dimensional vector.
 */
public class Vector3 {
    
    private final float[] mValues;

    /**
     * Create a new vector initialized to 0.
     */
    public Vector3() {
        mValues = new float[3];
    }

    /**
     * Create a new vector from given values.
     * @param x X value of the new vector.
     * @param y Y value of the new vector.
     * @param z Z value of the new vector.
     */
    public Vector3(float x, float y, float z) {
        this();
        this.mValues[0] = x;
        this.mValues[1] = y;
        this.mValues[2] = z;
    }

    /**
     * Returns the array backing this vector.
     * @return The array backing this vector.
     */
    public float[] getValues() {
        return mValues;
    }

    /**
     * Set values in this vector.
     * @param x Value to set as X.
     * @param y Value to set as Y.
     * @param z Value to set as Z.
     */
    public void set(float x, float y, float z) {
        this.mValues[0] = x;
        this.mValues[1] = y;
        this.mValues[2] = z;
    }

    /**
     * Set values in this vector from other vector.
     * @param source Vector from which values are set into this vector.
     */
    public void set(Vector3 source) {
        System.arraycopy(source.mValues, 0, this.mValues, 0, 3);
    }

    /**
     * Return the X value of this vector.
     * @return X value of this vector.
     */
    public float getX() {
        return mValues[0];
    }

    /**
     * Set the X value of this vector.
     * @param value Value to set.
     */
    public void setX(float value) {
        mValues[0] = value;
    }

    /**
     * Return the Y value of this vector.
     * @return Y value of this vector.
     */
    public float getY() {
        return mValues[1];
    }

    /**
     * Set the Y value of this vector.
     * @param value Value to set.
     */
    public void setY(float value) {
        mValues[1] = value;
    }

    /**
     * Return the Z value of this vector.
     * @return Z value of this vector.
     */
    public float getZ() {
        return mValues[2];
    }

    /**
     * Set the Z value of this vector.
     * @param value Value to set.
     */
    public void setZ(float value) {
        mValues[2] = value;
    }

    /**
     * Adds vector to this vector. Result is stored in this Vector.
     * @param vector Vector that is added to this Vector.
     */
    public void add(Vector3 vector) {
        this.mValues[0] += vector.mValues[0];
        this.mValues[1] += vector.mValues[1];
        this.mValues[2] += vector.mValues[2];
    }

    /**
     * Add values to this vector. Result is stored in this Vector.
     * @param x Value to add to the X value of this vector.
     * @param y Value to add to the Y value of this vector.
     * @param z Value to add to the Z value of this vector.
     */
    public void add(float x, float y, float z) {
        this.mValues[0] += x;
        this.mValues[1] += y;
        this.mValues[2] += z;
    }

    /**
     * Add one vector to the other and store the result in result vector.
     * @param result Vector in which result is stored.
     * @param left Vector to which the right Vector is added.
     * @param right Vector that is added to the left Vector.
     */
    public static void add(Vector3 result, Vector3 left, Vector3 right) {
        result.mValues[0] = left.mValues[0] + right.mValues[0];
        result.mValues[1] = left.mValues[1] + right.mValues[1];
        result.mValues[2] = left.mValues[2] + right.mValues[2];
    }

    /**
     * Subtracts vector from this vector. Result is stored in this Vector.
     * @param vector Vector that is subtracted from this Vector.
     */
    public void subtract(Vector3 vector) {
        this.mValues[0] -= vector.mValues[0];
        this.mValues[1] -= vector.mValues[1];
        this.mValues[2] -= vector.mValues[2];
    }

    /**
     * Subtract values from this vector. Result is stored in this Vector.
     * @param x Value to subtract from the X value of this vector.
     * @param y Value to subtract from the Y value of this vector.
     * @param z Value to subtract from the Z value of this vector.
     */
    public void subtract(float x, float y, float z) {
        this.mValues[0] -= x;
        this.mValues[1] -= y;
        this.mValues[2] -= z;
    }

    /**
     * Subtract one vector from the other and store the result in result vector.
     * @param result Vector in which result is stored.
     * @param left Vector from which the right vector is subtracted.
     * @param right Vector that is subtracted from the left vector.
     */
    public static void subtract(Vector3 result, Vector3 left, Vector3 right) {
        result.mValues[0] = left.mValues[0] - right.mValues[0];
        result.mValues[1] = left.mValues[1] - right.mValues[1];
        result.mValues[2] = left.mValues[2] - right.mValues[2];
    }

    /**
     * Multiply this vector by given scalar and store the result in this vector.
     * @param scalar Scalar that this vector is multiplied by.
     */
    public void multiply(float scalar) {
        this.mValues[0] *= scalar;
        this.mValues[1] *= scalar;
        this.mValues[2] *= scalar;
    }

    /**
     * Multiply this vector by given scalar and store the result in the result vector.
     * @param result Vector in which the result is stored.
     * @param scalar Scalar that this vector is multiplied by.
     */
    public void multiply(Vector3 result, float scalar) {
        result.mValues[0] = this.mValues[0] * scalar;
        result.mValues[1] = this.mValues[1] * scalar;
        result.mValues[2] = this.mValues[2] * scalar;
    }

    /**
     * Divide this vector by given scalar and store the result in this vector.
     * @param scalar Scalar that this vector is divided by.
     */
    public void divide(float scalar) {
        this.mValues[0] /= scalar;
        this.mValues[1] /= scalar;
        this.mValues[2] /= scalar;
    }

    /**
     * Divide this vector by given scalar and store the result in the result vector.
     * @param result Vector in which the result is stored.
     * @param scalar Scalar that this vector is divided by.
     */
    public void divide(Vector3 result, float scalar) {
        result.mValues[0] = this.mValues[0] / scalar;
        result.mValues[1] = this.mValues[1] / scalar;
        result.mValues[2] = this.mValues[2] / scalar;
    }

    /**
     * Calculate dot product of given vectors.
     * @param first One of the vectors that the dot product is calculated from.
     * @param second The other of the vectors that the dot product is calculated from.
     * @return Calculated dot product.
     */
    public static float dot(Vector3 first, Vector3 second) {
        return first.mValues[0] * second.mValues[0] + first.mValues[1] * second.mValues[1] + first.mValues[2] * second.mValues[2];
    }

    /**
     * Calculate cross product of given vectors and store the result in the result vector.
     * @param result Vector in which the result is stored.
     * @param left Left vector of the cross product calculation.
     * @param right Right vector of the cross product calculation.
     */
    public static void cross(Vector3 result, Vector3 left, Vector3 right) {
        result.setX(left.mValues[1] * right.mValues[2] - left.mValues[2] * right.mValues[1]);
        result.setX(left.mValues[2] * right.mValues[0] - left.mValues[0] * right.mValues[2]);
        result.setX(left.mValues[0] * right.mValues[1] - left.mValues[1] * right.mValues[0]);
    }

    /**
     * Calculate the length of this vector.
     * @return Length of this vector.
     */
    public float length() {
        return (float) Math.sqrt(mValues[0] * mValues[0] + mValues[1] * mValues[1] + mValues[2] * mValues[2]);
    }

    /**
     * Normalize this vector so it's length is 1.
     */
    public void normalize() {
        divide(length());
    }

    /**
     * Negates this Vector.
     */
    public void negate() {
        multiply(-1);
    }
}
