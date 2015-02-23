package com.onion.tests.support;

import com.onion.engine.Engine;

/**
 * Created by Jakub Petriska on 20. 2. 2015.
 */
public abstract class BaseEngineTest {

    private Engine mEngine;

    protected void setupEngine(String filesFolder) {
        setupEngine(null, filesFolder);
    }

    protected void setupEngine(String initialSceneName, String filesFolder) {
        mEngine = new Engine(initialSceneName,
                new MockEnginePlatformObjects.MockPlatform(filesFolder),
                new MockEnginePlatformObjects.MockRenderer(),
                new MockEnginePlatformObjects.MockTouchInput());
    }

    protected Engine getEngine() {
        return mEngine;
    }
}
