package com.onion.api.components;

import com.onion.api.Component;
import com.onion.api.GameObject;
import com.onion.api.Core;
import com.onion.api.MeshData;

/**
 * Created by Jakub Petriska on 3. 1. 2015.
 */
public class Mesh extends Component {

    public final String meshPath;
    public final MeshData meshData;

    /**
     * Creates new component and adds it to it's owner.
     *
     * @param owner
     */
    public Mesh(Core core, GameObject owner, String meshPath) {
        super(core, owner);
        this.meshPath = meshPath;
        this.meshData = core.meshManager.getMesh(meshPath);
    }

    @Override
    public void postUpdate() {
        core.renderer.render(this);
    }
}
