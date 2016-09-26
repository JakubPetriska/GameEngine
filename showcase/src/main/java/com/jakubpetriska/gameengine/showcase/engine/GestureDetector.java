package com.jakubpetriska.gameengine.showcase.engine;

import com.jakubpetriska.gameengine.api.Application;
import com.jakubpetriska.gameengine.api.Display;
import com.jakubpetriska.gameengine.api.Touch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jakub on 30. 4. 2015.
 */
public class GestureDetector {

    private static final float MINIMAL_SWIPE_LENGTH_IN_DP = 30;

    private Application application;

    private List<DetectedGesture> resultList = new ArrayList<>();

    public enum DetectedGesture {
        SWIPE_LEFT, SWIPE_RIGHT
    }

    public GestureDetector(Application application) {
        this.application = application;
    }

    private List<Integer> blockedTouchIds = new ArrayList<>();

    public List<DetectedGesture> detectGestures() {
        resultList.clear();
        Display display = application.getDisplay();
        List<Touch> touches = application.getTouchInput().getTouches();
        for (Touch touch : touches) {
            if(touch.getState() == Touch.STATE_RUNNING && !blockedTouchIds.contains(touch.getId())) {
                float x = touch.getX() - touch.getStartX();
                float y = touch.getY() - touch.getStartY();
                float length = ((float) Math.sqrt(x * x + y * y)) / display.densityScaleFactor;

                if (length >= MINIMAL_SWIPE_LENGTH_IN_DP) { // Swipe detected
                    if(Math.abs(x) >= Math.abs(y)) { // Horizontal swipe
                        resultList.add(x < 0 ? DetectedGesture.SWIPE_LEFT : DetectedGesture.SWIPE_RIGHT);
                        blockedTouchIds.add(touch.getId());
                    }
                }
            } else if (touch.getState() == Touch.STATE_ENDED || touch.getState() == Touch.STATE_BEGAN) {
                blockedTouchIds.remove((Integer) touch.getId());
            }
        }
        return resultList;
    }
}
