package com.monolith.game.engine.support;

import com.monolith.api.Component;
import com.monolith.game.Config;

/**
 * Simple {@link com.monolith.api.Component} computing FPS and sending float objects
 * with FPS values as messages out of the engine.
 */
public class FpsReporter extends Component {

    private static final int AVERAGING_FACTOR = 5;

    private int frameCount = 0;
    private float frameTimeSum = 0;

    @Override
    public void update() {
        if(!Config.SHOW_FPS) {
            return;
        }

        frameTimeSum += getApplication().getTime().getTimeDelta();
        ++frameCount;

        if(frameCount == AVERAGING_FACTOR) {
            float fps = frameCount / frameTimeSum;
            getApplication().getMessenger().sendMessage(fps);

            frameCount = 0;
            frameTimeSum = 0;
        }
    }
}
