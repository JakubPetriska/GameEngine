package com.jakubpetriska.gameengine.engine.messaging;

import com.jakubpetriska.gameengine.engine.Engine;
import com.jakubpetriska.gameengine.api.Messenger;
import com.jakubpetriska.gameengine.engine.ISystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Internal implementation of {@link Messenger}.
 */
public class MessengerInternal implements Messenger, ISystem {

    private final ExternalMessengerInternal mExternalMessengerInternal;

    /**
     * This List caches empty {@link java.util.Set} objects to prevent creating new ones
     * over and over.
     */
    private List<List<Object>> mListCache = new ArrayList<>();

    private final Integer mCachingMapSynchronizationToken = 0;

    /**
     * Map of messages by their types that holds messages valid for current frame.
     */
    private HashMap<String, List<Object>> mCurrentMessagesMap = new HashMap<>();

    /**
     * Map of messages by their types that receives new incoming messages.
     */
    private HashMap<String, List<Object>> mCachingMessagesMap = new HashMap<>();

    public MessengerInternal(ExternalMessengerInternal externalMessengerInternal) {
        this.mExternalMessengerInternal = externalMessengerInternal;
        mExternalMessengerInternal.setMessageReceiver(new ExternalMessengerInternal.MessageReceiver() {
            @Override
            public void onNewMessage(Object message) {
                synchronized (mCachingMapSynchronizationToken) {
                    String messageKey = getMessageKey(message);
                    List<Object> messageList;
                    if (mCachingMessagesMap.containsKey(messageKey)) {
                        messageList = mCachingMessagesMap.get(messageKey);
                    } else {
                        if (mListCache.size() > 0) {
                            messageList = mListCache.remove(0);
                        } else {
                            messageList = new ArrayList<>();
                        }
                        mCachingMessagesMap.put(messageKey, messageList);
                    }
                    messageList.add(message);
                }
            }
        });
    }

    @Override
    public void sendMessage(Object message) {
        mExternalMessengerInternal.sendMessage(message);
    }

    @Override
    public <T> void getMessages(Class<T> messagesClass, List<T> resultList) {
        String messageKey = getMessageKey(messagesClass);
        if (mCurrentMessagesMap.containsKey(messageKey)) {
            resultList.addAll((List<T>) mCurrentMessagesMap.get(messageKey));
        }
    }

    @Override
    public <T> T getLastMessage(Class<T> messageClass) {
        String messageKey = getMessageKey(messageClass);
        if (mCurrentMessagesMap.containsKey(messageKey)) {
            List<T> messages = (List<T>) mCurrentMessagesMap.get(messageKey);
            return messages.get(messages.size() - 1);
        } else {
            return null;
        }
    }

    /**
     * This must be called by {@link Engine}.
     */
    @Override
    public void update() {
        synchronized (mCachingMapSynchronizationToken) {
            // Clear the map of current messages from previous frame and cache the empty sets
            HashMap<String, List<Object>> mapToClear = mCurrentMessagesMap;
            mCurrentMessagesMap = mCachingMessagesMap;
            mCachingMessagesMap = mapToClear;

            for (String key : mapToClear.keySet()) {
                List<Object> messages = mapToClear.get(key);
                messages.clear();
                mListCache.add(messages);
            }
            mapToClear.clear();
        }
    }

    @Override
    public void postUpdate() {

    }

    private String getMessageKey(Object message) {
        return getMessageKey(message.getClass());
    }

    private String getMessageKey(Class messageClass) {
        return messageClass.getName();
    }
}
