package ca.jbrains.auction.test;

import org.jivesoftware.smack.packet.Message;
import org.junit.Test;

import ca.jbrains.auction.smack.SmackMessageObjectMother;

import static org.junit.Assert.assertEquals;

public class UnpackLeadingBidderFromReportPriceMessageTest {
    @Test
    public void happyPath() throws Exception {
        assertEquals(
                "jbrains",
                Main.leadingBidderAccordingTo(reportPriceMessageWithBidderNamed("jbrains")));
    }

    @Test
    public void noBidderAttribute() throws Exception {
        assertEquals(
                null,
                Main.leadingBidderAccordingTo(SmackMessageObjectMother
                        .messageWithText("SOLVersion: 1.1; Event: PRICE; CurrentPrice: 1000; Increment: 98; NotBidder: jbrains")));
    }

    @Test
    public void emptyBidderAttribute() throws Exception {
        assertEquals(
                "",
                Main.leadingBidderAccordingTo(reportPriceMessageWithBidderNamed("")));
    }

    @Test
    public void bidderNameContainsSpaces() throws Exception {
        assertEquals(
                "other bidder",
                Main.leadingBidderAccordingTo(reportPriceMessageWithBidderNamed("other bidder")));
    }

    @Test
    public void bidderNameHasSurroundingSpaces() throws Exception {
        assertEquals(
                "other bidder",
                Main.leadingBidderAccordingTo(reportPriceMessageWithBidderNamed("   other bidder  ")));
    }

    private Message reportPriceMessageWithBidderNamed(String bidderName) {
        return SmackMessageObjectMother
                .messageWithText("SOLVersion: 1.1; Event: PRICE; CurrentPrice: 1000; Increment: 98; Bidder: "
                        + bidderName);
    }
}
