package com.monolith.api;

/**
 * Created by Jakub on 9. 4. 2015.
 */
public abstract class Debug {

    public final boolean debug;
    public final boolean drawColliders;

    public Debug(boolean debug, boolean drawColliders) {
        this.debug = debug;
        this.drawColliders = drawColliders;
    }

    public abstract void log(String message);
}
