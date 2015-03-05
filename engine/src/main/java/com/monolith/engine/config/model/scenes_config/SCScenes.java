package com.monolith.engine.config.model.scenes_config;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Jakub Petriska on 3. 1. 2015.
 */
@Root
public class SCScenes {
    @Attribute
    public String defaultSceneName;

    @ElementList(inline = true)
    public List<SCScene> scenes;
}
