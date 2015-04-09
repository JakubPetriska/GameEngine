package com.monolith.api;

/**
 * Created by Jakub on 9. 4. 2015.
 */
public class DebugSettings {

    public final boolean debug;
    public final boolean drawColliders;

    public DebugSettings() {
        debug = false;
        drawColliders = false;
    }

    public DebugSettings(boolean debug, boolean drawColliders) {
        this.debug = debug;
        this.drawColliders = drawColliders;
    }
}
