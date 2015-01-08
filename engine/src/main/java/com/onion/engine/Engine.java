package com.onion.engine;

import com.onion.api.Component;
import com.onion.api.Core;
import com.onion.api.GameObject;
import com.onion.api.Scene;
import com.onion.api.components.Mesh;
import com.onion.engine.config.SceneCreator;
import com.onion.engine.config.model.initial_scene_state.ISScene;
import com.onion.engine.config.model.scenes_config.SCScenes;
import com.onion.platform.Platform;
import com.onion.platform.Renderer;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.util.ArrayList;
import java.util.List;

public class Engine {

    private Core mCore;

    private SCScenes mScenesConfig;

    private String mCurrentSceneName;
    private Scene mCurrentScene;

    /**
     *
     * @param startSceneName Name of the scene that this engine should start at first. If null engine will
     *                       use the default initial scene.
     * @param platform
     */
    public Engine(String startSceneName, Platform platform, Renderer renderer) {
        mCurrentSceneName = startSceneName;
        mCore = new Core(platform, renderer);
    }

    // TODO validate all documents
    public void onStart() {
        Serializer serializer = new Persister();
        try {
            mScenesConfig = serializer.read(SCScenes.class,
                    mCore.platform.getConfigFile(Config.SCENES_FILE));
            if(mScenesConfig == null) {
                throw new IllegalStateException("Error during retrieval scenes config file.");
            }
        } catch (Exception e) {
            throw new IllegalStateException("Error during retrieval scenes config file.", e);
        }

        if(mCurrentSceneName == null) {
            mCurrentSceneName = mScenesConfig.defaultSceneName;
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
                    mCore.platform.getConfigFile(configFilePath));
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Error during retrieval of scene config file " + configFilePath);
        }
        SceneCreatorCallbackImpl callback = new SceneCreatorCallbackImpl();
        SceneCreator sceneCreator = new SceneCreator(mCore, callback);
        Scene result = sceneCreator.create(scene);
        mCore.renderer.init(callback.meshNames);
        // TODO remove this - in future this will not be possible due to initial state document validation
        if(result == null) {
            throw new IllegalStateException("Scene was not created.");
        }
        return result;
    }

    public void onUpdate() {
        update(mCurrentScene.gameObjects);
        postUpdate(mCurrentScene.gameObjects);
    }

    private void update(List<GameObject> gameObjects) {
        for(GameObject gameObject : gameObjects) {
            for(Component component : gameObject.components) {
                component.update();
            }
            update(gameObject.children);
        }
    }

    private void postUpdate(List<GameObject> gameObjects) {
        for(GameObject gameObject : gameObjects) {
            for(Component component : gameObject.components) {
                component.postUpdate();
            }
            postUpdate(gameObject.children);
        }
    }

    public void onFinish() {

    }

    private static class SceneCreatorCallbackImpl implements SceneCreator.SceneCreatorCallback {

        public final List<String> meshNames = new ArrayList<>();

        @Override
        public void onNewComponent(Component component) {
            if(component instanceof Mesh) {
                Mesh mesh = (Mesh) component;
                if(!meshNames.contains(mesh.getMeshName())) {
                    meshNames.add(mesh.getMeshName());
                }
            }
        }
    };
}
