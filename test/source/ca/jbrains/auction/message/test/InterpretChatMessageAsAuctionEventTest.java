package ca.jbrains.auction.message.test;

import org.junit.*;

import static org.junit.Assert.*;

public class InterpretChatMessageAsAuctionEventTest {
    @Test
    public void joinAuctionMessage() throws Exception {
        fail("Aha! We use empty message to mean both 'I joined an auction' and 'auction closed'. This can't last.");
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
