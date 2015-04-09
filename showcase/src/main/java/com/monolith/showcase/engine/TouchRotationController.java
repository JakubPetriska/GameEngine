package com.monolith.showcase.engine;

import com.monolith.api.Component;
import com.monolith.api.Touch;

import java.util.List;

/**
 * This is simple behaviour script. It moves it's object according
 * to touch input.
 */
public class TouchRotationController extends Component {

    private static final float FACTOR = 0.1f;

    private float mLastTouchX;
    private float mLastTouchY;

    @Override
    public void update() {
        List<Touch> touches = getApplication().getTouchInput().getTouches();
        if (touches.size() > 0) {
            Touch touch = touches.get(0);
            float currentTouchX = touch.getX();
            float currentTouchY = touch.getY();

            if (touch.getState() != Touch.STATE_BEGAN) {
                getGameObject().transform.rotateBy(
                        -(currentTouchY - mLastTouchY) * FACTOR,
                        -(currentTouchX - mLastTouchX) * FACTOR,
                        0);
            }

            if (touch.getState() == Touch.STATE_ENDED) {
                mLastTouchX = 0;
                mLastTouchY = 0;
            } else {
                mLastTouchX = currentTouchX;
                mLastTouchY = currentTouchY;
            }
        }
    }
}
