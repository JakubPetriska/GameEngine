package com.onion.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.onion.android.rendering.RendererImpl;
import com.onion.engine.Engine;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Jakub Petriska on 29. 12. 2014.
 */
public class OnionEngineActivity extends Activity {

    private MyGLSurfaceView mGlSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mGlSurfaceView = new MyGLSurfaceView(this);
        setContentView(mGlSurfaceView);
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
        mGlSurfaceView.getEngine().onFinish();
    }
}
