package com.jakubpetriska.gameengine.showcase.engine;

import com.jakubpetriska.gameengine.api.Component;

/**
 * Created by Jakub on 6. 5. 2015.
 */
public class PickupAnimator extends Component {

    private static final float ROTATION_SPEED_DEGREES_PER_SECOND = 90;

    @Override
    public void update() {
        getGameObject().transform.rotateBy(
                0,
                ROTATION_SPEED_DEGREES_PER_SECOND * getApplication().getTime().getTimeDelta(),
                0);
    }
}
