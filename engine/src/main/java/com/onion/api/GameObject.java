package com.onion.api;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing an object in game.
 * Never create this class using its constructor,
 * always use Application class and corresponding methods.
 */
public class GameObject {

    public GameObject parent;
    public final List<GameObject> children = new ArrayList<GameObject>();

    public final Transform transform;

    public final List<Component> components = new ArrayList<Component>();

    public GameObject(Core core, GameObject parent) {
        this.parent = parent;

        transform = new Transform(core, this);
        components.add(transform);
    }
}
