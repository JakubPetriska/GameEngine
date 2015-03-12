package com.monolith.engine;

import com.monolith.api.Application;
import com.monolith.api.MeshData;
import com.monolith.api.Primitives;

import java.util.HashMap;

/**
 * Creates instances of {@link com.monolith.api.MeshData} when they are needed
 * and also caches them for later use.
 */
public class MeshManager {

    private final Application mApplication;

    // TODO maybe delete these objects when they are no longer used, requires calling something on renderer since it provides instances
    // TODO maybe on scene change
    private HashMap<String, MeshData> mNameToMeshMap = new HashMap<>();

    public MeshManager(Application application) {
        this.mApplication = application;
    }

    /**
     * Provides the instance of {@link com.monolith.api.MeshData} on given path.
     * Path can also be a name of primitive mesh.
     *
     * {@link com.monolith.api.MeshData} objects are stored so second call to this
     * method with the same argument will return same object as the first call.
     *
     * @param modelPath Path to the mesh file or name of the primitive mesh.
     * @return Constructed mesh.
     */
    public MeshData getMesh(String modelPath) {
        MeshData result;
        if (modelPath == null) {
            throw new IllegalArgumentException("Model path cannot be null");
        } else if (mNameToMeshMap.containsKey(modelPath)) {
            result = mNameToMeshMap.get(modelPath);
        } else {
            // Model is not loaded so load it
            if (modelPath.equals(Primitives.CUBE)) {
                result = createCube();
            } else {
                throw new IllegalStateException("Unavailable mesh requested");
            }
            mNameToMeshMap.put(modelPath, result);
        }
        return result;
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

        return mApplication.getRenderer().createMeshData(vertices, normals, trianglesVertices, trianglesNormals);
    }
}
