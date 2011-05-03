package ca.jbrains.auction.message;

import java.util.regex.Pattern;

import org.jivesoftware.smack.packet.Message;

import static org.hamcrest.Matchers.*;

public class Messages {
    public static Message joinAuction() {
        final Message joinAuctionMessage = new Message();
        joinAuctionMessage.setBody("SOLVersion: 1.1; Command: JOIN;");
        return joinAuctionMessage;
    }

    // SMELL? I'm not sure I like the Hamcrest Matcher here.
    public static org.hamcrest.Matcher<? super String> joinAuctionMatcher() {
        return is(anything());
    }

    public static String reportPriceMessageBody(int price, int increment,
            String bidder) {

        return String
                .format("SOLVersion: 1.1; Event: PRICE; CurrentPrice: %d; Increment: %d; Bidder: %s",
                        price, increment, bidder);
    }

    // Eventually this return type will be more strict than Object
    public static Object parse(Message message) {
        return parseBody(message.getBody());
    }

    public static Object parseBody(final String body) {
        // Why Java? Why can't you just make no regex match null string?
        // Why the blowing up? Don't be such a wimp.
        if (body == null)
            return new MiscellaneousEvent(body);

        java.util.regex.Matcher matcher = Pattern
                .compile(
                        "SOLVersion:\\s*1.1;\\s*Event:\\s*PRICE;\\s*CurrentPrice:\\s*(\\d+);\\s*Increment:\\s*(\\d+);\\s*Bidder:\\s*([^;]*)")
                .matcher(body);

        if (!matcher.matches())
            return new MiscellaneousEvent(body);

        if (matcher.groupCount() != 3)
            return new MiscellaneousEvent(body);

        // We can parseInt() safely because the regex demands digits
        final int currentPrice = Integer.parseInt(matcher.group(1).trim());
        final int bidIncrement = Integer.parseInt(matcher.group(2).trim());
        final String bidderName = matcher.group(3).trim();

        return new BiddingState(currentPrice, bidIncrement, bidderName);
    }
}