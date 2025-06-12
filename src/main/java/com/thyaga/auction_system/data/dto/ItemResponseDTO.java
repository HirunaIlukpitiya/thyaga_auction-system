package com.thyaga.auction_system.data.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thyaga.auction_system.util.AuctionStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ItemResponseDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal startingPrice;
    private BigDecimal currentBid;
    private AuctionStatus auctionStatus;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime auctionEndTime;

    private UserDTO seller;
    private List<BidResponseDTO> bids = new ArrayList<>();
    private Long version;

    @Data
    public static class UserDTO {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private String username;
    }
}