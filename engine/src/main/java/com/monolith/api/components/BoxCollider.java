package com.monolith.api.components;

import com.monolith.api.Color;
import com.monolith.api.Component;
import com.monolith.api.MeshData;
import com.monolith.api.Primitives;
import com.monolith.api.math.Matrix44;

/**
 * Created by Jakub on 9. 4. 2015.
 */
public class BoxCollider extends Component {

    public float sizeX = 1;
    public float sizeY = 1;
    public float sizeZ = 1;

    private MeshData meshData;

    @Override
    public void start() {
        this.meshData = getApplication().getModelManager().getMeshData(Primitives.CUBE);
    }

    private static final Matrix44 mColliderAbsoluteTransformation = new Matrix44();
    private static final Matrix44 mColliderLocalTransformation = new Matrix44();

    @Override
    public void postUpdate() {
        if (getApplication().debugSettings.drawColliders) {
            Matrix44 objectTransformation = getGameObject().transform.getRenderingTransformationMatrix();

            // TODO this should be platform dependent, currently is adjusted for OpenGL
            mColliderLocalTransformation.setIdentity();
            mColliderLocalTransformation.scale(sizeX, sizeY, sizeZ);

            Matrix44.multiply(mColliderAbsoluteTransformation, objectTransformation, mColliderLocalTransformation);

            getApplication().getRenderer().renderWireframe(meshData, Color.GREEN, mColliderAbsoluteTransformation);
        }
    }
}
