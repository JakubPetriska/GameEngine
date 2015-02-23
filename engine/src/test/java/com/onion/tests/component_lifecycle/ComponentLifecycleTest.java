package com.onion.tests.component_lifecycle;

import com.onion.tests.support.BaseEngineTest;

import org.junit.After;
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

    /**
     * Basic test of component lifecycle.
     *
     * Uses LifecycleAssertingComponent in the scene with 3 objects.
     * The two objects are children of the first object. All objects only
     * have a single component a LifecycleAssertingComponent component.
     */
    @Test
    public void basicComponentLifecycleTest() {
        getEngine().onStart();
        for(int i = 0; i < 10; ++i) {
            getEngine().onUpdate();
        }
        getEngine().onFinish();

        LifecycleAssertingComponent.checkObjectsInCacheAreOk();
    }

    @After
    public void testCleanup() {
        LifecycleAssertingComponent.clearObjectCache();
    }
}
