package com.monolith.api;

import com.monolith.api.components.Camera;
import com.monolith.api.components.Model;
import com.monolith.api.math.Matrix44;

/**
 * Provides rendering functionality.
 */
public interface Renderer {

    /**
     * Gets called by the engine before the rendering of a frame starts (calls to render(Model)).
     *
     * This ensures that all updates to game objects are applied for given frame.
     */
    void onStartRenderingFrame();

    /**
     * Renders model on the position of it's owning {@link com.monolith.api.GameObject}.
     *
     * @param mesh Mesh to render.
     * @param transformation Transformation of the rendered object.
     */
    void render(MeshData mesh, Matrix44 transformation);

    /**
     * Creates instance of MeshData with supplied data.
     * Implementation can return a subclass of MeshData to which it can store it's specific
     * data to use during rendering.
     * <p/>
     * Input parameters are similar to {@link com.monolith.api.MeshData}'s fields.
     */
    MeshData createMeshData(
            float[] vertices, float[] normals,
            int[] trianglesVertices, int[] trianglesNormals);

    void setCamera(Camera camera);

    Camera getCamera();
}
