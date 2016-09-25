package com.monolith.game.engine;

import com.monolith.api.Color;
import com.monolith.api.Component;
import com.monolith.api.GameObject;
import com.monolith.api.components.BoxCollider;
import com.monolith.api.components.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jakub on 30. 4. 2015.
 */
public class TrackGenerator extends Component {

    public static final String TAG_PICKUP = "pickup";
    public static final String TAG_OBSTACLE = "obstacle";

    private static final int TRACK_LENGTH_IN_PARTS = 10;
    private static final int EMPTY_TRACKS_COUNT = 2;

    private static final float TRACK_PART_LENGTH = 40;

    private static final int TRACK_PART_LENGTH_IN_BLOCKS = 20; // Must be even
    private static final int TRACK_PART_WIDTH_IN_BLOCKS = PlayerController.LANE_HALF_COUNT * 2 + 1; // Must be odd

    private static final float BLOCK_LENGTH = TRACK_PART_LENGTH / TRACK_PART_LENGTH_IN_BLOCKS;
    private static final float BLOCK_WIDTH = PlayerController.LANE_DISTANCE;

    private static final double PICKUP_PERC = 0.03;
    private static final double OBSTACLE_PERC = 0.97;

    private List<GameObject> trackParts = new ArrayList<>();

    @Override
    public void start() {
        addMissingTrackPieces();
    }

    @Override
    public void postUpdate() {
        if (trackParts.get(0).transform.getPositionZ() < -TRACK_PART_LENGTH) {
            getGameObject().removeChild(trackParts.remove(0));
        }
        addMissingTrackPieces();
    }

    private void addMissingTrackPieces() {
        GameObject lastTrackPart = trackParts.size() > 0 ? trackParts.get(trackParts.size() - 1) : null;
        for (int i = trackParts.size(); i < TRACK_LENGTH_IN_PARTS; ++i) {
            GameObject newTrackPart = generateNewTrackPart(i >= EMPTY_TRACKS_COUNT);
            if (lastTrackPart != null) {
                newTrackPart.transform.setPosition(0, 0,
                        lastTrackPart.transform.getPositionZ() + TRACK_PART_LENGTH);
            }
            trackParts.add(newTrackPart);
            lastTrackPart = newTrackPart;
        }
    }

    public void moveTrackBy(float movement) {
        if (trackParts.size() == 0) {
            return;
        }
        GameObject firstTrackPart = trackParts.get(0);
        firstTrackPart.transform.translateBy(0, 0, movement);

        int i = 1;
        while (i < trackParts.size()) {
            GameObject currentTrackPart = trackParts.get(i);
            currentTrackPart.transform.setPosition(0, 0, firstTrackPart.transform.getPositionZ() + TRACK_PART_LENGTH);

            firstTrackPart = currentTrackPart;
            ++i;
        }
    }

    private static final float ZERO_BLOCK_X = -PlayerController.LANE_HALF_COUNT * BLOCK_WIDTH;
    private static final float ZERO_BLOCK_Y = -(TRACK_PART_LENGTH_IN_BLOCKS / 2) * BLOCK_WIDTH + (BLOCK_WIDTH / 2);

    private GameObject generateNewTrackPart(boolean generateObjects) {
        GameObject newTrackPart = new GameObject(getGameObject());
        Model trackPartModel = new Model();
        trackPartModel.meshPath = "models/track_1.obj";
        newTrackPart.addComponent(trackPartModel);

        if (generateObjects) {
            for (int i = 0; i < TRACK_PART_LENGTH_IN_BLOCKS; ++i) {
                for (int j = 0; j < TRACK_PART_WIDTH_IN_BLOCKS; ++j) {
                    double rand = Math.random();
                    if (rand <= PICKUP_PERC) {
                        addPickup(newTrackPart, getX(j), getY(i));
                    } else if (rand >= OBSTACLE_PERC) {
                        addObstacle(newTrackPart, getX(j), getY(i));
                    }
                }
            }
        }

        return newTrackPart;
    }

    private float getX(int blockXIndex) {
        return ZERO_BLOCK_X + blockXIndex * BLOCK_WIDTH;
    }

    private float getY(int blockYIndex) {
        return ZERO_BLOCK_Y + blockYIndex * BLOCK_LENGTH;
    }

    private void addPickup(GameObject parent, float x, float z) {
        GameObject pickupContainer = new GameObject(parent);
        pickupContainer.tag = TAG_PICKUP;
        pickupContainer.transform.setPosition(x, 1, z);

        BoxCollider pickupCollider = new BoxCollider();
        pickupCollider.group = "static";
        pickupContainer.addComponent(pickupCollider);

        GameObject pickup = new GameObject(pickupContainer);
        pickup.transform.setScale(0.4f, 0.4f, 0.4f);
        pickup.addComponent(new PickupAnimator());

        Model model = new Model();
        model.meshPath = "models/star.obj";
        model.color = new Color(248, 234, 5, 255);
        pickup.addComponent(model);
    }

    private void addObstacle(GameObject parent, float x, float z) {
        GameObject obstacle = new GameObject(parent);
        obstacle.tag = TAG_OBSTACLE;
        obstacle.transform.setPosition(x, 1, z);
        obstacle.transform.setScale(BLOCK_WIDTH, 2, BLOCK_LENGTH);

        BoxCollider pickupCollider = new BoxCollider();
        pickupCollider.group = "static";
        obstacle.addComponent(pickupCollider);

        Model model = new Model();
        model.meshPath = "cube";
        model.color = new Color(31, 229, 55, 255);
        obstacle.addComponent(model);
    }
}
