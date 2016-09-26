package com.jakubpetriska.gameengine.api;

/**
 * Container storing information about one currently running touch event
 * in one particular frame.
 * <p/>
 * Touch event can be identified between frame using it's id.
 */
public class Touch {

    public static final int STATE_BEGAN = 1;
    public static final int STATE_RUNNING = 2;
    public static final int STATE_ENDED = 3;

    private int mId;
    private int mState;
    private float mX;
    private float mY;
    private float mStartX;
    private float mStartY;

    /**
     * Get the id of this touch event. This id can be used to track this
     * touch event across multiple frames.
     *
     * @return The id of this touch event.
     */
    public int getId() {
        return mId;
    }

    /**
     * Set the id of this touch event.
     *
     * @param id Id to set.
     */
    public void setId(int id) {
        this.mId = id;
    }

    /**
     * Return the state of this touch event.
     * <p/>
     * Can be one of {@link Touch#STATE_BEGAN}, {@link Touch#STATE_RUNNING} or {@link Touch#STATE_ENDED}.
     *
     * @return The state of this touch.
     */
    public int getState() {
        return mState;
    }

    /**
     * Set the state of this touch event.
     * <p/>
     * Must be one of {@link Touch#STATE_BEGAN}, {@link Touch#STATE_RUNNING} or {@link Touch#STATE_ENDED}.
     *
     * @param state State to set.
     */
    public void setState(int state) {
        this.mState = state;
    }

    /**
     * Get current x position of this touch event.
     *
     * @return Current x position of this touch event.
     */
    public float getX() {
        return mX;
    }

    /**
     * Set current x position of this touch event.
     *
     * @param x Position to set.
     */
    public void setX(float x) {
        this.mX = x;
    }

    /**
     * Get current y position of this touch event.
     *
     * @return Current y position of this touch event.
     */
    public float getY() {
        return mY;
    }

    /**
     * Set current y position of this touch event.
     *
     * @param y Position to set.
     */
    public void setY(float y) {
        this.mY = y;
    }

    /**
     * Get starting x position of this touch event.
     *
     * @return Starting x position of this touch event.
     */
    public float getStartX() {
        return mStartX;
    }

    /**
     * Set starting x position of this touch event.
     *
     * @param startX Position to set.
     */
    public void setStartX(float startX) {
        this.mStartX = startX;
    }

    /**
     * Get starting y position of this touch event.
     *
     * @return Starting y position of this touch event.
     */
    public float getStartY() {
        return mStartY;
    }

    /**
     * Set starting y position of this touch event.
     *
     * @param startY Position to set.
     */
    public void setStartY(float startY) {
        this.mStartY = startY;
    }
}
