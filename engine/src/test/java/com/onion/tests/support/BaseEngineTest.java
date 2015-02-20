package com.onion.tests.support;

import com.onion.engine.Engine;

import org.junit.After;
import org.junit.Before;

/**
 * Created by Jakub Petriska on 20. 2. 2015.
 */
public abstract class BaseEngineTest {

    private Engine mEngine;

    @Before
    public void prepareEngine() {
        mEngine = new Engine(null,
                new MockEnginePlatformObjects.MockPlatform(getFilesFolder()),
                new MockEnginePlatformObjects.MockRenderer(),
                new MockEnginePlatformObjects.MockTouchInput());
    }

    @After
    public void testCleanup() {
        LifecycleAssertingComponent.clearObjectCache();
    }

    protected abstract String getFilesFolder();

    public Engine getEngine() {
        return mEngine;
    }
}
