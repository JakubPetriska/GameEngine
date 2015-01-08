package com.onion.api;

import com.onion.platform.Renderer;

/**
 * Object of this class holds al important parts of the engine.
 */
public class Core {

    public final Renderer renderer;
    public final TouchInput touchInput;

    public Core(Renderer renderer, TouchInput touchInput) {
        this.renderer = renderer;
        this.touchInput = touchInput;
    }
}
