package ca.jbrains.auction.test;

import org.jivesoftware.smack.packet.Message;
import org.junit.Test;

import ca.jbrains.auction.smack.SmackMessageObjectMother;

import static org.junit.Assert.*;

public class ClassifyReportPriceAuctionMessageTest {
    @Test
    public void happyPath() throws Exception {
        assertTrue(Main
                .isReportPriceMessage(SmackMessageObjectMother
                        .messageWithText("SOLVersion: 1.1; Event: PRICE"
                                + irrelevantDetails())));
    }

    @Test
    public void notAnSolMessage() throws Exception {
        assertFalse(Main
                .isReportPriceMessage(SmackMessageObjectMother
                        .messageWithText("jbrains 3.0, bitchez; Event: PRICE"
                                + irrelevantDetails())));
    }

    @Test
    public void notAPriceEvent() throws Exception {
        assertFalse(Main
                .isReportPriceMessage(SmackMessageObjectMother
                        .messageWithText(messageTextForEventNamed("NOT PRICE"))));
    }

    @Test
    public void noBody() throws Exception {
        assertFalse(Main.isReportPriceMessage(new Message()));
    }

    private String messageTextForEventNamed(String eventName) {
        return "SOLVersion: 1.1; Event: " + eventName + irrelevantDetails();
    }

    private String irrelevantDetails() {
        return "; CurrentPrice: 1000; Increment: 98; Bidder: other bidder";
    }
}
