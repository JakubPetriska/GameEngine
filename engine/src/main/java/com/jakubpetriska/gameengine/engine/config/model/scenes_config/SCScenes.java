package com.jakubpetriska.gameengine.engine.config.model.scenes_config;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Scenes file root element model.
 */
@Root
public class SCScenes {
    @Attribute
    public String defaultSceneName;

    @ElementList(inline = true)
    public List<SCScene> scenes;
}
