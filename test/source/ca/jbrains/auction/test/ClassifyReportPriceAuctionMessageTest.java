package ca.jbrains.auction.test;

import org.jivesoftware.smack.packet.Message;
import org.junit.Test;

import ca.jbrains.auction.message.*;
import ca.jbrains.auction.smack.SmackMessageObjectMother;

import static org.junit.Assert.*;

public class ClassifyReportPriceAuctionMessageTest {
    @Test
    public void happyPath() throws Exception {
        final Message properReportPriceMessage = SmackMessageObjectMother
                .messageWithText("SOLVersion: 1.1; Event: PRICE; CurrentPrice: 1000; Increment: 98; Bidder: other bidder");
        assertTrue(Main.isReportPriceMessage(properReportPriceMessage));
        assertEquals(new BiddingState(1000, 98, "other bidder"),
                Messages.parse(properReportPriceMessage));
    }

    @Test
    public void notAnSolMessage() throws Exception {
        final Message message = SmackMessageObjectMother
                .messageWithText("jbrains 3.0, bitchez; Event: PRICE"
                        + irrelevantDetails());

        assertFalse(Main.isReportPriceMessage(message));

        assertEquals(new MiscellaneousEvent(message.getBody()),
                Messages.parse(message));
    }

    @Test
    public void notAPriceEvent() throws Exception {
        final Message message = SmackMessageObjectMother
                .messageWithText(messageTextForEventNamed("NOT PRICE"));
        assertFalse(Main.isReportPriceMessage(message));
        assertEquals(new MiscellaneousEvent(message.getBody()),
                Messages.parse(message));
    }

    @Test
    public void noBody() throws Exception {
        final Message message = new Message();
        assertFalse(Main.isReportPriceMessage(message));
        assertEquals(new MiscellaneousEvent(message.getBody()),
                Messages.parse(message));
    }

    @Test
    public void malformedCurrentPrice() throws Exception {
        final Message message = SmackMessageObjectMother
                .messageWithText("SOLVersion: 1.1; Event: PRICE; CurrentPrice: not a price; Increment: 98; Bidder: other bidder");
        // don't check isReportPriceMessage() since that's deprecated
        assertEquals(new MiscellaneousEvent(message.getBody()),
                Messages.parse(message));
    }

    @Test
    public void malformedBidIncrement() throws Exception {
        final Message message = SmackMessageObjectMother
                .messageWithText("SOLVersion: 1.1; Event: PRICE; CurrentPrice: 1000; Increment: not an increment; Bidder: other bidder");
        // don't check isReportPriceMessage() since that's deprecated
        assertEquals(new MiscellaneousEvent(message.getBody()),
                Messages.parse(message));
    }

    private String messageTextForEventNamed(String eventName) {
        return "SOLVersion: 1.1; Event: " + eventName + irrelevantDetails();
    }

    private String irrelevantDetails() {
        return "; CurrentPrice: 1000; Increment: 98; Bidder: other bidder";
    }
}
