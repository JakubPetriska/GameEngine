package com.monolith.api;

import com.monolith.engine.config.model.debug.DebugSettingsModel;

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

    public DebugSettings(DebugSettingsModel model) {
        this.debug = true;
        this.drawColliders = model.drawColliders == null ? true : model.drawColliders;
    }
}
