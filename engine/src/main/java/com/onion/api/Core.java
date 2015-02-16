package com.onion.api;

import com.onion.engine.MeshManager;
import com.onion.platform.Renderer;

/**
 * Object of this class holds al important parts of the engine.
 */
public class Core {

    public final Renderer renderer;
    public final TouchInput touchInput;

    public MeshManager meshManager;

    public Core(Renderer renderer, TouchInput touchInput) {
        this.renderer = renderer;
        this.touchInput = touchInput;
    }

    /**
     * Sets MeshManager instance to this object only if it was not already set.
     * @param meshManager
     */
    public void setMeshManager(MeshManager meshManager) {
        if(this.meshManager != null) {
            return;
        }
        this.meshManager = meshManager;
    }
}
