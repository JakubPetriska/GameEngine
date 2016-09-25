package com.jakubpetriska.gameengine.api;

/**
 * Represents {@link GameObject GameObject's} component.
 *
 * Components are used to add functionality to {@link GameObject GameObjects}.
 */
public abstract class Component {

    private Application mApplication;
    private GameObject mGameObject;

    protected Component() {
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

    /**
     * Returns the {@link GameObject} to which this Component is assigned.
     * @return {@link GameObject} to which this Component is assigned.
     */
    public final GameObject getGameObject() {
        return mGameObject;
    }

    /**
     * Called when Component is added to it's owning {@link GameObject}.
     */
    public void start() {

    }

    /**
     * Called every frame. Override this method to update state of this Component.
     */
    public void update() {

    }

    /**
     * Called every frame after {@link Component#update()} has been called on all
     * Components of all {@link GameObject GameObjects}.
     * Override this method to perform actions after state update, such as rendering.
     */
    public void postUpdate() {

    }

    /**
     * Called when Component is removed from it's owning {@link GameObject} and
     * in the end of engine instance's life.
     */
    public void finish() {

    }

}
