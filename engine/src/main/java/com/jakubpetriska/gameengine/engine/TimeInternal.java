package com.jakubpetriska.gameengine.engine;

import com.jakubpetriska.gameengine.api.Time;

/**
 * Internal implementation of {@link Time} interface.
 */
public class TimeInternal implements Time, ISystem {

    private final long engineInstanceStartTime;

    private float frameStart;
    private float timeDelta;

    public TimeInternal() {
        engineInstanceStartTime = java.lang.System.currentTimeMillis();
    }

    /**
     * Must be called by {@link Engine} on the start of every frame.
     * Recalculates values to be valid for this new frame.
     */
    @Override
    public void update() {
        float lastFrameStart = frameStart;

        long frameStartMillis = java.lang.System.currentTimeMillis() - engineInstanceStartTime;
        frameStart = frameStartMillis / 1000f;

        if(lastFrameStart != 0) {
            timeDelta = frameStart - lastFrameStart;
        }
    }

    @Override
    public void postUpdate() {

    }

    @Override
    public float getTimeFromStart() {
        return frameStart;
    }

    @Override
    public float getTimeDelta() {
        return timeDelta;
    }
}
