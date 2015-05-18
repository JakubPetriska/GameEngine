package com.monolith.api.android;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;

import com.monolith.android.Constants;
import com.monolith.android.EngineObjectStore;
import com.monolith.android.MonolithGLSurfaceView;
import com.monolith.api.external.ExternalMessenger;
import com.monolith.engine.Engine;

/**
 * {@link android.app.Activity} subclass showing engine content over the whole screen.
 */
public class MonolithActivity extends Activity {

    /**
     * Extra key to specify default scene name.
     */
    public static final String EXTRA_DEFAULT_SCENE_NAME = "DEFAULT_SCENE_NAME";

    private Engine mEngine;
    private MonolithGLSurfaceView mGlSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (savedInstanceState != null
                && savedInstanceState.containsKey(Constants.KEY_ENGINE_OBJECT_STORE_KEY)) {
            mEngine = EngineObjectStore.retrieve(savedInstanceState.getString(Constants.KEY_ENGINE_OBJECT_STORE_KEY));
        }
        if (mEngine == null) {
            mEngine= new Engine(getIntent().getStringExtra(EXTRA_DEFAULT_SCENE_NAME));
        }
        mGlSurfaceView = new MonolithGLSurfaceView(this, mEngine);
        setContentView(mGlSurfaceView);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        String engineObjectKey = EngineObjectStore.store(mGlSurfaceView.getEngine());
        outState.putString(Constants.KEY_ENGINE_OBJECT_STORE_KEY, engineObjectKey);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGlSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGlSurfaceView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFinishing()) {
            mGlSurfaceView.getEngine().onFinish();
        }
    }

    /**
     * Returns the {@link ExternalMessenger} instance.
     *
     * @return The {@link ExternalMessenger} instance.
     */
    protected ExternalMessenger getMessenger() {
        return mEngine.getExternalMessenger();
    }
}
