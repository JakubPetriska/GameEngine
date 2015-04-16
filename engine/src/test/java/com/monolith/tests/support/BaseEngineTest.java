package com.monolith.tests.support;

import com.monolith.engine.Engine;

/**
 * Created by Jakub Petriska on 20. 2. 2015.
 */
public abstract class BaseEngineTest {

    private Engine mEngine;

    protected void setupEngine(String filesFolder) {
        setupEngine(filesFolder, null);
    }

    protected void setupEngine(String filesFolder, String initialSceneName) {
        mEngine = new Engine(initialSceneName);
        mEngine.insertProvidedObjects(new MockEnginePlatformObjects.MockPlatform(filesFolder),
                new MockEnginePlatformObjects.MockRenderer(),
                new MockEnginePlatformObjects.MockTouchInput());
    }

    protected Engine getEngine() {
        return mEngine;
    }

    protected void runEngine(String filesFolder, String sceneName, int loopCount) {
        setupEngine(filesFolder, sceneName);
        getEngine().onStart();
        for (int i = 0; i < loopCount; ++i) {
            getEngine().onUpdate();
        }
        getEngine().onFinish();
    }
}
