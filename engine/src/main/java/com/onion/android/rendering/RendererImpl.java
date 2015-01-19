package com.onion.android.rendering;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import com.onion.android.MeshManager;
import com.onion.api.Transform;
import com.onion.api.components.Mesh;
import com.onion.platform.Renderer;

import org.lwjgl.util.vector.Vector3f;

import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Jakub Petriska on 3. 1. 2015.
 */
public abstract class RendererImpl implements GLSurfaceView.Renderer, Renderer {

    private static final String TAG = "RendererImpl";

    public static final int COORDS_PER_VERTEX_POSITION = 3;
    public static final int COORDS_PER_VERTEX_NORMAL = 3;
    public static final int VERTEX_STRIDE = (COORDS_PER_VERTEX_POSITION + COORDS_PER_VERTEX_NORMAL) * 4; // 4 bytes per vertex

    private static final String vertexShaderCode =
            "uniform mat4 uMVPMatrix;" +
                    "uniform mat4 uRotationMatrix;" +
                    "attribute vec4 vPosition;" +
                    "attribute vec4 vNormal;" +
                    "varying vec4 oNormal;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "  oNormal = normalize(uRotationMatrix * vNormal);" +
                    "}";

    private static final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 uLight;" +
                    "uniform vec4 uColor;" +
                    "varying vec4 oNormal;" +
                    "void main() {" +
                    "  float minFactor = 0.1;" +
                    "  float factor = max(0.0, dot(oNormal, normalize(uLight)));" +
                    "  gl_FragColor = ((factor * (1.0 - minFactor)) + minFactor) * uColor;" +
                    "}";

    private static final float[] light = new float[]{-0.75f, 1, -0.5f, 0};
    private static final float color[] = {0.2f, 0.709803922f, 0.898039216f, 1.0f};

    private MeshManager mMeshManager;

    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mModelMatrix = new float[16];
    private final float[] mTranslationMatrix = new float[16];
    private final float[] mRotationMatrix = new float[16];
    private final float[] mMVPMatrix = new float[16];

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
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 50);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // Draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 0, 0f, 0f, 1.0f, 0f, 1.0f, 0.0f);
    }

    private int mProgram;

    @Override
    public void init(List<String> meshNames) {
        mMeshManager = new MeshManager(meshNames);

        // prepare shaders and OpenGL program
        int vertexShader = loadShader(
                GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = loadShader(
                GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);
    }

    // Used to retrieve object absolute position
    private final Vector3f mPositionCache = new Vector3f();

    @Override
    public void render(Mesh mesh) {
        MeshManager.MeshData meshData = mMeshManager.getMesh(mesh.getMeshName());

        mesh.gameObject.transform.getWorldPosition(mPositionCache);
        // Create the model matrix
        Matrix.setIdentityM(mTranslationMatrix, 0);
        Matrix.translateM(mTranslationMatrix, 0,
                mPositionCache.x,
                mPositionCache.y,
                mPositionCache.z);
        Matrix.setIdentityM(mRotationMatrix, 0);
        Matrix.multiplyMM(mModelMatrix, 0, mTranslationMatrix, 0, mRotationMatrix, 0);

        // Compose MVP matrix
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

        GLES20.glUseProgram(mProgram);

        meshData.buffer.position(0);
        int positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(
                positionHandle, COORDS_PER_VERTEX_POSITION,
                GLES20.GL_FLOAT, false,
                VERTEX_STRIDE, meshData.buffer);

        meshData.buffer.position(COORDS_PER_VERTEX_POSITION);
        int normalHandle = GLES20.glGetAttribLocation(mProgram, "vNormal");
        GLES20.glEnableVertexAttribArray(normalHandle);
        GLES20.glVertexAttribPointer(
                normalHandle, COORDS_PER_VERTEX_NORMAL,
                GLES20.GL_FLOAT, false,
                VERTEX_STRIDE, meshData.buffer);


        // get handle to fragment shader's vColor member
        int colorHandle = GLES20.glGetUniformLocation(mProgram, "uColor");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(colorHandle, 1, color, 0);

        // set shader's light vector
        int lightHandle = GLES20.glGetUniformLocation(mProgram, "uLight");
        GLES20.glUniform4fv(lightHandle, 1, light, 0);

        // get handle to shape's transformation matrix
        int mVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mVPMatrixHandle, 1, false, mMVPMatrix, 0);

        // get handle to shape's transformation matrix
        int rotationMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uRotationMatrix");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(rotationMatrixHandle, 1, false, mRotationMatrix, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, meshData.vertexCount);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glDisableVertexAttribArray(normalHandle);
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
     * mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
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
