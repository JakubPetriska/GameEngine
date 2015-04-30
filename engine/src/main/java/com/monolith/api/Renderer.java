package com.monolith.api;

import com.monolith.api.components.Camera;
import com.monolith.api.math.Matrix44;

/**
 * Provides rendering functionality.
 */
public interface Renderer {

    /**
     * Gets called by the engine before the rendering of a frame starts (calls to render(Model)).
     * <p/>
     * This ensures that all updates to game objects are applied for given frame.
     */
    void onStartRenderingFrame();

    /**
     * Renders model.
     *
     * @param mesh           Mesh to render.
     * @param transformation Transformation of the rendered object.
     */
    void render(MeshData mesh, Matrix44 transformation);

    /**
     * Renders wireframe of the given mesh. Use
     *
     * @param mesh           Mesh to render.
     * @param color          Color of the rendered wireframe.
     * @param transformation Transformation of the rendered object.
     */
    void renderWireframe(MeshData mesh, Color color, Matrix44 transformation);

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
