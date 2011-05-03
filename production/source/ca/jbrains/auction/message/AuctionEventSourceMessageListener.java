package ca.jbrains.auction.message;

import java.util.*;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;

import com.google.common.base.Function;

public class AuctionEventSourceMessageListener implements MessageListener {

    private final List<AuctionEventListener> listeners;

    public AuctionEventSourceMessageListener() {
        this.listeners = new ArrayList<AuctionEventListener>();
    }

    public void addListener(AuctionEventListener listener) {
        listeners.add(listener);
    }

    @Override
    public void processMessage(Chat chat, Message message) {
        // Try migrating message-parsing behavior here
        final String body = message.getBody();
        if (Messages.joinAuctionBodyMatcher().matches(body)) {
            broadcast(new Function<AuctionEventListener, Object>() {
                @Override
                public Object apply(AuctionEventListener input) {
                    input.handleJoinAuctionEvent();
                    return null;
                }
            });
            return;
        }

        // SMELL Duplicated loops
        final Object event = Messages.parse(message);
        if (event instanceof BiddingState) {
            broadcast(new Function<AuctionEventListener, Object>() {
                @Override
                public Object apply(AuctionEventListener input) {
                    input.handleNewBiddingState((BiddingState) event);
                    return null;
                }
            });
        } else {
            broadcast(new Function<AuctionEventListener, Object>() {
                @Override
                public Object apply(AuctionEventListener input) {
                    input.handleGenericEvent(event);
                    return null;
                }
            });
        }
    }

    private void broadcast(final Function<AuctionEventListener, ?> announcement) {
        for (AuctionEventListener each : listeners) {
            announcement.apply(each);
        }
    }
}