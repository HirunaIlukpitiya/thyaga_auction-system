package com.thyaga.auction_system.data.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.thyaga.auction_system.util.AuctionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO {

    @NotBlank(message = "Item name is required")
    private String name;

    private String description;

    @NotNull(message = "Starting price is required")
    private BigDecimal startingPrice;

    private BigDecimal currentBid = BigDecimal.ZERO;

    private AuctionStatus auctionStatus = AuctionStatus.ACTIVE;

    @NotNull(message = "Auction end time is required")
    @Future(message = "Auction end time must be in the future")
    private LocalDateTime auctionEndTime;

    private Long version;
}
