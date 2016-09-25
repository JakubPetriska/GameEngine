package com.jakubpetriska.gameengine.android;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.jakubpetriska.gameengine.api.Display;
import com.jakubpetriska.gameengine.platform.Platform;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Jakub Petriska on 3. 1. 2015.
 */
public class AndroidPlatform implements Platform {

    public static final String LOG_TAG = "Game Engine engine";

    private Context mContext;

    public AndroidPlatform(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public InputStream getAssetFileInputStream(String path) {
        try {
            return mContext.getAssets().open(Constants.ENGINE_ASSETS_ROOT_PATH + path);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void log(String message) {
        Log.d(LOG_TAG, message);
    }

    @Override
    public Display createDisplay() {
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        android.view.Display display = wm.getDefaultDisplay();
        int width;
        int height;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point size = new Point();
            display.getSize(size);
            width = size.x;
            height = size.y;
        } else {
            width = display.getWidth();
            height = display.getHeight();
        }

        return new Display(width, height, metrics.density);
    }
}
