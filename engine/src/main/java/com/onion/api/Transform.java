package com.onion.api;

import org.lwjgl.util.vector.Vector3f;

public class Transform {

    private final GameObject mOwner;
	private final Vector3f mPosition = new Vector3f();

    Transform(GameObject mOwner) {
        this.mOwner = mOwner;
    }

    public void moveBy(float x, float y, float z) {
        mPosition.x += x;
        mPosition.y += y;
        mPosition.z += z;

        for(int i = 0; i < mOwner.children.size(); ++i) {
            mOwner.children.get(i).transform.moveBy(x, y, z);
        }
    }

    public float getPositionX() {
        return mPosition.x;
    }

    public float getPositionY() {
        return mPosition.y;
    }

    public float getPositionZ() {
        return mPosition.z;
    }
}
