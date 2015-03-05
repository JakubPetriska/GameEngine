package com.monolith.showcase.engine;

import com.monolith.api.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jakub Petriska on 23. 2. 2015.
 */
public class SceneSwitchingController extends Component {

    public static final String SWITCH_SCENES_MESSAGE = "SWITCH_SCENES";

    private static final String ONE_CUBE_SCENE = "one_cube_scene";
    private static final String TWO_CUBES_SCENE = "two_cubes_scene";

    private List<String> mMessageList = new ArrayList<>();

    @Override
    public void update() {
        getApplication().getMessenger().getMessages(mMessageList, String.class);
        for(String message : mMessageList) {
            if(SWITCH_SCENES_MESSAGE.equals(message)) {
                switchScenes();
                break;
            }
        }
        mMessageList.clear();
    }

    private void switchScenes() {
        String newSceneName;
        if(ONE_CUBE_SCENE.equals(getApplication().getCurrentSceneName())) {
            newSceneName = TWO_CUBES_SCENE;
        } else {
            newSceneName = ONE_CUBE_SCENE;
        }
        getApplication().changeScene(newSceneName);
    }
}
