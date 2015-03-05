package com.monolith.android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.monolith.engine.Engine;
import com.monolith.engine.messaging.InputMessenger;

/**
 * Created by Jakub Petriska on 14. 2. 2015.
 */
public class MonolithFragment extends Fragment {

    private String mDefaultSceneName = null;
    private MyGLSurfaceView mGlSurfaceView;

    public static MonolithFragment newInstance() {
        return new MonolithFragment();
    }

    public static MonolithFragment newInstance(String defaultSceneName) {
        MonolithFragment fragment = newInstance();
        fragment.mDefaultSceneName = defaultSceneName;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null
                && savedInstanceState.containsKey(Constants.KEY_ENGINE_OBJECT_STORE_KEY)) {
            Engine engine = EngineObjectStore.retrieve(savedInstanceState.getString(Constants.KEY_ENGINE_OBJECT_STORE_KEY));
            mGlSurfaceView = new MyGLSurfaceView(getActivity(), engine);
        } else {
            mGlSurfaceView = new MyGLSurfaceView(getActivity(), mDefaultSceneName);
        }
        return mGlSurfaceView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String engineObjectKey = EngineObjectStore.store(mGlSurfaceView.getEngine());
        outState.putString(Constants.KEY_ENGINE_OBJECT_STORE_KEY, engineObjectKey);
    }

    @Override
    public void onResume() {
        super.onResume();
        mGlSurfaceView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mGlSurfaceView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGlSurfaceView.getEngine().onFinish();
    }

    public InputMessenger getInputMessenger() {
        return mGlSurfaceView == null ? null : mGlSurfaceView.getEngine().getInputMessenger();
    }
}
