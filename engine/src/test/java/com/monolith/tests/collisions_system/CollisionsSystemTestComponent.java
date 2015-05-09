package com.monolith.tests.collisions_system;

import com.monolith.api.Component;
import com.monolith.api.components.BoxCollider;

import static org.junit.Assert.*;

/**
 * Created by Jakub on 9. 5. 2015.
 */
public class CollisionsSystemTestComponent extends Component {

    private int frame = 0;
    private BoxCollider collider;

    @Override
    public void update() {
        ++frame;

        if(frame == 1) {
            collider = new BoxCollider();
            getGameObject().addComponent(collider);
        } else if(frame == 2) {
            boolean registeredAgain = getApplication().getCollisionsSystem().registerCollider(collider);
            assertFalse("Collider was either not registered during start or registered for second time.",
                    registeredAgain);
        } else if(frame == 3) {
            boolean unregistered = getApplication().getCollisionsSystem().unregisterCollider(collider);
            assertTrue("Collider was not successfully unregistered.", unregistered);
        } else if(frame == 4) {
            boolean registered = getApplication().getCollisionsSystem().registerCollider(collider);
            assertTrue("Collider was not successfully registered.",
                    registered);
        } else if(frame == 5) {
            getGameObject().removeComponent(collider);
        } else if(frame == 6) {
            boolean unregisteredAgain = getApplication().getCollisionsSystem().unregisterCollider(collider);
            assertFalse("Collider was either not unregistered during finish or unregistered for second time.",
                    unregisteredAgain);
        }

        if (frame == 8) {
            collider = new BoxCollider();
            getGameObject().addComponent(collider);
        } else if(frame == 9) {
            boolean registeredAgain = getApplication().getCollisionsSystem().registerCollider(collider);
            assertFalse("Collider was either not registered during start or registered for second time.",
                    registeredAgain);
        } else if(frame == 10) {
            getGameObject().removeComponent(collider);
        } else if(frame == 11) {
            boolean unregisteredAgain = getApplication().getCollisionsSystem().unregisterCollider(collider);
            assertFalse("Collider was either not unregistered during finish or unregistered for second time.",
                    unregisteredAgain);
        }
    }
}
