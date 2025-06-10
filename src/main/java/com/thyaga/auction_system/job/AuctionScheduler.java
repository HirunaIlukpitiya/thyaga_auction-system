package com.thyaga.auction_system.job;

import com.thyaga.auction_system.data.entity.Bid;
import com.thyaga.auction_system.data.entity.Item;
import com.thyaga.auction_system.data.repository.BidRepository;
import com.thyaga.auction_system.data.repository.ItemRepository;
import com.thyaga.auction_system.util.AuctionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class AuctionScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuctionScheduler.class);

    private final ItemRepository itemRepository;
    private final BidRepository bidRepository;

    public AuctionScheduler(ItemRepository itemRepository, BidRepository bidRepository) {
        this.itemRepository = itemRepository;
        this.bidRepository = bidRepository;
    }

    @Scheduled(fixedDelay = 1800000)
    @Transactional
    public void closeExpiredAuctions() {
        LOGGER.info("Running scheduled task to close expired auctions");

        LocalDateTime timeNow = LocalDateTime.now();
        List<Item> expiredItems = itemRepository.findByAuctionStatusAndAuctionEndTimeBefore(AuctionStatus.ACTIVE, timeNow);

        if (expiredItems.isEmpty()) {
            LOGGER.info("No expired auctions found");
        } else {
            LOGGER.info("Found {} expired auctions", expiredItems.size());
            for (Item item : expiredItems) {
                LOGGER.info("Closing auction for item: {}", item.getId());

                Optional<Bid> highestBid = item.getBids().stream()
                        .max(Comparator.comparing(Bid::getAmount));

                if (highestBid.isPresent()) {
                    Bid winningBid = highestBid.get();
                    winningBid.setWinningBid(true);
                    bidRepository.save(winningBid);
                    LOGGER.info("Winning bid set for item: {} with bid: {}", item.getId(), winningBid.getId());
                }
                item.setAuctionStatus(AuctionStatus.CLOSE);
                itemRepository.save(item);

                LOGGER.info("Closed auction for item: {}", item.getId());
            }
        }

    }
}
