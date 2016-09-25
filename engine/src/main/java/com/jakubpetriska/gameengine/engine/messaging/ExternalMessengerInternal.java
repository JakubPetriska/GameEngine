package com.jakubpetriska.gameengine.engine.messaging;

import com.jakubpetriska.gameengine.api.external.ExternalMessenger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Internal implementation of {@link ExternalMessenger}.
 */
public class ExternalMessengerInternal {

    /**
     * Map that stores message receivers.
     * Keys are full class names.
     */
    private HashMap<String, Set<ExternalMessenger.MessageReceiver>> mReceiversMap = new HashMap<>();

    public ExternalMessengerInternal() {
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
            for(ExternalMessenger.MessageReceiver receiver : mReceiversMap.get(messageKey)) {
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

    private ExternalMessenger mExternalMessenger;

    public ExternalMessenger getExternalMessenger() {
        if(mExternalMessenger == null) {
            mExternalMessenger = new ExternalMessengerImpl();
        }
        return mExternalMessenger;
    }

    /**
     * ExternalMessenger is an object passed outside of the engine to allow
     * users to send messages in it.
     *
     * This class is implemented here to forbid casting ExternalMessenger
     * to ExternalMessengerInternal by engine users.
     */
    private class ExternalMessengerImpl implements ExternalMessenger {
        @Override
        public void sendMessage(Object message) {
            if(mMessageReceiver != null) {
                mMessageReceiver.onNewMessage(message);
            }
        }

        @Override
        public <T> void registerMessageReceiver(Class<T> messageClass, MessageReceiver<T> messageReceiver) {
            String messageKey = getMessageKey(messageClass);
            Set<MessageReceiver> messageClassReceivers;
            if(!mReceiversMap.containsKey(messageKey)) {
                messageClassReceivers = new HashSet<>();
                mReceiversMap.put(messageKey, messageClassReceivers);
            } else {
                messageClassReceivers = mReceiversMap.get(messageKey);
            }
            messageClassReceivers.add(messageReceiver);
        }

        @Override
        public <T> void unregisterMessageReceiver(Class<T> messageClass, MessageReceiver<T> messageReceiver) {
            String messageKey = getMessageKey(messageClass);
            if(mReceiversMap.containsKey(messageKey)) {
                mReceiversMap.get(messageKey).remove(messageReceiver);
            }
        }
    }
}
