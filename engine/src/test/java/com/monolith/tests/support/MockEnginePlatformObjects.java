package com.monolith.tests.support;

import com.monolith.api.MeshData;
import com.monolith.api.Touch;
import com.monolith.api.components.Mesh;
import com.monolith.platform.Platform;
import com.monolith.platform.Renderer;
import com.monolith.platform.TouchInputInternal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jakub Petriska on 19. 2. 2015.
 */
public class MockEnginePlatformObjects {

    public static class MockRenderer implements Renderer {

        @Override
        public void render(Mesh mesh) {
            // Do nothing
        }

        @Override
        public MeshData createMeshData(float[] vertices, float[] normals, int[] trianglesVertices, int[] trianglesNormals) {
            return new MeshData(vertices, normals, trianglesVertices, trianglesNormals);
        }
    }

    public static class MockTouchInput implements TouchInputInternal {

        private List<Touch> mTouches = new ArrayList<>();

        @Override
        public void update() {
            // Do nothing
        }

        @Override
        public List<Touch> getTouches() {
            return mTouches;
        }
    }

    public static class MockPlatform implements Platform {

        private final String mFilesFolder;

        public MockPlatform(String filesFolder) {
            mFilesFolder = filesFolder;
        }

        @Override
        public InputStream getAssetFile(String path) {
            try {
                return new FileInputStream(new File(Constants.FILES_FOLDER_PATH + mFilesFolder + "/" + path));
            } catch (FileNotFoundException e) {
                return null;
            }
        }
    }
}
