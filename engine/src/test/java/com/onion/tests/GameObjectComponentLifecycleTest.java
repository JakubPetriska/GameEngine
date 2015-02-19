package com.onion.tests;

import com.onion.engine.Engine;
import com.onion.tests.support.MockEnginePlatformObjects;

import org.junit.Before;

/**
 * Created by Jakub Petriska on 19. 2. 2015.
 */
public class GameObjectComponentLifecycleTest {

    private Engine mEngine;

    @Before
    public void prepareEngine() {
        mEngine = new Engine(null,
                new MockEnginePlatformObjects.MockPlatform(""),
                new MockEnginePlatformObjects.MockRenderer(),
                new MockEnginePlatformObjects.MockTouchInput());
    }

}
