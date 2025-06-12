package com.thyaga.auction_system.service;

import com.thyaga.auction_system.data.dto.ItemDTO;
import com.thyaga.auction_system.data.entity.Bid;
import com.thyaga.auction_system.data.entity.Item;
import com.thyaga.auction_system.data.entity.User;
import com.thyaga.auction_system.data.repository.ItemRepository;
import com.thyaga.auction_system.data.repository.UserRepository;
import com.thyaga.auction_system.exception.ThyagaAuctionException;
import com.thyaga.auction_system.exception.ThyagaAuctionStatus;
import com.thyaga.auction_system.util.AuctionStatus;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemService.class);
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public ItemService(UserRepository userRepository, ItemRepository itemRepository) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    public Item addItem(@Valid ItemDTO itemDTO, long userId) {
        LOGGER.info("Adding item for user with ID: {}", userId);
        Optional<User> user = userRepository.getUserById(userId);
        if (user.isEmpty()) {
            throw new ThyagaAuctionException(ThyagaAuctionStatus.USER_NOT_FOUND);
        }

        Item item = new Item();
        BeanUtils.copyProperties(itemDTO, item);
        item.setSeller(user.get());

        return itemRepository.save(item);
    }

    public Page<Item> getItems(long userId, int page, int size, String sortBy, String sortDirection) {

        LOGGER.info("Fetching all items for user with ID: {}", userId);
        Optional<User> user = userRepository.getUserById(userId);

        if (user.isEmpty()) {
            throw new ThyagaAuctionException(ThyagaAuctionStatus.USER_NOT_FOUND);
        }

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Item> itemsPage = itemRepository.findAllByAuctionStatus(AuctionStatus.ACTIVE, pageable);
        if (itemsPage.isEmpty()) {
            throw new ThyagaAuctionException(ThyagaAuctionStatus.ITEM_NOT_FOUND);
        }

        return itemsPage;
    }

    public Item getItem(long userId, long itemId) {
        LOGGER.info("Fetching item with ID: {} for user with ID: {}", itemId, userId);
        Optional<User> user = userRepository.getUserById(userId);

        if (user.isEmpty()) {
            throw new ThyagaAuctionException(ThyagaAuctionStatus.USER_NOT_FOUND);
        }

        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isEmpty()) {
            throw new ThyagaAuctionException(ThyagaAuctionStatus.ITEM_NOT_FOUND);
        }

        if (item.get().getAuctionStatus().equals(AuctionStatus.CLOSE)) {
            List<Bid> winnigBid = item.get().getBids().stream()
                    .filter(Bid::isWinningBid)
                    .toList();

            item.get().setBids(winnigBid);
        }

        return item.get();
    }
}
