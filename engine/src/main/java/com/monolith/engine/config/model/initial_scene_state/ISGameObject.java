package com.monolith.engine.config.model.initial_scene_state;

import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class ISGameObject {

	@Element
	public Transform transform;
	
	@ElementList(required=false)
	public List<ISGameObject> children;
	
	@ElementList(required=false)
	public List<ISComponent> components;
	
	public static class Transform {
		@Element
		public Position position;
	}
	
	public static class Position {
		@Attribute
		public float x;
		@Attribute
		public float y;
		@Attribute
		public float z;
	}
}
