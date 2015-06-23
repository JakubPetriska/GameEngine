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
                getGameObject().transform.getTransformationMatrix());
    }
}
