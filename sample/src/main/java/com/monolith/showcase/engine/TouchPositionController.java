package com.monolith.showcase.engine;

import com.monolith.api.Component;
import com.monolith.api.Touch;

import java.util.List;

/**
 * This is simple movement controlling {@link com.monolith.api.Component}.
 * It moves it's {@link com.monolith.api.GameObject} according to touch input.
 */
public class TouchPositionController extends Component {

    private static final float FACTOR = 0.005f;

    private int mLastTouchId = -1;
    private float mLastTouchX;
    private float mLastTouchY;

    @Override
    public void update() {
        List<Touch> touches = getApplication().getTouchInput().getTouches();
        if (touches.size() > 0) {
            Touch touch = null;
            if(mLastTouchId != -1) {
                // Try to retrieve the Touch we tracked last time
                for(int i = 0; i < touches.size(); ++i) {
                    Touch ithTouch = touches.get(i);
                    if(ithTouch.getId() == mLastTouchId) {
                        touch = ithTouch;
                        break;
                    }
                }
            }
            if(touch == null) {
                touch = touches.get(0);
            }

            float currentTouchX = touch.getX();
            float currentTouchY = touch.getY();

            if (touch.getState() != Touch.STATE_BEGAN && touch.getId() == mLastTouchId) {
                // Factor needs to be scaled according to screen pixel density
                // since touch coordinates are in screen pixels
                float countedFactor = FACTOR / getApplication().getDisplay().densityScaleFactor;
                getGameObject().transform.translateBy(
                        (currentTouchX - mLastTouchX) * countedFactor,
                        -(currentTouchY - mLastTouchY) * countedFactor,
                        0);
            }
            if(touch.getState() == Touch.STATE_ENDED) {
                mLastTouchId = -1;
            } else {
                mLastTouchId = touch.getId();
            }
            mLastTouchX = currentTouchX;
            mLastTouchY = currentTouchY;
        }
    }
}
