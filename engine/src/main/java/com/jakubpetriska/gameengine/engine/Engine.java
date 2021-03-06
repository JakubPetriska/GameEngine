package com.jakubpetriska.gameengine.engine;

import com.jakubpetriska.gameengine.api.Application;
import com.jakubpetriska.gameengine.api.CollisionsSystem;
import com.jakubpetriska.gameengine.api.Component;
import com.jakubpetriska.gameengine.api.Debug;
import com.jakubpetriska.gameengine.api.Display;
import com.jakubpetriska.gameengine.api.GameObject;
import com.jakubpetriska.gameengine.api.Messenger;
import com.jakubpetriska.gameengine.api.Renderer;
import com.jakubpetriska.gameengine.api.Time;
import com.jakubpetriska.gameengine.api.TouchInput;
import com.jakubpetriska.gameengine.api.components.Camera;
import com.jakubpetriska.gameengine.api.external.ExternalMessenger;
import com.jakubpetriska.gameengine.engine.config.SceneCreator;
import com.jakubpetriska.gameengine.engine.config.model.debug.DebugSettingsModel;
import com.jakubpetriska.gameengine.engine.config.model.initial_scene_state.ISScene;
import com.jakubpetriska.gameengine.engine.config.model.scenes_config.SCScene;
import com.jakubpetriska.gameengine.engine.config.model.scenes_config.SCScenes;
import com.jakubpetriska.gameengine.engine.messaging.ExternalMessengerInternal;
import com.jakubpetriska.gameengine.engine.messaging.MessengerInternal;
import com.jakubpetriska.gameengine.platform.Platform;
import com.jakubpetriska.gameengine.platform.TouchInputInternal;

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

    private ExternalMessengerInternal mExternalMessengerInternal;

    // Application objects
    private FullRenderer mRenderer;
    private TouchInputInternal mTouchInput;
    private MeshManager mMeshManager;
    private MessengerInternal mMessenger;
    private TimeInternal mTime;
    private CollisionsSystem mCollisionsSystem;
    private Display mDisplay;

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
     */
    public Engine(String startSceneName) {
        mCurrentSceneName = startSceneName;

        mExternalMessengerInternal = new ExternalMessengerInternal();
        mMessenger = new MessengerInternal(mExternalMessengerInternal);

        mTime = new TimeInternal();

        mInternalSystems.add(mTime);
        mInternalSystems.add(mMessenger);
    }

    private DebugSettingsModel parseDebugSettingsFile() {
        InputStream debugFileInputStream = mPlatform.getAssetFileInputStream(Config.DEBUG_FILE);
        if (debugFileInputStream == null) {
            return null;
        } else {
            Serializer serializer = new Persister();
            DebugSettingsModel parsedDebugSetings;
            try {
                parsedDebugSetings = serializer.read(DebugSettingsModel.class, debugFileInputStream);
                if (parsedDebugSetings == null) {
                    throw new IllegalStateException();
                } else {
                    return parsedDebugSetings;
                }
            } catch (Exception e) {
                throw new IllegalStateException("Error during retrieval of debug config file.", e);
            }
        }
    }

    /**
     * Inserts or replaces objects that are provided to Engine during it's creation.
     * These are platform specific objects created by platform specific code.
     *
     * @param platform   {@link Platform} instance provided by the specific platform.
     * @param renderer   {@link FullRenderer} instance provided by the specific platform.
     * @param touchInput {@link TouchInputInternal} instance provided by the specific platform.
     */
    public void insertProvidedObjects(Platform platform, FullRenderer renderer, TouchInputInternal touchInput) {
        Camera camera = mRenderer != null ? mRenderer.getCamera() : null;

        this.mPlatform = platform;
        this.mRenderer = renderer;

        mInternalSystems.remove(mTouchInput);
        mInternalSystems.add(touchInput);
        this.mTouchInput = touchInput;

        mDisplay = mPlatform.createDisplay();

        if (mApplication == null) {
            mApplication = new ApplicationImpl();

            mCollisionsSystem = new CollisionsSystem(mApplication);
            mInternalSystems.add(mCollisionsSystem);
        }

        mRenderer.setApplication(mApplication);
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
     * Loads scene's initial configuration and constructs {@link Scene} object.
     *
     * @param sceneName Name of the scene to construct.
     * @return Fully constructed {@link Scene} object.
     */
    private SceneCreator getScene(String sceneName) {
        // Find the scene object in the scenes definition structure
        mDummyScene.name = sceneName;
        int sceneIndex = Collections.binarySearch(mScenesConfig.scenes, mDummyScene);
        if (sceneIndex < 0) {
            throw new IllegalStateException("Scene " + sceneName + " that was requested could not be found.");
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
        return sceneCreator;
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

        for (int i = 0; i < mInternalSystems.size(); ++i) {
            mInternalSystems.get(i).postUpdate();
        }
    }

    /**
     * Helper recursive method to call {@link Component#update()} on all
     * {@link Component Components}.
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
     * {@link Component Components}.
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
     * {@link Component Components}.
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

    public ExternalMessenger getExternalMessenger() {
        return mExternalMessengerInternal.getExternalMessenger();
    }

    private class ApplicationImpl extends Application {

        private Debug mDebug;

        public ApplicationImpl() {
            DebugSettingsModel debugSettingsModel = parseDebugSettingsFile();
            boolean debug = debugSettingsModel != null;
            boolean drawColliders = debug && (debugSettingsModel.drawColliders == null || debugSettingsModel.drawColliders);
            mDebug = new Debug(debug, drawColliders) {
                @Override
                public void log(String message) {
                    mPlatform.log(message);
                }
            };
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
        public MeshManager getMeshManager() {
            return mMeshManager;
        }

        @Override
        public Messenger getMessenger() {
            return mMessenger;
        }

        @Override
        public CollisionsSystem getCollisionsSystem() {
            return mCollisionsSystem;
        }

        @Override
        public Time getTime() {
            return mTime;
        }

        @Override
        public Debug getDebug() {
            return mDebug;
        }

        @Override
        public Display getDisplay() {
            return mDisplay;
        }

        @Override
        public void changeScene(String newSceneName) {
            mMeshManager = new MeshManager(mApplication, mPlatform);

            SceneCreator newSceneCreator = getScene(newSceneName);
            mCurrentScene = newSceneCreator.scene;
            mRenderer.setCamera(newSceneCreator.camera);

            mCurrentSceneName = newSceneName;
        }

        @Override
        public String getCurrentSceneName() {
            return mCurrentSceneName;
        }
    }
}
