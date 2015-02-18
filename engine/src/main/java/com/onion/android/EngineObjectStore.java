package com.onion.android;

import com.onion.engine.Engine;

import java.util.HashMap;

/**
 * This class serves as storage for engine objects during screen orientation change.
 */
public class EngineObjectStore {

    private static long sLastNanoTime = -1;
    private static String sLastKey;
    private static HashMap<String, Engine> sEnginesMap = new HashMap<>();

    public static String store(Engine engine) {
        String key = generateKey();
        sEnginesMap.put(key, engine);
        return key;
    }

    /**
     * Generates key from current time in nanoseconds.
     * In case of conflict with the previous key method appends letters
     * to the end of the key.
     * @return Unique key
     */
    private static String generateKey() {
        long nanoTime = System.nanoTime();
        String key = Long.toString(nanoTime);
        if(nanoTime == sLastNanoTime) {
            do {
                key += "A";
            } while(key.length() <= sLastKey.length());
        }
        sLastNanoTime = nanoTime;
        sLastKey = key;
        return key;
    }

    public static Engine retrieve(String key) {
        return sEnginesMap.remove(key);
    }
}
