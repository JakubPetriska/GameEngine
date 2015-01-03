package com.jeb.api;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing an object in game.
 * Never create this class using its constructor,
 * always use Application class and corresponding methods.
 */
public class GameObject {
    public final Transform transform = new Transform();

    public final List<Component> components = new ArrayList<Component>();

    public GameObject parent;
    public final List<GameObject> children = new ArrayList<GameObject>();

    protected GameObject() {

    }

    public GameObject(GameObject parent) {
        this.parent = parent;
    }
}
