package com.monolith.api.components;

import com.monolith.api.Component;
import com.monolith.api.GameObject;
import com.monolith.api.math.Matrix44;
import com.monolith.api.math.Vector3;

import java.util.List;

/**
 * Holds transformation of a {@link com.monolith.api.GameObject}.
 * <p/>
 * Transformation is relative to parent GameObject.
 * World (absolute) transformation can be obtained using appropriate methods.
 */
public class Transform extends Component {

    // Used for translation calculations
    private final Vector3 sWorkVector = new Vector3();
    private final Vector3 sWorkVector2 = new Vector3();

    private boolean mTransformationMatrixValid = false;
    private final Matrix44 mTransformationMatrix = new Matrix44();

    /**
     * Translation of this transform relative to parent.
     */
    private final Vector3 mPosition = new Vector3();

    /**
     * Rotation of this transform relative to parent.
     */
    private final Vector3 mRotation = new Vector3();

    /**
     * Scale of this transform relative to parent.
     */
    private final Vector3 mScale = new Vector3();

    /**
     * Create new transformation.
     * <p/>
     * Newly created transformation is identity. It has 0 translation,
     * 0 rotation and scale of 1 in all axes.
     */
    public Transform() {
        mScale.set(1, 1, 1);
    }

    /**
     * Get the local space X position.
     *
     * @return The local space X position.
     */
    public float getPositionX() {
        return mPosition.getX();
    }

    /**
     * Get the local space Y position.
     *
     * @return The local space Z position.
     */
    public float getPositionY() {
        return mPosition.getY();
    }

    /**
     * Get the local space Z position.
     *
     * @return The local space Z position.
     */
    public float getPositionZ() {
        return mPosition.getZ();
    }

    /**
     * Get the local space rotation around X axis.
     *
     * @return The local space rotation around X axis.
     */
    public float getRotationX() {
        return mRotation.getX();
    }

    /**
     * Get the local space rotation around Y axis.
     *
     * @return The local space rotation around Y axis.
     */
    public float getRotationY() {
        return mRotation.getY();
    }

    /**
     * Get the local space rotation around Z axis.
     *
     * @return The local space rotation around Z axis.
     */
    public float getRotationZ() {
        return mRotation.getZ();
    }

    /**
     * Get the local space scale in direction of X axis.
     *
     * @return The local space scale in direction of X axis.
     */
    public float getScaleX() {
        return mScale.getX();
    }

    /**
     * Get the local space scale in direction of Y axis.
     *
     * @return The local space scale in direction of Y axis.
     */
    public float getScaleY() {
        return mScale.getY();
    }

    /**
     * Get the local space scale in direction of Z axis.
     *
     * @return The local space scale in direction of Z axis.
     */
    public float getScaleZ() {
        return mScale.getZ();
    }

    /**
     * Translate this transform by given amount in direction of each axis.
     * <p/>
     * The axes along which the transformation is translated are
     * transformed by this transformation.
     *
     * @param x Amount of translation in the direction of X axis.
     * @param y Amount of translation in the direction of Y axis.
     * @param z Amount of translation in the direction of Z axis.
     */
    public void translateBy(float x, float y, float z) {
        sWorkVector.set(x, y, z);

        Matrix44 transformationMatrix = getTransformationMatrix();
        transformationMatrix.transformVector(sWorkVector2, sWorkVector);

        mPosition.add(sWorkVector2);
        invalidate();
    }

    /**
     * Set the position (translation from origin) of this transformation.
     *
     * @param x X coordinate of position.
     * @param y Y coordinate of position.
     * @param z Z coordinate of position.
     */
    public void setPosition(float x, float y, float z) {
        mPosition.set(x, y, z);
        invalidate();
    }

    // TODO adjust the rotation so it is in range 0 - 360

    /**
     * Rotate this transformation by given amounts in degrees.
     *
     * @param x Amount of rotation around X axis.
     * @param y Amount of rotation around Y axis.
     * @param z Amount of rotation around Z axis.
     */
    public void rotateBy(float x, float y, float z) {
        mRotation.add(x, y, z);
        invalidate();
    }

    /**
     * Set the rotation of this transformation.
     *
     * @param x Amount of rotation around X axis.
     * @param y Amount of rotation around Y axis.
     * @param z Amount of rotation around Z axis.
     */
    public void setRotation(float x, float y, float z) {
        mRotation.set(x, y, z);
        invalidate();
    }

    /**
     * Scale this transformation by given amounts.
     *
     * @param x Amount of scale in direction of X axis.
     * @param y Amount of scale in direction of Y axis.
     * @param z Amount of scale in direction of Z axis.
     */
    public void scaleBy(float x, float y, float z) {
        mScale.setX(mScale.getX() * x);
        mScale.setY(mScale.getY() * y);
        mScale.setZ(mScale.getZ() * z);
        invalidate();
    }

    /**
     * Set scale of this transformation.
     *
     * @param x Amount of scale in direction of X axis.
     * @param y Amount of scale in direction of Y axis.
     * @param z Amount of scale in direction of Z axis.
     */
    public void setScale(float x, float y, float z) {
        mScale.set(x, y, z);
        invalidate();
    }

    private void invalidate() {
        if (mTransformationMatrixValid) {
            mTransformationMatrixValid = false;

            List<GameObject> children = getGameObject().children;
            for (int i = 0; i < children.size(); ++i) {
                children.get(i).transform.invalidate();
            }
        }
    }

    // Helper matrix for calculations of transformation matrices
    private final Matrix44 mHelperMatrix = new Matrix44();

    // TODO If anybody changes this matrix but not through the transformation rendering goes nuts

    /**
     * Get the transformation matrix.
     * <p/>
     * Transformation matrix is absolute. Meaning the transformation is from world's origin.
     *
     * @return The absolute transformation matrix.
     */
    public Matrix44 getTransformationMatrix() {
        if (!mTransformationMatrixValid) {
            GameObject parent = getGameObject().getParent();
            boolean hasParent = parent != null;
            Matrix44 parentTransformation = hasParent ? parent.transform.getTransformationMatrix() : null;
            Matrix44 localTransformation = hasParent ? mHelperMatrix : mTransformationMatrix;

            localTransformation.setIdentity();
            localTransformation.scale(mScale);
            localTransformation.rotateZ(mRotation.getZ());
            localTransformation.rotateX(mRotation.getX());
            localTransformation.rotateY(mRotation.getY());
            localTransformation.translate(mPosition);

            if (hasParent) {
                Matrix44.multiply(mTransformationMatrix, parentTransformation, localTransformation);
            }
            mTransformationMatrixValid = true;
        }
        return mTransformationMatrix;
    }
}
