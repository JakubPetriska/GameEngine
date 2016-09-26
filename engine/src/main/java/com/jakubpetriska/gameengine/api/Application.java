package com.jakubpetriska.gameengine.api;

import com.jakubpetriska.gameengine.engine.MeshManager;

/**
 * Provides important engine features. Instance of this class is passed everywhere around the
 * engine.
 * <p/>
 * Contains global application functionality, such as scene changing and
 * other more specific objects containing the specific data or functionality.
 */
public abstract class Application {

    public abstract Renderer getRenderer();

    public abstract TouchInput getTouchInput();

    public abstract MeshManager getMeshManager();

    public abstract Messenger getMessenger();

    public abstract CollisionsSystem getCollisionsSystem();

    public abstract Time getTime();

    public abstract Debug getDebug();

    /**
     * Keep in mind that this method can return different instance in different frames.
     * And therefore result of this method should not be cached.
     * This can happen due to change in screen orientation.
     *
     * @return Instance of Display relevant for current frame.
     */
    public abstract Display getDisplay();

    public abstract void changeScene(String newSceneName);

    public abstract String getCurrentSceneName();
}
