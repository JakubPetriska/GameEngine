package com.onion.api.components;

import com.onion.api.Component;
import com.onion.api.MeshData;

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
