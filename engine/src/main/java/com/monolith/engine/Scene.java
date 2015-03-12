package com.monolith.engine;

import com.monolith.api.GameObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds top level objects of a scene.
 */
public class Scene {
    public final List<GameObject> gameObjects = new ArrayList<>();
}
