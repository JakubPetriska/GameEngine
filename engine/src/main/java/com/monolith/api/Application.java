package com.monolith.api;

import com.monolith.engine.MeshManager;
import com.monolith.engine.messaging.Messenger;
import com.monolith.platform.Renderer;

/**
 * Object providing access to other objects that provide important features.
 */
public interface Application {
    public Renderer getRenderer();
    public TouchInput getTouchInput();
    public MeshManager getMeshManager();
    public Messenger getMessenger();

    public void changeScene(String newSceneName);
    public String getCurrentSceneName();
}
