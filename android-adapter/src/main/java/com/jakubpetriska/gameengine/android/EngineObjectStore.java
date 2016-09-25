package com.jakubpetriska.gameengine.android;

import com.jakubpetriska.gameengine.engine.Engine;
import com.jakubpetriska.gameengine.utilities.TimeBasedUniqueKeyGenerator;

import java.util.HashMap;

/**
 * This class serves as storage for engine objects during screen orientation change.
 */
public class EngineObjectStore {

    private static HashMap<String, Engine> sEnginesMap = new HashMap<>();

    public static String store(Engine engine) {
        String key = TimeBasedUniqueKeyGenerator.generateKey();
        sEnginesMap.put(key, engine);
        return key;
    }

    public static Engine retrieve(String key) {
        return sEnginesMap.remove(key);
    }
}
