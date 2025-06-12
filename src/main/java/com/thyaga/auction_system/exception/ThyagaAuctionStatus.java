package com.thyaga.auction_system.exception;

import lombok.Getter;

@Getter
public enum ThyagaAuctionStatus {
    BAD_REQUEST("400", "Bad Request"),
    UNAUTHORIZED("401", "Unauthorized"),
    FORBIDDEN("403", "Forbidden"),
    NOT_FOUND("404", "Not Found"),
    INTERNAL_SERVER_ERROR("500", "Internal Server Error"),
    SERVICE_UNAVAILABLE("503", "Service Unavailable"),
    OK("200", "OK"),
    CREATED("201", "Created"),
    ACCEPTED("202", "Accepted"),
    NO_CONTENT("204", "No Content"),
    INVALID_DATA("TAE0001", "Invalid Data"),
    DATA_ALREADY_EXISTS("TAE0002", "Data Already Exists"),
    DATA_NOT_FOUND("TAE0003", "Data Not Found"),
    INVALID_REQUEST("TAE0004", "Invalid Request"),
    PASSWORDS_DO_NOT_MATCH("TAE0005", "Passwords Do Not Match"),
    USERNAME_OR_EMAIL_ALREADY_EXISTS("TAE0006", "Username or Email Already Exists"),
    USER_NOT_FOUND("TAE0007", "User Not Found"),
    ITEM_NOT_FOUND("TAE0008", "Item Not Found"),
    CANNOT_BID_ON_OWN_ITEM("TAE0009", "Cannot Bid on Own Item"),
    AUCTION_CLOSED("TAE0010", "Auction Closed"),
    BID_TOO_LOW("TAE0011", "Bid Too Low"),
    CONCURRENT_BID_UPDATE("TAE0012", "Concurrent Bid Update Detected"),
    INVALID_CREDENTIALS("TAE0013", "Invalid Credentials");

    private final String statusCode;
    private final String statusDescription;

    ThyagaAuctionStatus(String statusCode, String statusDescription) {
        this.statusCode = statusCode;
        this.statusDescription = statusDescription;
    }
}
