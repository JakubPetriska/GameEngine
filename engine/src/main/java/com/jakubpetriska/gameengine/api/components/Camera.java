package com.jakubpetriska.gameengine.api.components;

import com.jakubpetriska.gameengine.api.Component;

/**
 * Represents a camera in the scene.
 */
public class Camera extends Component {

    /**
     * Near plane distance.
     */
    public float near = 0.5f;

    /**
     * Far plane distance.
     */
    public float far = 500;

    /**
     * Vertical field of view angle in degrees.
     */
    public float fieldOfView = 60;
}
