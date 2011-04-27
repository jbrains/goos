package ca.jbrains.auction.learning.test;

import java.util.*;
import java.util.regex.*;
import java.util.regex.Matcher;

import org.hamcrest.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class BidderAttributeRegularExpressionTest {
    private final Pattern extractBidderPattern = Pattern
            .compile(".*Bidder: ([^;]*).*");

    @Test
    public void extractsAttributeValue() throws Exception {
        assertMatchesString(
                Pattern.compile("Bidder: ([^;]*)").matcher("Bidder: jbrains"),
                "jbrains");
    }

    @Test
    public void matchesEntireMessageBody() throws Exception {
        assertMatchesString(
                extractBidderPattern
                        .matcher("SOLVersion: 1.1; Event: PRICE; CurrentPrice: 1000; Increment: 98; Bidder: jbrains"),
                "jbrains");
    }

    @Test
    public void doesNotAssumeBidderIsTheLastAttribute() throws Exception {
        assertMatchesString(
                extractBidderPattern
                        .matcher("SOLVersion: 1.1; Event: PRICE; Bidder: jbrains; CurrentPrice: 1000; Increment: 98"),
                "jbrains");
    }

    // I couldn't figure out how to write this as a good Hamcrest matcher
    private static void assertMatchesString(final Matcher matcher, String string) {
        assertTrue(matcher.matches());
        assertEquals(1, matcher.groupCount());
        assertEquals(string, matcher.group(1));
    }
}
