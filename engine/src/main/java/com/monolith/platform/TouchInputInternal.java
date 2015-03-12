package com.monolith.platform;

import com.monolith.api.TouchInput;

/**
 * Internal representation of {@link com.monolith.api.TouchInput} that specific platform must
 * provide.
 */
public interface TouchInputInternal extends TouchInput {
    /**
     * This method will be called by engine. It should be used to promote changes
     * in touches into the list of touches used by user application.
     */
    void update();
}
