package com.jeb.engine.config.model.scenes_config;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Created by Jakub Petriska on 3. 1. 2015.
 */
@Root(name="scene")
public class SCScene {

    @Attribute
    public String name;
}
