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
     * @param first First Vector to which the second Vector is added.
     * @param second Second Vector that is added to the first Vector.
     */
    public static void add(Vector3 result, Vector3 first, Vector3 second) {
        result.x = first.x + second.x;
        result.y = first.y + second.y;
        result.z = first.z + second.z;
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
     * Subtracts one Vector from another using the equation result = first - second.
     * @param result Vector in which result is stored.
     * @param first Vector from which the second Vector is subtracted.
     * @param second Vector that is subtracted from first Vector.
     */
    public static void subtract(Vector3 result, Vector3 first, Vector3 second) {
        result.x = first.x - second.x;
        result.y = first.y - second.y;
        result.z = first.z - second.z;
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
            return this.x == other.x
                    && this.y == other.y
                    && this.z == other.z;
        } else {
            return false;
        }
    }
}
