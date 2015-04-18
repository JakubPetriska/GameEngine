package com.monolith.engine.messaging;

import com.monolith.api.Messenger;
import com.monolith.engine.ISystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Internal implementation of {@link com.monolith.api.Messenger}.
 */
public class MessengerInternal implements Messenger, ISystem {

    private final InputMessengerInternal mInputMessengerInternal;

    /**
     * This List caches empty {@link java.util.Set} objects to prevent creating new ones
     * over and over.
     */
    private List<List<Object>> mListCache = new ArrayList<>();

    /**
     * Map of messages by their types that holds messages valid for current frame.
     */
    private HashMap<String, List<Object>> mCurrentMessagesMap = new HashMap<>();

    /**
     * Map of messages by their types that receives new incoming messages.
     */
    private HashMap<String, List<Object>> mCachingMessagesMap = new HashMap<>();

    public MessengerInternal(InputMessengerInternal inputMessengerInternal) {
        this.mInputMessengerInternal = inputMessengerInternal;
        mInputMessengerInternal.setMessageReceiver(new InputMessengerInternal.MessageReceiver() {
            @Override
            public void onNewMessage(Object message) {
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
        });
    }

    @Override
    public void sendMessage(Object message) {
        mInputMessengerInternal.sendMessage(message);
    }

    @Override
    public <T> void getMessages(Class<T> messagesClass, List<T> resultList) {
        String messageKey = getMessageKey(messagesClass);
        if (mCurrentMessagesMap.containsKey(messageKey)) {
            resultList.addAll((List<T>) mCurrentMessagesMap.get(messageKey));
        }
    }

    @Override
    public <T> T getLastMessage(Class<T> messagesClass) {
        String messageKey = getMessageKey(messagesClass);
        if (mCurrentMessagesMap.containsKey(messageKey)) {
            List<T> messages = (List<T>) mCurrentMessagesMap.get(messageKey);
            return messages.get(messages.size() - 1);
        } else {
            return null;
        }
    }

    /**
     * This must be called by {@link com.monolith.engine.Engine}.
     */
    @Override
    public void update() {
        // Clear the map of current messages from previous frame and cache the empty sets
        HashMap<String, List<Object>> mapToClear = mCurrentMessagesMap;
        for (String key : mapToClear.keySet()) {
            // TODO throws ConcurrentModificationException in performance test
            List<Object> messages = mapToClear.remove(key);
            messages.clear();
            mListCache.add(messages);
        }

        mCurrentMessagesMap = mCachingMessagesMap;
        mCachingMessagesMap = mapToClear;
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
