package com.monolith.platform;

import com.monolith.api.TouchInput;

/**
 * Internal representation of {@link com.monolith.api.TouchInput} that specific platform must
 * provide.
 */
public interface TouchInputInternal extends TouchInput, com.monolith.engine.System {

}
