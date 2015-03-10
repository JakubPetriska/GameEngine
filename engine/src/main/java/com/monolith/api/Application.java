package com.monolith.api;

import com.monolith.engine.MeshManager;
import com.monolith.engine.messaging.Messenger;
import com.monolith.platform.Renderer;

/**
 * Object providing access to other objects that provide important features.
 */
public interface Application {
    Renderer getRenderer();
    TouchInput getTouchInput();
    MeshManager getMeshManager();
    Messenger getMessenger();
    Time getTime();
    DebugLog getDebugLog();

    void changeScene(String newSceneName);
    String getCurrentSceneName();
}
