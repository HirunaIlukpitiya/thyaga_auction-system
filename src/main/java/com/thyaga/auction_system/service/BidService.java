package com.thyaga.auction_system.service;

import com.thyaga.auction_system.data.dto.BidDTO;
import com.thyaga.auction_system.data.dto.BidResponseDTO;
import com.thyaga.auction_system.data.entity.Bid;
import com.thyaga.auction_system.data.entity.Item;
import com.thyaga.auction_system.data.entity.User;
import com.thyaga.auction_system.data.repository.BidRepository;
import com.thyaga.auction_system.data.repository.ItemRepository;
import com.thyaga.auction_system.data.repository.UserRepository;
import com.thyaga.auction_system.exception.ThyagaAuctionException;
import com.thyaga.auction_system.exception.ThyagaAuctionStatus;
import com.thyaga.auction_system.util.AuctionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BidService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BidService.class);

    private final UserRepository userRepository;
    private final BidRepository bidRepository;
    private final ItemRepository itemRepository;

    public BidService(UserRepository userRepository,
                      BidRepository bidRepository,
                      ItemRepository itemRepository) {
        this.userRepository = userRepository;
        this.bidRepository = bidRepository;
        this.itemRepository = itemRepository;
    }

    @Retryable(retryFor = ObjectOptimisticLockingFailureException.class, maxAttempts = 3, backoff = @Backoff(delay = 100))
    @Transactional
    public BidResponseDTO placeBid(BidDTO bidDTO, Long userId, Long itemId) {
        LOGGER.info("Placing bid for user: {}, item: {}", userId, itemId);

        User user = userRepository.findById(userId).orElseThrow(() -> {
            LOGGER.error("User with ID: {} not found", userId);
            return new ThyagaAuctionException(ThyagaAuctionStatus.USER_NOT_FOUND);
        });

        Item item = itemRepository.findById(itemId).orElseThrow(() -> {
            LOGGER.error("Item with ID: {} not found", itemId);
            return new ThyagaAuctionException(ThyagaAuctionStatus.ITEM_NOT_FOUND);
        });

        if (item.getSeller().getId().equals(userId)) {
            LOGGER.error("User with ID: {} cannot bid on their own item with ID: {}", userId, itemId);
            throw new ThyagaAuctionException(ThyagaAuctionStatus.CANNOT_BID_ON_OWN_ITEM);
        }

        if (item.getAuctionStatus().equals(AuctionStatus.CLOSE)) {
            LOGGER.error("Cannot place bid on item with ID: {} as the auction is closed", itemId);
            throw new ThyagaAuctionException(ThyagaAuctionStatus.AUCTION_CLOSED);
        }

        if (bidDTO.getAmount().compareTo(item.getCurrentBid()) <= 0 ) {
            LOGGER.error("Bid amount: {} must be greater than the current bid: {}", bidDTO.getAmount(), item.getCurrentBid());
            throw new ThyagaAuctionException(ThyagaAuctionStatus.BID_TOO_LOW);
        }

        try {
            item.setCurrentBid(bidDTO.getAmount());
            itemRepository.save(item);

            Bid bid = new Bid();
            if (bidDTO.getBidTime() == null) {
                bidDTO.setBidTime(java.time.LocalDateTime.now());
            }

            BeanUtils.copyProperties(bidDTO, bid);
            bid.setUser(user);
            bid.setItem(item);

            bid = bidRepository.save(bid);
            BidResponseDTO bidResponse = new BidResponseDTO();
            BeanUtils.copyProperties(bid, bidResponse);
            bidResponse.setItemId(bid.getItem().getId());
            bidResponse.setUserId(bid.getUser().getId());
            LOGGER.info("Bid placed successfully with ID: {}", bid.getId());
            return bidResponse;
        } catch (ObjectOptimisticLockingFailureException e) {
            LOGGER.error("Cannot place bid on item with ID: {}", itemId, e);
            throw new ThyagaAuctionException(ThyagaAuctionStatus.CONCURRENT_BID_UPDATE);
        }
    }
}
