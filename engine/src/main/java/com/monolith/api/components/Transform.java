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

    // Used for computing, created here to avoid creating new instance over and over
    private final Vector3 helperVector = new Vector3();

    public final Vector3 position = new Vector3();

    public void translate(float x, float y, float z) {
        position.x += x;
        position.y += y;
        position.z += z;
    }

    /**
     * Returns world(absolute) transformation of this transform.
     *
     * @param output Vector in which result will be stored.
     */
    public void getWorldPosition(Vector3 output) {
        if (getGameObject().getParent() != null) {
            getGameObject().getParent().transform.getWorldPosition(helperVector);
            Vector3.add(output, position, helperVector);
        } else {
            output.set(position);
        }
    }
}
