package com.onion.api;

import org.lwjgl.util.vector.Vector3f;

/**
 * Transform hold transformation data of a GameObject.
 *
 * Transformation is relative to parent GameObject. World (absolute) transformation
 * data can be obtained using appropriate methods.
 */
public class Transform extends Component {

	public final Vector3f position = new Vector3f();

    private final Vector3f mCachedParentPosition = new Vector3f();

    protected Transform(Core core, GameObject gameObject) {
        super(core, gameObject);
    }

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
        if(gameObject.parent != null) {
            gameObject.parent.transform.getWorldPosition(mCachedParentPosition);
        } else {
            mCachedParentPosition.set(0, 0, 0);
        }
        Vector3f.add(mCachedParentPosition, position, output);
    }
}
