package com.jeb.engine;

import com.jeb.api.Scene;
import com.jeb.engine.config.SceneCreator;
import com.jeb.engine.config.model.initial_scene_state.ISScene;
import com.jeb.engine.config.model.scenes_config.SCScenes;
import com.jeb.platform.Platform;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

public class Engine {

    private Platform mPlatform;

    private SCScenes mScenesConfig;

    private String mCurrentSceneName;
    private Scene mCurrentScene;

    /**
     *
     * @param startSceneName Name of the scene that this engine should start at first. If null engine will
     *                       use the default initial scene.
     * @param platform
     */
    public Engine(String startSceneName, Platform platform) {
        mCurrentSceneName = startSceneName;
        mPlatform = platform;
    }

    // TODO validate all documents
    public void onStart() {
        Serializer serializer = new Persister();
        try {
            mScenesConfig = serializer.read(SCScenes.class,
                    mPlatform.getConfigFile(Config.SCENES_FILE));
            if(mScenesConfig == null) {
                throw new IllegalStateException("Error during retrieval scenes config file.");
            }
        } catch (Exception e) {
            throw new IllegalStateException("Error during retrieval scenes config file.", e);
        }

        if(mCurrentSceneName == null) {
            mCurrentSceneName = mScenesConfig.initialSceneName;
        }

        mCurrentScene = getScene(mCurrentSceneName, serializer);
	}

    private Scene getScene(String name) {
        return getScene(name, new Persister());
    }

    private Scene getScene(String name, Serializer serializer) {
        String configFilePath = Config.BASE_ASSETS_PATH + name + ".xml";
        ISScene scene;
        try {
            scene = serializer.read(ISScene.class,
                    mPlatform.getConfigFile(configFilePath));
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Error during retrieval of scene config file " + configFilePath);
        }
        Scene result = SceneCreator.create(scene);
        // TODO remove this - in future this will not be possible due to initial state document validation
        if(result == null) {
            throw new IllegalStateException("Scene was not created.");
        }
        return result;
    }

    public void onUpdate() {

    }

    public void onFinish() {

    }
}
