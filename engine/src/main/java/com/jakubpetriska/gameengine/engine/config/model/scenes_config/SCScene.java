package com.jakubpetriska.gameengine.engine.config.model.scenes_config;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Scenes file scene element model.
 */
@Root(name = "scene")
public class SCScene implements Comparable<SCScene> {

    @Attribute
    public String name;

    @Attribute
    public String sceneFilePath;

    @Override
    public int compareTo(SCScene another) {
        return name.compareTo(another.name);
    }
}
