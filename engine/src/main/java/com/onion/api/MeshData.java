package com.onion.api;

/**
 * Created by Jakub Petriska on 16. 2. 2015.
 */
public class MeshData {

    /**
     * An array containing coordinates of vertices in this mesh.
     * Every vertex has 3 coordinates.
     */
    public final float[] vertices;

    /**
     * An array containing coordinates of normals in this mesh.
     * Every normal has 3 coordinates.
     */
    public final float[] normals;

    /**
     * An array containing indices of vertices of triangles in this mesh.
     */
    public final int[] trianglesVertices;

    /**
     * An array containing indices of normals of vertices of triangles in this mesh.
     */
    public final int[] trianglesNormals;

    /**
     * Never use this directly. Always let platform specific code create these.
     */
    public MeshData(float[] vertices, float[] normals, int[] trianglesVertices, int[] trianglesNormals) {
        this.vertices = vertices;
        this.normals = normals;
        this.trianglesVertices = trianglesVertices;
        this.trianglesNormals = trianglesNormals;
    }
}
