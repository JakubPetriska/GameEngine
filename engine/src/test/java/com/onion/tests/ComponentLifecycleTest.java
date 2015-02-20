package com.onion.tests;

import com.onion.tests.support.BaseEngineTest;
import com.onion.tests.support.LifecycleAssertingComponent;

import org.junit.Test;

/**
 * Created by Jakub Petriska on 19. 2. 2015.
 */
public class ComponentLifecycleTest extends BaseEngineTest {

    private static final String FILES_FOLDER = "component_lifecycle_test";

    @Override
    protected String getFilesFolder() {
        return FILES_FOLDER;
    }

    @Test
    public void basicComponentLifecycleTest() {
        getEngine().onStart();
        for(int i = 0; i < 10; ++i) {
            getEngine().onUpdate();
        }
        getEngine().onFinish();

        LifecycleAssertingComponent.checkObjectsInCacheAreOk();
    }

}
