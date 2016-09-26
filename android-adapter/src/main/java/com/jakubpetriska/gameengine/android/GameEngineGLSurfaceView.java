package com.jakubpetriska.gameengine.android;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

import com.jakubpetriska.gameengine.android.rendering.RendererImpl;
import com.jakubpetriska.gameengine.engine.Engine;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GameEngineGLSurfaceView extends GLSurfaceView {

    private Engine mEngine;
    private TouchInputImpl mTouchInput;

    public GameEngineGLSurfaceView(Context context, Engine engine) {
        super(context);
        mEngine = engine;

        mTouchInput = new TouchInputImpl();
        RendererImpl renderer = new EngineControllingRendererImpl();

        mEngine.insertProvidedObjects(new AndroidPlatform(getContext()), renderer, mTouchInput);

        setEGLContextClientVersion(2);
        setRenderer(renderer);
    }

    public Engine getEngine() {
        return mEngine;
    }

    private class EngineControllingRendererImpl extends RendererImpl {

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            mEngine.onStart();
            super.onSurfaceCreated(gl, config);
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
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        mTouchInput.onTouchEvent(event);
        return true;
    }
}
