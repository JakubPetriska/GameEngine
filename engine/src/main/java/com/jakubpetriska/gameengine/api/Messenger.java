package com.jakubpetriska.gameengine.api;

import java.util.List;

/**
 * Allows sending messages out of the engine
 * and receiving messages coming into the engine.
 */
public interface Messenger {

    /**
     * Send message out of the engine.
     *
     * @param message Message to send.
     */
    void sendMessage(Object message);

    /**
     * Get messages of the given type received during the previous frame.
     *
     * @param messagesClass Class of the requested messages.
     * @param resultList    List into which all available messages will be added.
     * @param <T>           Type of requested messages.
     */
    <T> void getMessages(Class<T> messagesClass, List<T> resultList);

    /**
     * Get last message of the given type received during the previous frame.
     *
     * @param messageClass Class of the requested message.
     * @param <T> Type of requested message.
     * @return The last received message of the desired type.
     */
    <T> T getLastMessage(Class<T> messageClass);
}
