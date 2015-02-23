package com.onion.api;

import com.onion.engine.MeshManager;
import com.onion.engine.messaging.Messenger;
import com.onion.platform.Renderer;

/**
 * Object providing access to other objects that provide important features.
 */
public interface Application {
    public Renderer getRenderer();
    public TouchInput getTouchInput();
    public MeshManager getMeshManager();
    public Messenger getMessenger();
}
