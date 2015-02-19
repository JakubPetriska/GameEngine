package com.onion.android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onion.engine.Engine;

/**
 * Created by Jakub Petriska on 14. 2. 2015.
 */
public class OnionEngineFragment extends Fragment {

    private MyGLSurfaceView mGlSurfaceView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null
                && savedInstanceState.containsKey(Constants.KEY_ENGINE_OBJECT_STORE_KEY)) {
            Engine engine = EngineObjectStore.retrieve(savedInstanceState.getString(Constants.KEY_ENGINE_OBJECT_STORE_KEY));
            mGlSurfaceView = new MyGLSurfaceView(getActivity(), engine);
        } else {
            mGlSurfaceView = new MyGLSurfaceView(getActivity());
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
}
