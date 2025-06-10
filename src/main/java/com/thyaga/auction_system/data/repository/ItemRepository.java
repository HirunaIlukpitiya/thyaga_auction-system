package com.thyaga.auction_system.data.repository;

import com.thyaga.auction_system.data.entity.Item;
import com.thyaga.auction_system.util.AuctionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByAuctionStatus(AuctionStatus auctionStatus);

    List<Item> findByAuctionStatusAndAuctionEndTimeBefore(AuctionStatus auctionStatus, LocalDateTime auctionEndTimeBefore);
}
