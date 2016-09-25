package com.jakubpetriska.gameengine.tests.messaging;

import com.jakubpetriska.gameengine.api.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jakub Petriska on 23. 2. 2015.
 */
public class MessageAnsweringComponent extends Component {

    @Override
    public void update() {
        List<MessagingTest.InputMessage> inputMessages = new ArrayList<>();
        getApplication().getMessenger().getMessages(MessagingTest.InputMessage.class, inputMessages);

        if(inputMessages.size() > 0) {
            getApplication().getMessenger().sendMessage(new MessagingTest.OutputMessage());
        }
    }
}
