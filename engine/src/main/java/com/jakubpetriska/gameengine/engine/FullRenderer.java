package com.jakubpetriska.gameengine.engine;

import com.jakubpetriska.gameengine.api.Application;
import com.jakubpetriska.gameengine.api.Renderer;

/**
 * Created by Jakub on 9. 4. 2015.
 */
public interface FullRenderer extends Renderer {
    // TODO in future this can contain debug methods such as draw line

    void setApplication(Application application);
}
