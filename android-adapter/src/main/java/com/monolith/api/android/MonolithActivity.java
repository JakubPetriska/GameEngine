package com.monolith.api.android;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;

import com.monolith.android.Constants;
import com.monolith.android.EngineObjectStore;
import com.monolith.android.MyGLSurfaceView;
import com.monolith.engine.Engine;

/**
 * {@link android.app.Activity} subclass showing engine content over the whole screen.
 */
public class MonolithActivity extends Activity {

    /**
     * Extra key to specify default scene name.
     */
    public static final String EXTRA_DEFAULT_SCENE_NAME = "DEFAULT_SCENE_NAME";

    private MyGLSurfaceView mGlSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Engine engine;
        if (savedInstanceState != null
                && savedInstanceState.containsKey(Constants.KEY_ENGINE_OBJECT_STORE_KEY)) {
            engine = EngineObjectStore.retrieve(savedInstanceState.getString(Constants.KEY_ENGINE_OBJECT_STORE_KEY));
        } else {
            engine = new Engine(getIntent().getStringExtra(EXTRA_DEFAULT_SCENE_NAME));
        }
        mGlSurfaceView = new MyGLSurfaceView(this, engine);
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
}
