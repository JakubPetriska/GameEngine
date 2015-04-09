package com.monolith.engine;

import com.monolith.api.Application;
import com.monolith.api.Component;
import com.monolith.api.DebugSettings;
import com.monolith.api.DebugUtility;
import com.monolith.api.GameObject;
import com.monolith.api.Messenger;
import com.monolith.api.Renderer;
import com.monolith.api.Time;
import com.monolith.api.TouchInput;
import com.monolith.api.components.Camera;
import com.monolith.api.external.InputMessenger;
import com.monolith.engine.config.SceneCreator;
import com.monolith.engine.config.model.debug.DebugSettingsModel;
import com.monolith.engine.config.model.initial_scene_state.ISScene;
import com.monolith.engine.config.model.scenes_config.SCScene;
import com.monolith.engine.config.model.scenes_config.SCScenes;
import com.monolith.engine.messaging.InputMessengerInternal;
import com.monolith.engine.messaging.MessengerInternal;
import com.monolith.platform.Platform;
import com.monolith.platform.TouchInputInternal;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents one engine instance. Engine holds everything together.
 */
public class Engine {

    private Platform mPlatform;
    private Application mApplication;

    private InputMessengerInternal mInputMessengerInternal;

    // Application objects
    private FullRenderer mRenderer;
    private TouchInputInternal mTouchInput;
    private MeshManager mMeshManager;
    private MessengerInternal mMessenger;
    private TimeInternal mTime;

    private List<ISystem> mInternalSystems = new ArrayList<>();

    private SCScenes mScenesConfig;

    // This is only used for searching in scenes List
    private SCScene mDummyScene = new SCScene();

    private String mCurrentSceneName;
    private Scene mCurrentScene;

    /**
     * Constructs new Engine instance.
     *
     * @param startSceneName Name of the scene that this engine should show first.
     *                       If null, engine will use the default scene as specified
     *                       in scenes configuration file.
     * @param platform       {@link com.monolith.platform.Platform} instance provided by the specific platform.
     * @param touchInput     {@link com.monolith.platform.TouchInputInternal} instance provided by the specific platform.
     */
    public Engine(String startSceneName, Platform platform, FullRenderer renderer, TouchInputInternal touchInput) {
        mCurrentSceneName = startSceneName;
        this.mPlatform = platform;
        this.mRenderer = renderer;
        this.mTouchInput = touchInput;

        mInputMessengerInternal = new InputMessengerInternal();
        mMessenger = new MessengerInternal(mInputMessengerInternal);

        mTime = new TimeInternal();

        mInternalSystems.add(mTime);
        mInternalSystems.add(mMessenger);
        mInternalSystems.add(mTouchInput);

        mApplication = new ApplicationImpl(parseDebugSettingsFile());
    }

    private DebugSettings parseDebugSettingsFile() {
        InputStream debugFileInputStream = mPlatform.getAssetFileInputStream(Config.DEBUG_FILE);
        if (debugFileInputStream == null) {
            return new DebugSettings();
        } else {
            Serializer serializer = new Persister();
            DebugSettingsModel parsedDebugSetings;
            try {
                parsedDebugSetings = serializer.read(DebugSettingsModel.class, debugFileInputStream);
                if (parsedDebugSetings == null) {
                    throw new IllegalStateException();
                }
            } catch (Exception e) {
                throw new IllegalStateException("Error during retrieval of debug config file.", e);
            }
            return new DebugSettings(parsedDebugSetings);
        }
    }

    /**
     * Swaps objects that are provided to Engine during it's creation.
     * These are platform specific objects created by platform specific code.
     * <p/>
     * This method allows resuming the Engine in different platform context.
     */
    public void swapProvidedObjects(Platform platform, FullRenderer renderer, TouchInputInternal touchInput) {
        Camera camera = mRenderer.getCamera();

        this.mPlatform = platform;
        this.mRenderer = renderer;

        mInternalSystems.remove(mTouchInput);
        mInternalSystems.add(touchInput);
        this.mTouchInput = touchInput;

        mRenderer.setCamera(camera);
    }

    // This flag ensures that engine is initialized only once (onStart method)
    private boolean mInitialized = false;

    // TODO validate all xml files

    /**
     * Must be called by platform. Initializes the engine.
     * It is possible to call this method for the second time on the same object
     * however second initialization is not performed.
     */
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

    /**
     * Loads scenes configuration file. This file contains names of all scenes together with
     * paths to the files defining initial state for every scene.
     */
    private void loadScenesConfig() {
        Serializer serializer = new Persister();
        try {
            mScenesConfig = serializer.read(SCScenes.class,
                    mPlatform.getAssetFileInputStream(Config.SCENES_FILE));
            // Sort scenes for quicker lookup of scenes during scene loading
            Collections.sort(mScenesConfig.scenes);
            if (mScenesConfig == null) {
                throw new IllegalStateException("Error during retrieval of scenes config file.");
            }
        } catch (Exception e) {
            throw new IllegalStateException("Error during retrieval of scenes config file.", e);
        }
    }

    /**
     * Loads scene's initial configuration and constructs {@link com.monolith.engine.Scene} object.
     *
     * @param sceneName Name of the scene to construct.
     * @return Fully constructed {@link com.monolith.engine.Scene} object.
     */
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
                    mPlatform.getAssetFileInputStream(configFilePath));
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Error during retrieval of scene config file " + configFilePath);
        }
        SceneCreator sceneCreator = new SceneCreator(mApplication);
        sceneCreator.create(scene);

        mRenderer.setCamera(sceneCreator.camera);
        return sceneCreator.scene;
    }

    /**
     * Must be called by platform every frame.
     * This call is dispatched to all components which results in scene state update and rendering.
     */
    public void onUpdate() {
        for (int i = 0; i < mInternalSystems.size(); ++i) {
            mInternalSystems.get(i).update();
        }

        update(mCurrentScene.gameObjects);
        mRenderer.onStartRenderingFrame();
        postUpdate(mCurrentScene.gameObjects);
    }

    /**
     * Helper recursive method to call {@link Component#update()} on all
     * {@link com.monolith.api.Component Components}.
     *
     * @param gameObjects {@link java.util.List} of top level scene objects.
     */
    private void update(List<GameObject> gameObjects) {
        for (int i = 0; i < gameObjects.size(); ++i) {
            GameObject gameObject = gameObjects.get(i);
            for (int j = 0; j < gameObject.components.size(); ++j) {
                gameObject.components.get(j).update();
            }
            update(gameObject.children);
        }
    }

    /**
     * Helper recursive method to call {@link Component#postUpdate()} on all
     * {@link com.monolith.api.Component Components}.
     *
     * @param gameObjects {@link java.util.List} of top level scene objects.
     */
    private void postUpdate(List<GameObject> gameObjects) {
        for (int i = 0; i < gameObjects.size(); ++i) {
            GameObject gameObject = gameObjects.get(i);
            for (int j = 0; j < gameObject.components.size(); ++j) {
                gameObject.components.get(j).postUpdate();
            }
            postUpdate(gameObject.children);
        }
    }

    /**
     * Must be called by platform when this engine instance finishes.
     * This call is dispatched to all components.
     */
    public void onFinish() {
        finish(mCurrentScene.gameObjects);
    }

    /**
     * Helper recursive method to call {@link Component#finish()} on all
     * {@link com.monolith.api.Component Components}.
     *
     * @param gameObjects {@link java.util.List} of top level scene objects.
     */
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

    private class ApplicationImpl extends Application {

        public ApplicationImpl(DebugSettings debugSettings) {
            super(debugSettings);
        }

        @Override
        public Renderer getRenderer() {
            return mRenderer;
        }

        @Override
        public TouchInput getTouchInput() {
            return mTouchInput;
        }

        @Override
        public MeshManager getModelManager() {
            return mMeshManager;
        }

        @Override
        public Messenger getMessenger() {
            return mMessenger;
        }

        @Override
        public Time getTime() {
            return mTime;
        }

        private DebugUtility mDebugUtility;

        @Override
        public DebugUtility getDebugUtility() {
            if (mDebugUtility == null) {
                mDebugUtility = new DebugUtility() {
                    @Override
                    public void log(String message) {
                        mPlatform.log(message);
                    }
                };
            }
            return mDebugUtility;
        }

        @Override
        public void changeScene(String newSceneName) {
            mMeshManager = new MeshManager(mApplication, mPlatform);
            mCurrentScene = getScene(newSceneName);
            mCurrentSceneName = newSceneName;
        }

        @Override
        public String getCurrentSceneName() {
            return mCurrentSceneName;
        }
    }
}
