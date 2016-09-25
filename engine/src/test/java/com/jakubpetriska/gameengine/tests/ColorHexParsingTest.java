package com.jakubpetriska.gameengine.tests;

import com.jakubpetriska.gameengine.api.Color;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test of {@link Color} class.
 */
public class ColorHexParsingTest {

    private static final double COLOR_CHANNEL_MAX_DELTA = 0.002;

    @Test
    public void testHexColorParsing() {
        testColorHexParsing("#8000ff", 1, 0.5, 0, 1);
        testColorHexParsing("#8000ff80", 0.5, 0, 1, 0.5);
    }

    private void testColorHexParsing(String hexColorString,
                                     double expectedAlpha,
                                     double expectedRed,
                                     double expectedGreen,
                                     double expectedBlue) {
        Color color = new Color(hexColorString);
        assertEquals(expectedAlpha, color.alpha, COLOR_CHANNEL_MAX_DELTA);
        assertEquals(expectedRed, color.red, COLOR_CHANNEL_MAX_DELTA);
        assertEquals(expectedGreen, color.green, COLOR_CHANNEL_MAX_DELTA);
        assertEquals(expectedBlue, color.blue, COLOR_CHANNEL_MAX_DELTA);
    }
}
