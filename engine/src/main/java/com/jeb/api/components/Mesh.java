package com.jeb.api.components;

import com.jeb.api.Component;
import com.jeb.api.GameObject;
import com.jeb.engine.Core;

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
        mCore.renderer.render(this);
    }
}
