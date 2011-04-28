package ca.jbrains.auction.message.test;

import org.hamcrest.Matcher;
import org.jivesoftware.smack.packet.Message;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

import static org.junit.Assert.assertThat;

public class JoinAuctionMessageTest {
    public static class Messages {
        public static Message joinAuction() {
            return new Message();
        }

        public static Matcher<? super String> joinAuctionMatcher() {
            return is(anything());
        }
    }

    @Test
    public void create() throws Exception {
        final Message joinAuctionMessage = Messages.joinAuction();

        assertThat(joinAuctionMessage.getBody(), is(nullValue(String.class)));
        assertThat(joinAuctionMessage.getTo(), is(nullValue(String.class)));
    }
    
    @Test
    public void matcherMatchesMessage() throws Exception {
        assertThat(Messages.joinAuction().getBody(), Messages.joinAuctionMatcher());
    }
}
