package com.jakubpetriska.gameengine.api;

/**
 * Describes properties of device's display and provides convenience methods
 * for working with these properties.
 */
public class Display {

    /**
     * Width of device's display in it's current orientation.
     */
    public final int width;

    /**
     * Height of device's display in it's current orientation.
     */
    public final int height;

    /**
     * Factor that describes density of display pixels.
     *
     * This factor depends on DPI of device's display and will be 1
     * when display has 160 DPI. Factor is also proportionally scaled
     * with higher or lower DPI values.
     *
     * For example it will be 2 for display with 320 DPI
     * and 3 for display with 480 DPI.
     */
    public final float densityScaleFactor;

    // TODO add size and relative position of screen window

    public Display(int width, int height, float densityScaleFactor) {
        this.width = width;
        this.height = height;
        this.densityScaleFactor = densityScaleFactor;
    }

    /**
     * Converts given value in pixels into density dependent pixels.
     *
     * Density pixels are equal to actual screen pixels when density
     * scale factor is 1.
     *
     * Value in density pixels will always correspond to the same physical size
     * on every device while values in pixels can vary greatly for
     * the same physical size on different devices.
     *
     * @param pixels Pixels value to be converted.
     * @return Input pixels values converted into density pixels.
     */
    public float convertToDensityPixels(float pixels) {
        return pixels / densityScaleFactor;
    }
}
