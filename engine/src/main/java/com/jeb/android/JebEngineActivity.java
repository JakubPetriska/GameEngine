package com.jeb.android;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Window;

import com.jeb.android.AndroidPlatform;
import com.jeb.engine.Engine;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Jakub Petriska on 29. 12. 2014.
 */
public class JebEngineActivity extends Activity {

    private Engine mEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        mEngine = new Engine(null, new AndroidPlatform(this));

        GLSurfaceView glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setRenderer(new MyRenderer());
        setContentView(glSurfaceView);
    }

    private class MyRenderer implements GLSurfaceView.Renderer {

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            mEngine.onStart();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {

        }

        @Override
        public void onDrawFrame(GL10 gl) {
            mEngine.onUpdate();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mEngine.onFinish();
    }
}
