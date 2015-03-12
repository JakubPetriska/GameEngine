package com.monolith.api;

import com.monolith.engine.MeshManager;

/**
 * Container for important parts of the engine instance.
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
