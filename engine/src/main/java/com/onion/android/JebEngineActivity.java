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
public class JebEngineActivity extends Activity {

    private MyGLSurfaceView mGlSurfaceView;
    private Engine mEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        RendererImpl renderer = new MyRendererImpl();

        mEngine = new Engine(null, new AndroidPlatform(this), renderer);

        mGlSurfaceView = new MyGLSurfaceView(this);
        mGlSurfaceView.setRenderer(renderer);
        setContentView(mGlSurfaceView);
    }

    private class MyRendererImpl extends RendererImpl {

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            super.onSurfaceCreated(gl, config);
            mEngine.onStart();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            super.onSurfaceChanged(gl, width, height);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            super.onDrawFrame(gl);
            mEngine.onUpdate();
        }
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
        mEngine.onFinish();
    }
}
