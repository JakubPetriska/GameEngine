package com.jakubpetriska.gameengine.tests;

import com.jakubpetriska.gameengine.utilities.TimeBasedUniqueKeyGenerator;

import org.junit.Test;
import static org.junit.Assert.*;


public class TimeBasedUniqueKeyGeneratorTest {

    @Test
    public void keysShouldBeUnique() {
        // Generate keys
        String[] keys = new String[10];
        for(int i = 0; i < keys.length; ++i) {
            keys[i] = TimeBasedUniqueKeyGenerator.generateKey();
        }

        // Test uniqueness
        for(int i = 0; i < keys.length; ++i) {
            for(int j = i + 1; j < keys.length; ++j) {
                assertNotEquals("Generator generated duplicate keys",
                        keys[i], keys[j]);
            }
        }
    }
}
