package com.monolith.api.components;

import com.monolith.api.Component;
import com.monolith.api.MeshData;

/**
 * Component responsible for rendering a mesh.
 */
public class Model extends Component {

    /**
     * Name of primitive mesh.
     */
    public String meshPath;
    public float[] color = new float[]{0.2f, 0.709803922f, 0.898039216f, 1.0f};

    private String lastMeshPath;
    private MeshData meshData;

    public MeshData getMeshData() {
        return meshData;
    }

    @Override
    public void start() {
        this.meshData = getApplication().getMeshManager().getMeshData(meshPath);
        lastMeshPath = meshPath;
    }

    @Override
    public void postUpdate() {
        if(meshPath != lastMeshPath) { // Comparing objects to reduce the test cost
            this.meshData = getApplication().getMeshManager().getMeshData(meshPath);
            lastMeshPath = meshPath;
        }
        getApplication().getRenderer().render(
                meshData,
                color,
                getGameObject().transform.getTransformationMatrix());
    }
}
