package com.monolith.engine;

// TODO maybe this should be public feature?
// TODO maybe then it should be base class providing Application object

/**
 * Common API to the System. System can handle things such as time, messaging or rendering.
 */
public interface ISystem {
    void update();

    void postUpdate();
}
