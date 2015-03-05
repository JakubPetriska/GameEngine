package com.monolith.engine;

import com.monolith.api.Application;
import com.monolith.api.GameObject;
import com.monolith.api.TouchInput;
import com.monolith.engine.config.SceneCreator;
import com.monolith.engine.config.model.initial_scene_state.ISScene;
import com.monolith.engine.config.model.scenes_config.SCScene;
import com.monolith.engine.config.model.scenes_config.SCScenes;
import com.monolith.engine.messaging.InputMessenger;
import com.monolith.engine.messaging.InputMessengerInternal;
import com.monolith.engine.messaging.Messenger;
import com.monolith.platform.Platform;
import com.monolith.platform.Renderer;
import com.monolith.platform.TouchInputInternal;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.util.Collections;
import java.util.List;

public class Engine {

    private Platform mPlatform;
    private Application mApplication;

    private InputMessengerInternal mInputMessengerInternal;

    // Core objects
    private Renderer mRenderer;
    private TouchInputInternal mTouchInput;
    private MeshManager mMeshManager;
    private Messenger mMessenger;

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

        mInputMessengerInternal = new InputMessengerInternal();
        mMessenger = new Messenger(mInputMessengerInternal);

        mApplication = new ApplicationImpl();
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
        if (mInitialized) {
            return;
        }

        loadScenesConfig();

        if (mCurrentSceneName == null) {
            mCurrentSceneName = mScenesConfig.defaultSceneName;
        }

        mApplication.changeScene(mCurrentSceneName);

        mInitialized = true;
    }

    private void loadScenesConfig() {
        Serializer serializer = new Persister();
        try {
            mScenesConfig = serializer.read(SCScenes.class,
                    mPlatform.getAssetFile(Config.SCENES_FILE));
            // Sort scenes for quicker lookup of scenes during scene loading
            Collections.sort(mScenesConfig.scenes);
            if (mScenesConfig == null) {
                throw new IllegalStateException("Error during retrieval of scenes config file.");
            }
        } catch (Exception e) {
            throw new IllegalStateException("Error during retrieval of scenes config file.", e);
        }
    }

    private Scene getScene(String sceneName) {
        // Find the scene object in the scenes definition structure
        mDummyScene.name = sceneName;
        int sceneIndex = Collections.binarySearch(mScenesConfig.scenes, mDummyScene);
        if (sceneIndex < 0) {
            throw new IllegalStateException("Scene that was requested could not be found.");
        }
        String configFilePath = mScenesConfig.scenes.get(sceneIndex).sceneFilePath;

        ISScene scene;
        try {
            scene = new Persister().read(ISScene.class,
                    mPlatform.getAssetFile(configFilePath));
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Error during retrieval of scene config file " + configFilePath);
        }
        Scene result = new SceneCreator(mApplication).create(scene);
        // TODO remove this - in future this will not be possible due to initial state document validation
        // TODO this actually doesn't make sense, result can never be null
        if (result == null) {
            throw new IllegalStateException("Scene was not created.");
        }
        return result;
    }

    public void onUpdate() {
        // TODO create something like System? Common API for these two.
        mMessenger.update();
        mTouchInput.update();

        update(mCurrentScene.gameObjects);
        postUpdate(mCurrentScene.gameObjects);
    }

    private void update(List<GameObject> gameObjects) {
        for (int i = 0; i < gameObjects.size(); ++i) {
            GameObject gameObject = gameObjects.get(i);
            for (int j = 0; j < gameObject.components.size(); ++j) {
                gameObject.components.get(j).update();
            }
            update(gameObject.children);
        }
    }

    private void postUpdate(List<GameObject> gameObjects) {
        for (int i = 0; i < gameObjects.size(); ++i) {
            GameObject gameObject = gameObjects.get(i);
            for (int j = 0; j < gameObject.components.size(); ++j) {
                gameObject.components.get(j).postUpdate();
            }
            postUpdate(gameObject.children);
        }
    }

    public void onFinish() {
        finish(mCurrentScene.gameObjects);
    }

    private void finish(List<GameObject> gameObjects) {
        for (int i = 0; i < gameObjects.size(); ++i) {
            GameObject gameObject = gameObjects.get(i);
            for (int j = 0; j < gameObject.components.size(); ++j) {
                gameObject.components.get(j).finish();
            }
            finish(gameObject.children);
        }
    }

    public InputMessenger getInputMessenger() {
        return mInputMessengerInternal.getInputMessenger();
    }

    private class ApplicationImpl implements Application {

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

        @Override
        public Messenger getMessenger() {
            return mMessenger;
        }

        @Override
        public void changeScene(String newSceneName) {
            mMeshManager = new MeshManager(mApplication);
            mCurrentScene = getScene(newSceneName);
            mCurrentSceneName = newSceneName;
        }

        @Override
        public String getCurrentSceneName() {
            return mCurrentSceneName;
        }
    }
}
