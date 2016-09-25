package com.jakubpetriska.gameengine.android;

import android.view.MotionEvent;

import com.jakubpetriska.gameengine.api.Touch;
import com.jakubpetriska.gameengine.platform.TouchInputInternal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jakub Petriska on 8. 1. 2015.
 */
public class TouchInputImpl implements TouchInputInternal {

    // We will create touch objects only once
    // All currently unused Touch objects will be stored here for later
    private List<Touch> mFreeTouches = new ArrayList<>();

    // This keeps states of touches right from onTouchEvent method
    // By calling update states from here are flipped to mTouches List
    private List<Touch> mTouchesToUpdate = new ArrayList<>();
    // When new touch is added to to update list it is instead added in this one
    // and moved to mTouchesToUpdate and mTouches list later during update()
    private List<Touch> mNewTouchesToUpdate = new ArrayList<>();

    // This keeps states of touches for current game loop iteration
    private List<Touch> mTouches = new ArrayList<>();

    private int actionToState(int action) {
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                return Touch.STATE_BEGAN;
            case MotionEvent.ACTION_MOVE:
                return Touch.STATE_RUNNING;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                return Touch.STATE_ENDED;
            default:
                throw new IllegalStateException("Unhandled MotionEvent action " + action);
        }
    }

    private Touch getFreeTouch() {
        if (mFreeTouches.size() > 0) {
            return mFreeTouches.remove(0);
        } else {
            return new Touch();
        }
    }

    private void saveFreeTouch(Touch touch) {
        mFreeTouches.add(touch);
    }

    // WARNING
    // If between two calls of update more than one touch events for single pointer come
    // all but the last are lost.
    // TODO fix this, maybe rewrite the whole thing

    public void onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        int state = actionToState(action);
        if(action == MotionEvent.ACTION_MOVE) {
            for(int i = 0; i < event.getPointerCount(); ++i) {
                updateTouchOnIndex(event, i, state);
            }
        } else {
            updateTouchOnIndex(event, event.getActionIndex(), state);
        }
    }

    private void updateTouchOnIndex(MotionEvent event, int index, int state) {
        int pointerId = event.getPointerId(index);
        Touch touch = null;
        for (int j = 0; j < mTouchesToUpdate.size(); ++j) {
            Touch jthTouch = mTouchesToUpdate.get(j);
            if (jthTouch.getId() == pointerId) {
                touch = jthTouch;
            }
        }
        if (touch == null) {
            touch = getFreeTouch();
            mNewTouchesToUpdate.add(touch);
            touch.setId(pointerId);

            // Set starting position of this touch
            touch.setStartX(event.getX(index));
            touch.setStartY(event.getY(index));
        }
        touch.setState(state);
        touch.setX(event.getX(index));
        touch.setY(event.getY(index));
    }

    @Override
    public void update() {
        for (int i = 0; i < mTouches.size(); ++i) {
            Touch ithTouch = mTouches.get(i);
            if (ithTouch.getState() == Touch.STATE_ENDED) {
                saveFreeTouch(mTouches.remove(i));
            } else if (ithTouch.getState() == Touch.STATE_BEGAN) {
                // Change the state to Moving so for every gesture
                // state is BEGAN only in one game loop iteration
                ithTouch.setState(Touch.STATE_RUNNING);
            }
        }

        for (int i = 0; i < mTouchesToUpdate.size(); ++i) {
            Touch ithTouchToUpdate = mTouchesToUpdate.get(i);
            Touch ithTouch = mTouches.get(i);

            ithTouch.setState(ithTouchToUpdate.getState());
            ithTouch.setX(ithTouchToUpdate.getX());
            ithTouch.setY(ithTouchToUpdate.getY());
        }

        while (mNewTouchesToUpdate.size() > 0) {
            Touch ithTouch = mNewTouchesToUpdate.remove(0);
            mTouchesToUpdate.add(ithTouch);

            Touch newTouch = getFreeTouch();
            newTouch.setState(ithTouch.getState());
            newTouch.setX(ithTouch.getX());
            newTouch.setY(ithTouch.getY());
            newTouch.setStartX(ithTouch.getStartX());
            newTouch.setStartY(ithTouch.getStartY());

            mTouches.add(newTouch);
        }

        for (int i = 0; i < mTouchesToUpdate.size(); ++i) {
            Touch ithTouch = mTouchesToUpdate.get(i);
            if (ithTouch.getState() == Touch.STATE_ENDED) {
                saveFreeTouch(mTouchesToUpdate.remove(i));
            } else if (ithTouch.getState() == Touch.STATE_BEGAN) {
                // Change the state to Moving so for every gesture
                // state is BEGAN only in one game loop iteration
                ithTouch.setState(Touch.STATE_RUNNING);
            }
        }
    }

    @Override
    public void postUpdate() {

    }

    @Override
    public List<Touch> getTouches() {
        return mTouches;
    }
}
