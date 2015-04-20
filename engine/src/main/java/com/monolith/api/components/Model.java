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
    public MeshData meshData;

    @Override
    public void start() {
        this.meshData = getApplication().getModelManager().getMeshData(meshPath);
    }

    @Override
    public void postUpdate() {
        getApplication().getRenderer().render(
                meshData,
                getGameObject().transform.getTransformationMatrix());
    }
}
