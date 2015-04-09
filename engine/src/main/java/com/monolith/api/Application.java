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
public abstract class Application {
    public final DebugSettings debugSettings;

    public Application(DebugSettings debugSettings) {
        this.debugSettings = debugSettings;
    }

    public abstract Renderer getRenderer();

    public abstract TouchInput getTouchInput();

    public abstract MeshManager getModelManager();

    public abstract Messenger getMessenger();

    public abstract Time getTime();

    public abstract DebugUtility getDebugUtility();

    public abstract void changeScene(String newSceneName);

    public abstract String getCurrentSceneName();
}
