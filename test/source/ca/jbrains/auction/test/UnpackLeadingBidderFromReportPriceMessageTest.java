package ca.jbrains.auction.test;

import org.jivesoftware.smack.packet.Message;
import org.junit.*;

import ca.jbrains.auction.Main;

import static org.junit.Assert.*;

public class UnpackLeadingBidderFromReportPriceMessageTest {
    @Test
    public void happyPath() throws Exception {
        assertEquals(
                "jbrains",
                Main.leadingBidderAccordingTo(reportPriceMessageWithBidderNamed("jbrains")));
    }

    private Message reportPriceMessageWithBidderNamed(String bidderName) {
        return ClassifySniperBidAuctionMessageTest
                .messageWithText("SOLVersion: 1.1; Event: PRICE; CurrentPrice: 1000; Increment: 98; Bidder: "
                        + bidderName);
    }

    @Ignore
    @Test
    public void noBidderAttribute() throws Exception {

    }

    @Ignore
    @Test
    public void emptyBidderAttribute() throws Exception {

    }
}
