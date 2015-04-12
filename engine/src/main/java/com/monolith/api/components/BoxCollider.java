package com.monolith.api.components;

import com.monolith.api.Color;
import com.monolith.api.Component;
import com.monolith.api.MeshData;
import com.monolith.api.Primitives;
import com.monolith.api.math.Matrix44;

//TODO add documentation
/**
 * Created by Jakub on 9. 4. 2015.
 */
public class BoxCollider extends Component {

    public float sizeX = 1;
    public float sizeY = 1;
    public float sizeZ = 1;

    public float offsetX = 0;
    public float offsetY = 0;
    public float offsetZ = 0;

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
            Matrix44 objectTransformation = getGameObject().transform.getTransformationMatrix();

            mColliderLocalTransformation.setIdentity();
            mColliderLocalTransformation.scale(sizeX, sizeY, sizeZ);
            mColliderLocalTransformation.translate(offsetX, offsetY, offsetZ);

            Matrix44.multiply(mColliderAbsoluteTransformation, objectTransformation, mColliderLocalTransformation);

            getApplication().getRenderer().renderWireframe(meshData, Color.GREEN, mColliderAbsoluteTransformation);
        }
    }
}
