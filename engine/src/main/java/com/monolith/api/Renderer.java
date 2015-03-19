package com.monolith.api;

import com.monolith.api.components.Model;
import com.monolith.engine.ISystem;

/**
 * Provides rendering functionality.
 */
public interface Renderer extends ISystem {

    /**
     * Renders model on the position of it's owning {@link com.monolith.api.GameObject}.
     * @param model Model to render.
     */
    void render(Model model);

    /**
     * Creates instance of MeshData with supplied data.
     * Implementation can return a subclass of MeshData to which it can store it's specific
     * data to use during rendering.
     *
     * Input parameters are similar to {@link com.monolith.api.MeshData}'s fields.
     */
    MeshData createMeshData(
            float[] vertices, float[] normals,
            int[] trianglesVertices, int[] trianglesNormals);
}
