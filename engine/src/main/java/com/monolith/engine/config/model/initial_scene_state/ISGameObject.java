package com.monolith.engine.config.model.initial_scene_state;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root
public class ISGameObject {

    @Element(required = false)
    public Transform transform;

    @ElementList(required = false)
    public List<ISGameObject> children;

    @ElementList(required = false)
    public List<ISComponent> components;

    public static class Transform {
        @Element(required = false)
        public Vector position;
        @Element(required = false)
        public Vector rotation;
    }

    public static class Vector {
        @Attribute
        public float x;
        @Attribute
        public float y;
        @Attribute
        public float z;
    }
}
