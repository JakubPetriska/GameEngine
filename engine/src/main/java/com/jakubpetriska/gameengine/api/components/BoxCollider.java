package com.jakubpetriska.gameengine.api.components;

import com.jakubpetriska.gameengine.api.GameObject;
import com.jakubpetriska.gameengine.api.CollisionsSystem;
import com.jakubpetriska.gameengine.api.Color;
import com.jakubpetriska.gameengine.api.Component;
import com.jakubpetriska.gameengine.api.MeshData;
import com.jakubpetriska.gameengine.api.Primitives;
import com.jakubpetriska.gameengine.api.math.Matrix44;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents collider in the shape of the box.
 * <p/>
 * Collider cannot be rotated on it's own but is rotated by transformations of it's
 * {@link GameObject} and all parent {@link GameObject GameObjects}.
 */
public class BoxCollider extends Component {

    /**
     * Group into which this collider belongs.
     *
     * Colliders in the same group are not checked for collisions.
     *
     * Colliders that have no group set are not considered to be part of any group
     * and are checked for collision with all colliders.
     */
    public String group;

    /**
     * Size in the X axis.
     */
    public float sizeX = 1;

    /**
     * Size in the Y axis.
     */
    public float sizeY = 1;

    /**
     * Size in the Z axis.
     */
    public float sizeZ = 1;

    /**
     * Position offset in the X axis.
     */
    public float offsetX = 0;

    /**
     * Position offset in the Y axis.
     */
    public float offsetY = 0;

    /**
     * Position offset in the Z axis.
     */
    public float offsetZ = 0;

    private final Matrix44 mColliderAbsoluteTransformation = new Matrix44();
    private final Matrix44 sColliderLocalTransformation = new Matrix44(); // Used during calculations
    private final List<CollisionListener> mListeners = new ArrayList<>();

    private int mCollidingCollidersCount = 0;

    private MeshData meshData;

    /**
     * Listener that allows listening for starts and ends of collisions involving this collider.
     */
    public interface CollisionListener {
        /**
         * Called when collision of this collider with another collider starts.
         *
         * @param collisionObject Collider that this collider is colliding with.
         */
        void onCollisionDetected(BoxCollider collisionObject);

        /**
         * Called when collision of this collider with another collider ends.
         *
         * @param collisionObject Collider that this collider was colliding with.
         */
        void onCollisionEnded(BoxCollider collisionObject);
    }

    /**
     * Register {@link BoxCollider.CollisionListener} to listen for
     * collision events. Any number of listeners can be registered.
     *
     * @param listener Listener to register.
     */
    public void registerCollisionListener(CollisionListener listener) {
        if (!mListeners.contains(listener)) {
            mListeners.add(listener);
        }
    }

    /**
     * Unregister {@link BoxCollider.CollisionListener} from listening for
     * collision events.
     *
     * @param listener Listener to unregister.
     */
    public void unregisterCollisionListener(CollisionListener listener) {
        mListeners.remove(listener);
    }

    @Override
    public void start() {
        if (getApplication().getDebug().drawColliders) {
            this.meshData = getApplication().getMeshManager().getMeshData(Primitives.CUBE);
        }
        getApplication().getCollisionsSystem().registerCollider(this);
    }

    @Override
    public void finish() {
        getApplication().getCollisionsSystem().unregisterCollider(this);
    }

    /**
     * Called by the {@link CollisionsSystem} when collision of this collider with
     * another collider starts.
     *
     * @param collisionObject Collider that this collider is colliding with.
     */
    public void onCollisionDetected(BoxCollider collisionObject) {
        ++mCollidingCollidersCount;
        for (int i = 0; i < mListeners.size(); ++i) {
            mListeners.get(i).onCollisionDetected(collisionObject);
        }
    }

    /**
     * Called by the {@link CollisionsSystem} when collision of this collider with
     * another collider ends.
     *
     * @param collisionObject Collider that this collider was colliding with.
     */
    public void onCollisionEnded(BoxCollider collisionObject) {
        --mCollidingCollidersCount;
        for (int i = 0; i < mListeners.size(); ++i) {
            mListeners.get(i).onCollisionEnded(collisionObject);
        }
    }

    @Override
    public void postUpdate() {
        // Transformation matrix for this collider needs to be recalculated here
        // It is later used by CollisionsSystem
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

    /**
     * Returns the absolute transformation matrix for this collider. Transformation contains
     * it's relative offset and size.
     *
     * @return The absolute transformation matrix for this collider.
     */
    public Matrix44 getTransformationMatrix() {
        return mColliderAbsoluteTransformation;
    }
}
