package ca.jbrains.auction.message;


public interface AuctionEventListener {
    void handleNewBiddingState(BiddingState biddingState);

    void handleGenericEvent(Object object);

    void handleJoinAuctionEvent();
}