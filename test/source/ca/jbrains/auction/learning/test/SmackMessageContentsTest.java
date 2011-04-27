package ca.jbrains.auction.learning.test;

import org.jivesoftware.smack.packet.Message;
import org.junit.Test;

import ca.jbrains.auction.test.ClassifySniperBidAuctionMessageTest;

import static org.junit.Assert.*;

public class SmackMessageContentsTest {
    @Test
    public void messageContents() throws Exception {
        assertEquals(
                "SOLVersion 1.1; Command: Bid; Price: 1098",
                ClassifySniperBidAuctionMessageTest.messageWithText(
                        "SOLVersion 1.1; Command: Bid; Price: 1098").getBody());
        assertEquals("not the body", new Message("not the body").getTo());
        assertNull(new Message("not the body").getBody());
    }

}
