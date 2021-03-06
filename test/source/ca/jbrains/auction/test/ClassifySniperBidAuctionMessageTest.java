package ca.jbrains.auction.test;

import org.jivesoftware.smack.packet.Message;
import org.junit.Test;

import ca.jbrains.auction.smack.SmackMessageObjectMother;

import static org.junit.Assert.*;

public class ClassifySniperBidAuctionMessageTest {
    @Test
    public void happyPath() throws Exception {
        assertTrue(isSniperBidMessage(SmackMessageObjectMother
                .messageWithText("SOLVersion 1.1; Command: Bid; Price: 1098")));
    }

    @Test
    public void commandIsNotBid() throws Exception {
        assertFalse(isSniperBidMessage(SmackMessageObjectMother
                .messageWithText("SOLVersion 1.1; Command: XXX"
                        + irrelevantDetails())));
    }

    @Test
    public void notAnSolMessage() throws Exception {
        assertFalse(isSniperBidMessage(SmackMessageObjectMother
                .messageWithText("jbrains 3.0, bitchez; Command: Bid")));
    }

    @Test
    public void noBody() throws Exception {
        assertFalse(isSniperBidMessage(new Message()));
    }

    // REFACTOR Replace in tests with Messages.parse(message)
    // expecting SniperBidsEvent objects.
    private static boolean isSniperBidMessage(Message message) {
        return Main.messageBodyMatchesRegex(message,
                "SOLVersion 1.1.*Command: Bid.*");
    }

    private static String irrelevantDetails() {
        return "; Price: 1098";
    }
}
