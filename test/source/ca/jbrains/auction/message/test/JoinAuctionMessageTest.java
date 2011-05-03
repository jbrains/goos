package ca.jbrains.auction.message.test;

import org.jivesoftware.smack.packet.Message;
import org.junit.Test;

import ca.jbrains.auction.message.Messages;
import static org.hamcrest.Matchers.*;

import static org.junit.Assert.assertThat;

public class JoinAuctionMessageTest {
    @Test
    public void create() throws Exception {
        final Message joinAuctionMessage = Messages.joinAuction();

        assertThat(joinAuctionMessage.getBody(),
                equalTo("SOLVersion: 1.1; Command: JOIN;"));
        assertThat(joinAuctionMessage.getTo(), is(nullValue(String.class)));
    }

    @Test
    public void matcherMatchesMessage() throws Exception {
        assertThat(Messages.joinAuction().getBody(),
                Messages.joinAuctionBodyMatcher());
    }

    @Test
    public void matcherRejectsEmptyMessage() throws Exception {
        assertThat(new Message().getBody(), not(Messages.joinAuctionBodyMatcher()));
    }
}
