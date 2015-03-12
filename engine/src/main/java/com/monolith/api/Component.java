package com.monolith.api;

/**
 * Represents {@link com.monolith.api.GameObject GameObject's} component.
 *
 * Components are used to add functionality to {@link com.monolith.api.GameObject GameObjects}.
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
     * Returns the {@link com.monolith.api.GameObject} to which this Component is assigned.
     * @return {@link com.monolith.api.GameObject} to which this Component is assigned.
     */
    public final GameObject getGameObject() {
        return mGameObject;
    }

    /**
     * Called when Component is added to it's owning {@link com.monolith.api.GameObject}.
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
     * Components of all {@link com.monolith.api.GameObject GameObjects}.
     * Override this method to perform actions after state update, such as rendering.
     */
    public void postUpdate() {

    }

    /**
     * Called when Component is removed from it's owning {@link com.monolith.api.GameObject} and
     * in the end of engine instance's life.
     */
    public void finish() {

    }

}
