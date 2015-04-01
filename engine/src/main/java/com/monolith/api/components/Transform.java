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

    public static final Vector3 UP = new Vector3(0, 1, 0);
    public static final Vector3 FRONT = new Vector3(0, 0, 1);

    /**
     * Translation of this transform relative to parent.
     */
    public final Vector3 position = new Vector3();

    /**
     * Rotation of this transform relative to parent.
     */
    public final Vector3 rotation = new Vector3();

    /**
     * Scale of this transform relative to parent.
     */
    public final Vector3 scale = new Vector3();

    public void translate(float x, float y, float z) {
        position.x += x;
        position.y += y;
        position.z += z;
    }

    public void rotate(float x, float y, float z) {
        rotation.x += x;
        rotation.y += y;
        rotation.z += z;
    }
}
