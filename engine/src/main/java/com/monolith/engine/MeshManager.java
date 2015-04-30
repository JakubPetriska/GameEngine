package com.monolith.engine;

import com.monolith.api.Application;
import com.monolith.api.MeshData;
import com.monolith.api.Primitives;
import com.monolith.engine.primitives.Cube;
import com.monolith.platform.Platform;
import com.monolith.utilities.ObjLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Set;

/**
 * Creates instances of {@link com.monolith.api.MeshData} when they are needed
 * and also caches them for later use.
 */
public class MeshManager {

    private final Application mApplication;
    private final Platform mPlatform;

    // TODO maybe delete these objects when they are no longer used, requires calling something on renderer since it provides instances
    // TODO maybe on scene change
    private HashMap<String, MeshData> mNameToMeshMap = new HashMap<>();

    public MeshManager(Application application, Platform platform) {
        this.mApplication = application;
        this.mPlatform = platform;
    }

    public Set<String> getStoredMeshesPaths() {
        return mNameToMeshMap.keySet();
    }

    /**
     * Provides the instance of {@link com.monolith.api.MeshData} on given path.
     * Path can also be a name of primitive mesh.
     *
     * {@link com.monolith.api.MeshData} objects are stored so second call to this
     * method with the same argument will return same object as the first call.
     *
     * @param meshPath Path to the mesh file or name of the primitive mesh.
     * @return Constructed mesh.
     */
    public MeshData getMeshData(String meshPath) {
        MeshData result;
        if (meshPath == null) {
            throw new IllegalArgumentException("Model path cannot be null");
        } else if (mNameToMeshMap.containsKey(meshPath)) {
            result = mNameToMeshMap.get(meshPath);
        } else {
            // Model is not loaded so load it
            if (meshPath.equals(Primitives.CUBE)) {
                result = Cube.getCube(mApplication.getRenderer());
            } else {
                InputStream meshInputStream = mPlatform.getAssetFileInputStream(meshPath);
                if(meshInputStream == null) {
                    throw new IllegalStateException("Unavailable mesh " + meshPath + " requested");
                }
                try {
                    result = ObjLoader.loadMeshAsset(meshInputStream, mApplication.getRenderer());
                } catch (IOException e) {
                    throw new IllegalStateException("Obj model asset on path " + meshPath + " could not be loaded", e);
                }
            }
            mNameToMeshMap.put(meshPath, result);
        }
        return result;
    }
}
