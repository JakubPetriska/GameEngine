package com.onion.tests.support;

import com.onion.api.Component;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Jakub Petriska on 20. 2. 2015.
 */
public class LifecycleAssertingComponent extends Component {

    private static List<LifecycleAssertingComponent> sObjectCache = new ArrayList<>();

    private boolean mStartCalled = false;
    private boolean mUpdateCalled = false;
    private boolean mFinishCalled = false;

    public LifecycleAssertingComponent() {
        super();
        sObjectCache.add(this);
    }

    @Override
    public void start() {
        assertFalse("Start is being called for second time", mStartCalled);
        mStartCalled = true;
    }

    @Override
    public void update() {
        assertTrue("Start was not called before update", mStartCalled);

        assertFalse("Update is being called for second time", mUpdateCalled);
        mUpdateCalled = true;
    }

    @Override
    public void postUpdate() {
        assertTrue("Start was not called before postUpdate", mStartCalled);

        assertTrue("Update was not called before PostUpdate", mUpdateCalled);
        mUpdateCalled = false;
    }

    @Override
    public void finish() {
        assertTrue("Start was not called before finish", mStartCalled);

        assertFalse("Finish is being called for second time", mFinishCalled);
        mFinishCalled = true;
    }

    /**
     * This method should be called when this component is removed
     * from it's GameObject or if Engine's life ended.
     */
    public void checkEverythingOkInTheEnd() {
        assertTrue("Start was not called during this component's lifecycle", mStartCalled);
        assertTrue("Finish was not called during this component's lifecycle", mFinishCalled);
    }

    /**
     * This method should be called when Engine's life ended to ensure
     * all component's of this class had proper lifecycle.
     */
    public static void checkObjectsInCacheAreOk() {
        for(LifecycleAssertingComponent component : sObjectCache) {
            component.checkEverythingOkInTheEnd();
        }
    }

    public static void clearObjectCache() {
        sObjectCache.clear();
    }
}
