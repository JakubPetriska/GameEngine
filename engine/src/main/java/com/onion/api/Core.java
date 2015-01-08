package com.onion.api;

import com.onion.platform.Renderer;

/**
 * Object of this class holds al important parts of the engine.
 */
public class Core {

    public final Renderer renderer;

    public Core(Renderer renderer) {
        this.renderer = renderer;
    }
}
