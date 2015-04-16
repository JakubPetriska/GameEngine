package com.monolith.showcase.engine;

import com.monolith.api.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO add commentary
 */
public class RotationTestController extends Component {

    private static final float ROTATION_PER_SECOND = 15;

    private String currentRotationAxis = "x";

    @Override
    public void update() {
        String axisMessage = getApplication().getMessenger().getLastMessage(String.class);
        if(axisMessage != null) {
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
