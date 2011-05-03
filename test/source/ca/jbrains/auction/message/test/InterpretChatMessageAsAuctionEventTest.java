package ca.jbrains.auction.message.test;

import org.jmock.*;
import org.junit.*;

import ca.jbrains.auction.message.*;

public class InterpretChatMessageAsAuctionEventTest {
    private Mockery mockery = new Mockery();

    @Test
    public void joinAuctionMessage() throws Exception {
        final AuctionEventListener auctionEventListener = mockery
                .mock(AuctionEventListener.class);

        mockery.checking(new Expectations() {
            {
                oneOf(auctionEventListener).handleJoinAuctionEvent();
            }
        });

        AuctionEventSourceMessageListener auctionEventSourceMessageListener = new AuctionEventSourceMessageListener();
        auctionEventSourceMessageListener.addListener(auctionEventListener);

        auctionEventSourceMessageListener.processMessage(null,
                Messages.joinAuction());
    }

    @Ignore("TODO")
    @Test
    public void reportPriceMessage() throws Exception {
    }

    @Ignore("TODO")
    @Test
    public void emptyMessage() throws Exception {
    }

    @Ignore("TODO")
    @Test
    public void auctionClosedMessage() throws Exception {
    }
}
