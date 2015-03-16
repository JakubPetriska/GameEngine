package com.monolith.utilities;

import com.monolith.api.MeshData;
import com.monolith.api.Renderer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Creates {@link com.monolith.api.MeshData} objects from obj file input streams.
 * <p/>
 * Treats multiple object in a single file as one mesh. Ignores materials.
 */
public class ObjLoader {

    /**
     * Creates {@link com.monolith.api.MeshData} from given input stream.
     *
     * @param assetStream Input stream containing the mesh data. Stream is expected to be in
     *                    obj format.
     * @param renderer    Needed to create the {@link com.monolith.api.MeshData} object.
     * @return Loaded {@link com.monolith.api.MeshData} object.
     */
    public static MeshData loadMeshAsset(InputStream assetStream, Renderer renderer) {
        try {
            return loadMeshAssetInternal(assetStream, renderer);
        } catch (IOException e) {
            throw new IllegalStateException("Obj model asset could not be loaded", e);
        }
    }

    private static MeshData loadMeshAssetInternal(InputStream assetStream, Renderer renderer) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(assetStream));
        String line;

        int normalsCount = 0;
        int verticesCount = 0;
        int facesCount = 0;

        // First count how many vertices, normals and faces we have in the mesh
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("v ")) {
                verticesCount++;
            } else if (line.startsWith("vn ")) {
                normalsCount++;
            } else if (line.startsWith("f ")) {
                facesCount++;
            }
        }

        assetStream.reset();

        float[] vertices = new float[verticesCount * 3];
        float[] normals = new float[normalsCount * 3];
        int[] trianglesVertices = new int[facesCount * 3];
        int[] trianglesNormals = new int[trianglesVertices.length];

        int verticesIndex = 0;
        int normalsIndex = 0;
        int trianglesIndex = 0;

        // Now parse the actual data
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("v ")) {
                String[] v = line.split(" ");
                vertices[verticesIndex] = Float.parseFloat(v[1]);
                vertices[verticesIndex + 1] = Float.parseFloat(v[2]);
                vertices[verticesIndex + 2] = Float.parseFloat(v[3]);
                verticesIndex += 3;
            } else if (line.startsWith("vn ")) {
                String[] v = line.split(" ");
                normals[normalsIndex] = Float.parseFloat(v[1]);
                normals[normalsIndex + 1] = Float.parseFloat(v[2]);
                normals[normalsIndex + 2] = Float.parseFloat(v[3]);
                normalsIndex += 3;
            } else if (line.startsWith("f ")) {
                String[] v = line.split(" ");

                String[] firstVertex = v[1].split("//");
                String[] secondVertex = v[2].split("//");
                String[] thirdVertex = v[3].split("//");

                trianglesVertices[trianglesIndex] = parseIndex(firstVertex[0]);
                trianglesNormals[trianglesIndex] = parseIndex(firstVertex[1]);
                trianglesIndex++;

                trianglesVertices[trianglesIndex] = parseIndex(secondVertex[0]);
                trianglesNormals[trianglesIndex] = parseIndex(secondVertex[1]);
                trianglesIndex++;

                trianglesVertices[trianglesIndex] = parseIndex(thirdVertex[0]);
                trianglesNormals[trianglesIndex] = parseIndex(thirdVertex[1]);
                trianglesIndex++;
            }
        }

        return renderer.createMeshData(vertices, normals, trianglesVertices, trianglesNormals);
    }

    /**
     * Convenience method to parse index.
     * <p/>
     * In obj vertices and normals are indexed from 1.
     */
    private static int parseIndex(String index) {
        return Integer.parseInt(index) - 1;
    }
}
