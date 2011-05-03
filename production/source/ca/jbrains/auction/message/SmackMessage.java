package ca.jbrains.auction.message;

import org.jivesoftware.smack.packet.Message;

public class SmackMessage {
    public static Message withBody(String body) {
        final Message message = new Message();
        message.setBody(body);
        return message;
    }
}
