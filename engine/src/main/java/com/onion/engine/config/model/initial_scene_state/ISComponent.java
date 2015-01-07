package com.onion.engine.config.model.initial_scene_state;

import java.util.Map;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

@Root(name="component")
public class ISComponent {
	
	@Attribute
	public String type;
	
	@ElementMap(entry="param", key="name", attribute=true)
	public Map<String, String> params;
	
	public String getParamValue(String name) {
		return params.get(name);
	}
}
