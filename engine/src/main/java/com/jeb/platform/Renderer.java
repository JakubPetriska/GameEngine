package com.jeb.platform;

import com.jeb.api.components.Mesh;

import java.util.List;

/**
 * Created by Jakub Petriska on 3. 1. 2015.
 */
public interface Renderer {

    /**
     * Will be called by engine before any of the rendering calls from onStart.
     */
    public void init(List<String> meshNames);

    /**
     * Renders mesh. Will be called by components in onPostUpdate.
     * @param mesh
     */
    public void render(Mesh mesh);
}
