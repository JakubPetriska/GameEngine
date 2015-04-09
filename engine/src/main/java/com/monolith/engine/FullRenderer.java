package com.monolith.engine;

import com.monolith.api.Application;
import com.monolith.api.Renderer;

/**
 * Created by Jakub on 9. 4. 2015.
 */
public interface FullRenderer extends Renderer {
    // TODO in future this can contain debug methods such as draw line

    void setApplication(Application application);
}
