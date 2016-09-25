package com.jakubpetriska.gameengine.sample.engine;

import com.jakubpetriska.gameengine.api.Component;

/**
 * Simple {@link Component} computing FPS and sending float objects
 * with FPS values as messages out of the engine.
 */
public class FpsReporter extends Component {

    private static final int AVERAGING_FACTOR = 5;

    private int frameCount = 0;
    private float frameTimeSum = 0;

    @Override
    public void update() {
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
