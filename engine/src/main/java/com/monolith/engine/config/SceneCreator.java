package com.monolith.engine.config;

import com.monolith.api.Application;
import com.monolith.api.Component;
import com.monolith.api.GameObject;
import com.monolith.api.components.Model;
import com.monolith.api.components.Transform;
import com.monolith.engine.ComponentsConstants;
import com.monolith.engine.Scene;
import com.monolith.engine.config.model.initial_scene_state.ISComponent;
import com.monolith.engine.config.model.initial_scene_state.ISGameObject;
import com.monolith.engine.config.model.initial_scene_state.ISScene;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * Constructs the initial state of scene from it's definition. This definition
 * is given in scene configuration file.
 */
public class SceneCreator {

    private Application mApplication;

    public SceneCreator(Application application) {
        this.mApplication = application;
    }

    /**
     * Constructs the scene.
     *
     * @param scene Contains the initial scene definition.
     * @return Constructed {@link com.monolith.engine.Scene}.
     */
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
        if (initialGameObject.components != null) {
            for (ISComponent component : initialGameObject.components) {
                convertComponent(gameObject, component);
            }
        }
        if (initialGameObject.children != null) {
            for (ISGameObject childGameObject : initialGameObject.children) {
                convertGameObject(gameObject, childGameObject);
            }
        }
        return gameObject;
    }

    private void convertTransform(ISGameObject.Transform what, Transform into) {
        if (what == null) {
            return;
        }
        if (what.position != null) {
            into.translate(what.position.x, what.position.y, what.position.z);
        }
        if (what.rotation != null) {
            into.rotate(what.rotation.x, what.rotation.y, what.rotation.z);
        }
    }

    private Component convertComponent(GameObject owner, ISComponent initialComponent) {
        Component component;
        if (ComponentsConstants.COMPONENT_TYPE_MODEL.equals(initialComponent.type)) {
            component = new Model();
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
        if (initialComponent.params != null) {
            for (String paramName : initialComponent.params.keySet()) {
                Field field;
                try {
                    field = componentClass.getField(paramName);
                } catch (NoSuchFieldException e) {
                    throw new IllegalStateException("Unknown component parameter.");
                }
                String value = initialComponent.params.get(paramName);
                Object parsedValue;
                Class<?> fieldClass = field.getType();
                try {
                    if (fieldClass.equals(String.class)) { // String
                        parsedValue = value;
                    } else if (fieldClass.equals(Boolean.class)
                            || fieldClass.equals(boolean.class)) { // Boolean
                        parsedValue = Boolean.valueOf(value);
                    } else if (fieldClass.equals(Integer.class)
                            || fieldClass.equals(int.class)) { // Integer
                        parsedValue = Integer.valueOf(value);
                    } else if (fieldClass.equals(Long.class)
                            || fieldClass.equals(long.class)) { // Long
                        parsedValue = Long.valueOf(value);
                    } else if (fieldClass.equals(Float.class)
                            || fieldClass.equals(float.class)) { // Float
                        parsedValue = Float.valueOf(value);
                    } else if (fieldClass.equals(Double.class)
                            || fieldClass.equals(double.class)) { // Double
                        parsedValue = Double.valueOf(value);
                    } else {
                        throw new IllegalStateException(
                                "Component parameter with type " + fieldClass.getSimpleName()
                                        + " cannot be set from scene definition file.");
                    }
                } catch (NumberFormatException e) {
                    throw new IllegalStateException(
                            "Value " + value + " could not be parsed into " + fieldClass.getSimpleName()
                                    + ". Make sure you use decimal dot instead of comma for decimal numbers.");
                }
                try {
                    field.set(component, parsedValue);
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException("Parameter " + paramName + " is not visible.");
                }
            }
        }

        owner.addComponent(component);
        return component;
    }
}