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
    private final List<CollisionListener> mListeners = new ArrayList<>();

    private int mCollidingCollidersCount = 0;

    private MeshData meshData;

    public interface CollisionListener {
        void onCollisionDetected(BoxCollider collisionObject);

        void onCollisionEnded(BoxCollider collisionObject);
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
        if (getApplication().getDebug().drawColliders) {
            this.meshData = getApplication().getModelManager().getMeshData(Primitives.CUBE);
        }
        getApplication().getCollisionSystem().registerCollider(this);
    }

    @Override
    public void finish() {
        getApplication().getCollisionSystem().unregisterCollider(this);
    }

    public void onCollisionDetected(BoxCollider collisionObject) {
        ++mCollidingCollidersCount;
        for (int i = 0; i < mListeners.size(); ++i) {
            mListeners.get(i).onCollisionDetected(collisionObject);
        }
    }

    public void onCollisionEnded(BoxCollider collisionObject) {
        --mCollidingCollidersCount;
        for (int i = 0; i < mListeners.size(); ++i) {
            mListeners.get(i).onCollisionEnded(collisionObject);
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

        if (getApplication().getDebug().drawColliders) {
            getApplication().getRenderer().renderWireframe(meshData,
                    mCollidingCollidersCount > 0 ? Color.RED : Color.GREEN,
                    mColliderAbsoluteTransformation);
        }
    }

    public Matrix44 getTransformationMatrix() {
        return mColliderAbsoluteTransformation;
    }
}
