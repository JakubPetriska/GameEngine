package com.jeb.api.components;

import com.jeb.api.Component;
import com.jeb.api.GameObject;

/**
 * Created by Jakub Petriska on 3. 1. 2015.
 */
public class Mesh extends Component {

    private String mMesh;

    /**
     * Creates new component and adds it to it's owner.
     *
     * @param owner
     */
    public Mesh(GameObject owner) {
        super(owner);
    }

    public String getMesh() {
        return mMesh;
    }

    public void setMesh(String mMesh) {
        this.mMesh = mMesh;
    }

    @Override
    public void init() {

    }

    @Override
    public void update() {

    }

    @Override
    public void postUpdate() {

    }

    @Override
    public void finish() {

    }
}
