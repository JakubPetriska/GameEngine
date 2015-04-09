package com.monolith.api;

/**
 * Created by Jakub on 9. 4. 2015.
 */
public class DebugSettings {

    public final boolean drawColliders;

    public DebugSettings() {
        drawColliders = false;
    }

    public DebugSettings(boolean drawColliders) {
        this.drawColliders = drawColliders;
    }
}
