package com.monolith.api;

import com.monolith.api.components.BoxCollider;
import com.monolith.api.math.Matrix44;
import com.monolith.api.math.Vector3;
import com.monolith.engine.ISystem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jakub on 12. 4. 2015.
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

    public void registerCollider(BoxCollider collider) {
        if (!mColliders.contains(collider)) {
            mColliders.add(collider);
            mObbs.add(new Obb());
            mCollidingColliders.add(new ArrayList<BoxCollider>());
        }
    }

    public void unregisterCollider(BoxCollider collider) {
        int index = mColliders.indexOf(collider);
        if (index > -1) {
            mColliders.remove(index);
            // TODO maybe keep the OBB and List<BoxCollider> objects in cache for a while to avoid too much garbage collection
            mObbs.remove(index);
            List<BoxCollider> collidingColliders = mCollidingColliders.remove(index);
            while(collidingColliders.size() > 0) {
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

                boolean colliding = testCollision(firstObb, secondObb);
                // TODO optimize this search
                List<BoxCollider> collidingColliders = mCollidingColliders.get(i);
                int index = collidingColliders.indexOf(secondCollider);
                boolean wereCollidingBefore = index > -1;
                if (colliding && !wereCollidingBefore) {
                    firstCollider.onCollisionDetected(secondCollider);
                    secondCollider.onCollisionDetected(firstCollider);
                    collidingColliders.add(secondCollider);
                } else if(!colliding && wereCollidingBefore) {
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

    private boolean testCollision(Obb firstObb, Obb secondObb) {
        // TODO interleave this with first tests
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                mRotation.set(i, j, Vector3.dot(firstObb.axes[i], secondObb.axes[j]));
            }
        }
        Vector3.subtract(mTranslation, secondObb.center, firstObb.center);
        float translationX = Vector3.dot(mTranslation, firstObb.axes[0]);
        float translationY = Vector3.dot(mTranslation, firstObb.axes[1]);
        float translationZ = Vector3.dot(mTranslation, firstObb.axes[2]);
        mTranslation.set(translationX, translationY, translationZ);

        // TODO interleave this with first tests
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                mAbsRotation.set(i, j, Math.abs(mRotation.get(i, j)));
            }
        }

        float[] firstObbSize = firstObb.size.getValues();
        float[] secondObbSize = secondObb.size.getValues();
        float[] translationValues = mTranslation.getValues();

        for (int i = 0; i < 3; ++i) {
            float ra = firstObbSize[i];
            float rb = secondObbSize[0] * mAbsRotation.get(i, 0)
                    + secondObbSize[1] * mAbsRotation.get(i, 1)
                    + secondObbSize[2] * mAbsRotation.get(i, 2);
            if (Math.abs(translationValues[i]) > ra + rb) {
                return false;
            }
        }

        for (int i = 0; i < 3; ++i) {
            float ra = firstObbSize[0] * mAbsRotation.get(0, i)
                    + firstObbSize[1] * mAbsRotation.get(1, i)
                    + firstObbSize[2] * mAbsRotation.get(2, i);
            float rb = secondObbSize[i];
            float t = translationX * mRotation.get(0, i)
                    + translationY * mRotation.get(1, i)
                    + translationZ * mRotation.get(2, i);
            if (Math.abs(t) > ra + rb) {
                return false;
            }
        }

        // TODO add rest of the test

        return true;
    }
}
