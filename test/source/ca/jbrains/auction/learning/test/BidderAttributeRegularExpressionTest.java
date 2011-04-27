package ca.jbrains.auction.learning.test;

import java.util.regex.*;

import org.junit.Test;

import static org.junit.Assert.*;

public class BidderAttributeRegularExpressionTest {
    private final Pattern extractBidderPattern = Pattern
            .compile(".*Bidder: ([^;]*).*");

    @Test
    public void extractsAttributeValue() throws Exception {
        Matcher matcher = Pattern.compile("Bidder: ([^;]*)").matcher(
                "Bidder: jbrains");
        assertTrue(matcher.matches());
        assertEquals(1, matcher.groupCount());
        assertEquals("jbrains", matcher.group(1));
    }

    @Test
    public void matchesEntireMessageBody() throws Exception {
        Matcher matcher = extractBidderPattern
                .matcher("SOLVersion: 1.1; Event: PRICE; CurrentPrice: 1000; Increment: 98; Bidder: jbrains");
        assertTrue(matcher.matches());
        assertEquals(1, matcher.groupCount());
        assertEquals("jbrains", matcher.group(1));
    }

    @Test
    public void doesNotAssumeBidderIsTheLastAttribute() throws Exception {
        Matcher matcher = extractBidderPattern
                .matcher("SOLVersion: 1.1; Event: PRICE; Bidder: jbrains; CurrentPrice: 1000; Increment: 98");
        assertTrue(matcher.matches());
        assertEquals(1, matcher.groupCount());
        assertEquals("jbrains", matcher.group(1));
    }
}
