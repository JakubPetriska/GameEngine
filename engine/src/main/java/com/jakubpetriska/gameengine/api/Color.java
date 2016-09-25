package com.jakubpetriska.gameengine.api;

/**
 * Represents color.
 */
public class Color {

    public static final Color RED = new Color(1, 0, 0, 1);
    public static final Color GREEN = new Color(0, 1, 0, 1);
    public static final Color BLUE = new Color(0, 0, 1, 1);
    public static final Color LIGHT_GRAY = new Color(0.97f, 0.97f, 0.97f, 1);

    public float red;
    public float green;
    public float blue;
    public float alpha;

    public Color(int red, int green, int blue, int alpha) {
        this.red = red / 255f;
        this.green = green / 255f;
        this.blue = blue / 255f;
        this.alpha = alpha / 255f;
    }

    public Color(float red, float green, float blue, float alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public Color(String hexString) {
        if(hexString == null
            || hexString.isEmpty()
                || !hexString.startsWith("#")
                || !(hexString.length() != 7 || hexString.length() != 9)) {
            throw new IllegalArgumentException("Invalid format for hex color. Must start with #.");
        }
        int alphaOffset = hexString.length() == 7 ? 0 : 2;
        this.alpha = hexString.length() == 9 ? getColorChannelValue(hexString.substring(1, 3)) : 1;
        this.red = getColorChannelValue(hexString.substring(alphaOffset + 1, alphaOffset + 3));
        this.green = getColorChannelValue(hexString.substring(alphaOffset + 3, alphaOffset + 5));
        this.blue = getColorChannelValue(hexString.substring(alphaOffset + 5, alphaOffset + 7));
    }

    private float getColorChannelValue(String colorChannelHexCode) {
        return Integer.valueOf(colorChannelHexCode, 16) / 255f;
    }
}
