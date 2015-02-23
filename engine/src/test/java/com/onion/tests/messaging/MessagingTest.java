package com.onion.tests.messaging;

import com.onion.engine.messaging.InputMessenger;
import com.onion.tests.support.BaseEngineTest;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by Jakub Petriska on 23. 2. 2015.
 */
public class MessagingTest extends BaseEngineTest implements InputMessenger.MessageReceiver<MessagingTest.OutputMessage> {

    private static final String FILES_FOLDER = "messaging_test";

    @Before
    public void prepareEngine() {
        setupEngine(FILES_FOLDER);
    }

    public static class InputMessage {

    }

    public static class OutputMessage {

    }

    private boolean mMessageReceived;

    /**
     * Tests if input and output messaging is working.
     * Component inside the engine receives the InputMessage
     * object during update and sends out the OutputMessage object.
     */
    @Test
    public void basicFunctionTest() {
        getEngine().onStart();

        getEngine().getInputMessenger().registerMessageReceiver(this, OutputMessage.class);

        mMessageReceived = false;
        getEngine().getInputMessenger().sendMessage(new InputMessage());
        getEngine().onUpdate();

        // Message should have been received by now
        assertTrue("Message was not received.", mMessageReceived);
        mMessageReceived = false;

        for(int i = 0; i < 5; ++i) {
            getEngine().onUpdate();
            assertFalse("Message was received, but it was not supposed to be", mMessageReceived);
        }

        getEngine().getInputMessenger().sendMessage(new InputMessage());
        getEngine().onUpdate();

        // Message should have been received by now
        assertTrue("Message was not received.", mMessageReceived);
        mMessageReceived = false;

        for(int i = 0; i < 5; ++i) {
            getEngine().onUpdate();
            assertFalse("Message was received, but it was not supposed to be", mMessageReceived);
        }

        getEngine().onFinish();
    }

    @Override
    public void onNewMessage(OutputMessage message) {
        assertTrue("Received message is null", message != null);

        mMessageReceived = true;
    }
}
