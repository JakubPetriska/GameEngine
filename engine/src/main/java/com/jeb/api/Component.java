package com.jeb.api;

import com.jeb.engine.Core;

/**
 * Created by Jakub Petriska on 29. 12. 2014.
 */
public abstract class Component {

    protected final Core core;
    public final GameObject gameObject;

    /**
     * Creates new component and assigns it to it's owner.
     *
     * This constructor must always be present in every user created subclass. If overridden
     * super constructor must always be called.
     * @param gameObject
     */
    protected Component(Core core, GameObject gameObject) {
        this.core = core;
        if(gameObject == null) {
            throw new IllegalStateException("Component's owner cannot be null.");
        }
        this.gameObject = gameObject;
        gameObject.components.add(this);

        init();
    }

    /**
     * Used to setup the component.
     */
    public void init() {

    }

    /**
     * Used to update state.
     */
    public void update() {

    }

    /**
     * Used for performing actions after state update, such as rendering.
     */
    public void postUpdate() {

    }

    /**
     * Used for cleanup.
     */
    // so far not implemented
//    public void finish() {
//
//    }

}
