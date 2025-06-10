package com.thyaga.auction_system.exception;

public class ThyagaAuctionException extends RuntimeException {

    private final ThyagaAuctionStatus thyagaAuctionStatus;

    public ThyagaAuctionException(ThyagaAuctionStatus thyagaAuctionStatus) {
        super(thyagaAuctionStatus.getStatusDescription());
        this.thyagaAuctionStatus = thyagaAuctionStatus;
    }

    public String getErrorCode()  {
        return thyagaAuctionStatus.getStatusCode();
    }

    public String getMessage() {
        return thyagaAuctionStatus.getStatusDescription();
    }
}
