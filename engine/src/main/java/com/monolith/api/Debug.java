package com.monolith.api;

/**
 * Provides information about debug settings for the current application version
 * and provides debugging functionality.
 */
public abstract class Debug {

    /**
     * Marks whether application debugging settings were provided.
     */
    public final boolean debug;

    /**
     * Marks if colliders should be drawn.
     */
    public final boolean drawColliders;

    public Debug(boolean debug, boolean drawColliders) {
        this.debug = debug;
        this.drawColliders = drawColliders;
    }

    /**
     * Logs message int debug log.
     * @param message Message to log.
     */
    public abstract void log(String message);
}
