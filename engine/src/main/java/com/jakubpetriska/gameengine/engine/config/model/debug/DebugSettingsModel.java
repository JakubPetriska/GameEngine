package com.jakubpetriska.gameengine.engine.config.model.debug;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root
public class DebugSettingsModel {

    @Attribute(required = false)
    public Boolean drawColliders;
}
