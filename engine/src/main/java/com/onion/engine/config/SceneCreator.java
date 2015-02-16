package com.onion.engine.config;

import com.onion.api.Component;
import com.onion.api.GameObject;
import com.onion.engine.Scene;
import com.onion.api.Transform;
import com.onion.api.components.Mesh;
import com.onion.engine.ComponentsConstants;
import com.onion.api.Core;
import com.onion.engine.config.model.initial_scene_state.ISComponent;
import com.onion.engine.config.model.initial_scene_state.ISGameObject;
import com.onion.engine.config.model.initial_scene_state.ISScene;

import org.lwjgl.util.vector.Vector3f;

import java.lang.reflect.InvocationTargetException;

public class SceneCreator {

    private Core mCore;

    public SceneCreator(Core core) {
        this.mCore = core;
    }

    public Scene create(ISScene scene) {
        Scene runtimeSceneTree = new Scene();
        for (ISGameObject gameObject : scene.gameObjects) {
            runtimeSceneTree.gameObjects.add(convertGameObject(null, gameObject));
        }
        return runtimeSceneTree;
    }

    private GameObject convertGameObject(GameObject parent, ISGameObject initialGameObject) {
        GameObject gameObject = new GameObject(mCore, parent);
        convertTransform(initialGameObject.transform, gameObject.transform);
        for (ISComponent component : initialGameObject.components) {
            convertComponent(gameObject, component);
        }
        for (ISGameObject childGameObject : initialGameObject.children) {
            gameObject.children.add(convertGameObject(gameObject, childGameObject));
        }
        return gameObject;
    }

    private void convertTransform(ISGameObject.Transform what, Transform into) {
        into.moveBy(what.position.x, what.position.y, what.position.z);
    }

    private Component convertComponent(GameObject owner, ISComponent initialComponent) {
        if (ComponentsConstants.COMPONENT_TYPE_MESH.equals(initialComponent.type)) {
            String meshName = initialComponent.getParamValue(ComponentsConstants.MESH_PARAM_MESH_NAME);
            return new Mesh(mCore, owner, meshName);
        } if(ComponentsConstants.COMPONENT_TYPE_SCRIPT.equals(initialComponent.type)) {
            Component script;
			try {
				Class<Component> scriptClass = (Class<Component>) Class.forName(initialComponent.getParamValue(ComponentsConstants.SCRIPT_PARAM_SCRIPT_NAME));
				script = scriptClass.getConstructor(Core.class, GameObject.class).newInstance(mCore, owner);
			} catch (ClassCastException e) {
				throw new IllegalStateException("Script must inherit from Component class.");
			} catch (ClassNotFoundException e) {
				throw new IllegalStateException("Script class could not be found.");
			} catch (NoSuchMethodException e) {
				throw new IllegalStateException("Script must have constructor with two parameters (Core, GameObject) and it must be public.");
			} catch (IllegalAccessException e) {
				throw new IllegalStateException("Script's constructor must be public.");
			} catch (InstantiationException e) {
				throw new IllegalStateException("Script could not be instantiated.");
			} catch (InvocationTargetException e) {
				throw new IllegalStateException("Unknown error during script retrieval.");
			}
			return script;
		} else {
            throw new IllegalStateException("Unknown component type.");
        }
    }
}
