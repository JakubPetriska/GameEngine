package com.monolith.showcase.engine;

import com.monolith.api.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO add commentary
 */
public class RotationTestController extends Component {

    private static final float ROTATION_PER_SECOND = 15;

    private List<String> messages = new ArrayList<>();
    private String currentRotationAxis = "x";

    @Override
    public void update() {
        getApplication().getMessenger().getMessages(messages, String.class);
        if(messages.size() > 0) {
            String newRotationAxis = messages.get(0);
            getGameObject().transform.setRotation(0, 0, 0); // Reset rotation when axis changes
            currentRotationAxis = newRotationAxis;
            messages.clear();
        }

        float rotationDifference = getApplication().getTime().getTimeDelta() * ROTATION_PER_SECOND;
        getGameObject().transform.rotateBy(
                "x".equals(currentRotationAxis) ? rotationDifference : 0,
                "y".equals(currentRotationAxis) ? rotationDifference : 0,
                "z".equals(currentRotationAxis) ? rotationDifference : 0
        );
    }
}
