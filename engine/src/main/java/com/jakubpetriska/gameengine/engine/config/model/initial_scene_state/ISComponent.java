package com.jakubpetriska.gameengine.engine.config.model.initial_scene_state;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

import java.util.Map;

@Root(name = "component")
public class ISComponent {

    @Attribute
    public String type;

    @ElementMap(required = false, entry = "param", key = "name", attribute = true, inline = true)
    public Map<String, String> params;
}
