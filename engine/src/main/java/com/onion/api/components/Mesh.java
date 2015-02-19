package com.onion.api.components;

import com.onion.api.Component;
import com.onion.api.GameObject;
import com.onion.api.Core;
import com.onion.api.MeshData;

/**
 * Created by Jakub Petriska on 3. 1. 2015.
 */
public class Mesh extends Component {

    public String meshPath;
    public MeshData meshData;

    @Override
    public void start() {
        this.meshData = getCore().getMeshManager().getMesh(meshPath);
    }

    @Override
    public void postUpdate() {
        getCore().getRenderer().render(this);
    }
}
