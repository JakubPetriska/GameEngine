package com.onion.platform;

import com.onion.api.Core;
import com.onion.api.MeshData;
import com.onion.api.components.Mesh;

import java.util.List;

/**
 * Created by Jakub Petriska on 3. 1. 2015.
 */
public interface Renderer {

    /**
     * Allows renderer to do some initialization. This method is being called after
     * whole initial state scene was created.
     * @param core
     */
    public void init(Core core);

    /**
     * Renders mesh. Will be called by components in onPostUpdate.
     * @param mesh
     */
    public void render(Mesh mesh);

    /**
     * Creates instance of MeshData with supplied data.
     * Implementation can return a subclass of MeshData to which it can store it's specific
     * data to use during rendering.
     * @param vertices
     * @param normals
     * @param trianglesVertices
     * @param trianglesNormals
     */
    public MeshData createMeshData(
            float[] vertices, float[] normals,
            int[] trianglesVertices, int[] trianglesNormals);
}
