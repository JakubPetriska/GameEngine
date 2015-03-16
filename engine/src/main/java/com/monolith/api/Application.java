package com.monolith.api;

import com.monolith.engine.MeshManager;

/**
 * Provides important engine features. Instance of this class is passed everywhere around the
 * engine.
 *
 * Contains other more specific objects containing the specific data of functionality.
 *
 * Contains global application functionality, such as scene changing.
 */
public interface Application {
    Renderer getRenderer();

    TouchInput getTouchInput();

    MeshManager getModelManager();

    Messenger getMessenger();

    Time getTime();

    DebugLog getDebugLog();

    void changeScene(String newSceneName);

    String getCurrentSceneName();
}
