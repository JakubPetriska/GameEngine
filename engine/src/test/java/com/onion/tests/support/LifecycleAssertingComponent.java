package com.onion.tests.support;

import com.onion.api.Component;

/**
 * Created by Jakub Petriska on 20. 2. 2015.
 */
public class LifecycleAssertingComponent extends Component {

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void postUpdate() {
        super.postUpdate();
    }

    @Override
    public void finish() {
        super.finish();
    }
}
