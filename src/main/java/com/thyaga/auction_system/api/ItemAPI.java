package com.thyaga.auction_system.api;

import com.thyaga.auction_system.data.dto.ItemDTO;
import com.thyaga.auction_system.data.dto.ItemResponseDTO;
import com.thyaga.auction_system.data.entity.Item;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<Page<ItemResponseDTO>> getItems(@PathVariable("userId") long userId,
                                                          @RequestParam(value = "page", defaultValue = "0") int page,
                                                          @RequestParam(value = "size", defaultValue = "10") int size,
                                                          @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
                                                          @RequestParam(value = "sortDirection", defaultValue = "ASC") String sortDirection) {

        return new ResponseEntity<>(itemService.getItems(userId, page, size, sortBy, sortDirection), HttpStatus.OK);
    }

    @GetMapping(value = "/user/{userId}/items/{itemId}")
    public ResponseEntity<ItemResponseDTO> getItem(@PathVariable("userId") long userId,
                                        @PathVariable("itemId") long itemId) {
        return new ResponseEntity<>(itemService.getItem(userId, itemId), HttpStatus.OK);
    }

}
