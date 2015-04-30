package com.monolith.api;

import com.monolith.api.components.BoxCollider;
import com.monolith.api.math.Matrix44;
import com.monolith.api.math.Vector3;
import com.monolith.engine.ISystem;

import java.util.ArrayList;
import java.util.List;

/**
 * Detects collisions of colliders in the scene.
 */
public class CollisionSystem implements ISystem {

    private List<BoxCollider> mColliders = new ArrayList<>();
    private List<List<BoxCollider>> mCollidingColliders = new ArrayList<>();
    private List<Obb> mObbs = new ArrayList<>();

    // These represent coordinates of second OBB represented in coordinate space of the first OBB.
    private final Matrix44 mRotation = new Matrix44();
    private final Matrix44 mAbsRotation = new Matrix44();
    private final Vector3 mTranslation = new Vector3();

    public CollisionSystem() {
        mRotation.setIdentity();
        mAbsRotation.setIdentity();
    }

    private static class Obb {
        final Vector3 center = new Vector3();
        final Vector3[] axes = new Vector3[3];
        final Vector3 size = new Vector3();

        public Obb() {
            for (int i = 0; i < axes.length; ++i) {
                axes[i] = new Vector3();
            }
        }

        /**
         * Prepare the OBB to be transformed.
         * <p/>
         * During this center is set to 0 vector and axes to respective x, y and z axes.
         */
        void reset() {
            center.set(0, 0, 0);
            axes[0].set(1, 0, 0);
            axes[1].set(0, 1, 0);
            axes[2].set(0, 0, 1);
        }
    }

    /**
     * Register collider into the system. Collisions are only
     * detected on registered colliders.
     *
     * @param collider Collider to register.
     */
    public void registerCollider(BoxCollider collider) {
        if (!mColliders.contains(collider)) {
            mColliders.add(collider);
            mObbs.add(new Obb());
            mCollidingColliders.add(new ArrayList<BoxCollider>());
        }
    }

    /**
     * Unregister collider from the system.
     *
     * @param collider Collider to unregister.
     */
    public void unregisterCollider(BoxCollider collider) {
        int index = mColliders.indexOf(collider);
        if (index > -1) {
            mColliders.remove(index);
            // TODO maybe keep the OBB and List<BoxCollider> objects in cache for a while to avoid too much garbage collection
            mObbs.remove(index);
            List<BoxCollider> collidingColliders = mCollidingColliders.remove(index);
            while (collidingColliders.size() > 0) {
                BoxCollider collidingCollider = collidingColliders.remove(0);
                collider.onCollisionEnded(collidingCollider);
                collidingCollider.onCollisionEnded(collider);
            }
        }
    }

    @Override
    public void update() {

    }

    @Override
    public void postUpdate() {
        for (int i = 0; i < mColliders.size() - 1; ++i) {
            for (int j = i + 1; j < mColliders.size(); ++j) {
                BoxCollider firstCollider = mColliders.get(i);
                BoxCollider secondCollider = mColliders.get(j);

                Obb firstObb = mObbs.get(i);
                Obb secondObb = mObbs.get(j);

                if (i == 0) {
                    if (j == 1) {
                        transformObb(firstCollider, firstObb);
                    }
                    transformObb(secondCollider, secondObb);
                }

                boolean colliding;
                if (firstCollider.isStatic && secondCollider.isStatic) {
                    colliding = false;
                } else {
                    colliding = testCollision(firstObb, secondObb);
                }
                // TODO optimize this search
                List<BoxCollider> collidingColliders = mCollidingColliders.get(i);
                int index = collidingColliders.indexOf(secondCollider);
                boolean wereCollidingBefore = index > -1;
                if (colliding && !wereCollidingBefore) {
                    firstCollider.onCollisionDetected(secondCollider);
                    secondCollider.onCollisionDetected(firstCollider);
                    collidingColliders.add(secondCollider);
                } else if (!colliding && wereCollidingBefore) {
                    firstCollider.onCollisionEnded(secondCollider);
                    secondCollider.onCollisionEnded(firstCollider);
                    collidingColliders.remove(index);
                }
            }
        }
    }

    private void transformObb(BoxCollider collider, Obb obb) {
        obb.reset();

        Matrix44 transformation = collider.getTransformationMatrix();
        transformation.transformPoint(obb.center);
        transformation.transformVector(obb.axes[0]);
        transformation.transformVector(obb.axes[1]);
        transformation.transformVector(obb.axes[2]);

        float xAxisLength = obb.axes[0].length();
        obb.axes[0].divide(xAxisLength);
        float yAxisLength = obb.axes[1].length();
        obb.axes[1].divide(yAxisLength);
        float zAxisLength = obb.axes[2].length();
        obb.axes[2].divide(zAxisLength);

        obb.size.set(
                0.5f * xAxisLength,
                0.5f * yAxisLength,
                0.5f * zAxisLength
        );
    }

    private boolean testCollision(Obb a, Obb b) {
        Vector3.subtract(mTranslation, b.center, a.center);
        float translationX = Vector3.dot(mTranslation, a.axes[0]);
        float translationY = Vector3.dot(mTranslation, a.axes[1]);
        float translationZ = Vector3.dot(mTranslation, a.axes[2]);
        mTranslation.set(translationX, translationY, translationZ);

        float[] aSize = a.size.getValues();
        float[] bSize = b.size.getValues();
        float[] t = mTranslation.getValues();
        float rA;
        float rB;

        // Testing on axes of first OBB
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; j++) {
                mRotation.set(i, j, Vector3.dot(a.axes[i], b.axes[j]));
                mAbsRotation.set(i, j, Math.abs(mRotation.get(i, j)));
            }

            rA = aSize[i];
            rB = bSize[0] * mAbsRotation.get(i, 0)
                    + bSize[1] * mAbsRotation.get(i, 1)
                    + bSize[2] * mAbsRotation.get(i, 2);
            if (Math.abs(t[i]) > rA + rB) {
                return false;
            }
        }

        // Testing on axes of second OBB
        for (int i = 0; i < 3; ++i) {
            rA = aSize[0] * mAbsRotation.get(0, i)
                    + aSize[1] * mAbsRotation.get(1, i)
                    + aSize[2] * mAbsRotation.get(2, i);
            rB = bSize[i];
            if (Math.abs(t[0] * mRotation.get(0, i) + t[1] * mRotation.get(1, i) + t[2] * mRotation.get(2, i))
                    > rA + rB) {
                return false;
            }
        }

        // Test a0 x b0
        rA = aSize[1] * mAbsRotation.get(2, 0) + aSize[2] * mAbsRotation.get(1, 0);
        rB = bSize[1] * mAbsRotation.get(0, 2) + bSize[2] * mAbsRotation.get(0, 1);
        if (Math.abs(t[2] * mRotation.get(1, 0) - t[1] * mRotation.get(2, 0))
                > rA + rB) {
            return false;
        }

        // Test a0 x b1
        rA = aSize[1] * mAbsRotation.get(2, 1) + aSize[2] * mAbsRotation.get(1, 1);
        rB = bSize[0] * mAbsRotation.get(0, 2) + bSize[2] * mAbsRotation.get(0, 0);
        if (Math.abs(t[2] * mRotation.get(1, 1) - t[1] * mRotation.get(2, 1))
                > rA + rB) {
            return false;
        }

        // Test a0 x b2
        rA = aSize[1] * mAbsRotation.get(2, 2) + aSize[2] * mAbsRotation.get(1, 2);
        rB = bSize[0] * mAbsRotation.get(0, 1) + bSize[1] * mAbsRotation.get(0, 0);
        if (Math.abs(t[2] * mRotation.get(1, 2) - t[1] * mRotation.get(2, 2))
                > rA + rB) {
            return false;
        }

        // Test a1 x b0
        rA = aSize[0] * mAbsRotation.get(2, 0) + aSize[2] * mAbsRotation.get(0, 0);
        rB = bSize[1] * mAbsRotation.get(1, 2) + bSize[2] * mAbsRotation.get(1, 1);
        if (Math.abs(t[0] * mRotation.get(2, 0) - t[2] * mRotation.get(0, 0))
                > rA + rB) {
            return false;
        }

        // Test a1 x b1
        rA = aSize[0] * mAbsRotation.get(2, 1) + aSize[2] * mAbsRotation.get(0, 1);
        rB = bSize[0] * mAbsRotation.get(1, 2) + bSize[2] * mAbsRotation.get(1, 0);
        if (Math.abs(t[0] * mRotation.get(2, 1) - t[2] * mRotation.get(0, 1))
                > rA + rB) {
            return false;
        }

        // Test a1 x b2
        rA = aSize[0] * mAbsRotation.get(2, 2) + aSize[2] * mAbsRotation.get(0, 2);
        rB = bSize[0] * mAbsRotation.get(1, 1) + bSize[1] * mAbsRotation.get(1, 0);
        if (Math.abs(t[0] * mRotation.get(2, 2) - t[2] * mRotation.get(0, 2))
                > rA + rB) {
            return false;
        }

        // Test a2 x b0
        rA = aSize[0] * mAbsRotation.get(1, 0) + aSize[1] * mAbsRotation.get(0, 0);
        rB = bSize[1] * mAbsRotation.get(2, 2) + bSize[2] * mAbsRotation.get(2, 1);
        if (Math.abs(t[1] * mRotation.get(0, 0) - t[0] * mRotation.get(1, 0))
                > rA + rB) {
            return false;
        }

        // Test a2 x b1
        rA = aSize[0] * mAbsRotation.get(1, 1) + aSize[1] * mAbsRotation.get(0, 1);
        rB = bSize[0] * mAbsRotation.get(2, 2) + bSize[2] * mAbsRotation.get(2, 0);
        if (Math.abs(t[1] * mRotation.get(0, 1) - t[0] * mRotation.get(1, 1))
                > rA + rB) {
            return false;
        }

        // Test a2 x b2
        rA = aSize[0] * mAbsRotation.get(1, 2) + aSize[1] * mAbsRotation.get(0, 2);
        rB = bSize[0] * mAbsRotation.get(2, 1) + bSize[1] * mAbsRotation.get(2, 0);
        if (Math.abs(t[1] * mRotation.get(0, 2) - t[0] * mRotation.get(1, 2))
                > rA + rB) {
            return false;
        }

        return true;
    }
}
