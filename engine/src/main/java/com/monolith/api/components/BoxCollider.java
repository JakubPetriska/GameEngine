package com.monolith.api.components;

import com.monolith.api.Color;
import com.monolith.api.Component;
import com.monolith.api.MeshData;
import com.monolith.api.Primitives;
import com.monolith.api.math.Matrix44;

import java.util.ArrayList;
import java.util.List;

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

    private final Matrix44 mColliderAbsoluteTransformation = new Matrix44();
    private final Matrix44 sColliderLocalTransformation = new Matrix44(); // Used during calculations
    private List<CollisionListener> mListeners = new ArrayList<>();

    // TODO just for debuging add collision exit callback to fully enable this
    private boolean mColliding;

    private MeshData meshData;

    public interface CollisionListener {
        void onCollisionDetected(BoxCollider collisionObject);
    }

    public void registerCollisionListener(CollisionListener listener) {
        if (!mListeners.contains(listener)) {
            mListeners.add(listener);
        }
    }

    public void unregisterCollisionListener(CollisionListener listener) {
        mListeners.remove(listener);
    }

    @Override
    public void start() {
        if (getApplication().debugSettings.drawColliders) {
            this.meshData = getApplication().getModelManager().getMeshData(Primitives.CUBE);
        }
        getApplication().getCollisionSystem().registerCollider(this);
    }

    @Override
    public void finish() {
        getApplication().getCollisionSystem().unregisterCollider(this);
    }

    public void onCollisionDetected(BoxCollider collisionObject) {
        mColliding = true;
        for (int i = 0; i < mListeners.size(); ++i) {
            mListeners.get(i).onCollisionDetected(collisionObject);
        }
    }

    @Override
    public void postUpdate() {
        // Transformation matrix for this collider needs to be recalculated here
        // It is later used by CollisionSystem
        Matrix44 objectTransformation = getGameObject().transform.getTransformationMatrix();

        sColliderLocalTransformation.setIdentity();
        sColliderLocalTransformation.scale(sizeX, sizeY, sizeZ);
        sColliderLocalTransformation.translate(offsetX, offsetY, offsetZ);

        Matrix44.multiply(mColliderAbsoluteTransformation, objectTransformation, sColliderLocalTransformation);

        if (getApplication().debugSettings.drawColliders) {
            getApplication().getRenderer().renderWireframe(meshData,
                    mColliding ? Color.RED : Color.GREEN,
                    mColliderAbsoluteTransformation);
        }
    }

    public Matrix44 getTransformationMatrix() {
        return mColliderAbsoluteTransformation;
    }
}
