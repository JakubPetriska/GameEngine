package com.onion.api;

/**
 * Created by Jakub Petriska on 8. 1. 2015.
 */
public class Touch {

    public static final int STATE_BEGAN = 1;
    public static final int STATE_MOVING = 2;
    public static final int STATE_ENDED = 3;

    private int mId;
    private int mState;
    private float mX;
    private float mY;

    public int getId() {
        return mId;
    }

    public int getState() {
        return mState;
    }

    public float getX() {
        return mX;
    }

    public float getY() {
        return mY;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public void setState(int state) {
        this.mState = state;
    }

    public void setX(float x) {
        this.mX = x;
    }

    public void setY(float y) {
        this.mY = y;
    }
}
