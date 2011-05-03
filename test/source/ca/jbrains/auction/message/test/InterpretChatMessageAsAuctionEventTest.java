package ca.jbrains.auction.message.test;

import org.jmock.*;
import org.junit.Test;

import ca.jbrains.auction.message.*;

public class InterpretChatMessageAsAuctionEventTest {
    private final Mockery mockery = new Mockery();
    private final AuctionEventListener auctionEventListener = mockery
            .mock(AuctionEventListener.class);
    private final AuctionEventSourceMessageListener auctionEventSourceMessageListener = eventSourceWithListener(auctionEventListener);

    @Test
    public void joinAuctionMessage() throws Exception {
        mockery.checking(new Expectations() {
            {
                oneOf(auctionEventListener).handleJoinAuctionEvent();
            }
        });

        auctionEventSourceMessageListener.processMessage(null,
                AuctionMessages.joinAuction());
    }

    @Test
    public void reportPriceMessage() throws Exception {
        final BiddingState biddingState = new BiddingState(1000, 25, "jbrains");

        mockery.checking(new Expectations() {
            {
                oneOf(auctionEventListener).handleNewBiddingState(biddingState);
            }
        });

        auctionEventSourceMessageListener.processMessage(null,
                AuctionMessages.reportPrice(1000, 25, "jbrains"));
    }

    @Test
    public void emptyMessage() throws Exception {
        final String body = "irrelevant message body";

        mockery.checking(new Expectations() {
            {
                oneOf(auctionEventListener).handleGenericEvent(
                        new MiscellaneousEvent(body));
            }
        });

        auctionEventSourceMessageListener.processMessage(null,
                SmackMessage.withBody(body));
    }

    @Test
    public void auctionClosedMessage() throws Exception {
        mockery.checking(new Expectations() {
            {
                oneOf(auctionEventListener).handleAuctionClosed();
            }
        });

        auctionEventSourceMessageListener.processMessage(null,
                AuctionMessages.auctionClosed());
    }

    private static AuctionEventSourceMessageListener eventSourceWithListener(
            AuctionEventListener auctionEventListener) {

        AuctionEventSourceMessageListener auctionEventSourceMessageListener = new AuctionEventSourceMessageListener();
        auctionEventSourceMessageListener.addListener(auctionEventListener);
        return auctionEventSourceMessageListener;
    }
}
