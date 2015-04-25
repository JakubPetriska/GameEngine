package com.monolith.android.rendering;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import com.monolith.api.Application;
import com.monolith.api.Color;
import com.monolith.api.GameObject;
import com.monolith.api.MeshData;
import com.monolith.api.components.Camera;
import com.monolith.api.components.Transform;
import com.monolith.api.math.Matrix44;
import com.monolith.engine.FullRenderer;
import com.monolith.engine.MeshManager;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Jakub Petriska on 3. 1. 2015.
 */
public abstract class RendererImpl implements GLSurfaceView.Renderer, FullRenderer {

    private static final String TAG = "RendererImpl";

    private Application mApplication;

    public static final int BYTES_PER_FLOAT = 4;
    public static final int COORDS_PER_VERTEX_POSITION = 3;
    public static final int COORDS_PER_VERTEX_NORMAL = 3;
    public static final int VERTEX_STRIDE = (COORDS_PER_VERTEX_POSITION + COORDS_PER_VERTEX_NORMAL) * BYTES_PER_FLOAT;

    private static final String vertexShaderObject =
            "uniform mat4 uMVPMatrix;" +
                    "uniform mat4 uModelMatrix;" +
                    "attribute vec4 vPosition;" +
                    "attribute vec3 vNormal;" +
                    "varying vec4 oNormal;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "  vec4 normal = vec4(vNormal.x, vNormal.y, vNormal.z, 0);" +
                    "  oNormal = normalize(uModelMatrix * normal);" +
                    "}";

    private static final String fragmentShaderObject =
            "precision mediump float;" +
                    "uniform vec4 uLightDirection;" +
                    "uniform vec4 uColor;" +
                    "varying vec4 oNormal;" +
                    "void main() {" +
                    "  float minFactor = 0.3;" +
                    "  float factor = max(0.0, dot(oNormal, normalize(uLightDirection)));" +
                    "  gl_FragColor = ((factor * (1.0 - minFactor)) + minFactor) * uColor;" +
                    "}";

    private static final String vertexShaderLine =
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    private static final String fragmentShaderLine =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    private static final float[] lightDirection = new float[]{-0.75f, 1, -0.5f, 0};
    private static final float[] color = {0.2f, 0.709803922f, 0.898039216f, 1.0f};

    private static final float[] wireframeLineColor = {0, 0, 0, 0};

    // These are helper variables used during calculations of position of every model
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mCameraMatrix = new float[16]; // Camera space transformation
    private final float[] mMVMatrix = new float[16]; // Model View transformation matrix
    private final float[] mMVPMatrix = new float[16];
    private final Matrix44 mModelMatrixCopy = new Matrix44();

    private int mShaderProgramObject;
    private int mObjectShaderPositionHandle;
    private int mObjectShaderNormalHandle;
    private int mObjectShaderColorHandle;
    private int mObjectShaderLightHandle;
    private int mObjectShaderMVPMatrixHandle;
    private int mObjectShaderModelMatrixHandle;

    private int mShaderProgramLine;
    private int mLineShaderPositionHandle;
    private int mLineShaderColorHandle;
    private int mLineShaderMVPMatrixHandle;

    private float mScreenRatio;
    private Camera mCamera;
    private float mLastCameraNear;
    private float mLastCameraFar;
    private float mLastCameraFov;
    private boolean mFrustumSet = false;

    @Override
    public void setCamera(Camera camera) {
        mCamera = camera;
    }

    @Override
    public Camera getCamera() {
        return mCamera;
    }

    @Override
    public void setApplication(Application application) {
        mApplication = application;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        // Enable back face culling
        GLES20.glEnable(GLES20.GL_CULL_FACE);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glDepthMask(true);
        GLES20.glDepthFunc(GLES20.GL_LEQUAL);
        GLES20.glDepthRangef(0.0f, 1.0f);

        // Prepare shaders and OpenGL program for normal 3D objects
        int vertexShader = loadShader(
                GLES20.GL_VERTEX_SHADER,
                vertexShaderObject);
        int fragmentShader = loadShader(
                GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderObject);

        mShaderProgramObject = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mShaderProgramObject, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mShaderProgramObject, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mShaderProgramObject);

        mObjectShaderPositionHandle = GLES20.glGetAttribLocation(mShaderProgramObject, "vPosition");
        mObjectShaderNormalHandle = GLES20.glGetAttribLocation(mShaderProgramObject, "vNormal");
        mObjectShaderColorHandle = GLES20.glGetUniformLocation(mShaderProgramObject, "uColor");
        mObjectShaderLightHandle = GLES20.glGetUniformLocation(mShaderProgramObject, "uLightDirection");
        mObjectShaderMVPMatrixHandle = GLES20.glGetUniformLocation(mShaderProgramObject, "uMVPMatrix");
        mObjectShaderModelMatrixHandle = GLES20.glGetUniformLocation(mShaderProgramObject, "uModelMatrix");

        if (mApplication.getDebug().debug) {
            // Prepare shaders and OpenGL program for lines
            vertexShader = loadShader(
                    GLES20.GL_VERTEX_SHADER,
                    vertexShaderLine);
            fragmentShader = loadShader(
                    GLES20.GL_FRAGMENT_SHADER,
                    fragmentShaderLine);

            mShaderProgramLine = GLES20.glCreateProgram();             // create empty OpenGL Program
            GLES20.glAttachShader(mShaderProgramLine, vertexShader);   // add the vertex shader to program
            GLES20.glAttachShader(mShaderProgramLine, fragmentShader); // add the fragment shader to program
            GLES20.glLinkProgram(mShaderProgramLine);

            mLineShaderPositionHandle = GLES20.glGetAttribLocation(mShaderProgramLine, "vPosition");
            mLineShaderColorHandle = GLES20.glGetUniformLocation(mShaderProgramLine, "vColor");
            mLineShaderMVPMatrixHandle = GLES20.glGetUniformLocation(mShaderProgramLine, "uMVPMatrix");
        }

        // Upload mesh data of objects to the GPU, these are objects that were already uploaded
        // Data are lost during screen rotation
        MeshManager meshManager = mApplication.getMeshManager();
        for(String meshPath : meshManager.getStoredMeshesPaths()) {
            AndroidMeshData meshData = (AndroidMeshData) meshManager.getMeshData(meshPath);
            meshData.wireframeDataBuffer = 0;
            uploadObjectDataToGpuForMesh(meshData);
        }

        // Set our view matrix
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 0, 0f, 0f, 1.0f, 0f, 1.0f, 0.0f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        GLES20.glViewport(0, 0, width, height);

        mScreenRatio = (float) width / height;
    }

    private void setupFrustumIfPossibleAndNeeded() {
        if (mFrustumSet && mCamera.near == mLastCameraNear
                && mCamera.far == mLastCameraFar
                && mCamera.fieldOfView == mLastCameraFov) {
            return;
        }

        float halfHeight = (float) (Math.sin(Math.toRadians(mCamera.fieldOfView / 2)) * mCamera.near);
        float halfWidth = mScreenRatio * halfHeight;
        Matrix.frustumM(mProjectionMatrix, 0,
                -halfWidth, // Left
                halfWidth, // Right
                -halfHeight, // Bottom
                halfHeight, // Top
                mCamera.near, mCamera.far);

        mLastCameraNear = mCamera.near;
        mLastCameraFar = mCamera.far;
        mLastCameraFov = mCamera.fieldOfView;
        mFrustumSet = true;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // Empty, overridden for completeness
    }

    // Used during calculation of world-camera transformation
    private List<GameObject> mCameraParents = new ArrayList<>();

    // Helper variables that the object transformation matrix creation algorithm uses
    private final float[] mModelDuplicateMatrix = new float[16];
    private final float[] mRelativeModelMatrix = new float[16];

    @Override
    public void onStartRenderingFrame() {
        // Draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // Set the camera matrices
        if (mCamera != null) {
            setupFrustumIfPossibleAndNeeded();

            // Get the inverse camera transformation
            GameObject gameObject = mCamera.getGameObject();
            do {
                mCameraParents.add(0, gameObject);
                gameObject = gameObject.getParent();
            } while (gameObject != null);

            Matrix.setIdentityM(mCameraMatrix, 0);
            for (int i = 0; i < mCameraParents.size(); ++i) {
                Transform transform = mCameraParents.get(i).transform;

                Matrix.setIdentityM(mRelativeModelMatrix, 0);

                Matrix.scaleM(mRelativeModelMatrix, 0,
                        transform.getScaleX() == 0 ? 0 : 1 / transform.getScaleX(),
                        transform.getScaleY() == 0 ? 0 : 1 / transform.getScaleY(),
                        transform.getScaleZ() == 0 ? 0 : 1 / transform.getScaleZ());

                Matrix.rotateM(mRelativeModelMatrix, 0,
                        -transform.getRotationZ(),
                        0, 0, -1);
                Matrix.rotateM(mRelativeModelMatrix, 0,
                        -transform.getRotationX(),
                        1, 0, 0);
                Matrix.rotateM(mRelativeModelMatrix, 0,
                        -transform.getRotationY(),
                        0, -1, 0);

                Matrix.translateM(mRelativeModelMatrix, 0,
                        transform.getPositionX(), // Revert the direction because of change of the coordinate system handedness
                        -transform.getPositionY(),
                        -transform.getPositionZ());

                Matrix.multiplyMM(mModelDuplicateMatrix, 0, mRelativeModelMatrix, 0, mCameraMatrix, 0);
                System.arraycopy(mModelDuplicateMatrix, 0, mCameraMatrix, 0, 16);
            }
            mCameraParents.clear();
        }
    }

    // TODO comment this code

    /**
     * Creates packed buffer for MeshData object.
     */
    private static FloatBuffer createObjectBuffer(MeshData meshData) {
        int vertexCount = meshData.trianglesVertices.length;

        // Create a packed data buffer containing position and normal data for each vertex
        float[] vertexData = new float[
                (vertexCount * RendererImpl.COORDS_PER_VERTEX_POSITION)
                        + (vertexCount * RendererImpl.COORDS_PER_VERTEX_NORMAL)];
        for (int i = 0; i < vertexCount; i++) {
            // We need to reverse the order of vertices in triangles due to change in coordinate system handedness
            int vertexDataPositionOffset = (vertexCount - 1 - i) * 6;
            int vertexDataNormalOffset = vertexDataPositionOffset + RendererImpl.COORDS_PER_VERTEX_POSITION;
            int sourcePositionOffset = meshData.trianglesVertices[i] * RendererImpl.COORDS_PER_VERTEX_POSITION;
            int sourceNormalOffset = meshData.trianglesNormals[i] * RendererImpl.COORDS_PER_VERTEX_NORMAL;

            System.arraycopy(meshData.vertices, sourcePositionOffset, vertexData, vertexDataPositionOffset, RendererImpl.COORDS_PER_VERTEX_POSITION);
            System.arraycopy(meshData.normals, sourceNormalOffset, vertexData, vertexDataNormalOffset, RendererImpl.COORDS_PER_VERTEX_NORMAL);
        }

        ByteBuffer vb = ByteBuffer.allocateDirect(vertexData.length * BYTES_PER_FLOAT);
        vb.order(ByteOrder.nativeOrder());
        FloatBuffer resultBuffer = vb.asFloatBuffer();
        resultBuffer.put(vertexData);
        resultBuffer.position(0);
        return resultBuffer;
    }

    /**
     * Creates packed buffer for MeshData wireframe object.
     * <p/>
     * Wireframe is drawn as GL_LINES.
     */
    private static FloatBuffer createWireframeBuffer(MeshData meshData) {
        int vertexCount = meshData.trianglesVertices.length;

        // Create a packed data buffer containing position data for each vertex
        float[] vertexData = new float[
                (vertexCount * RendererImpl.COORDS_PER_VERTEX_POSITION) * 2]; // All vertices are duplicated for lines
        int vertexDataPositionOffset = 0;
        for (int i = 0; i < vertexCount; i += 3) {

            int sourceVertexPositionOffset = meshData.trianglesVertices[i] * RendererImpl.COORDS_PER_VERTEX_POSITION;
            System.arraycopy(meshData.vertices, sourceVertexPositionOffset, vertexData, vertexDataPositionOffset, RendererImpl.COORDS_PER_VERTEX_POSITION);
            vertexDataPositionOffset += 3;

            sourceVertexPositionOffset = meshData.trianglesVertices[i + 1] * RendererImpl.COORDS_PER_VERTEX_POSITION;
            System.arraycopy(meshData.vertices, sourceVertexPositionOffset, vertexData, vertexDataPositionOffset, RendererImpl.COORDS_PER_VERTEX_POSITION);
            vertexDataPositionOffset += 3;

            sourceVertexPositionOffset = meshData.trianglesVertices[i + 1] * RendererImpl.COORDS_PER_VERTEX_POSITION;
            System.arraycopy(meshData.vertices, sourceVertexPositionOffset, vertexData, vertexDataPositionOffset, RendererImpl.COORDS_PER_VERTEX_POSITION);
            vertexDataPositionOffset += 3;

            sourceVertexPositionOffset = meshData.trianglesVertices[i + 2] * RendererImpl.COORDS_PER_VERTEX_POSITION;
            System.arraycopy(meshData.vertices, sourceVertexPositionOffset, vertexData, vertexDataPositionOffset, RendererImpl.COORDS_PER_VERTEX_POSITION);
            vertexDataPositionOffset += 3;

            sourceVertexPositionOffset = meshData.trianglesVertices[i + 2] * RendererImpl.COORDS_PER_VERTEX_POSITION;
            System.arraycopy(meshData.vertices, sourceVertexPositionOffset, vertexData, vertexDataPositionOffset, RendererImpl.COORDS_PER_VERTEX_POSITION);
            vertexDataPositionOffset += 3;

            sourceVertexPositionOffset = meshData.trianglesVertices[i] * RendererImpl.COORDS_PER_VERTEX_POSITION;
            System.arraycopy(meshData.vertices, sourceVertexPositionOffset, vertexData, vertexDataPositionOffset, RendererImpl.COORDS_PER_VERTEX_POSITION);
            vertexDataPositionOffset += 3;
        }

        ByteBuffer vb = ByteBuffer.allocateDirect(vertexData.length * BYTES_PER_FLOAT);
        vb.order(ByteOrder.nativeOrder());
        FloatBuffer resultBuffer = vb.asFloatBuffer();
        resultBuffer.put(vertexData);
        resultBuffer.position(0);
        return resultBuffer;
    }

    private static class AndroidMeshData extends MeshData {

        int objectDataBuffer = 0;
        int wireframeDataBuffer = 0;

        public AndroidMeshData(
                float[] vertices, float[] normals,
                int[] trianglesVertices, int[] trianglesNormals) {
            super(vertices, normals, trianglesVertices, trianglesNormals);
        }
    }

    private void uploadObjectDataToGpuForMesh(AndroidMeshData meshData) {
        FloatBuffer dataBuffer = createObjectBuffer(meshData);

        int[] buffer = new int[1];
        GLES20.glGenBuffers(1, buffer, 0);
        meshData.objectDataBuffer = buffer[0];

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, meshData.objectDataBuffer);

        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,
                dataBuffer.capacity() * BYTES_PER_FLOAT,
                dataBuffer, GLES20.GL_STATIC_DRAW);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        dataBuffer.clear();
    }

    @Override
    public MeshData createMeshData(float[] vertices, float[] normals, int[] trianglesVertices, int[] trianglesNormals) {
        AndroidMeshData meshData = new AndroidMeshData(vertices, normals, trianglesVertices, trianglesNormals);
        uploadObjectDataToGpuForMesh(meshData);
        return meshData;
    }

    private static void uploadWireframeDataToGpuForMesh(AndroidMeshData meshData) {
        FloatBuffer dataBuffer = createWireframeBuffer(meshData);

        int[] buffer = new int[1];
        GLES20.glGenBuffers(1, buffer, 0);
        meshData.wireframeDataBuffer = buffer[0];

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, meshData.wireframeDataBuffer);

        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,
                dataBuffer.capacity() * BYTES_PER_FLOAT,
                dataBuffer, GLES20.GL_STATIC_DRAW);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        dataBuffer.clear();
    }

    @Override
    public void render(MeshData mesh, Matrix44 transformation) {
        if (mCamera == null) {
            return;
        }
        AndroidMeshData meshData = (AndroidMeshData) mesh;

        // Copy the transformation matrix
        mModelMatrixCopy.set(transformation);

        // Scale matrix according to handedness change
        mModelMatrixCopy.scale(-1, 1, 1);

        // Compose MVP matrix
        Matrix.multiplyMM(mMVPMatrix, 0, mCameraMatrix, 0, mModelMatrixCopy.getValues(), 0);
        Matrix.multiplyMM(mMVMatrix, 0, mViewMatrix, 0, mMVPMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVMatrix, 0);

        GLES20.glUseProgram(mShaderProgramObject);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, meshData.objectDataBuffer);

        GLES20.glEnableVertexAttribArray(mObjectShaderPositionHandle);
        GLES20.glVertexAttribPointer(
                mObjectShaderPositionHandle, COORDS_PER_VERTEX_POSITION,
                GLES20.GL_FLOAT, false,
                VERTEX_STRIDE, 0);

        GLES20.glEnableVertexAttribArray(mObjectShaderNormalHandle);
        GLES20.glVertexAttribPointer(
                mObjectShaderNormalHandle, COORDS_PER_VERTEX_NORMAL,
                GLES20.GL_FLOAT, false,
                VERTEX_STRIDE, COORDS_PER_VERTEX_POSITION * BYTES_PER_FLOAT);

        // Unbind from the buffer
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        GLES20.glUniform4fv(mObjectShaderColorHandle, 1, color, 0);
        GLES20.glUniform4fv(mObjectShaderLightHandle, 1, lightDirection, 0);
        GLES20.glUniformMatrix4fv(mObjectShaderMVPMatrixHandle, 1, false, mMVPMatrix, 0);
        GLES20.glUniformMatrix4fv(mObjectShaderModelMatrixHandle, 1, false, mModelMatrixCopy.getValues(), 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, meshData.trianglesVertices.length);

        GLES20.glDisableVertexAttribArray(mObjectShaderPositionHandle);
        GLES20.glDisableVertexAttribArray(mObjectShaderNormalHandle);
    }

    @Override
    public void renderWireframe(MeshData mesh, Color color, Matrix44 transformation) {
        if (mCamera == null) {
            return;
        }
        if (mShaderProgramLine == 0) {
            throw new IllegalStateException("Wireframe rendering is only available during debug mode.");
        }

        AndroidMeshData meshData = (AndroidMeshData) mesh;
        if (meshData.wireframeDataBuffer == 0) {
            uploadWireframeDataToGpuForMesh(meshData);
        }

        // Copy the transformation matrix
        mModelMatrixCopy.set(transformation);

        // Scale matrix according to handedness change
        mModelMatrixCopy.scale(-1, 1, 1);

        // Compose MVP matrix
        Matrix.multiplyMM(mMVPMatrix, 0, mCameraMatrix, 0, mModelMatrixCopy.getValues(), 0);
        Matrix.multiplyMM(mMVMatrix, 0, mViewMatrix, 0, mMVPMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVMatrix, 0);

        GLES20.glUseProgram(mShaderProgramLine);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, meshData.wireframeDataBuffer);
        GLES20.glEnableVertexAttribArray(mLineShaderPositionHandle);
        GLES20.glVertexAttribPointer(
                mLineShaderPositionHandle, COORDS_PER_VERTEX_POSITION,
                GLES20.GL_FLOAT, false,
                COORDS_PER_VERTEX_POSITION * BYTES_PER_FLOAT, 0);

        // Unbind from the buffer
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        // Set color for drawing the lines
        wireframeLineColor[0] = color.red;
        wireframeLineColor[1] = color.green;
        wireframeLineColor[2] = color.blue;
        wireframeLineColor[3] = color.alpha;
        GLES20.glUniform4fv(mLineShaderColorHandle, 1, wireframeLineColor, 0);

        GLES20.glUniformMatrix4fv(mLineShaderMVPMatrixHandle, 1, false, mMVPMatrix, 0);

        GLES20.glDrawArrays(GLES20.GL_LINES, 0, meshData.trianglesVertices.length * 2);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mLineShaderPositionHandle);
    }

    /**
     * Utility method for compiling a OpenGL shader.
     * <p/>
     * <p><strong>Note:</strong> When developing shaders, use the checkGlError()
     * method to debug shader coding errors.</p>
     *
     * @param type       - Vertex or fragment shader type.
     * @param shaderCode - String containing the shader code.
     * @return - Returns an id for the shader.
     */
    public static int loadShader(int type, String shaderCode) {

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    /**
     * Utility method for debugging OpenGL calls. Provide the name of the call
     * just after making it:
     * <p/>
     * <pre>
     * mColorHandle = GLES20.glGetUniformLocation(mShaderProgramObject, "vColor");
     * MyGLRenderer.checkGlError("glGetUniformLocation");</pre>
     *
     * If the operation is not successful, the check throws an error.
     *
     * @param glOperation - Name of the OpenGL call to check.
     */
    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }
}
