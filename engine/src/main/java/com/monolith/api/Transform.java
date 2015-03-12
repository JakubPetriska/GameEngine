package com.monolith.api;

import org.lwjgl.util.vector.Vector3f;

/**
 * Holds transformation data of a {@link com.monolith.api.GameObject}.
 *
 * Transformation is relative to parent GameObject. World (absolute) transformation
 * data can be obtained using appropriate methods.
 */
public class Transform extends Component {

	public final Vector3f position = new Vector3f();

    private final Vector3f mCachedParentPosition = new Vector3f();

    public void moveBy(float x, float y, float z) {
        position.x += x;
        position.y += y;
        position.z += z;
    }

    /**
     * Returns world(absolute) transformation of this transform.
     * @param output Vector3f in which result will be stored.
     */
    public void getWorldPosition(Vector3f output) {
        if(getGameObject().getParent() != null) {
            getGameObject().getParent().transform.getWorldPosition(mCachedParentPosition);
        } else {
            mCachedParentPosition.set(0, 0, 0);
        }
        Vector3f.add(mCachedParentPosition, position, output);
    }
}
