package com.monolith.engine.primitives;

import com.monolith.api.MeshData;
import com.monolith.api.Renderer;

/**
 * Creates instances of {@link com.monolith.api.MeshData} for primitive cube model.
 */
public class Cube {

    public static MeshData getCube(Renderer renderer) {
        float vertices[] = {
                -1, 1, -1,   // top left front
                1, 1, -1,    // top right front
                1, -1, -1,   // bottom right front
                -1, -1, -1,  // bottom left front
                -1, 1, 1,    // top left back
                1, 1, 1,     // top right back
                1, -1, 1,    // bottom right back
                -1, -1, 1};  // bottom left back

        float normals[] = new float[]{
                0, 0, -1, // front
                0, 0, 1, // back
                0, 1, 0, // up
                0, -1, 0, // down
                1, 0, 0, // right
                -1, 0, 0 // left
        };

        int trianglesVertices[] = {
                0, 2, 3, 0, 1, 2, // front
                5, 4, 7, 5, 7, 6, // back
                4, 1, 0, 4, 5, 1, // top
                3, 6, 7, 3, 2, 6, // bottom
                1, 6, 2, 1, 5, 6, // right
                4, 0, 3, 4, 3, 7  // left
        };

        int trianglesNormals[] = {
                0, 0, 0, 0, 0, 0, // front
                1, 1, 1, 1, 1, 1, // back
                2, 2, 2, 2, 2, 2, // top
                3, 3, 3, 3, 3, 3, // bottom
                4, 4, 4, 4, 4, 4, // right
                5, 5, 5, 5, 5, 5  // left
        };

        return renderer.createMeshData(vertices, normals, trianglesVertices, trianglesNormals);
    }
}