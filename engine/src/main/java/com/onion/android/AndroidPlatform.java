package com.onion.android;

import android.content.Context;

import com.onion.platform.Platform;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Jakub Petriska on 3. 1. 2015.
 */
public class AndroidPlatform implements Platform {

    private Context mContext;

    public AndroidPlatform(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public InputStream getConfigFile(String path) {
        try {
            return mContext.getAssets().open(path);
        } catch (IOException e) {
            throw new IllegalStateException("Error opening configuration file " + path + ".", e);
        }
    }
}
