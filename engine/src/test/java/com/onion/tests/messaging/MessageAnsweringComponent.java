package com.onion.tests.messaging;

import com.onion.api.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jakub Petriska on 23. 2. 2015.
 */
public class MessageAnsweringComponent extends Component {

    @Override
    public void update() {
        List<MessagingTest.InputMessage> inputMessages = new ArrayList<>();
        getApplication().getMessenger().getMessages(inputMessages, MessagingTest.InputMessage.class);

        if(inputMessages.size() > 0) {
            getApplication().getMessenger().sendMessage(new MessagingTest.OutputMessage());
        }
    }
}
