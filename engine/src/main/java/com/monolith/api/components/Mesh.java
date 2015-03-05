package com.monolith.api.components;

import com.monolith.api.Component;
import com.monolith.api.MeshData;

/**
 * Created by Jakub Petriska on 3. 1. 2015.
 */
public class Mesh extends Component {

    public String meshPath;
    public MeshData meshData;

    @Override
    public void start() {
        this.meshData = getApplication().getMeshManager().getMesh(meshPath);
    }

    @Override
    public void postUpdate() {
        getApplication().getRenderer().render(this);
    }
}
