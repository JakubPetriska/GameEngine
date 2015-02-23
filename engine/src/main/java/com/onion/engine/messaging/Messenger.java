package com.onion.engine.messaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class allows sending messages out of the engine
 * and receiving messages coming into the engine.
 */
public class Messenger {

    private final InputMessengerInternal mInputMessengerInternal;

    /**
     * This List caches empty List objects to prevent creating new lists
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

    public Messenger(InputMessengerInternal inputMessengerInternal) {
        this.mInputMessengerInternal = inputMessengerInternal;
        mInputMessengerInternal.setMessageReceiver(new InputMessengerInternal.MessageReceiver() {
            @Override
            public void onNewMessage(Object message) {
                String messageKey = getMessageKey(message);
                Set<Object> messageSet;
                if(mCachingMessagesMap.containsKey(messageKey)) {
                    messageSet = mCachingMessagesMap.get(messageKey);
                } else {
                    if(mSetCache.size() > 0) {
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

    public void sendMessage(Object message) {
        mInputMessengerInternal.sendMessage(message);
    }

    /**
     * Return messages of the given class received during the previous frame.
     * @param resultList List into which all available messages will be added.
     * @param messagesClass Class of the requested messages.
     * @param <T> Type of requested messages.
     */
    public <T> void getMessages(List<T> resultList, Class<T> messagesClass) {
        String messageKey = getMessageKey(messagesClass);
        if(mCurrentMessagesMap.containsKey(messageKey)) {
            resultList.addAll((Set<T>) mCurrentMessagesMap.get(messageKey));
        }
    }

    // TODO maybe this is not the best idea. (different api of every engine component, different lifecycle), Touch input has the same problem.
    // TODO Probably creating concept of System would fix this?
    /**
     * This must be called by engine itself.
     */
    public void update() {
        // Clear the map of current messages from previous frame and cache the empty sets
        HashMap<String, Set<Object>> mapToClear = mCurrentMessagesMap;
        for(String key : mapToClear.keySet()) {
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
