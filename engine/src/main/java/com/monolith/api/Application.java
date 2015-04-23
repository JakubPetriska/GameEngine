package com.monolith.api;

import com.monolith.engine.MeshManager;

/**
 * Provides important engine features. Instance of this class is passed everywhere around the
 * engine.
 *
 * Contains global application functionality, such as scene changing and
 * other more specific objects containing the specific data or functionality.
 */
public abstract class Application {

    public abstract Renderer getRenderer();

    public abstract TouchInput getTouchInput();

    public abstract MeshManager getModelManager();

    public abstract Messenger getMessenger();

    public abstract CollisionSystem getCollisionSystem();

    public abstract Time getTime();

    public abstract Debug getDebug();

    public abstract Display getDisplay();

    public abstract void changeScene(String newSceneName);

    public abstract String getCurrentSceneName();
}
