package com.monolith.api;

/**
 * This class provides valuable information about time.
 */
public interface Time {

    /**
     * Returns time of the start of this frame from
     * the start of this engine instance in seconds.
     * @return Time of the start of this frame in seconds.
     */
    float getTime();

    /**
     * Returns time delta between the start of this frame and
     * the last frame.
     * @return Time delta between the start of this frame and
     * the last frame.
     */
    float getTimeDelta();
}
