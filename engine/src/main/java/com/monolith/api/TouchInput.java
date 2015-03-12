package com.monolith.api;

import java.util.List;

/**
 * Provides access to application's touch input.
 */
public interface TouchInput {

    /**
     * Returns list of {@link com.monolith.api.Touch} objects representing
     * states, valid for current frame, of all currently running touch events.
     * @return List of objects representing states of all currently running touch events
     * for current frame.
     */
    List<Touch> getTouches();
}
