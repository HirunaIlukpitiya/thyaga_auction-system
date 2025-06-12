package com.thyaga.auction_system.api;

import com.thyaga.auction_system.data.dto.BidDTO;
import com.thyaga.auction_system.data.dto.BidResponseDTO;
import com.thyaga.auction_system.data.entity.Bid;
import com.thyaga.auction_system.service.BidService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class BidAPI {

    private final BidService bidService;

    public BidAPI(BidService bidService) {
        this.bidService = bidService;
    }

    @PostMapping(value = "/user/{userId}/items/{itemId}/bids")
    public ResponseEntity<BidResponseDTO> placeBid(@PathVariable("userId") long userId,
                                                   @PathVariable("itemId") long itemId,
                                                   @RequestBody BidDTO bidDTO) {

        return new ResponseEntity<>(bidService.placeBid(bidDTO, userId, itemId), HttpStatus.CREATED);
    }
}
