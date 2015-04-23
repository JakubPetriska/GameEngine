package com.monolith.showcase.engine;

import com.monolith.api.Component;

/**
 * Rotates the {@link com.monolith.api.GameObject} in the positive direction of
 * the axis given in the incoming message.
 *
 * This serves for visualizing that all rotation directions are done right.
 */
public class TransformationTestRotationController extends Component {

    private static final float ROTATION_PER_SECOND = 15;

    private String currentRotationAxis = "x";

    @Override
    public void update() {
        String axisMessage = getApplication().getMessenger().getLastMessage(String.class);
        if (axisMessage != null) {
            getGameObject().transform.setRotation(0, 0, 0); // Reset rotation when axis changes
            currentRotationAxis = axisMessage;
        }

        float rotationDifference = getApplication().getTime().getTimeDelta() * ROTATION_PER_SECOND;
        getGameObject().transform.rotateBy(
                "x".equals(currentRotationAxis) ? rotationDifference : 0,
                "y".equals(currentRotationAxis) ? rotationDifference : 0,
                "z".equals(currentRotationAxis) ? rotationDifference : 0
        );
    }
}
