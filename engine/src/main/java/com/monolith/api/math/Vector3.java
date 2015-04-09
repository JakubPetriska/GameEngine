package com.monolith.api.math;

/**
 * Represents three dimensional vector.
 */
public class Vector3 {

    public float x;
    public float y;
    public float z;

    public Vector3() {
    }

    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(Vector3 source) {
        this.x = source.x;
        this.y = source.y;
        this.z = source.z;
    }

    /**
     * Adds Vector to this Vector. Result is stored in this Vector.
     * @param vector Vector that is added to this Vector.
     */
    public void add(Vector3 vector) {
        this.x += vector.x;
        this.y += vector.y;
        this.z += vector.z;
    }

    /**
     * Adds to Vectors together.
     * @param result Vector in which result is stored.
     * @param left Vector to which the right Vector is added.
     * @param right Vector that is added to the left Vector.
     */
    public static void add(Vector3 result, Vector3 left, Vector3 right) {
        result.x = left.x + right.x;
        result.y = left.y + right.y;
        result.z = left.z + right.z;
    }

    /**
     * Subtracts Vector from this Vector. Result is stored in this Vector.
     * @param vector Vector that is subtracted from this Vector.
     */
    public void subtract(Vector3 vector) {
        this.x -= vector.x;
        this.y -= vector.y;
        this.z -= vector.z;
    }

    /**
     * Subtracts one Vector from another using the equation result = left - right.
     * @param result Vector in which result is stored.
     * @param left Vector from which the right Vector is subtracted.
     * @param right Vector that is subtracted from left Vector.
     */
    public static void subtract(Vector3 result, Vector3 left, Vector3 right) {
        result.x = left.x - right.x;
        result.y = left.y - right.y;
        result.z = left.z - right.z;
    }

    /**
     * Multiplies this Vector by given scalar. Result is stored in this Vector.
     * @param scalar Scalar that this Vector is multiplied by.
     */
    public void multiply(float scalar) {
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
    }

    /**
     * Divides this Vector with given scalar. Result is stored in this Vector.
     * @param scalar Scalar that this Vector is divided by.
     */
    public void divide(float scalar) {
        this.x /= scalar;
        this.y /= scalar;
        this.z /= scalar;
    }

    /**
     * Negates this Vector.
     */
    public void negate() {
        this.x *= -1;
        this.y *= -1;
        this.z *= -1;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Vector3) {
            Vector3 other = (Vector3) obj;
            // TODO floating point comparison using == - fix this!
            return this.x == other.x
                    && this.y == other.y
                    && this.z == other.z;
        } else {
            return false;
        }
    }
}
