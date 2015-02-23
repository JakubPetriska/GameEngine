package com.onion.engine.messaging;

/**
 * Created by Jakub Petriska on 21. 2. 2015.
 */
public interface InputMessenger {

    public void sendMessage(Object message);

    public <T> void registerMessageReceiver(MessageReceiver<T> messageReceiver, Class<T> messageClass);

    public <T> void unregisterMessageReceiver(InputMessenger.MessageReceiver<T> messageReceiver, Class<T> messageClass);

    public interface MessageReceiver<T> {
        public void onNewMessage(T message);
    }
}
