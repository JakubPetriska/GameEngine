package com.monolith.api;

/**
 * Contains data about vertices, faces and normals of mesh.
 *
 * <strong>Mesh data are read-only.</strong>
 * Any change to any of the fields will not result in any change in the rendered mesh.
 * Also for one model there exists only one instance of MeshData.
 *
 * <strong>Never create this class directly.</strong>
 * You should not need to create your own instances of MeshData.
 * Creation of custom meshes through code is currently not supported.
 */
public class MeshData {

    /**
     * Contains coordinates of vertices in this mesh.
     * Every vertex has 3 coordinates.
     */
    public final float[] vertices;

    /**
     * Contains coordinates of normals in this mesh.
     * Every normal has 3 coordinates.
     */
    public final float[] normals;

    /**
     * Contains indices of vertices of triangles in this mesh.
     */
    public final int[] trianglesVertices;

    /**
     * Contains indices of normals of vertices of triangles in this mesh.
     *
     * Must be the same length as {@link com.monolith.api.MeshData#trianglesVertices}.
     * Meshes without normals are currently not supported.
     */
    public final int[] trianglesNormals;

    /**
     * Never use this directly.
     */
    public MeshData(float[] vertices, float[] normals, int[] trianglesVertices, int[] trianglesNormals) {
        this.vertices = vertices;
        this.normals = normals;
        this.trianglesVertices = trianglesVertices;
        this.trianglesNormals = trianglesNormals;
    }
}
