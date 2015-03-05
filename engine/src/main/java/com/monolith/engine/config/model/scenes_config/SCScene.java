package com.monolith.engine.config.model.scenes_config;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Created by Jakub Petriska on 3. 1. 2015.
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
