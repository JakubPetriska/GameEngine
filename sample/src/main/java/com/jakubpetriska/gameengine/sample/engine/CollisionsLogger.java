package com.jakubpetriska.gameengine.sample.engine;

import com.jakubpetriska.gameengine.api.Component;
import com.jakubpetriska.gameengine.api.GameObject;
import com.jakubpetriska.gameengine.api.components.BoxCollider;

/**
 * Simple {@link Component} listening for collisions occurring
 * on the single {@link BoxCollider} attached to it's
 * {@link GameObject}.
 *
 * Collision occurrence is logged using debug log.
 */
public class CollisionsLogger extends Component {

    public String name;

    @Override
    public void start() {
        BoxCollider collider = getGameObject().getComponent(BoxCollider.class);
        collider.registerCollisionListener(new BoxCollider.CollisionListener() {
            @Override
            public void onCollisionDetected(BoxCollider collisionObject) {
                getApplication().getDebug().log(name + " colliding");
            }

            @Override
            public void onCollisionEnded(BoxCollider collisionObject) {
                getApplication().getDebug().log(name + " not colliding");
            }
        });
    }
}
