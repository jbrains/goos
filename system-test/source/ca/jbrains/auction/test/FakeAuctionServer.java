package ca.jbrains.auction.test;

import java.util.concurrent.*;

import org.hamcrest.Matcher;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;

import ca.jbrains.auction.message.Messages;
import static ca.jbrains.auction.test.Main.ITEM_ID_AS_LOGIN;
import static org.hamcrest.Matchers.*;

import static org.junit.Assert.assertThat;

public class FakeAuctionServer {
    public static final class SingleMessageListener implements MessageListener {
        private final ArrayBlockingQueue<Message> messages = new ArrayBlockingQueue<Message>(
                1);

        @Override
        public void processMessage(Chat chat, Message message) {
            messages.add(message);
        }

        public void receivesAMessage() throws InterruptedException {
            receivesAMessage(is(anything()));
        }

        public void receivesAMessage(Matcher<? super String> messagePattern)
                throws InterruptedException {
            final Message message = messages.poll(5, TimeUnit.SECONDS);
            assertThat("Message", message, is(notNullValue()));
            assertThat(message.getBody(), messagePattern);
        }
    }

    public static final String XMPP_HOSTNAME = "localhost";
    private static final String AUCTION_RESOURCE = "Auction";
    private static final String AUCTION_PASSWORD = "auction";

    private final String itemId;
    private final XMPPConnection connection;
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
        messageListener.receivesAMessage(Messages.joinAuctionBodyMatcher());
    }

    public void announceClosed() throws XMPPException {
        currentChat.sendMessage(auctionClosedMessage());
    }

    private static Message auctionClosedMessage() {
        return new Message();
    }

    public void stop() {
        connection.disconnect();
    }

    public String getItemId() {
        return itemId;
    }

    public void reportPrice(int price, int increment, String bidder)
            throws XMPPException {

        currentChat.sendMessage(Messages.reportPriceMessageBody(price,
                increment, bidder));
    }

    public void hasReceivedBid(int bid, String bidder)
            throws InterruptedException {

        assertThat(currentChat.getParticipant(), equalTo(bidder));
        messageListener.receivesAMessage(matchingReceivedBidPattern());
        try {
            // ASSUME Bid increment is 98
            currentChat.sendMessage(Messages.reportPriceMessageBody(bid, 98,
                    bidder));
        } catch (XMPPException reported) {
            reported.printStackTrace();
        }
    }

    private static Matcher<String> matchingReceivedBidPattern() {
        // ASSUME Price is 1098
        return equalTo("SOLVersion 1.1; Command: Bid; Price: 1098");
    }
}
