package ca.jbrains.auction.smack;

import org.jivesoftware.smack.packet.Message;

public class SmackMessageObjectMother {

    public static Message messageWithText(String text) {
        final Message message = new Message();
        message.setBody(text);
        return message;
    }

}
