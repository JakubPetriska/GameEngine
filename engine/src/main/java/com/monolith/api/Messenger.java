package com.monolith.api;

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
     * @param resultList    List into which all available messages will be added.
     * @param messagesClass Class of the requested messages.
     * @param <T>           Type of requested messages.
     */
    <T> void getMessages(List<T> resultList, Class<T> messagesClass);

    /**
     * Get last message of the given type received during the previous frame.
     *
     * @param messagesClass
     * @param <T>
     * @return
     */
    <T> T getLastMessage(Class<T> messagesClass);
}
