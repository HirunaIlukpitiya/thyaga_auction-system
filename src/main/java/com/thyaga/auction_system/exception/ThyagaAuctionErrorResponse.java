package com.thyaga.auction_system.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ThyagaAuctionErrorResponse {

    private String errorCode;
    private String message;
}
