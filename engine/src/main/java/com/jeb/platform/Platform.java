package com.jeb.platform;

import java.io.InputStream;

/**
 * Created by Jakub Petriska on 3. 1. 2015.
 */
public interface Platform {

    public InputStream getConfigFile(String path);
}
