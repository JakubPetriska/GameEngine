package com.onion.engine;

import com.onion.api.Component;
import com.onion.api.Core;
import com.onion.api.GameObject;
import com.onion.api.TouchInput;
import com.onion.api.components.Mesh;
import com.onion.engine.config.SceneCreator;
import com.onion.engine.config.model.initial_scene_state.ISScene;
import com.onion.engine.config.model.scenes_config.SCScene;
import com.onion.engine.config.model.scenes_config.SCScenes;
import com.onion.platform.Platform;
import com.onion.platform.Renderer;
import com.onion.platform.TouchInputInternal;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Engine {

    private Platform mPlatform;
    private Core mCore;

    // Core objects
    private Renderer mRenderer;
    private TouchInputInternal mTouchInput;
    private MeshManager mMeshManager;

    private SCScenes mScenesConfig;

    // This is only used for searching in scenes List
    private SCScene mDummyScene = new SCScene();

    private String mCurrentSceneName;
    private Scene mCurrentScene;

    /**
     * @param startSceneName Name of the scene that this engine should start at first. If null engine will
     *                       use the default initial scene.
     * @param platform
     * @param touchInput
     */
    public Engine(String startSceneName, Platform platform, Renderer renderer, TouchInputInternal touchInput) {
        mCurrentSceneName = startSceneName;
        this.mPlatform = platform;
        this.mRenderer = renderer;
        this.mTouchInput = touchInput;

        mCore = new CoreImpl();

        mMeshManager = new MeshManager(mCore);
    }

    /**
     * Swaps objects that are provided to Engine during it's creation.
     * These are platform specific objects created by platform specific code.
     */
    public void swapProvidedObjects(Platform platform, Renderer renderer, TouchInputInternal touchInput) {
        this.mPlatform = platform;
        this.mRenderer = renderer;
        this.mTouchInput = touchInput;
    }

    // This flag ensures that engine is initialized only once (onStart method)
    private boolean mInitialized = false;

    // TODO validate all documents
    public void onStart() {
        if(mInitialized) {
            return;
        }

        mMeshManager.loadMeshes(); // Load all meshes into memory

        // Load scenes config file, load definition of scene that will be displayed
        // and construct this scene's object tree
        Serializer serializer = new Persister();
        try {
            mScenesConfig = serializer.read(SCScenes.class,
                    mPlatform.getAssetFile(Config.SCENES_FILE));
            // Sort scenes for quicker lookup of scenes during scene loading
            Collections.sort(mScenesConfig.scenes);
            if(mScenesConfig == null) {
                throw new IllegalStateException("Error during retrieval of scenes config file.");
            }
        } catch (Exception e) {
            throw new IllegalStateException("Error during retrieval of scenes config file.", e);
        }

        if(mCurrentSceneName == null) {
            mCurrentSceneName = mScenesConfig.defaultSceneName;
        }
        mCurrentScene = getScene(mCurrentSceneName, serializer);

        mInitialized = true;
	}

    private Scene getScene(String sceneName, Serializer serializer) {
        // Find the scene object in the scenes definition structure
        mDummyScene.name = sceneName;
        int sceneIndex = Collections.binarySearch(mScenesConfig.scenes, mDummyScene);
        if(sceneIndex < 0) {
            throw new IllegalStateException("Scene that was requested could not be found.");
        }
        String configFilePath = mScenesConfig.scenes.get(sceneIndex).sceneFilePath;

        ISScene scene;
        try {
            scene = serializer.read(ISScene.class,
                    mPlatform.getAssetFile(configFilePath));
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Error during retrieval of scene config file " + configFilePath);
        }
        Scene result = new SceneCreator(mCore).create(scene);
        // TODO remove this - in future this will not be possible due to initial state document validation
        // TODO this actually doesn't make sense, result can never be null
        if(result == null) {
            throw new IllegalStateException("Scene was not created.");
        }
        return result;
    }

    public void onUpdate() {
        mTouchInput.update();

        update(mCurrentScene.gameObjects);
        postUpdate(mCurrentScene.gameObjects);
    }

    private void update(List<GameObject> gameObjects) {
        for(int i = 0; i < gameObjects.size(); ++i) {
            GameObject gameObject = gameObjects.get(i);
            for(int j = 0; j < gameObject.components.size(); ++j) {
                gameObject.components.get(j).update();
            }
            update(gameObject.children);
        }
    }

    private void postUpdate(List<GameObject> gameObjects) {
        for(int i = 0; i < gameObjects.size(); ++i) {
            GameObject gameObject = gameObjects.get(i);
            for(int j = 0; j < gameObject.components.size(); ++j) {
                gameObject.components.get(j).postUpdate();
            }
            postUpdate(gameObject.children);
        }
    }

    public void onFinish() {

    }

    private class CoreImpl implements Core {

        @Override
        public Renderer getRenderer() {
            return mRenderer;
        }

        @Override
        public TouchInput getTouchInput() {
            return mTouchInput;
        }

        @Override
        public MeshManager getMeshManager() {
            return mMeshManager;
        }
    }
}
