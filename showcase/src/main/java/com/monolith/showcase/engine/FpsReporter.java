package com.monolith.showcase.engine;

import com.monolith.api.Component;

/**
 * Created by Jakub on 16. 4. 2015.
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
