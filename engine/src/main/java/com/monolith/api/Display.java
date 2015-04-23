package com.monolith.api;

// TODO comment, add that it uses dp similar to Android
/**
 * Created by Jakub on 23. 4. 2015.
 */
public class Display {
    // TODO add size and relative position of screen window

    // TODO comment, note that w and h can change
    public final int width;
    public final int height;
    public final float densityScaleFactor;

    public Display(int width, int height, float densityScaleFactor) {
        this.width = width;
        this.height = height;
        this.densityScaleFactor = densityScaleFactor;
    }

    public float convertToDensityPixels(float pixels) {
        return pixels / densityScaleFactor;
    }
}
