package com.jakubpetriska.gameengine.tests.support;

import com.jakubpetriska.gameengine.api.Application;
import com.jakubpetriska.gameengine.api.Color;
import com.jakubpetriska.gameengine.api.Display;
import com.jakubpetriska.gameengine.api.MeshData;
import com.jakubpetriska.gameengine.api.Touch;
import com.jakubpetriska.gameengine.api.components.Camera;
import com.jakubpetriska.gameengine.api.math.Matrix44;
import com.jakubpetriska.gameengine.engine.FullRenderer;
import com.jakubpetriska.gameengine.platform.Platform;
import com.jakubpetriska.gameengine.platform.TouchInputInternal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class MockEnginePlatformObjects {

    public static class MockRenderer implements FullRenderer {

        @Override
        public void onStartRenderingFrame() {

        }

        @Override
        public void render(MeshData mesh, Color color, Matrix44 transformation) {
            // Do nothing
        }

        @Override
        public void renderWireframe(MeshData mesh, Color color, Matrix44 transformation) {
            // Do nothing
        }

        @Override
        public MeshData createMeshData(float[] vertices, float[] normals, int[] trianglesVertices, int[] trianglesNormals) {
            return new MeshData(vertices, normals, trianglesVertices, trianglesNormals);
        }

        @Override
        public void setCamera(Camera camera) {

        }

        @Override
        public Camera getCamera() {
            return null;
        }

        @Override
        public void setApplication(Application application) {

        }
    }

    public static class MockTouchInput implements TouchInputInternal {

        private List<Touch> mTouches = new ArrayList<>();

        @Override
        public void update() {
            // Do nothing
        }

        @Override
        public void postUpdate() {

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
        public InputStream getAssetFileInputStream(String path) {
            try {
                return new FileInputStream(new File(Constants.FILES_FOLDER_PATH + mFilesFolder + "/" + path));
            } catch (FileNotFoundException e) {
                return null;
            }
        }

        @Override
        public void log(String message) {
            // empty
        }

        @Override
        public Display createDisplay() {
            return new Display(1920, 1080, 3);
        }
    }
}
