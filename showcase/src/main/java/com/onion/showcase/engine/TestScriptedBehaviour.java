package com.onion.showcase.engine;

import com.onion.api.Component;
import com.onion.api.Touch;

import java.util.List;

public class TestScriptedBehaviour extends Component {

    private static final float FACTOR = 0.005f;

    private float mLastTouchX = -1;
    private float mLastTouchY = -1;

    @Override
    public void update() {
        List<Touch> touches = getApplication().getTouchInput().getTouches();
        if(touches.size() > 0) {
            Touch touch = touches.get(0);
            float currentTouchX = touch.getX();
            float currentTouchY = touch.getY();

            if(mLastTouchX != -1 && mLastTouchY != -1) {
                getGameObject().transform.moveBy(
                        -(currentTouchX - mLastTouchX) * FACTOR,
                        -(currentTouchY - mLastTouchY) * FACTOR,
                        0);
            }

            if(touch.getState() == Touch.STATE_ENDED) {
                mLastTouchX = -1;
                mLastTouchY = -1;
            } else {
                mLastTouchX = currentTouchX;
                mLastTouchY = currentTouchY;
            }
        }
    }
}
