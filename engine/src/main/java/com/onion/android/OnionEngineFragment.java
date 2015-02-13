package com.onion.android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Jakub Petriska on 14. 2. 2015.
 */
public class OnionEngineFragment extends Fragment {

    private MyGLSurfaceView mGlSurfaceView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mGlSurfaceView = new MyGLSurfaceView(getActivity());
        return mGlSurfaceView;
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
