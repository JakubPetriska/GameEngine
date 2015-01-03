package com.jeb.engine.config;

import com.jeb.api.Component;
import com.jeb.api.GameObject;
import com.jeb.api.Scene;
import com.jeb.api.Transform;
import com.jeb.api.components.Mesh;
import com.jeb.engine.ComponentsConstants;
import com.jeb.engine.config.model.initial_scene_state.ISComponent;
import com.jeb.engine.config.model.initial_scene_state.ISGameObject;
import com.jeb.engine.config.model.initial_scene_state.ISScene;

import org.lwjgl.util.vector.Vector3f;

public class SceneCreator {

    public static Scene create(ISScene scene) {
        Scene runtimeSceneTree = new Scene();
        for (ISGameObject gameObject : scene.gameObjects) {
            runtimeSceneTree.gameObjects.add(convertGameObject(null, gameObject));
        }
        return runtimeSceneTree;
    }

    private static GameObject convertGameObject(GameObject parent, ISGameObject initialGameObject) {
        GameObject gameObject = new GameObject(parent);
        convertTransform(initialGameObject.transform, gameObject.transform);
        for (ISComponent component : initialGameObject.components) {
            convertComponent(gameObject, component);
        }
        for (ISGameObject childGameObject : initialGameObject.children) {
            gameObject.children.add(convertGameObject(gameObject, childGameObject));
        }
        return gameObject;
    }

    private static void convertTransform(ISGameObject.Transform what, Transform into) {
        convertPosition(what.position, into.position);
    }

    private static void convertPosition(ISGameObject.Position what, Vector3f into) {
        into.x = what.x;
        into.y = what.y;
        into.z = what.z;
    }

    private static void convertComponent(GameObject owner, ISComponent initialComponent) {
        if (ComponentsConstants.COMPONENT_TYPE_MESH.equals(initialComponent.type)) {
            Mesh mesh = new Mesh(owner);
            mesh.setMesh(initialComponent.getParamValue(ComponentsConstants.MESH_PARAM_MESH));
        } /*if(ComponentsConstants.COMPONENT_TYPE_BEHAVIOUR.equals(initialComponent.type)) {
            Behaviour behaviour = null;
			try {
				Class<Behaviour> behaviourClass = (Class<Behaviour>) Class.forName(initialComponent.getParamValue(ComponentsConstants.BEHAVIOUR_PARAM_SCRIPT));
				behaviour = behaviourClass.getConstructor().newInstance();
			} catch (ClassCastException e) {
				throw new IllegalStateException("Behaviour script must inherit from Behaviour class.");
			} catch (ClassNotFoundException e) {
				throw new IllegalStateException("Behaviour script class could not be found.");
			} catch (NoSuchMethodException e) {
				throw new IllegalStateException("Behaviour script must have parameterless constructor.");
			} catch (IllegalAccessException e) {
				throw new IllegalStateException("Behaviour script's parameterless constructor must be public.");
			} catch (InstantiationException e) {
				throw new IllegalStateException("Behaviour script could not be instantiated.");
			} catch (InvocationTargetException e) {
				throw new IllegalStateException("Unknown error during script retrieval.");
			}
			component = componentCreator.createBehaviour(owner, behaviour);
		} */ else {
            throw new IllegalStateException("Unknown component type.");
        }
    }
}
