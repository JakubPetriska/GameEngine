package com.monolith.engine.messaging;

import com.monolith.api.external.InputMessenger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * This class allows sending messages into the engine and receive messages
 * from the engine.
 */
public class InputMessengerInternal {

    /**
     * Map that stores message receivers.
     * Keys are full class names.
     */
    private HashMap<String, Set<InputMessenger.MessageReceiver>> mReceiversMap = new HashMap<>();

    public InputMessengerInternal() {
    }

    private MessageReceiver mMessageReceiver;

    /**
     * This us used by MessengerInternal to listen for messages from the outside.
     * @param messageReceiver
     */
    public void setMessageReceiver(MessageReceiver messageReceiver) {
        this.mMessageReceiver = messageReceiver;
    }

    public interface MessageReceiver {

        /**
         * Called when new message arrived.
         * @param message Message object.
         */
        void onNewMessage(Object message);
    }

    /**
     * This is called from the outside (by MessengerInternal) when
     * new message from the inside of the engine comes.
     * @param message Message object.
     */
    public void sendMessage(Object message) {
        String messageKey = getMessageKey(message);
        if(mReceiversMap.containsKey(messageKey)) {
            for(InputMessenger.MessageReceiver receiver : mReceiversMap.get(messageKey)) {
                receiver.onNewMessage(message);
            }
        }
    }

    private String getMessageKey(Object message) {
        return getMessageKey(message.getClass());
    }

    private String getMessageKey(Class messageClass) {
        return messageClass.getName();
    }

    private InputMessenger mInputMessenger;

    public InputMessenger getInputMessenger() {
        if(mInputMessenger == null) {
            mInputMessenger = new InputMessengerImpl();
        }
        return mInputMessenger;
    }

    /**
     * InputMessenger is an object passed outside of the engine to allow
     * users to send messages in it.
     *
     * This class is implemented here to forbid casting InputMessenger
     * to InputMessengerInternal by engine users.
     */
    private class InputMessengerImpl implements InputMessenger {
        @Override
        public void sendMessage(Object message) {
            if(mMessageReceiver != null) {
                mMessageReceiver.onNewMessage(message);
            }
        }

        @Override
        public <T> void registerMessageReceiver(MessageReceiver<T> messageReceiver, Class<T> messageClass) {
            String messageKey = getMessageKey(messageClass);
            Set<InputMessenger.MessageReceiver> messageClassReceivers;
            if(!mReceiversMap.containsKey(messageKey)) {
                messageClassReceivers = new HashSet<>();
                mReceiversMap.put(messageKey, messageClassReceivers);
            } else {
                messageClassReceivers = mReceiversMap.get(messageKey);
            }
            messageClassReceivers.add(messageReceiver);
        }

        @Override
        public <T> void unregisterMessageReceiver(MessageReceiver<T> messageReceiver, Class<T> messageClass) {
            String messageKey = getMessageKey(messageClass);
            if(mReceiversMap.containsKey(messageKey)) {
                mReceiversMap.get(messageKey).remove(messageReceiver);
            }
        }
    }
}
