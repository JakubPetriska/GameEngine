package com.monolith.api;

/**
 * Container storing information about one currently running touch event
 * in one particular frame.
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

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public int getState() {
        return mState;
    }

    public void setState(int state) {
        this.mState = state;
    }

    public float getX() {
        return mX;
    }

    public void setX(float x) {
        this.mX = x;
    }

    public float getY() {
        return mY;
    }

    public void setY(float y) {
        this.mY = y;
    }

    public float getStartX() {
        return mStartX;
    }

    public void setStartX(float mStartX) {
        this.mStartX = mStartX;
    }

    public float getStartY() {
        return mStartY;
    }

    public void setStartY(float mStartY) {
        this.mStartY = mStartY;
    }
}
