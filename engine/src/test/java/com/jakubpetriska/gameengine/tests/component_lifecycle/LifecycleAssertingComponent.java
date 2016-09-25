package com.jakubpetriska.gameengine.tests.component_lifecycle;

import com.jakubpetriska.gameengine.api.Component;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Component asserting correctness of it's own lifecycle.
 */
public class LifecycleAssertingComponent extends Component {

    private static List<LifecycleAssertingComponent> sObjectCache = new ArrayList<>();

    private boolean mStartCalled = false;
    private boolean mUpdateCalled = false;
    private boolean mFinishCalled = false;

    private String mTag;

    public LifecycleAssertingComponent(String tag) {
        this();
        mTag = tag;
    }

    public LifecycleAssertingComponent() {
        super();
        sObjectCache.add(this);
    }

    @Override
    public void start() {
        assertFalse(getMessage("Start is being called for second time"), mStartCalled);
        mStartCalled = true;
    }

    @Override
    public void update() {
        assertTrue(getMessage("Start was not called before update"), mStartCalled);

        assertFalse(getMessage("Update is being called for second time"), mUpdateCalled);
        mUpdateCalled = true;
    }

    @Override
    public void postUpdate() {
        assertTrue(getMessage("Start was not called before postUpdate"), mStartCalled);

        assertTrue(getMessage("Update was not called before PostUpdate"), mUpdateCalled);
        mUpdateCalled = false;
    }

    @Override
    public void finish() {
        assertTrue(getMessage("Start was not called before finish"), mStartCalled);

        assertFalse(getMessage("Finish is being called for second time"), mFinishCalled);
        mFinishCalled = true;
    }

    /**
     * This method should be called when this component is removed
     * from it's GameObject or if Engine's life ended.
     */
    public void checkEverythingOkInTheEnd() {
        assertTrue(getMessage("Start was not called during this component's lifecycle"), mStartCalled);
        assertTrue(getMessage("Finish was not called during this component's lifecycle"), mFinishCalled);
    }

    private String getMessage(String message) {
        if(mTag == null || mTag.length() == 0) {
            return message;
        } else {
            return message + " for component " + mTag;
        }
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
