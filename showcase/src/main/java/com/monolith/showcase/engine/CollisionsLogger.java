package com.monolith.showcase.engine;

import com.monolith.api.Component;
import com.monolith.api.components.BoxCollider;

/**
 * Simple {@link com.monolith.api.Component} listening for collisions occurring
 * on the single {@link com.monolith.api.components.BoxCollider} attached to it's
 * {@link com.monolith.api.GameObject}.
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
