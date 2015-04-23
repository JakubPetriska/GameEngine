package com.monolith.showcase.engine;

import com.monolith.api.Component;
import com.monolith.api.components.BoxCollider;

/**
 * Created by Jakub on 13. 4. 2015.
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
