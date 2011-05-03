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
        if (AuctionMessages.joinAuctionBodyMatcher().matches(body)) {
            broadcast(joinedAuctionAnnouncement());
        } else if (AuctionMessages.auctionClosedBodyMatcher().matches(body)) {
            broadcast(auctionClosedAnnouncement());
        } else {
            // SMELL This sticks out like a sore thumb
            final Object event = AuctionMessages.parse(message);
            if (event instanceof BiddingState) {
                broadcast(newBiddingStateAnnouncement((BiddingState) event));
            } else {
                broadcast(genericEventAnnouncement(event));
            }
        }
    }

    private Function<AuctionEventListener, ?> genericEventAnnouncement(
            final Object event) {

        return new Function<AuctionEventListener, Object>() {
            @Override
            public Object apply(AuctionEventListener input) {
                input.handleGenericEvent(event);
                return null;
            }
        };
    }

    private Function<AuctionEventListener, Object> newBiddingStateAnnouncement(
            final BiddingState biddingState) {
        return new Function<AuctionEventListener, Object>() {
            @Override
            public Object apply(AuctionEventListener input) {
                input.handleNewBiddingState(biddingState);
                return null;
            }
        };
    }

    private static Function<AuctionEventListener, ?> joinedAuctionAnnouncement() {
        return new Function<AuctionEventListener, Object>() {
            @Override
            public Object apply(AuctionEventListener input) {
                input.handleJoinAuctionEvent();
                return null;
            }
        };
    }

    private static Function<AuctionEventListener, ?> auctionClosedAnnouncement() {
        return new Function<AuctionEventListener, Object>() {
            @Override
            public Object apply(AuctionEventListener input) {
                input.handleAuctionClosed();
                return null;
            }
        };
    }

    private void broadcast(final Function<AuctionEventListener, ?> announcement) {
        for (AuctionEventListener each : listeners) {
            announcement.apply(each);
        }
    }
}