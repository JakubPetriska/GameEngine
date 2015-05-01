package com.monolith.api;

import java.util.List;

/**
 * Provides access to application's touch input.
 */
public interface TouchInput {

    /**
     * Returns list of {@link com.monolith.api.Touch} objects representing
     * states, valid for current frame, of all currently running touch events.
     * <p/>
     * Keep in mind that you should not cache or keep Touch objects anyhow.
     * Reason for this is that it is not guaranteed that in next frame there will be the
     * same Touch object having the same id as in previous frame. This behaviour can also
     * differ through platforms.
     *
     * @return List of objects representing states of all currently running touch events
     * for current frame.
     */
    List<Touch> getTouches();
}
