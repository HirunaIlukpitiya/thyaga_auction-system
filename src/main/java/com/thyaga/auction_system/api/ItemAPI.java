package com.thyaga.auction_system.api;

import com.thyaga.auction_system.data.dto.ItemDTO;
import com.thyaga.auction_system.data.entity.Item;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.thyaga.auction_system.service.ItemService;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class ItemAPI {

    private final ItemService itemService;

    public ItemAPI(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping(value = "/user/{userId}/items")
    public ResponseEntity<Item> addItem(@Valid @RequestBody ItemDTO itemDTO,
                                        @PathVariable("userId") long userId) {
        return new ResponseEntity<>(itemService.addItem(itemDTO, userId), HttpStatus.CREATED);
    }

    @GetMapping(value = "/user/{userId}/items")
    public ResponseEntity<List<Item>> getItems(@PathVariable("userId") long userId) {

        return new ResponseEntity<>(itemService.getItems(userId), HttpStatus.OK);
    }

    @GetMapping(value = "/user/{userId}/items/{itemId}")
    public ResponseEntity<Item> getItem(@PathVariable("userId") long userId,
                                        @PathVariable("itemId") long itemId) {
        return new ResponseEntity<>(itemService.getItem(userId, itemId), HttpStatus.OK);
    }

}
