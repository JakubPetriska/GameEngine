package com.monolith.engine;

import com.monolith.api.Time;

/**
 * Internal implementation of Time interface.
 */
public class TimeInternal implements Time {

    private long engineInstanceStartTime;

    private float frameStart;
    private float timeDelta;

    public TimeInternal() {
        engineInstanceStartTime = System.currentTimeMillis();
    }

    public void update() {
        float lastFrameStart = frameStart;

        long frameStartMillis = System.currentTimeMillis() - engineInstanceStartTime;
        frameStart = frameStartMillis / 1000f;

        if(lastFrameStart != 0) {
            timeDelta = frameStart - lastFrameStart;
        }
    }

    @Override
    public float getTime() {
        return frameStart;
    }

    @Override
    public float getTimeDelta() {
        return timeDelta;
    }
}
