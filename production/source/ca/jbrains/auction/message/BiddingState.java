package ca.jbrains.auction.message;

import lombok.Data;

@Data
public class BiddingState {
    private final int currentPrice;
    private final int biddingIncrement;
    private final String bidderName;
}