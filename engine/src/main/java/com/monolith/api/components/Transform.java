package com.monolith.api.components;

import com.monolith.api.Component;
import com.monolith.api.GameObject;
import com.monolith.api.math.Matrix44;
import com.monolith.api.math.Vector3;

import java.util.List;

// TODO comment this class
/**
 * Holds transformation data of a {@link com.monolith.api.GameObject}.
 * <p/>
 * Transformation is relative to parent GameObject. World (absolute) transformation
 * data can be obtained using appropriate methods.
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

    public Transform() {
        mScale.x = 1;
        mScale.y = 1;
        mScale.z = 1;
    }

    public float getPositionX() {
        return mPosition.x;
    }

    public float getPositionY() {
        return mPosition.y;
    }

    public float getPositionZ() {
        return mPosition.z;
    }

    public float getRotationX() {
        return mRotation.x;
    }

    public float getRotationY() {
        return mRotation.y;
    }

    public float getRotationZ() {
        return mRotation.z;
    }

    public float getScaleX() {
        return mScale.x;
    }

    public float getScaleY() {
        return mScale.y;
    }

    public float getScaleZ() {
        return mScale.z;
    }

    /**
     * Translation is done relative to the already present transform. TODO finish this comment
     * @param x
     * @param y
     * @param z
     */
    public void translateBy(float x, float y, float z) {
        sWorkVector.x = x;
        sWorkVector.y = y;
        sWorkVector.z = z;

        Matrix44 transformationMatrix = getTransformationMatrix();
        transformationMatrix.transformVector(sWorkVector2, sWorkVector);

        mPosition.x += sWorkVector2.x;
        mPosition.y += sWorkVector2.y;
        mPosition.z += sWorkVector2.z;
        invalidate();
    }

    public void setPosition(float x, float y, float z) {
        mPosition.x = x;
        mPosition.y = y;
        mPosition.z = z;
        invalidate();
    }

    // TODO adjust the rotation so it is in range 0 - 360

    public void rotateBy(float x, float y, float z) {
        mRotation.x += x;
        mRotation.y += y;
        mRotation.z += z;
        invalidate();
    }

    public void setRotation(float x, float y, float z) {
        mRotation.x = x;
        mRotation.y = y;
        mRotation.z = z;
        invalidate();
    }

    public void scaleBy(float x, float y, float z) {
        mScale.x = x;
        mScale.y = y;
        mScale.z = z;
        invalidate();
    }

    public void setScale(float x, float y, float z) {
        mScale.x = x;
        mScale.y = y;
        mScale.z = z;
        invalidate();
    }

    private void invalidate() {
        mTransformationMatrixValid = false;

        List<GameObject> children = getGameObject().children;
        for(int i = 0; i < children.size(); ++i) {
            children.get(i).transform.invalidate();
        }
    }

    // Helper matrix for calculations of transformation matrices
    private final Matrix44 mHelperMatrix = new Matrix44();

    // TODO If anybody changes this matrix but not through the transformation rendering goes nuts
    public Matrix44 getTransformationMatrix() {
        if(!mTransformationMatrixValid) {
            GameObject parent = getGameObject().getParent();
            boolean hasParent = parent != null;
            Matrix44 parentTransformation = hasParent ? parent.transform.getTransformationMatrix() : null;
            Matrix44 localTransformation = hasParent ? mHelperMatrix : mTransformationMatrix;

            localTransformation.setIdentity();
            localTransformation.scale(mScale);
            localTransformation.rotateZ(mRotation.z);
            localTransformation.rotateX(mRotation.x);
            localTransformation.rotateY(mRotation.y);
            localTransformation.translate(mPosition);

            if(hasParent) {
                Matrix44.multiply(mTransformationMatrix, parentTransformation, localTransformation);
            }
            mTransformationMatrixValid = true;
        }
        return mTransformationMatrix;
    }
}
