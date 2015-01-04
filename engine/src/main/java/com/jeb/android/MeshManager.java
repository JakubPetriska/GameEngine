package com.jeb.android;

import com.jeb.android.rendering.RendererImpl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jakub Petriska on 4. 1. 2015.
 */
public class MeshManager {

    public static final String MESH_NAME_CUBE = "cube";

    private HashMap<String, MeshData> mNameToMeshMap = new HashMap<>();

    public MeshManager(List<String> meshNames) {
        for(String meshName : meshNames) {
            if(meshName.equals(MESH_NAME_CUBE)) {
                mNameToMeshMap.put(meshName, createCube());
            } else {
                throw new IllegalStateException("Unknown mesh name.");
            }
        }
    }

    public MeshData getMesh(String name) {
        return mNameToMeshMap.get(name);
    }

    public static class MeshData {
        public final int vertexCount;
        public final FloatBuffer buffer;

        public MeshData(float[] vertices, short[] verticesOrder, float[] normals, short[] normalsOrder) {
            vertexCount = verticesOrder.length;

            // Create a packed data buffer containing position and normal data dor each vertex
            float[] vertexData = new float[
                    (verticesOrder.length * RendererImpl.COORDS_PER_VERTEX_POSITION)
                            + (normalsOrder.length * RendererImpl.COORDS_PER_VERTEX_NORMAL)];
            for (int i = 0; i < verticesOrder.length; i++) {
                int vertexDataPositionOffset = i * 6;
                int vertexDataNormalOffset = vertexDataPositionOffset + RendererImpl.COORDS_PER_VERTEX_POSITION;
                int sourcePositionOffset = verticesOrder[i] * RendererImpl.COORDS_PER_VERTEX_POSITION;
                int sourceNormalOffset = normalsOrder[i] * RendererImpl.COORDS_PER_VERTEX_NORMAL;
                for (int j = 0; j < RendererImpl.COORDS_PER_VERTEX_POSITION; ++j) {
                    vertexData[vertexDataPositionOffset + j] = vertices[sourcePositionOffset + j];
                }
                for (int j = 0; j < RendererImpl.COORDS_PER_VERTEX_NORMAL; ++j) {
                    vertexData[vertexDataNormalOffset + j] = normals[sourceNormalOffset + j];
                }
            }

            ByteBuffer vb = ByteBuffer.allocateDirect(
                    // (# of coordinate values * 4 bytes per float)
                    vertexData.length * 4);
            vb.order(ByteOrder.nativeOrder());
            buffer = vb.asFloatBuffer();
            buffer.put(vertexData);
            buffer.position(0);
        }
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

        short verticesOrder[] = {
                0, 2, 3, 0, 1, 2, // front
                5, 4, 7, 5, 7, 6, // back
                4, 1, 0, 4, 5, 1, // top
                3, 6, 7, 3, 2, 6, // bottom
                1, 6, 2, 1, 5, 6, // right
                4, 0, 3, 4, 3, 7  // left
        };

        float normals[] = new float[]{
                0, 0, -1, // front
                0, 0, 1, // back
                0, 1, 0, // up
                0, -1, 0, // down
                1, 0, 0, // right
                -1, 0, 0 // left
        };

        short normalsOrder[] = {
                0, 0, 0, 0, 0, 0, // front
                1, 1, 1, 1, 1, 1, // back
                2, 2, 2, 2, 2, 2, // top
                3, 3, 3, 3, 3, 3, // bottom
                4, 4, 4, 4, 4, 4, // right
                5, 5, 5, 5, 5, 5  // left
        };

        return new MeshData(vertices, verticesOrder, normals, normalsOrder);
    }
}
