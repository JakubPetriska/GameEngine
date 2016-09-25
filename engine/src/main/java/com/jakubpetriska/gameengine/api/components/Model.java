package com.jakubpetriska.gameengine.api.components;

import com.jakubpetriska.gameengine.api.Color;
import com.jakubpetriska.gameengine.api.Component;
import com.jakubpetriska.gameengine.api.MeshData;

/**
 * Component responsible for rendering a mesh.
 */
public class Model extends Component {

    /**
     * Name of primitive mesh.
     */
    public String meshPath;

    /**
     * Color of the rendered object.
     */
    public Color color;

    private String lastMeshPath;
    private MeshData meshData;

    @Override
    public void start() {
        this.meshData = getApplication().getMeshManager().getMeshData(meshPath);
        if(color == null) {
            color = Color.LIGHT_GRAY;
        }
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
