package com.monolith.showcase.engine;

import com.monolith.api.Component;
import com.monolith.api.GameObject;
import com.monolith.api.Primitives;
import com.monolith.api.components.Model;

/**
 * Created by Jakub on 16. 4. 2015.
 */
public class CubesGenerator extends Component {

    private static final float CUBE_SPACE = 2;

    public int cubeLayerCount;

    @Override
    public void start() {
        for (int layer = 0; layer < cubeLayerCount; ++layer) {
            float distance = (layer + 1) * CUBE_SPACE;
            int halfLayerSizeFloor = 1 + layer;
            for (int i = -halfLayerSizeFloor; i <= halfLayerSizeFloor; ++i) {
                for (int j = -halfLayerSizeFloor; j <= halfLayerSizeFloor; ++j) {
                    addCube(i * CUBE_SPACE, distance, j * CUBE_SPACE);
                    addCube(i * CUBE_SPACE, -distance, j * CUBE_SPACE);
                }
            }
        }
    }

    private void addCube(float x, float y, float z) {
        GameObject newCube = new GameObject(getGameObject());
        Model model = new Model();
        model.meshPath = Primitives.CUBE;
        newCube.addComponent(model);
        newCube.transform.setPosition(x, y, z);
    }
}
