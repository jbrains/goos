package ca.jbrains.auction.message;

import java.util.*;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;

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
            for (AuctionEventListener each : listeners) {
                each.handleJoinAuctionEvent();
            }
            return;
        }

        // SMELL Duplicated loops
        final Object event = Messages.parse(message);
        if (event instanceof BiddingState) {
            BiddingState biddingState = (BiddingState) event;
            for (AuctionEventListener each : listeners) {
                each.handleNewBiddingState(biddingState);
            }
        } else {
            for (AuctionEventListener each : listeners) {
                each.handleGenericEvent(event);
            }
        }
    }
}