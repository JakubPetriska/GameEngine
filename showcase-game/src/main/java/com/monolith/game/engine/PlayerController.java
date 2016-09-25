package com.monolith.game.engine;

import com.monolith.api.Component;
import com.monolith.api.GameObject;
import com.monolith.api.components.BoxCollider;
import com.monolith.api.components.Model;

import java.util.List;

/**
 * Created by Jakub on 30. 4. 2015.
 */
public class PlayerController extends Component implements BoxCollider.CollisionListener {

    public static final String FINAL_MESSAGE = "FINISH";

    private static final int POINTS_PER_PICKUP = 10;

    public static final float LANE_DISTANCE = 1.8f;
    public static final int LANE_HALF_COUNT = 2; // Actual lane count is equal to LANE_HALF_COUNT * 2 + 1

    private static final float INITIAL_SPEED_UNITS_PER_SECOND = 25;
    private static final float ACCELERATION_UNITS_PER_SECOND = 0.35f;
    private static final float LANE_TRANSITION_SPEED = 2.5f;
    private static final float MAX_SPACESHIP_ROLL = 10;
    private static final float MAX_SPACESHIP_HEADING = 3;

    private GameObject trackContainer;
    private TrackGenerator trackGenerator;
    private GestureDetector gestureDetector;

    private float currentSpeed = INITIAL_SPEED_UNITS_PER_SECOND;
    private int currentLane = 0;
    private int transitionDirection = 0;
    private boolean continueLaneTransition;

    private boolean collisionListenersRegistered = false;

    private boolean paused = false;

    @Override
    public void start() {
        gestureDetector = new GestureDetector(getApplication());
        for (GameObject sibling : getGameObject().getParent().children) {
            if ("track_container".equals(sibling.tag)) {
                trackContainer = sibling;
                trackGenerator = sibling.getComponent(TrackGenerator.class);
            }
        }
        if (trackGenerator == null || trackContainer == null) {
            throw new IllegalStateException("Needed object could not be obtained.");
        }
    }

    @Override
    public void update() {
        if (!collisionListenersRegistered) {
            for (GameObject child : getGameObject().children) {
                if ("spaceship_collider".equals(child.tag)) {
                    BoxCollider collider = child.getComponent(BoxCollider.class);
                    collider.registerCollisionListener(this);
                }
            }
            collisionListenersRegistered = true;
        }

        List<GestureDetector.DetectedGesture> detectedGestures = gestureDetector.detectGestures();
        if (paused) {
            return;
        }

        float timeDelta = getApplication().getTime().getTimeDelta();
        currentSpeed += timeDelta * ACCELERATION_UNITS_PER_SECOND;
        trackGenerator.moveTrackBy(timeDelta * -currentSpeed);

        if (detectedGestures.size() > 0) {
            int newTransitionDirection = 0;
            switch (detectedGestures.get(0)) {
                case SWIPE_LEFT:
                    newTransitionDirection = -1;
                    break;
                case SWIPE_RIGHT:
                    newTransitionDirection = 1;
                    break;
            }
            if (transitionDirection == 0) {
                // Condition ensures that the spaceship does not move out of the track
                if (Math.abs(currentLane) != LANE_HALF_COUNT
                        || ((newTransitionDirection > 0 && currentLane < 0)
                        || (newTransitionDirection < 0 && currentLane > 0))) {
                    transitionDirection = newTransitionDirection;
                }
            } else if (newTransitionDirection != transitionDirection) {
                if (continueLaneTransition) {
                    continueLaneTransition = false;
                } else {
                    currentLane += transitionDirection;
                    transitionDirection *= -1;
                }
            } else {
                continueLaneTransition = true;
            }
        }

        if (transitionDirection != 0) {
            float transitionFraction = Math.abs(getTrackPosition() - currentLane);
            if (transitionFraction >= 1) {
                transitionFraction = 1;
            }
            float interpolation = interpolateLaneTransitionSpeed(transitionFraction);
            // Set the position on the track
            if (transitionFraction == 1) {
                currentLane += transitionDirection;
                if(!continueLaneTransition) {
                    transitionDirection = 0;
                } else {
                    continueLaneTransition = false;
                }
                setTrackPosition(currentLane);
                getGameObject().transform.setRotation(
                        getGameObject().transform.getRotationX(),
                        0, 0);
            } else {
                moveTrackPositionBy(
                        LANE_TRANSITION_SPEED
                                * interpolation
                                * transitionDirection
                                * getApplication().getTime().getTimeDelta());

                // Set the rotation of spaceship
                float rotationFraction = Math.abs(transitionFraction * 2 - 1) - 1;
                float rotation = transitionDirection * rotationFraction * interpolation;
                getGameObject().transform.setRotation(
                        getGameObject().transform.getRotationX(),
                        -rotation * MAX_SPACESHIP_HEADING,
                        rotation * MAX_SPACESHIP_ROLL);
            }
        }
    }

    private void moveTrackPositionBy(float trackPosition) {
        trackContainer.transform.translateBy(-trackPosition * LANE_DISTANCE, 0, 0);
    }

    private void setTrackPosition(float trackPosition) {
        trackContainer.transform.setPosition(-trackPosition * LANE_DISTANCE, 0, 0);
    }

    private float getTrackPosition() {
        return -trackContainer.transform.getPositionX() / LANE_DISTANCE;
    }

    private static float interpolateLaneTransitionSpeed(float transitionFraction) {
        return (float) (Math.sin(transitionFraction * Math.PI) + 0.6);
    }

    @Override
    public void onCollisionDetected(BoxCollider boxCollider) {
        if (TrackGenerator.TAG_OBSTACLE.equals(boxCollider.getGameObject().tag)) {
            paused = true;
            getApplication().getMessenger().sendMessage(FINAL_MESSAGE);
        } else if (TrackGenerator.TAG_PICKUP.equals(boxCollider.getGameObject().tag)) {
            getApplication().getMessenger().sendMessage(POINTS_PER_PICKUP);
            GameObject pickup = boxCollider.getGameObject();
            pickup.getParent().removeChild(pickup);
        }
    }

    @Override
    public void onCollisionEnded(BoxCollider boxCollider) {

    }
}
