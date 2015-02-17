package com.onion.engine;

import com.onion.api.Core;
import com.onion.api.MeshData;
import com.onion.api.Primitives;

import java.util.HashMap;

/**
 * Created by Jakub Petriska on 16. 2. 2015.
 */
public class MeshManager {

    private final Core mCore;

    // TODO maybe delete these objects when they are no longer used, requires calling something on renderer since it provides instances
    private HashMap<String, MeshData> mNameToMeshMap = new HashMap<>();

    public MeshManager(Core core) {
        this.mCore = core;
    }

    public MeshData getMesh(String modelPath) {
        if(modelPath == null) {
            throw new IllegalArgumentException("Model path cannot be null");
        } else if(mNameToMeshMap.containsKey(modelPath)) {
            return mNameToMeshMap.get(modelPath);
        } else {
            throw new IllegalStateException("Unavailable mesh requested.");
        }
    }

    // TODO only meshes for current scene should be loaded - improve this in future
    /**
     * Loads all available meshes into memory.
     */
    public void loadMeshes() {
        MeshData cube = createCube();
        mNameToMeshMap.put(Primitives.CUBE, cube);
    }

    private MeshData createCube() {
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

        return mCore.renderer.createMeshData(vertices, normals, trianglesVertices, trianglesNormals);
    }
}
