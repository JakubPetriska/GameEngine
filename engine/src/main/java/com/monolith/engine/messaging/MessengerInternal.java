package com.monolith.engine.messaging;

import com.monolith.api.Messenger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Internal implementation of {@link com.monolith.api.Messenger}.
 */
public class MessengerInternal implements Messenger {

    private final InputMessengerInternal mInputMessengerInternal;

    /**
     * This List caches empty {@link java.util.Set} objects to prevent creating new ones
     * over and over.
     */
    private List<Set<Object>> mSetCache = new ArrayList<>();

    /**
     * Map of messages by their types that holds messages valid for current frame.
     */
    private HashMap<String, Set<Object>> mCurrentMessagesMap = new HashMap<>();

    /**
     * Map of messages by their types that receives new incoming messages.
     */
    private HashMap<String, Set<Object>> mCachingMessagesMap = new HashMap<>();

    public MessengerInternal(InputMessengerInternal inputMessengerInternal) {
        this.mInputMessengerInternal = inputMessengerInternal;
        mInputMessengerInternal.setMessageReceiver(new InputMessengerInternal.MessageReceiver() {
            @Override
            public void onNewMessage(Object message) {
                String messageKey = getMessageKey(message);
                Set<Object> messageSet;
                if (mCachingMessagesMap.containsKey(messageKey)) {
                    messageSet = mCachingMessagesMap.get(messageKey);
                } else {
                    if (mSetCache.size() > 0) {
                        messageSet = mSetCache.remove(0);
                    } else {
                        messageSet = new HashSet<>();
                    }
                    mCachingMessagesMap.put(messageKey, messageSet);
                }
                messageSet.add(message);
            }
        });
    }

    @Override
    public void sendMessage(Object message) {
        mInputMessengerInternal.sendMessage(message);
    }

    @Override
    public <T> void getMessages(List<T> resultList, Class<T> messagesClass) {
        String messageKey = getMessageKey(messagesClass);
        if (mCurrentMessagesMap.containsKey(messageKey)) {
            resultList.addAll((Set<T>) mCurrentMessagesMap.get(messageKey));
        }
    }

    // TODO maybe this is not the best idea. (different api of every engine component, different lifecycle), Touch input has the same problem.
    // TODO Probably creating concept of System would fix this?

    /**
     * This must be called by {@link com.monolith.engine.Engine}.
     */
    public void update() {
        // Clear the map of current messages from previous frame and cache the empty sets
        HashMap<String, Set<Object>> mapToClear = mCurrentMessagesMap;
        for (String key : mapToClear.keySet()) {
            Set<Object> set = mapToClear.remove(key);
            set.clear();
            mSetCache.add(set);
        }

        mCurrentMessagesMap = mCachingMessagesMap;
        mCachingMessagesMap = mapToClear;
    }

    private String getMessageKey(Object message) {
        return getMessageKey(message.getClass());
    }

    private String getMessageKey(Class messageClass) {
        return messageClass.getName();
    }
}
