package com.jeb.api;

import com.jeb.engine.Core;

/**
 * Created by Jakub Petriska on 29. 12. 2014.
 */
public abstract class Component {

    protected final Core mCore;
    public final GameObject owner;

    /**
     * Creates new component and adds it to it's owner.
     * @param owner
     */
    protected Component(Core core, GameObject owner) {
        this.mCore = core;
        if(owner == null) {
            throw new IllegalStateException("Component's owner cannot be null.");
        }
        this.owner = owner;
        owner.components.add(this);

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
