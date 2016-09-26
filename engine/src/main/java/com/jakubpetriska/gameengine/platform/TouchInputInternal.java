package com.jakubpetriska.gameengine.platform;

import com.jakubpetriska.gameengine.api.TouchInput;
import com.jakubpetriska.gameengine.engine.ISystem;

/**
 * Internal representation of {@link TouchInput} that specific platform must
 * provide.
 */
public interface TouchInputInternal extends TouchInput, ISystem {

}
