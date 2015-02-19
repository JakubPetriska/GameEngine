package com.onion.android;

import android.view.MotionEvent;

import com.onion.api.Touch;
import com.onion.platform.TouchInputInternal;

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

    // WARNING
    // If between two calls of update more than one touch events for single pointer come
    // all but the last are lost.
    // TODO fix this, maybe rewrite the whole thing

    public void onTouchEvent(MotionEvent event) {
        int pointerId = event.getPointerId(event.getActionIndex());
        Touch touch = null;
        for(int i = 0; i < mTouchesToUpdate.size(); ++i) {
            Touch ithTouch = mTouchesToUpdate.get(i);
            if(ithTouch.getId() == pointerId) {
                touch = ithTouch;
            }
        }
        if(touch == null) {
            if(mFreeTouches.size() > 0) {
                touch = mFreeTouches.remove(0);
            } else {
                touch = new Touch();
            }
            mNewTouchesToUpdate.add(touch);
            touch.setId(pointerId);
        }
        touch.setState(actionToState(event.getActionMasked()));
        touch.setX(event.getX());
        touch.setY(event.getY());
    }

    private int actionToState(int action) {
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                return Touch.STATE_BEGAN;
            case MotionEvent.ACTION_MOVE:
                return Touch.STATE_RUNNING;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                return Touch.STATE_ENDED;
            default:
                throw new IllegalStateException("Unhandled MotionEvent action");
        }
    }

    @Override
    public void update() {
        for(int i = 0; i < mTouches.size(); ++i) {
            Touch ithTouch = mTouches.get(i);
            if(ithTouch.getState() == Touch.STATE_ENDED) {
                mFreeTouches.add(mTouches.remove(i));
            } else if(ithTouch.getState() == Touch.STATE_BEGAN) {
                // Change the state to Moving so for every gesture
                // state is BEGAN only in one game loop iteration
                ithTouch.setState(Touch.STATE_RUNNING);
            }
        }

        for(int i = 0; i < mTouchesToUpdate.size(); ++i) {
            Touch ithTouchToUpdate = mTouchesToUpdate.get(i);
            Touch ithTouch = mTouches.get(i);

            ithTouch.setState(ithTouchToUpdate.getState());
            ithTouch.setX(ithTouchToUpdate.getX());
            ithTouch.setY(ithTouchToUpdate.getY());
        }

        while(mNewTouchesToUpdate.size() > 0) {
            Touch ithTouch = mNewTouchesToUpdate.remove(0);
            mTouchesToUpdate.add(ithTouch);

            Touch newTouch;
            if(mFreeTouches.size() > 0) {
                newTouch = mFreeTouches.remove(0);
            } else {
                newTouch = new Touch();
            }

            newTouch.setState(ithTouch.getState());
            newTouch.setX(ithTouch.getX());
            newTouch.setY(ithTouch.getY());

            mTouches.add(newTouch);
        }

        for(int i = 0; i < mTouchesToUpdate.size(); ++i) {
            Touch ithTouch = mTouchesToUpdate.get(i);
            if(ithTouch.getState() == Touch.STATE_ENDED) {
                mFreeTouches.add(mTouchesToUpdate.remove(i));
            } else if(ithTouch.getState() == Touch.STATE_BEGAN) {
                // Change the state to Moving so for every gesture
                // state is BEGAN only in one game loop iteration
                ithTouch.setState(Touch.STATE_RUNNING);
            }
        }
    }

    @Override
    public List<Touch> getTouches() {
        return mTouches;
    }
}
