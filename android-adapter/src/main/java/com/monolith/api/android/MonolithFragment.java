package com.monolith.api.android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.monolith.android.Constants;
import com.monolith.android.EngineObjectStore;
import com.monolith.android.MyGLSurfaceView;
import com.monolith.api.external.InputMessenger;
import com.monolith.engine.Engine;

/**
 * {@link android.support.v4.app.Fragment} subclass showing the engine content.
 * This can be used anywhere in your application.
 */
public class MonolithFragment extends Fragment {

    private String mDefaultSceneName = null;
    private MyGLSurfaceView mGlSurfaceView;

    /**
     * Creates new instance of this fragment. Default scene will be displayed.
     *
     * @return New instance of this fragment.
     */
    public static MonolithFragment newInstance() {
        return new MonolithFragment();
    }

    /**
     * Creates new instance of this fragment. Scene specified as a parameter will be
     * shown or default scene if the name of the scene is null.
     *
     * @param defaultSceneName Name of the scene to show in the engine. If null default scene
     *                         will be displayed.
     * @return New instance of this fragment.
     */
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

    /**
     * Returns the {@link com.monolith.api.external.InputMessenger} instance.
     *
     * @return The {@link com.monolith.api.external.InputMessenger} instance.
     */
    public InputMessenger getInputMessenger() {
        return mGlSurfaceView == null ? null : mGlSurfaceView.getEngine().getInputMessenger();
    }
}
