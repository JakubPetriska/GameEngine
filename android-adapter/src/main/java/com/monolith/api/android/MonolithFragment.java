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
import com.monolith.api.external.ExternalMessenger;
import com.monolith.engine.Engine;

/**
 * {@link android.support.v4.app.Fragment} subclass showing the engine content.
 * This can be used anywhere in your application.
 */
public class MonolithFragment extends Fragment {

    private Engine mEngine;
    private String mDefaultSceneName = null;
    private MyGLSurfaceView mGlSurfaceView;

    /**
     * Creates new instance of this fragment. Default scene will be displayed.
     *
     * @return New instance of this fragment.
     */
    public static MonolithFragment newInstance() {
        return newInstance(null);
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
        MonolithFragment fragment = new MonolithFragment();
        fragment.mDefaultSceneName = defaultSceneName;
        fragment.createEngine();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null
                && savedInstanceState.containsKey(Constants.KEY_ENGINE_OBJECT_STORE_KEY)) {
            mEngine = EngineObjectStore.retrieve(savedInstanceState.getString(Constants.KEY_ENGINE_OBJECT_STORE_KEY));
        }
        if (mEngine == null) {
            createEngine();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mGlSurfaceView = new MyGLSurfaceView(getActivity(), mEngine);
        return mGlSurfaceView;
    }

    private void createEngine() {
        mEngine = new Engine(mDefaultSceneName);
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
        if(isRemoving() || getActivity().isFinishing()) {
            mGlSurfaceView.getEngine().onFinish();
        }
    }

    /**
     * Returns the {@link ExternalMessenger} instance.
     *
     * @return The {@link ExternalMessenger} instance.
     */
    public ExternalMessenger getMessenger() {
        return mEngine.getExternalMessenger();
    }
}
