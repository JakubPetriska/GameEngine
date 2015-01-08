package com.onion.api.components;

import com.onion.api.Component;
import com.onion.api.GameObject;
import com.onion.api.Core;

/**
 * Created by Jakub Petriska on 3. 1. 2015.
 */
public class Mesh extends Component {

    private final String mMeshName;

    /**
     * Creates new component and adds it to it's owner.
     *
     * @param owner
     */
    public Mesh(Core core, GameObject owner, String meshName) {
        super(core, owner);
        mMeshName = meshName;
    }

    public String getMeshName() {
        return mMeshName;
    }

    @Override
    public void postUpdate() {
        core.renderer.render(this);
    }
}
