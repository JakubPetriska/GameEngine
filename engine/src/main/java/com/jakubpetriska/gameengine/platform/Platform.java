package com.jakubpetriska.gameengine.platform;

import com.jakubpetriska.gameengine.api.Display;

import java.io.InputStream;

/**
 * Represents functionality that must be separately implemented by every supported platform.
 */
public interface Platform {

    /**
     * Opens InputStreams to project files.
     * <p/>
     * In case of any exception this method must return null so
     * caller can deal with problems.
     *
     * @param path Path to the file relative to platform's specific project files folder.
     */
    InputStream getAssetFileInputStream(String path);

    /**
     * Logs the message.
     *
     * @param message Message to log.
     */
    void log(String message);

    Display createDisplay();
}
