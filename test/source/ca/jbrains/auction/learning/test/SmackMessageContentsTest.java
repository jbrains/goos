package ca.jbrains.auction.learning.test;

import org.jivesoftware.smack.packet.Message;
import org.junit.Test;

import ca.jbrains.auction.smack.SmackMessageObjectMother;

import static org.junit.Assert.*;

public class SmackMessageContentsTest {
    @Test
    public void correctWayToCreateAMessage() throws Exception {
        assertEquals(
                "SOLVersion 1.1; Command: Bid; Price: 1098",
                SmackMessageObjectMother.messageWithText(
                        "SOLVersion 1.1; Command: Bid; Price: 1098").getBody());
    }
    
    @Test
    public void incorrectWaysToCreateAMessage() throws Exception {
        assertNull(new Message("not the body").getBody());
        
        // in fact...
        assertEquals("not the body", new Message("not the body").getTo());
    }
}
