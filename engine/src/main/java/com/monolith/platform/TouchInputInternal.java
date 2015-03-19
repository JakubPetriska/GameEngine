package com.monolith.platform;

import com.monolith.api.TouchInput;
import com.monolith.engine.ISystem;

/**
 * Internal representation of {@link com.monolith.api.TouchInput} that specific platform must
 * provide.
 */
public interface TouchInputInternal extends TouchInput, ISystem {

}
