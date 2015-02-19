package com.onion.tests.support;

import com.onion.api.MeshData;
import com.onion.api.Touch;
import com.onion.api.components.Mesh;
import com.onion.platform.Platform;
import com.onion.platform.Renderer;
import com.onion.platform.TouchInputInternal;

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
