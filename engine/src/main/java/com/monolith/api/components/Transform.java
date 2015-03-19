package com.monolith.api.components;

import com.monolith.api.Component;
import com.monolith.api.math.Vector3;

/**
 * Holds transformation data of a {@link com.monolith.api.GameObject}.
 * <p/>
 * Transformation is relative to parent GameObject. World (absolute) transformation
 * data can be obtained using appropriate methods.
 */
public class Transform extends Component {

    public final Vector3 position = new Vector3();

    private final Vector3 mCachedParentPosition = new Vector3();

    public void moveBy(float x, float y, float z) {
        position.x += x;
        position.y += y;
        position.z += z;
    }

    /**
     * Returns world(absolute) transformation of this transform.
     *
     * @param output Vector3f in which result will be stored.
     */
    public void getWorldPosition(Vector3 output) {
        if (getGameObject().getParent() != null) {
            getGameObject().getParent().transform.getWorldPosition(mCachedParentPosition);
        } else {
            mCachedParentPosition.set(0, 0, 0);
        }
        Vector3.add(output, mCachedParentPosition, position);
    }
}
