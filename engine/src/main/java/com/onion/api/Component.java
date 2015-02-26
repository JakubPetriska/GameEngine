package com.onion.api;

/**
 * Created by Jakub Petriska on 29. 12. 2014.
 */
public abstract class Component {

    private Application mApplication;
    private GameObject mGameObject;

    public Component() {
    }

    /**
     * Sets up this component. Can be called only once.
     * @param gameObject GameObject to which this component will be assigned.
     */
    void setup(Application application, GameObject gameObject) {
        if(mApplication != null || mGameObject != null) {
            throw new IllegalStateException("Component can be added to GameObject only once.");
        }

        this.mApplication = application;
        if(gameObject == null) {
            throw new IllegalStateException("Component's owner cannot be null.");
        }
        this.mGameObject = gameObject;
    }

    protected final Application getApplication() {
        return mApplication;
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
