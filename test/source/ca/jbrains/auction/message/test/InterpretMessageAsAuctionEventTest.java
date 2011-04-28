package ca.jbrains.auction.message.test;

import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

import ca.jbrains.auction.message.*;

@RunWith(JMock.class)
public class InterpretMessageAsAuctionEventTest {
    public interface AuctionEventListener {
        void biddingState(BiddingState auctionState);
    }

    private final Mockery mockery = new Mockery();
    private final AuctionEventListener auctionEventListener = mockery
            .mock(AuctionEventListener.class);

    @Test
    public void reportPriceMessage() throws Exception {
        mockery.checking(new Expectations() {
            {
                oneOf(auctionEventListener).biddingState(
                        new BiddingState(12, 34, "jbrains"));
            }
        });

        interpret("SOLVersion: 1.1; Event: PRICE; CurrentPrice: 12; Increment: 34; Bidder: jbrains");
    }

    private void interpret(String messageBody) {
        auctionEventListener.biddingState(parse(messageBody));
    }

    private BiddingState parse(String messageBody) {
        return (BiddingState) Messages.parseBody(messageBody);
    }
}
