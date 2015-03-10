package com.monolith.android;

import android.content.Context;
import android.util.Log;

import com.monolith.platform.Platform;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Jakub Petriska on 3. 1. 2015.
 */
public class AndroidPlatform implements Platform {

    public static final String LOG_TAG = "Monolith engine";

    private Context mContext;

    public AndroidPlatform(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public InputStream getAssetFile(String path) {
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
}
