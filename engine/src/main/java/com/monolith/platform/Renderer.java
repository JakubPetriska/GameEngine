package com.monolith.platform;

import com.monolith.api.MeshData;
import com.monolith.api.components.Mesh;

/**
 * Created by Jakub Petriska on 3. 1. 2015.
 */
public interface Renderer {

    /**
     * Renders mesh. Will be called by components in onPostUpdate.
     *
     * @param mesh
     */
    public void render(Mesh mesh);

    /**
     * Creates instance of MeshData with supplied data.
     * Implementation can return a subclass of MeshData to which it can store it's specific
     * data to use during rendering.
     *
     * @param vertices
     * @param normals
     * @param trianglesVertices
     * @param trianglesNormals
     */
    public MeshData createMeshData(
            float[] vertices, float[] normals,
            int[] trianglesVertices, int[] trianglesNormals);
}
