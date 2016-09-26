package com.jakubpetriska.gameengine.utilities;

/**
 * Generates {@link java.lang.String} keys based on current time.
 */
public class TimeBasedUniqueKeyGenerator {

    private static long sLastNanoTime = -1;
    private static String sLastKey;

    /**
     * Generates key from current time in nanoseconds.
     * In case of conflict with the previous key method appends letters
     * to the end of the key.
     * @return Unique key
     */
    public static String generateKey() {
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
}
