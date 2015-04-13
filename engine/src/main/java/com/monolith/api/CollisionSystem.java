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
    private List<Obb> mObbs = new ArrayList<>();

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
        }
    }

    public void unregisterCollider(BoxCollider collider) {
        int index = mColliders.indexOf(collider);
        if (index > -1) {
            mColliders.remove(index);
            // TODO maybe keep the OBB objects in cache for a while to avoid too much garbage collection
            mObbs.remove(index);
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

                if (testCollision(firstObb, secondObb)) {
                    firstCollider.onCollisionDetected(secondCollider);
                    secondCollider.onCollisionDetected(firstCollider);
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
        obb.size.set(
                Math.abs(collider.sizeX / 2),
                Math.abs(collider.sizeY / 2),
                Math.abs(collider.sizeY / 2)
        );
    }

    // These represent coordinates of second OBB represented in
    // coordinate space of the first OBB.
    private final Matrix44 mRotation = new Matrix44();
    private final Matrix44 mAbsRotation = new Matrix44();
    private final Vector3 mTranslation = new Vector3();

    private boolean testCollision(Obb firstObb, Obb secondObb) {
        mRotation.setIdentity();
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

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                mAbsRotation.set(i, j, Math.abs(mRotation.get(i, j)));
            }
        }

        for (int i = 0; i < 3; ++i) {
            float ra = firstObb.size.getValues()[i];
            float rb = secondObb.size.getX() * mAbsRotation.get(i, 0)
                    + secondObb.size.getY() * mAbsRotation.get(i, 1)
                    + secondObb.size.getZ() * mAbsRotation.get(i, 2);
            if (Math.abs(mTranslation.getValues()[i]) > ra + rb) {
                return false;
            }
        }

        return true;
    }
}
