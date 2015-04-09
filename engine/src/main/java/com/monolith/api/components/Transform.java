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

    private boolean mTransformationMatrixValid = false;
    private final Matrix44 mTransformationMatrix = new Matrix44();

    private boolean mRenderingTransformationMatrixValid = false;
    private final Matrix44 mRenderingTransformationMatrix = new Matrix44();

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

    public void translateBy(float x, float y, float z) {
        mPosition.x += x;
        mPosition.y += y;
        mPosition.z += z;
        invalidate();
    }

    public void setTranslation(float x, float y, float z) {
        mPosition.x = x;
        mPosition.y = y;
        mPosition.z = z;
        invalidate();
    }

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
        mRenderingTransformationMatrixValid = false;

        List<GameObject> children = getGameObject().children;
        for(int i = 0; i < children.size(); ++i) {
            children.get(i).transform.invalidate();
        }
    }

    // Helper matrix for calculations of transformation matrices
    private final Matrix44 mHelperMatrix = new Matrix44();

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

    public Matrix44 getRenderingTransformationMatrix() {
        if(!mRenderingTransformationMatrixValid) {
            GameObject parent = getGameObject().getParent();
            boolean hasParent = parent != null;
            Matrix44 parentTransformation = hasParent ? parent.transform.getRenderingTransformationMatrix() : null;
            Matrix44 localTransformation = hasParent ? mHelperMatrix : mRenderingTransformationMatrix;

            // TODO this should be platform dependent, currently is adjusted for OpenGL
            localTransformation.setIdentity();
            localTransformation.scale(mScale);
            localTransformation.rotateZ(-mRotation.z);
            localTransformation.rotateX(mRotation.x);
            localTransformation.rotateY(-mRotation.y);
            localTransformation.translate(-mPosition.x, mPosition.y, mPosition.z);

            if(hasParent) {
                Matrix44.multiply(mRenderingTransformationMatrix, parentTransformation, localTransformation);
            }
            mRenderingTransformationMatrixValid = true;
        }
        return mRenderingTransformationMatrix;
    }
}
