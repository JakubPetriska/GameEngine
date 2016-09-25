package com.jakubpetriska.gameengine.tests.collisions_system;

import com.jakubpetriska.gameengine.tests.support.BaseEngineTest;

import org.junit.Test;

/**
 * Created by Jakub on 9. 5. 2015.
 */
public class CollisionsSystemTest extends BaseEngineTest {

    private static final String FILES_FOLDER = "collisions_system_test";

    /**
     * Basic test of collisions system.
     *
     * Tests registering and unregistering of colliders.
     */
    @Test
    public void basicComponentLifecycleTest() {
        runEngine(FILES_FOLDER, "main_scene", 15);
    }
}
