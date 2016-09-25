package com.jakubpetriska.gameengine.engine;

import com.jakubpetriska.gameengine.api.GameObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds top level objects of a scene.
 */
public class Scene {
    public final List<GameObject> gameObjects = new ArrayList<>();
}
