package ca.jbrains.auction.message.test;

import org.jivesoftware.smack.packet.Message;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

import static org.junit.Assert.assertThat;

public class JoinAuctionMessageTest {
    public static class Messages {
        public static Message joinAuction() {
            return new Message();
        }
    }

    @Test
    public void create() throws Exception {
        final Message joinAuctionMessage = Messages.joinAuction();

        assertThat(joinAuctionMessage.getBody(), is(nullValue(String.class)));
        assertThat(joinAuctionMessage.getTo(), is(nullValue(String.class)));
    }
}
