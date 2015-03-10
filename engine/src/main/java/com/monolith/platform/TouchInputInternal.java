package com.monolith.platform;

import com.monolith.api.TouchInput;

/**
 * Created by Jakub Petriska on 8. 1. 2015.
 */
public interface TouchInputInternal extends TouchInput {
    /**
     * This method will be called by engine. It should be used to promote changes
     * in touches into the list of touches used by user application.
     */
    void update();
}
