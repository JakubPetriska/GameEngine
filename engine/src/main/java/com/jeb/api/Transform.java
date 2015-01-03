package com.jeb.api;

import org.lwjgl.util.vector.Vector3f;

public class Transform {
	public final Vector3f position = new Vector3f();
	
	public void add(Transform transform) {
		Vector3f.add(position, transform.position, position);
	}
	
	public void subtract(Transform transform) {
		Vector3f.sub(position, transform.position, position);
	}
	
	public void copyFrom(Transform transform) {
		position.x = transform.position.x;
		position.y = transform.position.y;
		position.z = transform.position.z;
	}
}
