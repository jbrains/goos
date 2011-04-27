package ca.jbrains.auction;

import static ca.jbrains.auction.Main.ITEM_ID_AS_LOGIN;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.util.concurrent.*;

import org.hamcrest.Matcher;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;

public class FakeAuctionServer {
    public static final class SingleMessageListener implements MessageListener {
        private final ArrayBlockingQueue<Message> messages = new ArrayBlockingQueue<Message>(
                1);

        @Override
        public void processMessage(Chat chat, Message message) {
            messages.add(message);
        }

        public void receivesAMessage() throws InterruptedException {
            assertThat("Message", messages.poll(5, SECONDS), is(notNullValue()));
        }

        public void receivesAMessage(Matcher<String> messagePattern)
                throws InterruptedException {
            final Message message = messages.poll(5, TimeUnit.SECONDS);
            assertThat("Message", message, is(notNullValue()));
            assertThat(message.getBody(), messagePattern);
        }
    }

    public static final String AUCTION_RESOURCE = "Auction";
    public static final String XMPP_HOSTNAME = "localhost";
    private static final String AUCTION_PASSWORD = "auction";

    private final String itemId;
    private XMPPConnection connection;
    private Chat currentChat;
    private final SingleMessageListener messageListener = new SingleMessageListener();

    public FakeAuctionServer(String itemId) {
        this.itemId = itemId;
        this.connection = new XMPPConnection(XMPP_HOSTNAME);
    }

    public void startSellingItem() throws XMPPException {
        connection.connect();
        connection.login(String.format(ITEM_ID_AS_LOGIN, itemId),
                AUCTION_PASSWORD, AUCTION_RESOURCE);
        connection.getChatManager().addChatListener(new ChatManagerListener() {
            @Override
            public void chatCreated(Chat chat, boolean createdLocally) {
                FakeAuctionServer.this.currentChat = chat;
                chat.addMessageListener(messageListener);
            }
        });
    }

    public void hasReceivedJoinRequestFromSniper() throws InterruptedException {
        messageListener.receivesAMessage();
    }

    public void announceClosed() throws XMPPException {
        currentChat.sendMessage(new Message());
    }

    public void stop() {
        connection.disconnect();
    }

    public String getItemId() {
        return itemId;
    }

    public void reportPrice(int price, int increment, String bidder)
            throws XMPPException {

        currentChat
                .sendMessage(String
                        .format("SOLVersion: 1.1; Event: PRICE; CurrentPrice: %d; Increment: 98; Bidder: other bidder",
                                price, increment, bidder));
    }

    @SuppressWarnings("unused")
    public void hasReceivedBid(int bid, String bidder)
            throws InterruptedException {
        assertThat(currentChat.getParticipant(), equalTo(bidder));
        messageListener
                .receivesAMessage(equalTo("SOLVersion 1.1; Command: Bid; Price: 1098"));
    }
}
