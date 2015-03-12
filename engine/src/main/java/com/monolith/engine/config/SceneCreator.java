package com.monolith.engine.config;

import com.monolith.api.Component;
import com.monolith.api.GameObject;
import com.monolith.engine.Scene;
import com.monolith.api.components.Transform;
import com.monolith.api.components.Mesh;
import com.monolith.engine.ComponentsConstants;
import com.monolith.api.Application;
import com.monolith.engine.config.model.initial_scene_state.ISComponent;
import com.monolith.engine.config.model.initial_scene_state.ISGameObject;
import com.monolith.engine.config.model.initial_scene_state.ISScene;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class SceneCreator {

    private Application mApplication;

    public SceneCreator(Application application) {
        this.mApplication = application;
    }

    public Scene create(ISScene scene) {
        Scene runtimeSceneTree = new Scene();
        for (ISGameObject gameObject : scene.gameObjects) {
            runtimeSceneTree.gameObjects.add(convertGameObject(null, gameObject));
        }
        return runtimeSceneTree;
    }

    private GameObject convertGameObject(GameObject parent, ISGameObject initialGameObject) {
        GameObject gameObject = new GameObject(mApplication, parent);
        convertTransform(initialGameObject.transform, gameObject.transform);
        if(initialGameObject.components != null) {
            for (ISComponent component : initialGameObject.components) {
                convertComponent(gameObject, component);
            }
        }
        if(initialGameObject.children != null) {
            for (ISGameObject childGameObject : initialGameObject.children) {
                convertGameObject(gameObject, childGameObject);
            }
        }
        return gameObject;
    }

    private void convertTransform(ISGameObject.Transform what, Transform into) {
        into.moveBy(what.position.x, what.position.y, what.position.z);
    }

    private Component convertComponent(GameObject owner, ISComponent initialComponent) {
        Component component;
        if (ComponentsConstants.COMPONENT_TYPE_MESH.equals(initialComponent.type)) {
            component = new Mesh();
        } else {
			try {
				Class<Component> scriptClass = (Class<Component>) Class.forName(initialComponent.type);
				component = scriptClass.getConstructor().newInstance();
			} catch (ClassCastException e) {
				throw new IllegalStateException("Script must inherit from Component class.");
			} catch (ClassNotFoundException e) {
				throw new IllegalStateException("Unknown component type.");
			} catch (NoSuchMethodException e) {
				throw new IllegalStateException("Script must have constructor with no parameters and it must be public.");
			} catch (IllegalAccessException e) {
				throw new IllegalStateException("Script's constructor must be public.");
			} catch (InstantiationException e) {
				throw new IllegalStateException("Script could not be instantiated.");
			} catch (InvocationTargetException e) {
				throw new IllegalStateException("Unknown error during script retrieval.");
			}
		}
        Class componentClass = component.getClass();
        // Set all parameters on new component
        // TODO this thing currently supports only strings - add other types
        if(initialComponent.params != null) {
            for(String paramName : initialComponent.params.keySet()) {
                Field field;
                try {
                    field = componentClass.getField(paramName);
                } catch (NoSuchFieldException e) {
                    throw new IllegalStateException("Unknown component parameter.");
                }
                try {
                    field.set(component, initialComponent.params.get(paramName));
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException("Parameter " + paramName + " is not visible.");
                }
            }
        }

        owner.addComponent(component);
        return component;
    }
}
