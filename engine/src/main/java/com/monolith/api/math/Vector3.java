package com.monolith.api.math;

// TODO add comments
/**
 * Represents three dimensional vector.
 */
public class Vector3 {
    
    private final float[] mValues;

    public Vector3() {
        mValues = new float[3];
    }

    // TODO add offset
//    public Vector3(float[] values) {
//        if (values.length < 3) {
//            throw new IllegalStateException("Array backing a Vector3 must be at least 3 elements long.");
//        }
//        mValues = values;
//    }

    public Vector3(float x, float y, float z) {
        this();
        this.mValues[0] = x;
        this.mValues[1] = y;
        this.mValues[2] = z;
    }

    public float[] getValues() {
        return mValues;
    }

    public void set(float x, float y, float z) {
        this.mValues[0] = x;
        this.mValues[1] = y;
        this.mValues[2] = z;
    }

    public void set(Vector3 source) {
        this.mValues[0] = source.mValues[0];
        this.mValues[1] = source.mValues[1];
        this.mValues[2] = source.mValues[2];
    }

    public float getX() {
        return mValues[0];
    }

    public void setX(float value) {
        mValues[0] = value;
    }

    public float getY() {
        return mValues[1];
    }

    public void setY(float value) {
        mValues[1] = value;
    }

    public float getZ() {
        return mValues[2];
    }

    public void setZ(float value) {
        mValues[2] = value;
    }

    /**
     * Adds Vector to this Vector. Result is stored in this Vector.
     * @param vector Vector that is added to this Vector.
     */
    public void add(Vector3 vector) {
        this.mValues[0] += vector.mValues[0];
        this.mValues[1] += vector.mValues[1];
        this.mValues[2] += vector.mValues[2];
    }

    public void add(float x, float y, float z) {
        this.mValues[0] += x;
        this.mValues[1] += y;
        this.mValues[2] += z;
    }

    /**
     * Adds to Vectors together.
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
     * Subtracts Vector from this Vector. Result is stored in this Vector.
     * @param vector Vector that is subtracted from this Vector.
     */
    public void subtract(Vector3 vector) {
        this.mValues[0] -= vector.mValues[0];
        this.mValues[1] -= vector.mValues[1];
        this.mValues[2] -= vector.mValues[2];
    }

    public void subtract(float x, float y, float z) {
        this.mValues[0] -= x;
        this.mValues[1] -= y;
        this.mValues[2] -= z;
    }

    /**
     * Subtracts one Vector from another using the equation result = left - right.
     * @param result Vector in which result is stored.
     * @param left Vector from which the right Vector is subtracted.
     * @param right Vector that is subtracted from left Vector.
     */
    public static void subtract(Vector3 result, Vector3 left, Vector3 right) {
        result.mValues[0] = left.mValues[0] - right.mValues[0];
        result.mValues[1] = left.mValues[1] - right.mValues[1];
        result.mValues[2] = left.mValues[2] - right.mValues[2];
    }

    /**
     * Multiplies this Vector by given scalar. Result is stored in this Vector.
     * @param scalar Scalar that this Vector is multiplied by.
     */
    public void multiply(float scalar) {
        this.mValues[0] *= scalar;
        this.mValues[1] *= scalar;
        this.mValues[2] *= scalar;
    }

    public void multiply(Vector3 result, float scalar) {
        result.mValues[0] = this.mValues[0] * scalar;
        result.mValues[1] = this.mValues[1] * scalar;
        result.mValues[2] = this.mValues[2] * scalar;
    }

    /**
     * Divides this Vector with given scalar. Result is stored in this Vector.
     * @param scalar Scalar that this Vector is divided by.
     */
    public void divide(float scalar) {
        this.mValues[0] /= scalar;
        this.mValues[1] /= scalar;
        this.mValues[2] /= scalar;
    }

    public void divide(Vector3 result, float scalar) {
        result.mValues[0] = this.mValues[0] / scalar;
        result.mValues[1] = this.mValues[1] / scalar;
        result.mValues[2] = this.mValues[2] / scalar;
    }

    public static float dot(Vector3 firstVector, Vector3 secondVector) {
        return firstVector.mValues[0] * secondVector.mValues[0] + firstVector.mValues[1] * secondVector.mValues[1] + firstVector.mValues[2] * secondVector.mValues[2];
    }

    public static void cross(Vector3 result, Vector3 left, Vector3 right) {
        result.setX(left.mValues[1] * right.mValues[2] - left.mValues[2] * right.mValues[1]);
        result.setX(left.mValues[2] * right.mValues[0] - left.mValues[0] * right.mValues[2]);
        result.setX(left.mValues[0] * right.mValues[1] - left.mValues[1] * right.mValues[0]);
    }

    public float length() {
        return (float) Math.sqrt(mValues[0] * mValues[0] + mValues[1] * mValues[1] + mValues[2] * mValues[2]);
    }

    public void normalize() {
        divide(length());
    }

    /**
     * Negates this Vector.
     */
    public void negate() {
        multiply(-1);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Vector3) {
            Vector3 other = (Vector3) obj;
            // TODO floating point comparison using == - fix this!
            return this.mValues[0] == other.mValues[0]
                    && this.mValues[1] == other.mValues[1]
                    && this.mValues[2] == other.mValues[2];
        } else {
            return false;
        }
    }
}
