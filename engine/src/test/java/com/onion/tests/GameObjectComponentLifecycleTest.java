package com.onion.tests;

import com.onion.engine.Engine;
import com.onion.tests.support.MockEnginePlatformObjects;

import org.junit.Before;

/**
 * Created by Jakub Petriska on 19. 2. 2015.
 */
public class GameObjectComponentLifecycleTest {

    private static final String FILES_FOLDER = "";

    private Engine mEngine;

    @Before
    public void prepareEngine() {
        mEngine = new Engine(null,
                new MockEnginePlatformObjects.MockPlatform(FILES_FOLDER),
                new MockEnginePlatformObjects.MockRenderer(),
                new MockEnginePlatformObjects.MockTouchInput());
    }

}
