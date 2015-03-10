package com.monolith.engine.messaging;

/**
 * Created by Jakub Petriska on 21. 2. 2015.
 */
public interface InputMessenger {

    void sendMessage(Object message);

    <T> void registerMessageReceiver(MessageReceiver<T> messageReceiver, Class<T> messageClass);

    <T> void unregisterMessageReceiver(InputMessenger.MessageReceiver<T> messageReceiver, Class<T> messageClass);

    interface MessageReceiver<T> {
        void onNewMessage(T message);
    }
}
