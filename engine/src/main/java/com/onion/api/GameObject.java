package com.onion.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class representing an object in game.
 *
 * GameObject only acts as container for components which
 * give object properties. For example mesh rendering is also
 * done using a component.
 */
public class GameObject {

    private Application mApplication;
    private GameObject mParent;

    /**
     * List containing all GameObject's children.
     *
     * Unmodifiable, use appropriate GameObject instance
     * methods to remove children.
     */
    public final List<GameObject> children;
    private final List<GameObject> mChildren = new ArrayList<>();

    /**
     * List containing all GameObject's components.
     *
     * Unmodifiable, use appropriate GameObject instance
     * methods to add or remove components.
     */
    public final List<Component> components;
    private final List<Component> mComponents = new ArrayList<>();

    public final Transform transform;

    public GameObject(Application application, GameObject parent) {
        this.mApplication = application;
        this.mParent = parent;
        children = Collections.unmodifiableList(mChildren);
        components = Collections.unmodifiableList(mComponents);

        if(parent != null) {
            parent.mChildren.add(this);
        }

        transform = new Transform();
        addComponent(transform);
    }

    public GameObject getParent() {
        return mParent;
    }

    public void setParent(GameObject parent) {
        if(getParent() == null) {
            throw new IllegalStateException("Cannot change parent of top level object.");
        }
        if(parent == null) {
            throw new IllegalStateException("GameObject's parent cannot be set to null.");
        }

        getParent().mChildren.remove(this);
        parent.mChildren.add(this);
        mParent = parent;
    }

    /**
     * Removes childObject from children of this object if
     * childObject is a child of this GameObject.
     *
     * After removing, childObject is no longer valid GameObject
     * and cannot be used any further. Also no references to it should be
     * kept.
     *
     * @param childObject Child GameObject to remove.
     */
    public void removeChild(GameObject childObject) {
        if(mChildren.contains(childObject)) {
            // TODO remove all components here
            for(int i = 0; i < childObject.mChildren.size(); ++i) {
                childObject.removeChild(childObject.mChildren.get(i));
            }
            mChildren.remove(childObject);
        }
    }

    public void addComponent(Component component) {
        if(!mComponents.contains(component)) {
            component.setup(mApplication, this);
            mComponents.add(component);

            component.start();
        }
    }

    public void removeComponent(Component component) {
        if(mComponents.contains(component)) {
            component.finish();
            mComponents.remove(component);
        }
    }
}
