package com.onion.api;

/**
 * Created by Jakub Petriska on 29. 12. 2014.
 */
public abstract class Component {

    private Core mCore;
    private GameObject mGameObject;

    protected Component() {
    }

    /**
     * Sets up this component. Can be called only once.
     * @param gameObject GameObject to which this component will be assigned.
     */
    void setup(Core core, GameObject gameObject) {
        if(mCore != null || mGameObject != null) {
            throw new IllegalStateException("Component can be setup only once");
        }

        this.mCore = core;
        if(gameObject == null) {
            throw new IllegalStateException("Component's owner cannot be null.");
        }
        this.mGameObject = gameObject;
    }

    protected final Core getCore() {
        return mCore;
    }

    public final GameObject getGameObject() {
        return mGameObject;
    }

    /**
     * Used to setup the component.
     */
    public void start() {

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
    public void finish() {

    }

}
