package com.jeb.engine;

import com.jeb.platform.Platform;
import com.jeb.platform.Renderer;

/**
 * Object of this class holds al important parts of the engine.
 */
public class Core {

    public final Platform platform;
    public final Renderer renderer;

    public Core(Platform platform, Renderer renderer) {
        this.platform = platform;
        this.renderer = renderer;
    }
}
